package org.go;

import org.go.expcetion.SchedulerException;

/**
 * The interface to be implemented by classes that want to be informed of major
 * <code>{@link Scheduler}</code> events.
 * 
 * @see Scheduler
 * @see JobListener
 * @see TriggerListener
 * 
 * @author hejie
 */
public interface SchedulerListener {

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> when a <code>{@link org.quartz.JobDetail}</code>
	 * is scheduled.
	 * </p>
	 */
    void jobScheduled(Trigger trigger);

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> when a 
	 * group of <code>{@link org.quartz.JobDetail}s</code> has been paused.
	 * </p>
	 * 
	 * @param jobGroup the paused group, or null if all were paused
	 */
    void jobsPaused(String jobGroup);

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> when a 
	 * group of <code>{@link org.quartz.JobDetail}s</code> has been un-paused.
	 * </p>
	 */
    void jobsResumed(String jobGroup);

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> when a <code>{@link org.quartz.JobDetail}</code>
	 * is unscheduled.
	 * </p>
	 * 
	 * @see SchedulerListener#schedulingDataCleared()
	 */
    void jobUnscheduled(TriggerKey triggerKey);

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> when a serious error has
	 * occurred within the scheduler - such as repeated failures in the <code>JobStore</code>,
	 * or the inability to instantiate a <code>Job</code> instance when its
	 * <code>Trigger</code> has fired.
	 * </p>
	 * 
	 * <p>
	 * The <code>getErrorCode()</code> method of the given SchedulerException
	 * can be used to determine more specific information about the type of
	 * error that was encountered.
	 * </p>
	 */
    void schedulerError(String msg, SchedulerException cause);

    /**
	 * 
	 */
    void schedulerInited();

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> to inform the listener
	 * that it has move to standby mode.
	 * </p>
	 */
    void schedulerInStandbyMode();

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> to inform the listener
	 * that it has shutdown.
	 * </p>
	 */
    void schedulerShutdown();

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> to inform the listener
	 * that it has begun the shutdown sequence.
	 * </p>
	 */
    void schedulerShuttingdown();

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> to inform the listener
	 * that it has started.
	 * </p>
	 */
    void schedulerStarted();

    /**
	 * Called by the <code>{@link Scheduler}</code> to inform the listener
	 * that all jobs, triggers and calendars were deleted.
	 */
    void schedulingDataCleared();

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> when a <code>{@link Trigger}</code>
	 * has reached the condition in which it will never fire again.
	 * </p>
	 */
    void triggerFinalized(Trigger trigger);

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> when a <code>{@link Trigger}</code>
	 * has been paused.
	 * </p>
	 */
    void triggerPaused(TriggerKey triggerKey);

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> when a <code>{@link Trigger}</code>
	 * has been un-paused.
	 * </p>
	 */
    void triggerResumed(TriggerKey triggerKey);

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> when a 
	 * group of <code>{@link Trigger}s</code> has been paused.
	 * </p>
	 * 
	 * <p>If all groups were paused then triggerGroup will be null</p>
	 * 
	 * @param triggerGroup the paused group, or null if all were paused
	 */
    void triggersPaused(String triggerGroup);

    /**
	 * <p>
	 * Called by the <code>{@link Scheduler}</code> when a 
	 * group of <code>{@link Trigger}s</code> has been un-paused.
	 * </p>
	 */
    void triggersResumed(String triggerGroup);
}
