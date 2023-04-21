package com.openglesbook.stenciltest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.openglesbook.common.ESShader;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public class StencilTestRenderer implements GLSurfaceView.Renderer {

    public StencilTestRenderer(Context context) {
        mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(mVerticesData).position(0);
        mIndices = ByteBuffer.allocateDirect(mIndicesData.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        mIndices.put(mIndicesData).position(0);
    }

    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        String vShaderStr = "attribute vec4 a_position;   \n" + "void main()                  \n" + "{                            \n" + "   gl_Position = a_position; \n" + "}                            \n";
        String fShaderStr = "precision mediump float;  \n" + "uniform vec4  u_color;    \n" + "void main()               \n" + "{                         \n" + "  gl_FragColor = u_color; \n" + "}                         \n";
        mProgramObject = ESShader.loadProgram(vShaderStr, fShaderStr);
        mPositionLoc = GLES20.glGetAttribLocation(mProgramObject, "a_position");
        mColorLoc = GLES20.glGetUniformLocation(mProgramObject, "u_color");
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClearStencil(0x1);
        GLES20.glClearDepthf(0.75f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_STENCIL_TEST);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void onDrawFrame(GL10 glUnused) {
        float[][] colors = { { 1.0f, 0.0f, 0.0f, 1.0f }, { 0.0f, 1.0f, 0.0f, 1.0f }, { 0.0f, 0.0f, 1.0f, 1.0f }, { 1.0f, 1.0f, 0.0f, 0.0f } };
        int[] numStencilBits = new int[1];
        int[] stencilValues = { 0x7, 0x0, 0x2, 0xff };
        GLES20.glViewport(0, 0, mWidth, mHeight);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_STENCIL_BUFFER_BIT);
        GLES20.glUseProgram(mProgramObject);
        GLES20.glVertexAttribPointer(mPositionLoc, 3, GLES20.GL_FLOAT, false, 0, mVertices);
        GLES20.glEnableVertexAttribArray(mPositionLoc);
        GLES20.glStencilFunc(GLES20.GL_LESS, 0x7, 0x3);
        GLES20.glStencilOp(GLES20.GL_REPLACE, GLES20.GL_DECR, GLES20.GL_DECR);
        mIndices.position(0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, mIndices);
        GLES20.glStencilFunc(GLES20.GL_GREATER, 0x3, 0x3);
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_DECR, GLES20.GL_KEEP);
        mIndices.position(6);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, mIndices);
        GLES20.glStencilFunc(GLES20.GL_EQUAL, 0x1, 0x3);
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_INCR, GLES20.GL_INCR);
        mIndices.position(12);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, mIndices);
        GLES20.glStencilFunc(GLES20.GL_EQUAL, 0x2, 0x1);
        GLES20.glStencilOp(GLES20.GL_INVERT, GLES20.GL_KEEP, GLES20.GL_KEEP);
        mIndices.position(18);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, mIndices);
        GLES20.glGetIntegerv(GLES20.GL_STENCIL_BITS, numStencilBits, 0);
        stencilValues[3] = ~(((1 << numStencilBits[0]) - 1) & 0x1) & 0xff;
        GLES20.glStencilMask(0x0);
        for (int i = 0; i < 4; ++i) {
            GLES20.glStencilFunc(GLES20.GL_EQUAL, stencilValues[i], 0xff);
            GLES20.glUniform4f(mColorLoc, colors[i][0], colors[i][1], colors[i][2], colors[i][3]);
            mIndices.position(24);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, mIndices);
        }
    }

    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    private int mProgramObject;

    private int mPositionLoc;

    private int mColorLoc;

    private int mWidth;

    private int mHeight;

    private FloatBuffer mVertices;

    private ShortBuffer mIndices;

    private final float[] mVerticesData = { -0.75f, 0.25f, 0.50f, -0.25f, 0.25f, 0.50f, -0.25f, 0.75f, 0.50f, -0.75f, 0.75f, 0.50f, 0.25f, 0.25f, 0.90f, 0.75f, 0.25f, 0.90f, 0.75f, 0.75f, 0.90f, 0.25f, 0.75f, 0.90f, -0.75f, -0.75f, 0.50f, -0.25f, -0.75f, 0.50f, -0.25f, -0.25f, 0.50f, -0.75f, -0.25f, 0.50f, 0.25f, -0.75f, 0.50f, 0.75f, -0.75f, 0.50f, 0.75f, -0.25f, 0.50f, 0.25f, -0.25f, 0.50f, -1.00f, -1.00f, 0.00f, 1.00f, -1.00f, 0.00f, 1.00f, 1.00f, 0.00f, -1.00f, 1.00f, 0.00f };

    private final short[] mIndicesData = { 0, 1, 2, 0, 2, 3, 4, 5, 6, 4, 6, 7, 8, 9, 10, 8, 10, 11, 12, 13, 14, 12, 14, 15, 16, 17, 18, 16, 18, 19 };
}
