package purej.job;

/**
 * <p>
 * The interface to be implemented by classes that want to be informed when a
 * <code>{@link purej.job.JobDetail}</code> executes. In general,
 * applications that use a <code>Scheduler</code> will not have use for this
 * mechanism.
 * </p>
 * 
 * @see Scheduler
 * @see Job
 * @see JobExecutionContext
 * @see JobExecutionException
 * @see TriggerListener
 * 
 * @author James House
 */
public interface JobListener {

    /**
     * <p>
     * Get the name of the <code>JobListener</code>.
     * </p>
     */
    public String getName();

    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> when a
     * <code>{@link purej.job.JobDetail}</code> is about to be
     * executed (an associated <code>{@link Trigger}</code> has occured).
     * </p>
     * 
     * <p>
     * This method will not be invoked if the execution of the Job was vetoed by
     * a <code>{@link TriggerListener}</code>.
     * </p>
     * 
     * @see #jobExecutionVetoed(JobExecutionContext)
     */
    public void jobToBeExecuted(JobExecutionContext context);

    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> when a
     * <code>{@link purej.job.JobDetail}</code> was about to be
     * executed (an associated <code>{@link Trigger}</code> has occured), but
     * a <code>{@link TriggerListener}</code> vetoed it's execution.
     * </p>
     * 
     * @see #jobToBeExecuted(JobExecutionContext)
     */
    public void jobExecutionVetoed(JobExecutionContext context);

    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> after a
     * <code>{@link purej.job.JobDetail}</code> has been
     * executed, and be for the associated <code>Trigger</code>'s
     * <code>triggered(xx)</code> method has been called.
     * </p>
     */
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException);
}
