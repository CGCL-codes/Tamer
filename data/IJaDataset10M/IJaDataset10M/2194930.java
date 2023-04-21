package de.spotnik.gpl.util;

/**
 * This is a static/abstract class with some help-/useful methods.
 */
public abstract class ThreadUtilities {

    /**
     * do nothing
     */
    public static void bore() {
        try {
            Thread thread = Thread.currentThread();
            synchronized (thread) {
                thread.wait();
            }
        } catch (InterruptedException ie) {
        }
    }

    /**
     * do nothing for milliseconds
     * 
     * @param millis
     */
    public static void bore(long millis) {
        if (millis > 0) {
            try {
                Thread thread = Thread.currentThread();
                synchronized (thread) {
                    thread.wait(millis);
                }
            } catch (InterruptedException ie) {
            }
        }
    }

    /**
     * wake up thread
     * 
     * @param waitingThread
     */
    public static void wakeUp(Thread waitingThread) {
        try {
            synchronized (waitingThread) {
                waitingThread.notify();
            }
        } catch (IllegalMonitorStateException ime) {
        }
    }
}
