package com.adam.framework.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.Date;

/**
 * A simple replacement for the java.util.Timer class in JDK 1.3.
 * <P>
 * Functionally this class performs similarly to the java.util.Timer
 * class in JDK 1.3 although it probably is not as robust. The main
 * difference is that it uses a simple List for its task queue that
 * it traverses linearly, so it may not scale as well as the 1.3
 * version.
 * <P>
 * Refer to the JDK 1.3 documentation for a full description of how
 * this works.
 *
 * @author	Joseph Mocker
 */
public class Timer {

    private TimerEngine timer = null;

    protected List tasks = null;

    private Object lock = new Object();

    /**
    * Create a new Timer.
    */
    public Timer() {
        this(false);
    }

    /**
    * Create a new Timer. Additionally specify if the underlying
    * timer thread is a daemon thread or not.
    *
    * @param isDaemon true if timer thread should run as a daemon.
    */
    public Timer(boolean isDaemon) {
        tasks = new ArrayList();
        timer = new TimerEngine();
        timer.setDaemon(isDaemon);
        timer.start();
    }

    /**
    * Schedule a single-execution task after a delay.
    *
    * @param task task to be scheduled.
    * @param delay delay in milliseconds before task is to be executed.
    */
    public void schedule(TimerTask task, long delay) {
        schedule(task, System.currentTimeMillis() + delay, 0);
    }

    /**
    * Schedule a single-execution task at a specific time.
    *
    * @param task task to be scheduled.
    * @param time time task is to be executed.
    */
    public void schedule(TimerTask task, Date time) {
        schedule(task, time.getTime() - System.currentTimeMillis(), 0);
    }

    /**
    * Schedule a fixed-delay task beginning at a specific time.
    *
    * @param task task to be scheduled.
    * @param firstTime time the task is to be first executed.
    * @param period time in milliseconds betweek successive task executions.
    */
    public void schedule(TimerTask task, Date firstTime, long period) {
        schedule(task, firstTime.getTime() - System.currentTimeMillis(), period);
    }

    /**
     * Schedule a fixed-delay task after a delay.
     *
     * @param task task to be scheduled.
     * @param delay delay in milliseconds before task is to be executed.
     * @param period time in milliseconds betweek successive task executions.
     */
    public void schedule(TimerTask task, long delay, long period) {
        if (timer.isCancelled()) {
            throw new IllegalStateException();
        }
        task.schedule(delay, period, true);
        synchronized (lock) {
            tasks.add(task);
        }
        timer.wakeUp();
    }

    /**
    * Schedule a fixed-rate task beginning at a specific time.
    *
    * @param task task to be scheduled.
    * @param firstTime time the task is to be first executed.
    * @param period time in milliseconds betweek successive task executions.
    */
    public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
        scheduleAtFixedRate(task, firstTime.getTime() - System.currentTimeMillis(), period);
    }

    /**
    * Schedule a fixed-rate task after a delay.
    *
    * @param task task to be scheduled.
    * @param delay delay in milliseconds before task is to be executed.
    * @param period time in milliseconds betweek successive task executions.
    */
    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        if (timer.isCancelled()) {
            throw new IllegalStateException();
        }
        task.schedule(delay, period, false);
        synchronized (lock) {
            tasks.add(task);
        }
        timer.wakeUp();
    }

    /**
    * Terminates this timer.
    */
    public void cancel() {
        timer.cancel();
    }

    /**
    * Execution and Scheduling engine for the Timer object.
    * <P>
    * This class performs the scheduling and execution of all the
    * TimerTasks that have been added to the parent Timer object.
    *
    * @author	Joseph Mocker
    */
    protected class TimerEngine extends Thread {

        private boolean cancelled = false;

        public void run() {
            while (!cancelled) {
                TimerTask current = null;
                synchronized (lock) {
                    for (Iterator i = tasks.iterator(); i.hasNext(); ) {
                        TimerTask task = (TimerTask) i.next();
                        if (task.scheduledExecutionTime() == 0 || task.isCancelled()) {
                            i.remove();
                            continue;
                        }
                        if (task.scheduledExecutionTime() <= System.currentTimeMillis()) {
                            task.run();
                            task.reschedule();
                            if (task.scheduledExecutionTime() == 0) {
                                continue;
                            }
                        }
                        if (current == null) {
                            current = task;
                        } else if (task.scheduledExecutionTime() < current.scheduledExecutionTime()) {
                            current = task;
                        }
                    }
                }
                synchronized (this) {
                    try {
                        if (current != null) {
                            long timeout = current.scheduledExecutionTime() - System.currentTimeMillis();
                            if (timeout > 0) {
                                wait(timeout);
                            }
                        } else {
                            wait();
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        protected synchronized void wakeUp() {
            notifyAll();
        }

        protected synchronized void cancel() {
            cancelled = true;
            wakeUp();
        }

        protected synchronized boolean isCancelled() {
            return cancelled;
        }
    }
}
