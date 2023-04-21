package com.volantis.mps.message.store;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This daemon runs perodically after being {@link #start}ed until  it is
 * {@link #stop}ped.  Whilst it is running it executes the task provided to
 * it each time it is invoked.  To allow clean VM exit it is necessary to use
 * the {@link #stop} method because the timer thread used does not (according
 * to Sun's JavaDoc) run as a daemon thread.
 */
public final class MessageStoreDaemon {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The timer that is used to repeatedly schedule the execution of the
     * {@link #task} provided.  This will not run until {@link #start} has been
     * called and should be cancelled by calling {@link #stop}.
     */
    private final Timer timer = new Timer();

    /**
     * The interval, in minutes, that the task provided should be executed.
     * Care should be taken over the repetition time and the runtime of the
     * task to ensure the task can complete.
     */
    private final int minutes;

    /**
     * The task to be executed each time period that this daemon is scheduled
     * for activity, based on the {@link #minutes} specified.
     */
    private final TimerTask task;

    /**
     * The delay before the first task is scheduled and thereafter how often
     * the task is scheduled to run.  This reuires the is {@link #minutes} set.
     */
    protected int delayAndPeriod = -1;

    /**
     * Initialise the message store daemon with the repeat time that it should
     * operate on, the task it should execute each time.
     *
     * @param minutes The number of minutes between invokations of the task.
     * @param task    The task to invoke.
     */
    public MessageStoreDaemon(int minutes, TimerTask task) {
        this.minutes = minutes;
        this.task = task;
        initialiseDelay();
    }

    /**
     * Initialise the delay period if it has not already been done.
     * -1 is used to indicate that the value needs to be (re)calculated.
     *
     */
    private void initialiseDelay() {
        delayAndPeriod = minutes * 60 * 1000;
    }

    /**
     * Start the message store daemon.
     */
    public void start() {
        if (delayAndPeriod == -1) {
            initialiseDelay();
        }
        timer.schedule(task, delayAndPeriod, delayAndPeriod);
    }

    /**
     * Stop the message store daemon.  This can be called multiple times on
     * the same daemon without any problem.
     */
    public void stop() {
        task.cancel();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        stop();
    }
}
