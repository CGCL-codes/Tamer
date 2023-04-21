package com.google.android.samples.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.OpenGLContext;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;

/**
 * Example of how to use OpenGL|ES in a custom view
 *
 */
public class GLView1 extends Activity {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(new GLView(getApplication()));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

class GLView extends View {

    /**
     * The View constructor is a good place to allocate our OpenGL context
     */
    public GLView(Context context) {
        super(context);
        mGLContext = new OpenGLContext(0);
        mCube = new Cube();
        mAnimate = false;
    }

    @Override
    protected void onAttachedToWindow() {
        mAnimate = true;
        Message msg = mHandler.obtainMessage(INVALIDATE);
        mNextTime = SystemClock.uptimeMillis();
        mHandler.sendMessageAtTime(msg, mNextTime);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        mAnimate = false;
        super.onDetachedFromWindow();
    }

    /**
     * Draw the view content
     * 
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (true) {
            GL10 gl = (GL10) (mGLContext.getGL());
            mGLContext.waitNative();
            mGLContext.makeCurrent(this);
            int w = getWidth();
            int h = getHeight();
            gl.glViewport(0, 0, w, h);
            float ratio = (float) w / h;
            gl.glMatrixMode(gl.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustumf(-ratio, ratio, -1, 1, 2, 12);
            gl.glDisable(gl.GL_DITHER);
            gl.glClearColor(1, 1, 1, 1);
            gl.glEnable(gl.GL_SCISSOR_TEST);
            gl.glScissor(0, 0, w, h);
            gl.glClear(gl.GL_COLOR_BUFFER_BIT);
            gl.glMatrixMode(gl.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(0, 0, -3.0f);
            gl.glScalef(0.5f, 0.5f, 0.5f);
            gl.glRotatef(mAngle, 0, 1, 0);
            gl.glRotatef(mAngle * 0.25f, 1, 0, 0);
            gl.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
            gl.glEnableClientState(gl.GL_VERTEX_ARRAY);
            gl.glEnableClientState(gl.GL_COLOR_ARRAY);
            gl.glEnable(gl.GL_CULL_FACE);
            mCube.draw(gl);
            mAngle += 1.2f;
            mGLContext.waitGL();
            mGLContext.post();
        }
    }

    private static final int INVALIDATE = 1;

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (mAnimate && msg.what == INVALIDATE) {
                invalidate();
                msg = obtainMessage(INVALIDATE);
                long current = SystemClock.uptimeMillis();
                if (mNextTime < current) {
                    mNextTime = current + 20;
                }
                sendMessageAtTime(msg, mNextTime);
                mNextTime += 20;
            }
        }
    };

    private OpenGLContext mGLContext;

    private Cube mCube;

    private float mAngle;

    private long mNextTime;

    private boolean mAnimate;
}

class Cube {

    public Cube() {
        int one = 0x10000;
        int vertices[] = { -one, -one, -one, one, -one, -one, one, one, -one, -one, one, -one, -one, -one, one, one, -one, one, one, one, one, -one, one, one };
        int colors[] = { 0, 0, 0, one, one, 0, 0, one, one, one, 0, one, 0, one, 0, one, 0, 0, one, one, one, 0, one, one, one, one, one, one, 0, one, one, one };
        byte indices[] = { 0, 4, 5, 0, 5, 1, 1, 5, 6, 1, 6, 2, 2, 6, 7, 2, 7, 3, 3, 7, 4, 3, 4, 0, 4, 7, 6, 4, 6, 5, 3, 0, 1, 3, 1, 2 };
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asIntBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(gl.GL_CW);
        gl.glVertexPointer(3, gl.GL_FIXED, 0, mVertexBuffer);
        gl.glColorPointer(4, gl.GL_FIXED, 0, mColorBuffer);
        gl.glDrawElements(gl.GL_TRIANGLES, 36, gl.GL_UNSIGNED_BYTE, mIndexBuffer);
    }

    private IntBuffer mVertexBuffer;

    private IntBuffer mColorBuffer;

    private ByteBuffer mIndexBuffer;
}
