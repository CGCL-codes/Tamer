package org.mobicents.timers;

/**
 * Base class for {@link Runnable}'s to be run after tx commit.
 * @author martins
 *
 */
public abstract class AfterTxCommitRunnable implements Runnable {

    public enum Type {

        SET, CANCEL
    }

    protected final TimerTask task;

    protected final FaultTolerantScheduler scheduler;

    public AfterTxCommitRunnable(TimerTask task, FaultTolerantScheduler scheduler) {
        this.task = task;
        this.scheduler = scheduler;
    }

    public abstract Type getType();
}
