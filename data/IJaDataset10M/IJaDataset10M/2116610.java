package org.iqual.chaplin.objekty2009.semaphore;

import org.iqual.chaplin.FromContext;

/**
 * @author Zbynek Slajchrt
 * @since 10.10.2009 17:22:11
 */
public abstract class SemaphoreRunner implements Runnable {

    @FromContext
    abstract void nextLight();

    private final long interval;

    private boolean stopped = false;

    public SemaphoreRunner(long interval) {
        this.interval = interval;
    }

    public void stop() {
        stopped = true;
    }

    public void run() {
        while (!stopped) {
            nextLight();
            pause();
        }
    }

    private void pause() {
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
