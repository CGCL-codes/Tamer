package com.akjava.android.openglsamples.redbook;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLU;
import com.akjava.android.openglsamples.GLUT;

public class CubeRenderer extends RedBookSimpleRenderer {

    public CubeRenderer(Context context) {
        super(context);
    }

    public void setUpBuffers() {
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-1.0f, 1.0f, -1.0f, 1.0f, 1.5f, 20.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 10.f);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        gl.glScalef(1.0f, 2.0f, 1.0f);
        GLUT.glutWireCube(gl, 1.0f);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        setUpBuffers();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL10.GL_FLAT);
    }
}
