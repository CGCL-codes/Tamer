package org.ws4d.java.util.concurrency;

import org.ws4d.java.logging.Log;
import org.ws4d.java.util.concurrency.TimedSemaphore.WaitTimeExpiredException;

/**
 * Provides mechanism for module thread synchronization via semaphore. Stops
 * module via system exit, if any semaphore subscriber is not ready in wait time
 * If run is set to true in any module, all other users of the same semaphore
 * must set run to true in defined wait time or system is stopped via
 * system.exit();
 */
public abstract class AbstractSemaphoreUser {

    /** Default max time for waiting until end of start. */
    private static final long WAIT_START_TIME = 5000;

    /** Max time for waiting until end of start. */
    private long waitStartTime = WAIT_START_TIME;

    /** Semaphore for synchronization of module extending this class. */
    private TimedSemaphore startSemaphore;

    /**
	 * If <code>false</code> stop the running thread (e.g.
	 * receiver/connection).
	 */
    private boolean run = false;

    /**
	 * Returns <code>true</code> if module is running, <code>false</code>
	 * otherwise.
	 * 
	 * @return indicates whether the module is running, or not.
	 */
    public final boolean isRunning() {
        return run;
    }

    /**
	 * Set running mode of module. If set running mode =
	 * <code>true<code> semaphore is 
	 * checked (if set).
	 * 
	 * Semaphore notifier added (only if changed to false).
	 * If changed to run and other semaphore users aren't ready in
	 * wait time, module is stopped via system exit.
	 * 
	 * @param running <code>true</code>
	 */
    public final void setRunning(boolean running) {
        if (running && !this.run) {
            this.run = true;
            if (startSemaphore != null) {
                try {
                    startSemaphore.ready(waitStartTime);
                } catch (WaitTimeExpiredException e) {
                    Log.error("ERROR:AbstractSemaphoreUser: Modul start takes too much time ( > " + waitStartTime + "): " + e.getMessage());
                    System.exit(0);
                }
            }
        } else if (!running && this.run) {
            this.run = false;
            if (startSemaphore != null) {
                startSemaphore.addNotifier();
            }
        }
    }

    /**
	 * Sets semaphore in module. If semaphore is changed old notifier is removed
	 * and new added.
	 * 
	 * @param startSemaphore semaphore to wait.
	 */
    public final void setSemaphore(TimedSemaphore startSemaphore) {
        if (this.startSemaphore != null) {
            this.startSemaphore.removeNotifier();
            this.startSemaphore = startSemaphore;
            if (startSemaphore != null) {
                this.startSemaphore.addNotifier();
            }
        } else {
            this.startSemaphore = startSemaphore;
            if (startSemaphore != null) {
                this.startSemaphore.addNotifier();
            }
        }
    }

    /**
	 * Sets time in milliseconds to max wait.
	 * 
	 * @param milliseconds time to wait.
	 */
    public final void setMaxWaitStartTime(long milliseconds) {
        this.waitStartTime = milliseconds;
    }
}
