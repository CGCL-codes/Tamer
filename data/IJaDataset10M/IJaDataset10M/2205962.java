package org.draycasejr.war.base.utility;

/**
 * @author DCASE * project: chatty0_2_0 * created: Nov 4, 2004
 */
public class TimeoutTimer implements Runnable {

    private TimeoutListener listener;

    private TimeoutEvent timeout;

    public TimeoutTimer(TimeoutListener toListener, TimeoutEvent toEvent) {
        this.listener = toListener;
        this.timeout = toEvent;
    }

    public void run() {
        try {
            do {
                Thread.sleep(timeout.getInterval());
                timeout.increment();
                listener.timedOut(timeout);
            } while (timeout.isContinuous());
        } catch (InterruptedException e) {
        }
    }
}
