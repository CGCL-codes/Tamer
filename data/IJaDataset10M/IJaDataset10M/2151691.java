package org.go;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author hejie
 *
 */
public interface GoContext {

    /**
	 * Get the value with the given key from the context's data map.
	 * 
	 * @param key
	 */
    Object get(Object key);

    /**
	 * <p>
	 * Get a handle to the <code>Calendar</code> referenced by the <code>Trigger</code>
	 * instance that fired the <code>Job</code>.
	 * </p>
	 */
    Calendar getCalendar();

    /**
	 * The actual time the trigger fired. For instance the scheduled time may
	 * have been 10:00:00 but the actual fire time may have been 10:00:03 if
	 * the scheduler was too busy.
	 * 
	 * @return Returns the fireTime.
	 * @see #getScheduledFireTime()
	 */
    Date getFireTime();

    /**
	 * The amount of time the job ran for (in milliseconds).  The returned 
	 * value will be -1 until the job has actually completed (or thrown an 
	 * exception), and is therefore generally only useful to 
	 * <code>JobListener</code>s and <code>TriggerListener</code>s.
	 * 
	 * @return Returns the jobRunTime.
	 */
    long getJobRunTime();

    WorkDataMap getMergedJobDataMap();

    Date getNextFireTime();

    Date getPreviousFireTime();

    int getRefireCount();

    /**
	 * Returns the result (if any) that the <code>Job</code> set before its 
	 * execution completed (the type of object set as the result is entirely up 
	 * to the particular job).
	 * 
	 * <p>
	 * The result itself is meaningless to Quartz, but may be informative
	 * to <code>{@link GoListener}s</code> or 
	 * <code>{@link TriggerListener}s</code> that are watching the job's 
	 * execution.
	 * </p> 
	 * 
	 * @return Returns the result.
	 */
    Object getResult();

    /**
	 * The scheduled time the trigger fired for. For instance the scheduled
	 * time may have been 10:00:00 but the actual fire time may have been
	 * 10:00:03 if the scheduler was too busy.
	 * 
	 * @return Returns the scheduledFireTime.
	 * @see #getFireTime()
	 */
    Date getScheduledFireTime();

    /**
	 * <p>
	 * Get a handle to the <code>Scheduler</code> instance that fired the
	 * <code>Job</code>.
	 * </p>
	 */
    Scheduler getScheduler();

    /**
	 * <p>
	 * Get a handle to the <code>Trigger</code> instance that fired the
	 * <code>Job</code>.
	 * </p>
	 */
    Trigger getTrigger();

    /**
	 * <p>
	 * If the <code>Job</code> is being re-executed because of a 'recovery'
	 * situation, this method will return <code>true</code>.
	 * </p>
	 */
    boolean isRecovering();

    /**
	 * Put the specified value into the context's data map with the given key.
	 * Possibly useful for sharing data between listeners and jobs.
	 *
	 * <p>NOTE: this data is volatile - it is lost after the job execution
	 * completes, and all TriggerListeners and JobListeners have been 
	 * notified.</p> 
	 *  
	 * @param key
	 * @param value
	 */
    void put(Object key, Object value);

    /**
	 * Set the result (if any) of the <code>Job</code>'s execution (the type of 
	 * object set as the result is entirely up to the particular job).
	 * 
	 * <p>
	 * The result itself is meaningless to Quartz, but may be informative
	 * to <code>{@link GoListener}s</code> or 
	 * <code>{@link TriggerListener}s</code> that are watching the job's 
	 * execution.
	 * </p> 
	 */
    void setResult(Object result);
}
