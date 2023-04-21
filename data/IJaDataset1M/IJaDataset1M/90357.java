package org.apache.commons.exec;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Generalization of <code>ExecuteWatchdog</code>
 * 
 * @see org.apache.commons.exec.ExecuteWatchdog
 */
public class Watchdog implements Runnable {

    private Vector observers = new Vector(1);

    private final long timeout;

    private boolean stopped = false;

    public Watchdog(final long timeout) {
        if (timeout < 1) {
            throw new IllegalArgumentException("timeout must not be less than 1.");
        }
        this.timeout = timeout;
    }

    public void addTimeoutObserver(final TimeoutObserver to) {
        observers.addElement(to);
    }

    public void removeTimeoutObserver(final TimeoutObserver to) {
        observers.removeElement(to);
    }

    protected final void fireTimeoutOccured() {
        Enumeration e = observers.elements();
        while (e.hasMoreElements()) {
            ((TimeoutObserver) e.nextElement()).timeoutOccured(this);
        }
    }

    public synchronized void start() {
        stopped = false;
        Thread t = new Thread(this, "WATCHDOG");
        t.setDaemon(true);
        t.start();
    }

    public synchronized void stop() {
        stopped = true;
        notifyAll();
    }

    public synchronized void run() {
        final long until = System.currentTimeMillis() + timeout;
        long now;
        while (!stopped && until > (now = System.currentTimeMillis())) {
            try {
                wait(until - now);
            } catch (InterruptedException e) {
            }
        }
        if (!stopped) {
            fireTimeoutOccured();
        }
    }
}
