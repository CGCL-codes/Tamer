package com.qualcomm.QCARSamples.FrameMarkers;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import android.util.Log;
import com.qualcomm.QCAR.QCAR;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/** The renderer class for the FrameMarkers sample. */
public class FrameMarkersRenderer implements GLSurfaceView.Renderer {

    public boolean mIsActive = false;

    /** Native function for initializing the renderer. */
    public native void initRendering();

    /** Native function to update the renderer. */
    public native void updateRendering(int width, int height);

    /** Called when the surface is created or recreated. */
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        DebugLog.LOGD("GLRenderer::onSurfaceCreated");
        initRendering();
        QCAR.onSurfaceCreated();
    }

    /** Called when the surface changed size. */
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        DebugLog.LOGD("GLRenderer::onSurfaceChanged");
        updateRendering(width, height);
        QCAR.onSurfaceChanged(width, height);
    }

    Intent intent = null;

    public void actionTrigger(int idMarker) {
        Log.i("MarkerID: ", " " + idMarker);
    }

    private void startActivity(Intent intent2) {
    }

    /** The native render function. */
    public native void renderFrame();

    /** Called to draw the current frame. */
    public void onDrawFrame(GL10 gl) {
        if (!mIsActive) return;
        renderFrame();
    }
}
