package org.eclipse.swt.widgets;

/**
 * Instances of this class are used to ensure that an
 * application cannot interfere with the locking mechanism
 * used to implement asynchonous and synchronous communication
 * between widgets and background threads.
 */
class RunnableLock {

    Runnable runnable;

    Thread thread;

    Throwable throwable;

    RunnableLock(Runnable runnable) {
        this.runnable = runnable;
    }

    boolean done() {
        return runnable == null || throwable != null;
    }

    void run() {
        if (runnable != null) runnable.run();
        runnable = null;
    }
}
