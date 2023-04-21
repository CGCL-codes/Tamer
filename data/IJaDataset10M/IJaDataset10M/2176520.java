package org.mobicents.slee.resource.jdbc.task;

/**
 * A task to be executed asynchronously by the JDBC RA.
 * 
 * @author martins
 * 
 */
public interface JdbcTask {

    /**
	 * Invoked by the JDBC RA, requests asynchronously execution of the task
	 * logic.
	 * 
	 * 
	 * @param taskContext
	 *            the context provided by the RA to help the task execution.
	 * 
	 * @return the result of the task execution, which if not null, and valid
	 *         (not null event object and type), will be used by the RA to fire
	 *         an event into the SLEE.
	 */
    public JdbcTaskResult execute(JdbcTaskContext taskContext);
}
