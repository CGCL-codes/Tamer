package info.jonclark.util;

import java.util.*;

/**
 * A simple way of determining if a task has timed out. If the length of time
 * specified by millis passes without the refresh() method being called
 * then all TimeoutListeneres are notified that the task has become stale.
 */
public class Timeout {

    private static final int DEFAULT_RESOLUTION_DIV = 10;

    private final Vector<TimeoutListener> vListeners = new Vector<TimeoutListener>(1);

    private static final Timer timer = new Timer();

    private Date refreshed = new Date();

    private boolean notified = false;

    private TimerTask task = null;

    /**
     * Set the timeout for this timeout using the default
     * resolution.
     * 
     * @param timeoutMillis
     */
    public void setTimeout(long timeoutMillis) {
        setTimeout(timeoutMillis, timeoutMillis / DEFAULT_RESOLUTION_DIV);
    }

    /**
     * Set the timeout period for this timer specifying a resolution
     * 
     * Note the longest time that can pass before the timeout will
     * detect a stall is equal to timeoutMillis + resolutionMillis - 1ms.
     * 
     * @param timeoutMillis
     * @param resolutionMillis
     */
    private void setTimeout(final long timeoutMillis, final long resolutionMillis) {
        if (task != null) task.cancel();
        if (timeoutMillis > 0 && resolutionMillis > 0) {
            task = new TimerTask() {

                public void run() {
                    final Date now = new Date();
                    if (now.getTime() - refreshed.getTime() > timeoutMillis) {
                        notifyTimeoutListeners();
                    }
                }
            };
            timer.schedule(task, resolutionMillis, resolutionMillis);
        }
    }

    /**
     * Disables this timeout
     */
    public void cancel() {
        if (task != null) task.cancel();
    }

    /**
     * Notifies the timer that the task is not stalled so
     * that the timeout will not fire.
     */
    public void refresh() {
        notified = false;
        refreshed = new Date();
    }

    /**
     * Adds a listener that will be notified of timeouts
     * @param t The TimeoutListener that will be notified
     */
    public void addTimeoutListener(TimeoutListener t) {
        vListeners.add(t);
    }

    /**
     * Notify all timeout listeners that a timeout has occurred
     */
    private void notifyTimeoutListeners() {
        if (!notified) {
            notified = true;
            for (TimeoutListener t : vListeners) t.taskStalled();
        }
    }
}
