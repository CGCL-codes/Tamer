package org.eclipse.core.runtime.jobs;

import org.eclipse.core.runtime.IStatus;

/**
 * An event describing a change to the state of a job.
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 * @see IJobChangeListener
 * @since 3.0
 */
public interface IJobChangeEvent {

    /**
	 * The amount of time in milliseconds to wait after scheduling the job before it 
	 * should be run, or <code>-1</code> if not applicable for this type of event.  
	 * This value is only applicable for the <code>scheduled</code> event.
	 * 
	 * @return the delay time for this event
	 */
    public long getDelay();

    /**
	 * The job on which this event occurred.
	 * 
	 * @return the job for this event
	 */
    public Job getJob();

    /**
	 * The result returned by the job's run method, or <code>null</code> if
	 * not applicable.  This value is only applicable for the <code>done</code> event.
	 * 
	 * @return the status for this event
	 */
    public IStatus getResult();
}
