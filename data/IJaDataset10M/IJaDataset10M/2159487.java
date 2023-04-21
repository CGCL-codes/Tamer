package com.ec.core.db;

/**
 *  Provides a way to run a Runnable object with a timer.  If the run() method
 *  does not return before timeout elapses, a TimeoutException is thrown.
 *  otherwise the constructor returns normally.
 */
public class JavaAlarm extends Thread {

    Runnable r;

    boolean completed;

    public JavaAlarm(Runnable r, long timeout) throws TimeoutException {
        super();
        this.r = r;
        this.completed = false;
        start();
        synchronized (this) {
            try {
                if (!completed) wait(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!completed) {
            stop();
            throw new TimeoutException("Runnable did not complete within " + timeout + "ms");
        }
    }

    public void run() {
        completed = false;
        r.run();
        completed = true;
        synchronized (this) {
            notifyAll();
        }
    }
}
