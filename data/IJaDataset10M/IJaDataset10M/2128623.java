package com.akjava.android.openglsamples.redbook;

import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import com.akjava.android.openglsamples.GLUT;
import com.akjava.android.openglsamples.OpenGLUtils;

public class ColorMatRenderer extends RedBookSimpleRenderer {

    public ColorMatRenderer(Context context) {
        super(context);
    }

    public void setUpBuffers() {
        float eqn[] = { 0.0f, 1.0f, 0.0f, 0.0f };
        float eqn2[] = { 1.0f, 0.0f, 0.0f, 0.0f };
        eqnBuffer = FloatBuffer.wrap(eqn);
        eqn2Buffer = FloatBuffer.wrap(eqn2);
    }

    FloatBuffer eqnBuffer, eqn2Buffer;

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        if (w <= h) gl.glOrthof(-1.5f, 1.5f, -1.5f * (float) h / (float) w, 1.5f * (float) h / (float) w, -10.0f, 10.0f); else gl.glOrthof(-1.5f * (float) w / (float) h, 1.5f * (float) w / (float) h, -1.5f, 1.5f, -10.0f, 10.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        diffuseMaterialBuffer.clear();
        diffuseMaterialBuffer.put(diffuseMaterial);
        diffuseMaterialBuffer.position(0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuseMaterialBuffer);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        GLUT.glutSolidSphere(gl, 1.0f, 20, 16);
    }

    float diffuseMaterial[] = { 1.0f, 0.5f, 0.5f, 1.0f };

    FloatBuffer diffuseMaterialBuffer = OpenGLUtils.allocateFloatBuffer(4 * 4);

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        setUpBuffers();
        float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        float light_position[] = { 1.0f, 1.0f, 1.0f, 0.0f };
        diffuseMaterialBuffer.clear();
        diffuseMaterialBuffer.put(diffuseMaterial);
        diffuseMaterialBuffer.position(0);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuseMaterialBuffer);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, FloatBuffer.wrap(mat_specular));
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 25.0f);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, FloatBuffer.wrap(light_position));
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
    }

    public void actionLeft() {
        diffuseMaterial[0] += 0.1;
        if (diffuseMaterial[0] > 1.0) {
            diffuseMaterial[0] = 0;
        }
    }

    public void actionUp() {
        diffuseMaterial[1] += 0.1;
        if (diffuseMaterial[1] > 1.0) {
            diffuseMaterial[1] = 0;
        }
    }

    public void actionDown() {
    }

    public void actionRight() {
        diffuseMaterial[2] += 0.1;
        if (diffuseMaterial[2] > 1.0) {
            diffuseMaterial[2] = 0;
        }
    }

    public void actionCenter() {
        diffuseMaterial[0] = 0.5f;
        diffuseMaterial[1] = 0.5f;
        diffuseMaterial[2] = 0.5f;
    }
}
