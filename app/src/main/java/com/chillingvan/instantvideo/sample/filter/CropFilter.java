package com.chillingvan.instantvideo.sample.filter;

import android.opengl.GLES20;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.OpenGLUtil;
import com.chillingvan.canvasgl.glcanvas.BasicTexture;
import com.chillingvan.canvasgl.textureFilter.BasicTextureFilter;

public class CropFilter extends BasicTextureFilter {

    private float scaleX, scaleY;
    private float left,right, top, bottom;

    private static final String UNIFORM_SCALEX = "scaleX";
    private static final String UNIFORM_SCALEY = "scaleY";

    private static final String UNIFORM_LEFT = "left";
    private static final String UNIFORM_RIGHT = "right";
    private static final String UNIFORM_TOP = "top";
    private static final String UNIFORM_BOTTOM = "bottom";

    public static final String CROP_VERTEX_SHADER = ""
            + "uniform mat4 " + MATRIX_UNIFORM + ";\n"
            + "uniform mat4 " + TEXTURE_MATRIX_UNIFORM + ";\n"
            + "attribute vec2 " + POSITION_ATTRIBUTE + ";\n"
            + "uniform float " + UNIFORM_SCALEX + ";\n"
            + "uniform float " + UNIFORM_SCALEY + ";\n"
            + "varying vec2 fromCenter;\n"
            + "varying vec2 scaledFromCenter;\n"
            + "varying vec2 " + VARYING_TEXTURE_COORD + ";\n"
            + "void main() {\n"
            + "  vec4 pos = vec4(" + POSITION_ATTRIBUTE + ", 0.0, 1.0);\n"
            + "  gl_Position = " + MATRIX_UNIFORM + " * pos;\n"
            + "  " + VARYING_TEXTURE_COORD + " = (" + TEXTURE_MATRIX_UNIFORM + " * pos).xy;\n"
            + "  fromCenter = " + VARYING_TEXTURE_COORD + " - vec2(.5 , .5);\n"
            + "  scaledFromCenter = fromCenter * vec2("+UNIFORM_SCALEX+","+UNIFORM_SCALEY+");\n"
            + "  vTextureCoord = vec2(.5,.5) + scaledFromCenter;\n"
            + "}\n";

    public static final String CROP_FRAGMENT_SHADER = ""
            + "precision mediump float;\n"
            + "varying vec2 " + VARYING_TEXTURE_COORD + ";\n"
            + "uniform float " + ALPHA_UNIFORM + ";\n"
            + "uniform float " + UNIFORM_LEFT + ";\n"
            + "uniform float " + UNIFORM_RIGHT + ";\n"
            + "uniform float " + UNIFORM_TOP + ";\n"
            + "uniform float " + UNIFORM_BOTTOM + ";\n"
            + "uniform " + SAMPLER_2D + " " + TEXTURE_SAMPLER_UNIFORM + ";\n"
            + "void main() {\n"
            + "if( "+ VARYING_TEXTURE_COORD + ".x > " + UNIFORM_LEFT + " &&  "+ VARYING_TEXTURE_COORD + ".x < " + UNIFORM_RIGHT + " &&  "+ VARYING_TEXTURE_COORD + ".y > " + UNIFORM_TOP + " &&  "+ VARYING_TEXTURE_COORD + ".y < " + UNIFORM_BOTTOM + ") {"
            + " gl_FragColor = texture2D(" + TEXTURE_SAMPLER_UNIFORM + ", " + VARYING_TEXTURE_COORD + ");\n"
            + "} else {"
            + " gl_FragColor = " + "vec4(0, 0, 0, 0)" + ";\n"
            + "}"
            + "}\n";

    @Override
    public String getVertexShader() {
        return CROP_VERTEX_SHADER;
    }

    @Override
    public String getFragmentShader() {
        return CROP_FRAGMENT_SHADER;
    }

    @Override
    public void onPreDraw(int program, BasicTexture texture, ICanvasGL canvas) {
        super.onPreDraw(program, texture, canvas);
        int scaleXLocation = GLES20.glGetUniformLocation(program, UNIFORM_SCALEX);
        OpenGLUtil.setFloat(scaleXLocation, scaleX);

        int scaleYLocation = GLES20.glGetUniformLocation(program, UNIFORM_SCALEY);
        OpenGLUtil.setFloat(scaleYLocation, scaleY);

        int leftLocation = GLES20.glGetUniformLocation(program, UNIFORM_LEFT);
        OpenGLUtil.setFloat(leftLocation, left);

        int rightLocation = GLES20.glGetUniformLocation(program, UNIFORM_RIGHT);
        OpenGLUtil.setFloat(rightLocation, right);

        int topLocation = GLES20.glGetUniformLocation(program, UNIFORM_TOP);
        OpenGLUtil.setFloat(topLocation, top);

        int bottomLocation = GLES20.glGetUniformLocation(program, UNIFORM_BOTTOM);
        OpenGLUtil.setFloat(bottomLocation, bottom);
    }

    public CropFilter(float scaleX, float scaleY, float width, float height, float cropWidth, float cropHeight) {
        super();
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        left = width/2.0f - cropWidth/2.0f;
        left = left/width;
        right = width/2.0f + cropWidth/2.0f;
        right = right/width;
        top = height/2.0f - cropHeight/2.0f;
        top = top/height;
        bottom = height/2.0f + cropHeight/2.0f;
        bottom = bottom/height;
    }
}
