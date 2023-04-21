package com.endless.visualizer;

import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class Visualizer {

    public Visualizer(GLSurfaceView s) {
        assert s != null;
        surface = s;
        surface.setEGLConfigChooser(8, 8, 8, 8, 8, 0);
        surface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        flow = new ErythrocyteFlow(true);
        surface.setRenderer(flow);
    }

    public void pause() {
        surface.onPause();
    }

    public void resume() {
        surface.onResume();
    }

    public void set_histogram(final double[] histogram) {
        surface.queueEvent(new Runnable() {

            public void run() {
                flow.set_histogram(histogram);
                surface.requestRender();
            }
        });
    }

    private ErythrocyteFlow flow = null;

    private GLSurfaceView surface = null;
}

;
