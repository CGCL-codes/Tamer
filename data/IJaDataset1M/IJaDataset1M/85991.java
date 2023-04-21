package org.jseda.thread;

/**
 * ManagedThread is an interface that specifies some basic thread control
 * mechanisms.
 * @author "John McNair" <john@mcnair.org>
 */
public interface ManagedThread extends Runnable {

    /**
   * Notify this thread to stop executing.
   */
    void cancel();

    /**
   * Determine whether or not this thread is running.
   *
   * @return true if this thread is running, false otherwise
   */
    boolean isRunning();
}
