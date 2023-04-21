package org.eclipse.core.internal.jobs;

import java.util.*;
import org.eclipse.core.internal.runtime.Assert;
import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;

/**
 * Implicit jobs are jobs that are running by virtue of a JobManager.begin/end
 * pair. They act like normal jobs, except they are tied to an arbitrary thread
 * of the client's choosing, and they can be nested.
 */
class ImplicitJobs {

    /**
	 * Cached unused instance that can be reused 
	 */
    private ThreadJob jobCache = null;

    protected JobManager manager;

    /**
	 * Set of suspended scheduling rules.
	 */
    private final Set suspendedRules = new HashSet(20);

    /**
	 * Maps (Thread->ThreadJob), threads to the currently running job for that
	 * thread.
	 */
    private final Map threadJobs = new HashMap(20);

    ImplicitJobs(JobManager manager) {
        this.manager = manager;
    }

    void begin(ISchedulingRule rule, IProgressMonitor monitor, boolean suspend) {
        if (JobManager.DEBUG_BEGIN_END) JobManager.debug("Begin rule: " + rule);
        final Thread currentThread = Thread.currentThread();
        ThreadJob threadJob;
        synchronized (this) {
            threadJob = (ThreadJob) threadJobs.get(currentThread);
            if (threadJob != null) {
                threadJob.push(rule);
                return;
            }
            if (rule == null) return;
            Job realJob = manager.currentJob();
            if (realJob != null && realJob.getRule() != null) threadJob = newThreadJob(realJob.getRule()); else {
                threadJob = newThreadJob(rule);
                threadJob.acquireRule = true;
            }
            if (rule != null && isSuspended(rule)) threadJob.acquireRule = false;
            threadJob.setRealJob(realJob);
            threadJob.setThread(currentThread);
        }
        try {
            threadJob.push(rule);
            if (threadJob.acquireRule) {
                if (manager.runNow(threadJob)) manager.getLockManager().addLockThread(Thread.currentThread(), rule); else threadJob.joinRun(monitor);
            }
        } finally {
            synchronized (this) {
                threadJobs.put(currentThread, threadJob);
                if (suspend && rule != null) suspendedRules.add(rule);
            }
            if (threadJob.isBlocked) {
                threadJob.isBlocked = false;
                manager.reportUnblocked(monitor);
            }
        }
    }

    synchronized void end(ISchedulingRule rule, boolean resume) {
        if (JobManager.DEBUG_BEGIN_END) JobManager.debug("End rule: " + rule);
        ThreadJob threadJob = (ThreadJob) threadJobs.get(Thread.currentThread());
        if (threadJob == null) Assert.isLegal(rule == null, "endRule without matching beginRule: " + rule); else if (threadJob.pop(rule)) {
            endThreadJob(threadJob, resume);
        }
    }

    /**
	 * Called when a worker thread has finished running a job. At this
	 * point, the worker thread must not own any scheduling rules
	 * @param lastJob The last job to run in this thread
	 */
    void endJob(InternalJob lastJob) {
        final Thread currentThread = Thread.currentThread();
        IStatus error;
        synchronized (this) {
            ThreadJob threadJob = (ThreadJob) threadJobs.get(currentThread);
            if (threadJob == null) return;
            String msg = "Worker thread ended job: " + lastJob + ", but still holds rule: " + threadJob;
            error = new Status(IStatus.ERROR, Platform.PI_RUNTIME, 1, msg, null);
            endThreadJob(threadJob, false);
        }
        try {
            InternalPlatform.getDefault().log(error);
        } catch (RuntimeException e) {
            System.err.println(error.getMessage());
        }
    }

    private void endThreadJob(ThreadJob threadJob, boolean resume) {
        Thread currentThread = Thread.currentThread();
        threadJobs.remove(currentThread);
        ISchedulingRule rule = threadJob.getRule();
        if (resume && rule != null) suspendedRules.remove(rule);
        if (threadJob.acquireRule) manager.getLockManager().removeLockThread(currentThread, rule);
        if (threadJob.isRunning()) manager.endJob(threadJob, Status.OK_STATUS, false);
        recycle(threadJob);
    }

    /**
	 * Returns true if this rule has been suspended, and false otherwise.
	 */
    private boolean isSuspended(ISchedulingRule rule) {
        if (suspendedRules.size() == 0) return false;
        for (Iterator it = suspendedRules.iterator(); it.hasNext(); ) if (((ISchedulingRule) it.next()).contains(rule)) return true;
        return false;
    }

    /**
	 * Returns a new or reused ThreadJob instance. 
	 */
    private ThreadJob newThreadJob(ISchedulingRule rule) {
        if (jobCache != null) {
            ThreadJob job = jobCache;
            job.setRule(rule);
            job.acquireRule = job.isRunning = false;
            job.realJob = null;
            jobCache = null;
            return job;
        }
        return new ThreadJob(manager, rule);
    }

    /**
	 * Indicates that a thread job is no longer in use and can be reused. 
	 */
    private void recycle(ThreadJob job) {
        if (jobCache == null && job.recycle()) jobCache = job;
    }

    /**
	 * Implements IJobManager#resume(ISchedulingRule)
	 * @param rule
	 */
    void resume(ISchedulingRule rule) {
        end(rule, true);
        if (JobManager.DEBUG_BEGIN_END) JobManager.debug("Resume rule: " + rule);
    }

    /**
	 * Implements IJobManager#suspend(ISchedulingRule, IProgressMonitor)
	 * @param rule
	 * @param monitor
	 */
    void suspend(ISchedulingRule rule, IProgressMonitor monitor) {
        if (JobManager.DEBUG_BEGIN_END) JobManager.debug("Suspend rule: " + rule);
        begin(rule, monitor, true);
    }

    /**
	 * Implements IJobManager#transferRule(ISchedulingRule, Thread)
	 */
    synchronized void transfer(ISchedulingRule rule, Thread destinationThread) {
        if (rule == null) return;
        final Thread currentThread = Thread.currentThread();
        if (currentThread == destinationThread) return;
        ThreadJob job = (ThreadJob) threadJobs.get(destinationThread);
        Assert.isLegal(job == null);
        job = (ThreadJob) threadJobs.get(currentThread);
        Assert.isLegal(job != null);
        Assert.isLegal(job.getRule() == rule);
        job.setThread(destinationThread);
        threadJobs.remove(currentThread);
        threadJobs.put(destinationThread, job);
        if (job.acquireRule) {
            manager.getLockManager().removeLockThread(currentThread, rule);
            manager.getLockManager().addLockThread(destinationThread, rule);
        }
    }
}
