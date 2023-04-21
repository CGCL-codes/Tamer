package com.akjava.android.openglsamples.redbook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.akjava.android.openglsamples.RedbookSampleGLView;

public class AargbActivity extends Activity {

    /** Set to true to enable checking of the OpenGL error code after every OpenGL call. Set to
     * false for faster code.
     *
     *
     *I tested ADP1.
     *dont show anything.
     */
    private static final boolean DEBUG_CHECK_GL_ERROR = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("aargb");
        mGLView = new RedbookSampleGLView(this);
        setContentView(mGLView);
        AargbRenderer render = new AargbRenderer(this);
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
