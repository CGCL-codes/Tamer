package purej.job.core;

import purej.job.Scheduler;
import purej.job.SchedulerConfigException;
import purej.job.SchedulerException;

/**
 * <p>
 * Responsible for creating the instances of <code>{@link JobRunShell}</code>
 * to be used within the <class>{@link JobScheduler}</code> instance.
 * </p>
 * 
 * <p>
 * Although this interface looks a lot like an 'object pool', implementations do
 * not have to support the re-use of instances. If an implementation does not
 * wish to pool instances, then the <code>borrowJobRunShell()</code> method
 * would simply create a new instance, and the <code>returnJobRunShell </code>
 * method would do nothing.
 * </p>
 * 
 * @author James House
 */
public interface JobRunShellFactory {

    /**
     * <p>
     * Initialize the factory, providing a handle to the <code>Scheduler</code>
     * that should be made available within the <code>JobRunShell</code> and
     * the <code>JobExecutionCOntext</code> s within it, and a handle to the
     * <code>SchedulingContext</code> that the shell will use in its own
     * operations with the <code>JobStore</code>.
     * </p>
     */
    public void initialize(Scheduler scheduler, SchedulingContext schedCtxt) throws SchedulerConfigException;

    /**
     * <p>
     * Called by the
     * <code>{@link purej.job.core.JobSchedulerThread}</code> to
     * obtain instances of <code>{@link JobRunShell}</code>.
     * </p>
     */
    public JobRunShell borrowJobRunShell() throws SchedulerException;

    /**
     * <p>
     * Called by the
     * <code>{@link purej.job.core.JobSchedulerThread}</code> to
     * return instances of <code>{@link JobRunShell}</code>.
     * </p>
     */
    public void returnJobRunShell(JobRunShell jobRunShell);
}
