package com.narirelays.ems.utils.watcher;

public abstract class IntervalThread implements Runnable {

    /**
     * Whether or not this thread is active.
     */
    private volatile boolean active = false;

    /**
     * The interval in seconds to run this thread
     */
    private int interval = -1;

    /**
     * The name of this pool (for loggin/display purposes).
     */
    private String name;

    /**
     * This instance's thread
     */
    private Thread runner;

    /**
     * Construct a new interval thread that will run on the given interval
     * with the given name.
     *
     * @param intervalSeconds the number of seconds to run the thread on
     * @param name            the name of the thread
     */
    public IntervalThread(int intervalSeconds, String name) {
        this.interval = intervalSeconds * 1000;
        this.name = name;
    }

    /**
     * Start the thread on the specified interval.
     */
    public void start() {
        active = true;
        if (runner == null && interval > 0) {
            runner = new Thread(this);
            runner.start();
        }
    }

    /**
     * Stop the interval thread.
     */
    public void stop() {
        active = false;
        if (runner != null) {
            runner.interrupt();
            runner = null;
        }
    }

    /**
     * Not for public use.  This thread is automatically
     * started when a new instance is retrieved with the getInstance method.
     * Use the start() and stop() methods to control the thread.
     *
     * @see Thread#run()
     */
    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        while (active) {
            try {
                doInterval();
                Thread.sleep(interval);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * String representation of this object.  Just the name given to it an
     * instantiation.
     *
     * @return the string name of this pool
     */
    public String toString() {
        return name;
    }

    /**
     * The interval has expired and now it's time to do something.
     */
    protected abstract void doInterval();
}
