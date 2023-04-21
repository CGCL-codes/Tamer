package org.dfdaemon.il2.core.scheduler;

import java.util.Date;

/**
 * Timer service for scheduler management.
 *
 * @author octo
 */
public interface Scheduler {

    /**
     * Schedule with specified taskIterator for dynamic durations control.
     *
     * @param schedulerRunnable to execute
     * @param schedulerIterator date iterator
     * @return instance, with which task can be controlled
     */
    SchedulerTask schedule(SchedulerRunnable schedulerRunnable, SchedulerIterator schedulerIterator);

    /**
     * Schedule with specified taskIterator for dynamic durations control.
     *
     * @param schedulerRunnable to execute
     * @param first             start
     * @param schedulerIterator date iterator
     * @return instance, with which task can be controlled
     */
    SchedulerTask schedule(SchedulerRunnable schedulerRunnable, Date first, SchedulerIterator schedulerIterator);

    /**
     * Schedule periodic task with specified delay and period.
     *
     * @param schedulerRunnable to execute
     * @param delay             delay in milliseconds before task is to be executed.
     * @param period            time in milliseconds between successive task executions.
     * @return instance, with which task can be controlled
     */
    SchedulerTask schedule(SchedulerRunnable schedulerRunnable, long delay, long period);

    /**
     * Schedule delayed task.
     *
     * @param schedulerRunnable to execute
     * @param delay             delay in milliseconds before task is to be executed.
     * @return instance, with which task can be controlled
     */
    SchedulerTask schedule(SchedulerRunnable schedulerRunnable, long delay);
}
