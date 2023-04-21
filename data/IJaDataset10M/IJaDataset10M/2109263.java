package org.mobicents.slee.resource.cluster;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.timers.FaultTolerantScheduler;
import org.mobicents.timers.PeriodicScheduleStrategy;

/**
 * Implementation of the FT RA Timer.
 * 
 * @author martins
 * 
 */
public class FaultTolerantTimerImpl implements FaultTolerantTimer {

    private final SleeContainer sleeContainer;

    private final String raEntity;

    private FaultTolerantScheduler scheduler;

    /**
	 * 
	 * @param sleeContainer
	 * @param raEntity
	 */
    public FaultTolerantTimerImpl(SleeContainer sleeContainer, String raEntity) {
        this.sleeContainer = sleeContainer;
        this.raEntity = raEntity;
    }

    @Override
    public void configure(FaultTolerantTimerTaskFactory taskFactory, int threads) throws IllegalArgumentException, IllegalStateException, NullPointerException {
        if (isConfigured()) {
            throw new IllegalStateException();
        }
        if (threads < 0) {
            throw new IllegalArgumentException();
        }
        this.scheduler = new FaultTolerantScheduler("fts-raentity-" + raEntity, threads, sleeContainer.getCluster(), (byte) 0, sleeContainer.getTransactionManager().getRealTransactionManager(), new FaultTolerantTimerTaskFactoryWrapper(taskFactory));
    }

    @Override
    public boolean isConfigured() {
        return scheduler != null;
    }

    @Override
    public void schedule(FaultTolerantTimerTask task, long delay, TimeUnit unit) {
        if (!isConfigured()) {
            throw new IllegalStateException();
        }
        if (task == null) {
            throw new NullPointerException("null task");
        }
        long delayMs = unit.toMillis(delay);
        long startTime = System.currentTimeMillis() + delayMs;
        FaultTolerantTimerTaskDataWrapper data = new FaultTolerantTimerTaskDataWrapper(task.getTaskData(), startTime, -1, null);
        FaultTolerantTimerTaskWrapper taskWrapper = new FaultTolerantTimerTaskWrapper(task, data);
        scheduler.schedule(taskWrapper);
    }

    @Override
    public void scheduleAtFixedRate(FaultTolerantTimerTask task, long initialDelay, long period, TimeUnit unit) {
        if (!isConfigured()) {
            throw new IllegalStateException();
        }
        if (task == null) {
            throw new NullPointerException("null task");
        }
        if (period < 0) {
            throw new IllegalArgumentException();
        }
        long initialDelayMs = unit.toMillis(initialDelay);
        long periodMs = unit.toMillis(period);
        long startTime = System.currentTimeMillis() + initialDelayMs;
        FaultTolerantTimerTaskDataWrapper data = new FaultTolerantTimerTaskDataWrapper(task.getTaskData(), startTime, periodMs, PeriodicScheduleStrategy.atFixedRate);
        FaultTolerantTimerTaskWrapper taskWrapper = new FaultTolerantTimerTaskWrapper(task, data);
        scheduler.schedule(taskWrapper);
    }

    @Override
    public void scheduleWithFixedDelay(FaultTolerantTimerTask task, long initialDelay, long delay, TimeUnit unit) {
        if (!isConfigured()) {
            throw new IllegalStateException();
        }
        if (task == null) {
            throw new NullPointerException("null task");
        }
        if (delay < 0) {
            throw new IllegalArgumentException();
        }
        long initialDelayMs = unit.toMillis(initialDelay);
        long delayMs = unit.toMillis(delay);
        long startTime = System.currentTimeMillis() + initialDelayMs;
        FaultTolerantTimerTaskDataWrapper data = new FaultTolerantTimerTaskDataWrapper(task.getTaskData(), startTime, delayMs, PeriodicScheduleStrategy.withFixedDelay);
        FaultTolerantTimerTaskWrapper taskWrapper = new FaultTolerantTimerTaskWrapper(task, data);
        scheduler.schedule(taskWrapper);
    }

    @Override
    public void cancel(Serializable taskID) {
        if (!isConfigured()) {
            throw new IllegalStateException();
        }
        if (taskID == null) {
            throw new NullPointerException("null task id");
        }
        scheduler.cancel(taskID);
    }

    public void shutdown() {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}
