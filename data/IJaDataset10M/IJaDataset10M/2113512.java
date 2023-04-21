package com.akjava.android.openglsamples.redbook;

import android.app.Activity;
import android.os.Bundle;
import com.akjava.android.openglsamples.RedbookSampleGLView;

public class SmoothActivity extends Activity {

    /** Set to true to enable checking of the OpenGL error code after every OpenGL call. Set to
     * false for faster code.
     *
     */
    private static final boolean DEBUG_CHECK_GL_ERROR = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("smooth");
        mGLView = new RedbookSampleGLView(this);
        setContentView(mGLView);
        SmoothRenderer render = new SmoothRenderer(this);
        mGLView.setEventHandleRender(render);
        mGLView.requestFocus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    private RedbookSampleGLView mGLView;
}
