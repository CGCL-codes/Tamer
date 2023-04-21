package org.freelords.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** Collects events and runs them at specified times.
  * 
  * This class contains an internal class ActionReceipt that contains information about a specific
  * thing to do at a specific time, and just works down this list on a separate thread.
  * Note that the main loop is synchronized, so do <em>not</em> specify data
  * that might have to be accessed by other threads.
  * 
  * @author James Andrews
  */
public class TimerCentral {

    /** Contains information about things to run.
	  * 
	  * This information is three-fold. We have the time when the action is schedules, a
	  * Runnable that is run at the specific time, and information about cancellation
	  * of events.
	  */
    public static class ActionReceipt implements Comparable<ActionReceipt>, Runnable {

        /** The runnable */
        private final Runnable runMe;

        /** Starting point in time from now in milliseconds */
        private final long runMeAt;

        /** Flag for indicating that it should be canceled */
        private boolean canceled;

        /** Initialize the ActionReceipt */
        private ActionReceipt(Runnable runMe, long runMeAt) {
            this.runMe = runMe;
            this.runMeAt = runMeAt;
        }

        /** Compares this ActionReceipt to another one */
        @Override
        public int compareTo(ActionReceipt arg0) {
            final long diff = runMeAt - arg0.runMeAt;
            if (diff > 0L) {
                return 1;
            } else if (diff < 0L) {
                return -1;
            }
            return 0;
        }

        /** Performs the action, if it hasn't been canceled before */
        @Override
        public void run() {
            if (!canceled) {
                runMe.run();
            }
        }

        /** Sets status to canceled */
        public void cancel() {
            canceled = true;
        }

        /** Returns if the receipt is canceled */
        public boolean isCanceled() {
            return canceled;
        }
    }

    /** The list of all given ActionReceipts */
    private SortedList<ActionReceipt> eventsToRun = new SortedList<ActionReceipt>();

    /** The thread in which the Actions are started (and run) */
    private final Thread mainThread = new Thread() {

        @Override
        public void run() {
            checkEventLoop();
        }
    };

    /** Starts the thread, which also starts the actions. */
    public void start() {
        mainThread.start();
    }

    /** Interrupts the thread. */
    public void stop() {
        mainThread.interrupt();
    }

    /** Removes all pending events to run. */
    public synchronized void clear() {
        eventsToRun.clear();
    }

    /** Adds another event to run. From this we see, that the unit of our Timer is milliseconds.
	  * 
	  * @param run a Runnable that encapsulates what to do.
	  * @param unit the unit of the time interval when to run the action.
	  * @param time the length of the time interval when to run the action.
	  * @return the assembled ActionReceipt that is scheduled for running.
	  */
    public synchronized ActionReceipt addToFuture(Runnable run, TimeUnit unit, long time) {
        long now = System.currentTimeMillis();
        long runAt = now + unit.toMillis(time);
        ActionReceipt ar = new ActionReceipt(run, runAt);
        eventsToRun.add(ar);
        notifyAll();
        return ar;
    }

    /** Main event loop: Work down the list of things to do. */
    private synchronized void checkEventLoop() {
        while (true) {
            long now = System.currentTimeMillis();
            long waitUntil = Long.MAX_VALUE;
            List<ActionReceipt> runUs = new ArrayList<ActionReceipt>();
            for (Iterator<ActionReceipt> faIt = eventsToRun.iterator(); faIt.hasNext(); ) {
                ActionReceipt fa = faIt.next();
                if (fa.isCanceled()) {
                    faIt.remove();
                    continue;
                }
                if (fa.runMeAt <= now) {
                    faIt.remove();
                    runUs.add(fa);
                } else {
                    waitUntil = Math.min(fa.runMeAt, waitUntil);
                    break;
                }
            }
            run(runUs);
            try {
                if (waitUntil < Long.MAX_VALUE) {
                    waitUntil -= System.currentTimeMillis();
                    if (waitUntil <= 0L) {
                        continue;
                    } else {
                        wait(waitUntil);
                    }
                } else {
                    wait();
                }
            } catch (InterruptedException ie) {
            }
            if (mainThread.isInterrupted()) {
                return;
            }
        }
    }

    /**
	 * Override this if you need run() to occur on a specific thread, e.g.
	 * a Swing or SWT thread
	 * @param runUs A list of runnables
	 */
    protected void run(List<ActionReceipt> runUs) {
        for (ActionReceipt run : runUs) {
            try {
                run.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
