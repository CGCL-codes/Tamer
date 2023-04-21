package org.xvr.xvrengine.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

public abstract class ProcessCommand extends AbstractHandler {

    protected static final int MONITOR_TOTAL_WORK = 100;

    protected static final int MONITOR_10_WORK = 10;

    protected static final int MONITOR_20_WORK = 20;

    protected static final int MONITOR_30_WORK = 30;

    private Job _job;

    public ProcessCommand() {
        this._job = new Job("ProcessCommand Job") {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                return runJob(monitor);
            }
        };
    }

    /**
	 * Executes the {@link Job}.
	 */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        this._job.schedule();
        return null;
    }

    /**
	 * The command executes the method body when called. The default implementation does nothing.
	 * Subclasses must implement the method.
	 * @param monitor the monitor to be used for reporting progress and responding to cancelation. The monitor is never <code>null</code>
	 * @return resulting status of the run. The result must not be <code>null</code>.
	 */
    protected abstract IStatus runJob(IProgressMonitor monitor);

    /**
	 * Sets whether the underline {@link Job} is launched directly by the user or not. 
	 * @param val true if the job is a user-initiated job, false otherwise.
	 */
    public void setUserJob(boolean val) {
        this._job.setUser(val);
    }
}
