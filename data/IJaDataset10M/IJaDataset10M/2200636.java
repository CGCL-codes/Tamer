package com.opengl;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class OpenGLActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final GLRenderer gLRenderer = new GLRenderer();
        GLSurfaceView gLSurfaceView = new GLSurfaceView(this) {

            private float x = 0;

            private float y = 0;

            @Override
            public boolean onTouchEvent(final MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = event.getX();
                    y = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    final float xdiff = (x - event.getX());
                    final float ydiff = (y - event.getY());
                    queueEvent(new Runnable() {

                        public void run() {
                            gLRenderer.setxAngle(gLRenderer.getxAngle() + ydiff);
                            gLRenderer.setyAngle(gLRenderer.getyAngle() - xdiff);
                        }
                    });
                    x = event.getX();
                    y = event.getY();
                }
                return true;
            }
        };
        gLSurfaceView.setRenderer(gLRenderer);
        setContentView(gLSurfaceView);
    }
}
