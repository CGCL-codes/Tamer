package org.eclipse.core.internal.jobs;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.core.internal.runtime.Messages;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;

/**
 * A worker thread processes jobs supplied to it by the worker pool.  When
 * the worker pool gives it a null job, the worker dies.
 */
public class Worker extends Thread {

    private static int nextWorkerNumber = 0;

    private volatile InternalJob currentJob;

    private final WorkerPool pool;

    public Worker(WorkerPool pool) {
        super("Worker-" + nextWorkerNumber++);
        this.pool = pool;
        setContextClassLoader(pool.defaultContextLoader);
    }

    /**
	 * Returns the currently running job, or null if none.
	 */
    public Job currentJob() {
        return (Job) currentJob;
    }

    private IStatus handleException(InternalJob job, Throwable t) {
        String message = NLS.bind(Messages.jobs_internalError, job.getName());
        return new Status(IStatus.ERROR, Platform.PI_RUNTIME, Platform.PLUGIN_ERROR, message, t);
    }

    private void log(IStatus result) {
        try {
            final InternalPlatform platform = InternalPlatform.getDefault();
            if (platform.isRunning()) {
                platform.log(result);
                return;
            }
        } catch (RuntimeException e) {
        }
        Throwable t = result.getException();
        if (t != null) t.printStackTrace();
    }

    public void run() {
        setPriority(Thread.NORM_PRIORITY);
        try {
            while ((currentJob = pool.startJob(this)) != null) {
                if (currentJob == null) return;
                currentJob.setThread(this);
                IStatus result = Status.OK_STATUS;
                try {
                    result = currentJob.run(currentJob.getProgressMonitor());
                } catch (OperationCanceledException e) {
                    result = Status.CANCEL_STATUS;
                } catch (Exception e) {
                    result = handleException(currentJob, e);
                } catch (ThreadDeath e) {
                    throw e;
                } catch (Error e) {
                    result = handleException(currentJob, e);
                } finally {
                    Thread.interrupted();
                    if (result == null) result = handleException(currentJob, new NullPointerException());
                    pool.endJob(currentJob, result);
                    if ((result.getSeverity() & (IStatus.ERROR | IStatus.WARNING)) != 0) log(result);
                    currentJob = null;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            currentJob = null;
            pool.endWorker(this);
        }
    }
}
