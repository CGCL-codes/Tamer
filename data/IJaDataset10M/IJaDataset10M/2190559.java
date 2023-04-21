package org.jcrpg.threed.core;

import com.ardor3d.framework.FrameHandler;

public class GameThread extends Thread {

    private final FrameHandler frameWork;

    private volatile boolean exit = false;

    public GameThread(final FrameHandler frameWork) {
        this.frameWork = frameWork;
    }

    @Override
    public void run() {
        try {
            frameWork.init();
            while (!exit) {
                frameWork.updateFrame();
            }
        } catch (final Throwable t) {
            System.err.println("Throwable caught in MainThread - exiting");
            t.printStackTrace(System.err);
        }
    }

    public void exit() {
        exit = true;
    }
}
