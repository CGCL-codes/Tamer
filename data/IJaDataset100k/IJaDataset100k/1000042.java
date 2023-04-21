package com.rbnb.api;

final class Timer {

    /**
     * is <code>java.util.Timer</code> supported?
     * <p>
     * A value of 0 is unknown, -1 is not supported, and 1 is supported.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 09/03/2002
     */
    private static byte Supported = 0;

    /**
     * priority queue for <code>TimerTasks</code>.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 09/04/2002
     */
    private PriorityQueue queue = null;

    /**
     * the timer thread.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 09/03/2002
     */
    private TimerThread thread = null;

    private IndirectTimer timer = null;

    public Timer() {
        super();
        if (Supported == 1) {
            timer = new IndirectTimer();
        } else if (Supported == -1) {
            queue = new PriorityQueue();
            thread = new TimerThread(queue);
            thread.start();
        } else {
            try {
                timer = new IndirectTimer();
                Supported = 1;
            } catch (java.lang.NoClassDefFoundError e) {
                Supported = -1;
                timer = null;
                queue = new PriorityQueue();
                thread = new TimerThread(queue);
                thread.start();
            }
        }
    }

    public Timer(boolean isDaemonI) {
        super();
        if (Supported == 1) {
            timer = new IndirectTimer(isDaemonI);
        } else if (Supported == -1) {
            queue = new PriorityQueue();
            thread = new TimerThread(queue);
            thread.start();
        } else {
            try {
                timer = new IndirectTimer(isDaemonI);
                Supported = 1;
            } catch (java.lang.NoClassDefFoundError e) {
                Supported = -1;
                timer = null;
                queue = new PriorityQueue();
                thread = new TimerThread(queue);
                thread.start();
            }
        }
    }

    private final void addTask(TimerTask taskI, boolean fixedRateI, long timeI, long periodI) {
        if (thread.cancelled) {
            throw new java.lang.IllegalStateException("Timer has been cancelled.");
        } else if (thread.queue == null) {
            throw new java.lang.IllegalStateException("Timer has terminated.");
        }
        taskI.queue(queue, fixedRateI, timeI, periodI);
        queue.add(taskI);
    }

    public final void cancel() {
        if (timer != null) {
            timer.cancel();
        } else if (Supported == -1) {
            thread.cancel();
            thread = null;
        }
    }

    public final void schedule(TimerTask taskI, long delayI) {
        if (timer != null) {
            timer.schedule(taskI.getTimerTask(), delayI);
        } else if (delayI < 0) {
            throw new java.lang.IllegalArgumentException("Cannot schedule task with a negative delay.");
        } else {
            addTask(taskI, false, System.currentTimeMillis() + delayI, 0);
        }
    }

    public final void schedule(TimerTask taskI, java.util.Date timeI) {
        if (timer != null) {
            timer.schedule(taskI.getTimerTask(), timeI);
        } else {
            addTask(taskI, false, timeI.getTime(), 0);
        }
    }

    public final void schedule(TimerTask taskI, long delayI, long periodI) {
        if (timer != null) {
            timer.schedule(taskI.getTimerTask(), delayI, periodI);
        } else if (delayI < 0) {
            throw new java.lang.IllegalArgumentException("Cannot schedule task with a negative delay.");
        } else {
            addTask(taskI, false, System.currentTimeMillis() + delayI, periodI);
        }
    }

    public final void schedule(TimerTask taskI, java.util.Date timeI, long periodI) {
        if (timer != null) {
            timer.schedule(taskI.getTimerTask(), timeI, periodI);
        } else {
            addTask(taskI, false, timeI.getTime(), periodI);
        }
    }

    public final void scheduleAtFixedRate(TimerTask taskI, long delayI, long periodI) {
        if (timer != null) {
            timer.scheduleAtFixedRate(taskI.getTimerTask(), delayI, periodI);
        } else if (delayI < 0) {
            throw new java.lang.IllegalArgumentException("Cannot schedule task with a negative delay.");
        } else {
            addTask(taskI, true, System.currentTimeMillis() + delayI, periodI);
        }
    }

    public final void scheduleAtFixedRate(TimerTask taskI, java.util.Date timeI, long periodI) {
        if (timer != null) {
            timer.scheduleAtFixedRate(taskI.getTimerTask(), timeI, periodI);
        } else {
            addTask(taskI, true, timeI.getTime(), periodI);
        }
    }
}
