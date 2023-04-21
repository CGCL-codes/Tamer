package org.eclipse.core.internal.jobs;

import java.text.*;
import java.util.*;
import org.eclipse.core.internal.runtime.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.*;
import org.eclipse.osgi.util.NLS;

/**
 * Implementation of API type IJobManager
 * 
 * Implementation note: all the data structures of this class are protected
 * by a single lock object held as a private field in this class.  The JobManager
 * instance itself is not used because this class is publicly reachable, and third
 * party clients may try to sychronize on it.
 * 
 * The WorkerPool class uses its own monitor for synchronizing its data
 * structures. To avoid deadlock between the two classes, the JobManager
 * must NEVER call the worker pool while its own monitor is held.
 */
public class JobManager implements IJobManager {

    private static final String OPTION_DEADLOCK_ERROR = Platform.PI_RUNTIME + "/jobs/errorondeadlock";

    private static final String OPTION_DEBUG_BEGIN_END = Platform.PI_RUNTIME + "/jobs/beginend";

    private static final String OPTION_DEBUG_JOBS = Platform.PI_RUNTIME + "/jobs";

    private static final String OPTION_DEBUG_JOBS_TIMING = Platform.PI_RUNTIME + "/jobs/timing";

    private static final String OPTION_LOCKS = Platform.PI_RUNTIME + "/jobs/locks";

    static final boolean DEBUG = Boolean.TRUE.toString().equalsIgnoreCase(InternalPlatform.getDefault().getOption(OPTION_DEBUG_JOBS));

    static final boolean DEBUG_BEGIN_END = Boolean.TRUE.toString().equalsIgnoreCase(InternalPlatform.getDefault().getOption(OPTION_DEBUG_BEGIN_END));

    static final boolean DEBUG_DEADLOCK = Boolean.TRUE.toString().equalsIgnoreCase(InternalPlatform.getDefault().getOption(OPTION_DEADLOCK_ERROR));

    private static DateFormat DEBUG_FORMAT;

    static final boolean DEBUG_LOCKS = Boolean.TRUE.toString().equalsIgnoreCase(InternalPlatform.getDefault().getOption(OPTION_LOCKS));

    static final boolean DEBUG_TIMING = Boolean.TRUE.toString().equalsIgnoreCase(InternalPlatform.getDefault().getOption(OPTION_DEBUG_JOBS_TIMING));

    /**
	 * The singleton job manager instance. It must be a singleton because
	 * all job instances maintain a reference (as an optimization) and have no way 
	 * of updating it.
	 */
    private static JobManager instance;

    /**
	 * Scheduling rule used for validation of client-defined rules.
	 */
    private static final ISchedulingRule nullRule = new ISchedulingRule() {

        public boolean contains(ISchedulingRule rule) {
            return rule == this;
        }

        public boolean isConflicting(ISchedulingRule rule) {
            return rule == this;
        }
    };

    /**
	 * True if this manager is active, and false otherwise.  A job manager
	 * starts out active, and becomes inactive if it has been shutdown
	 * and not restarted.
	 */
    private volatile boolean active = true;

    final ImplicitJobs implicitJobs = new ImplicitJobs(this);

    private final JobListeners jobListeners = new JobListeners();

    /**
	 * The lock for synchronizing all activity in the job manager.  To avoid deadlock,
	 * this lock must never be held for extended periods, and must never be
	 * held while third party code is being called.
	 */
    private final Object lock = new Object();

    private final LockManager lockManager = new LockManager();

    /**
	 * The pool of worker threads.
	 */
    private WorkerPool pool;

    private ProgressProvider progressProvider = null;

    /**
	 * Jobs that are currently running. Should only be modified from changeState
	 */
    private final HashSet running;

    /**
	 * Jobs that are sleeping.  Some sleeping jobs are scheduled to wake
	 * up at a given start time, while others will sleep indefinitely until woken.
	 * Should only be modified from changeState
	 */
    private final JobQueue sleeping;

    /**
	 * True if this manager has been suspended, and false otherwise.  A job manager
	 * starts out not suspended, and becomes suspended when <code>suspend</code>
	 * is invoked. Once suspended, no jobs will start running until <code>resume</code>
	 * is called.
	 */
    private boolean suspended = false;

    /**
	 * jobs that are waiting to be run. Should only be modified from changeState
	 */
    private final JobQueue waiting;

    public static void debug(String msg) {
        StringBuffer msgBuf = new StringBuffer(msg.length() + 40);
        if (DEBUG_TIMING) {
            if (DEBUG_FORMAT == null) DEBUG_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");
            DEBUG_FORMAT.format(new Date(), msgBuf, new FieldPosition(0));
            msgBuf.append('-');
        }
        msgBuf.append('[').append(Thread.currentThread()).append(']').append(msg);
        System.out.println(msgBuf.toString());
    }

    /**
	 * Returns the job manager singleton. For internal use only.
	 */
    public static synchronized JobManager getInstance() {
        if (instance == null) new JobManager();
        return instance;
    }

    /**
	 * For debugging purposes only
	 */
    public static String printState(int state) {
        switch(state) {
            case Job.NONE:
                return "NONE";
            case Job.WAITING:
                return "WAITING";
            case Job.SLEEPING:
                return "SLEEPING";
            case Job.RUNNING:
                return "RUNNING";
            case InternalJob.BLOCKED:
                return "BLOCKED";
            case InternalJob.ABOUT_TO_RUN:
                return "ABOUT_TO_RUN";
            case InternalJob.ABOUT_TO_SCHEDULE:
                return "ABOUT_TO_SCHEDULE";
        }
        return "UNKNOWN";
    }

    public static void shutdown() {
        if (instance != null) {
            instance.doShutdown();
            instance = null;
        }
    }

    private JobManager() {
        instance = this;
        synchronized (lock) {
            waiting = new JobQueue(false);
            sleeping = new JobQueue(true);
            running = new HashSet(10);
            pool = new WorkerPool(this);
        }
    }

    public void addJobChangeListener(IJobChangeListener listener) {
        jobListeners.add(listener);
    }

    public void beginRule(ISchedulingRule rule, IProgressMonitor monitor) {
        validateRule(rule);
        implicitJobs.begin(rule, monitorFor(monitor), false);
    }

    /**
	 * Cancels a job
	 */
    protected boolean cancel(InternalJob job) {
        IProgressMonitor monitor = null;
        synchronized (lock) {
            switch(job.getState()) {
                case Job.NONE:
                    return true;
                case Job.RUNNING:
                    if (job.internalGetState() == Job.RUNNING) {
                        monitor = job.getProgressMonitor();
                        break;
                    }
                default:
                    changeState(job, Job.NONE);
            }
        }
        if (monitor != null) {
            if (!monitor.isCanceled()) monitor.setCanceled(true);
            return false;
        }
        jobListeners.done((Job) job, Status.CANCEL_STATUS, false);
        return true;
    }

    public void cancel(Object family) {
        for (Iterator it = select(family).iterator(); it.hasNext(); ) cancel((Job) it.next());
    }

    /**
	 * Atomically updates the state of a job, adding or removing from the
	 * necessary queues or sets.
	 */
    private void changeState(InternalJob job, int newState) {
        synchronized (lock) {
            int oldState = job.internalGetState();
            switch(oldState) {
                case Job.NONE:
                case InternalJob.ABOUT_TO_SCHEDULE:
                    break;
                case InternalJob.BLOCKED:
                    job.remove();
                    break;
                case Job.WAITING:
                    try {
                        waiting.remove(job);
                    } catch (RuntimeException e) {
                        Assert.isLegal(false, "Tried to remove a job that wasn't in the queue");
                    }
                    break;
                case Job.SLEEPING:
                    try {
                        sleeping.remove(job);
                    } catch (RuntimeException e) {
                        Assert.isLegal(false, "Tried to remove a job that wasn't in the queue");
                    }
                    break;
                case Job.RUNNING:
                case InternalJob.ABOUT_TO_RUN:
                    running.remove(job);
                    break;
                default:
                    Assert.isLegal(false, "Invalid job state: " + job + ", state: " + oldState);
            }
            job.internalSetState(newState);
            switch(newState) {
                case Job.NONE:
                    job.setStartTime(InternalJob.T_NONE);
                case InternalJob.BLOCKED:
                    break;
                case Job.WAITING:
                    waiting.enqueue(job);
                    break;
                case Job.SLEEPING:
                    sleeping.enqueue(job);
                    break;
                case Job.RUNNING:
                case InternalJob.ABOUT_TO_RUN:
                    job.setStartTime(InternalJob.T_NONE);
                    running.add(job);
                    break;
                case InternalJob.ABOUT_TO_SCHEDULE:
                    break;
                default:
                    Assert.isLegal(false, "Invalid job state: " + job + ", state: " + newState);
            }
        }
    }

    /**
	 * Returns a new progress monitor for this job, belonging to the given
	 * progress group.  Returns null if it is not a valid time to set the job's group.
	 */
    protected IProgressMonitor createMonitor(InternalJob job, IProgressMonitor group, int ticks) {
        synchronized (lock) {
            if (job.getState() != Job.NONE) return null;
            IProgressMonitor monitor = null;
            if (progressProvider != null) monitor = progressProvider.createMonitor((Job) job, group, ticks);
            if (monitor == null) monitor = new NullProgressMonitor();
            return monitor;
        }
    }

    /**
	 * Returns a new progress monitor for this job.  Never returns null.
	 */
    private IProgressMonitor createMonitor(Job job) {
        IProgressMonitor monitor = null;
        if (progressProvider != null) monitor = progressProvider.createMonitor(job);
        if (monitor == null) monitor = new NullProgressMonitor();
        return monitor;
    }

    public IProgressMonitor createProgressGroup() {
        if (progressProvider != null) return progressProvider.createProgressGroup();
        return new NullProgressMonitor();
    }

    public Job currentJob() {
        Thread current = Thread.currentThread();
        if (current instanceof Worker) return ((Worker) current).currentJob();
        synchronized (lock) {
            for (Iterator it = running.iterator(); it.hasNext(); ) {
                Job job = (Job) it.next();
                if (job.getThread() == current) return job;
            }
        }
        return null;
    }

    /**
	 * Returns the delay in milliseconds that a job with a given priority can
	 * tolerate waiting.
	 */
    private long delayFor(int priority) {
        switch(priority) {
            case Job.INTERACTIVE:
                return 0L;
            case Job.SHORT:
                return 50L;
            case Job.LONG:
                return 100L;
            case Job.BUILD:
                return 500L;
            case Job.DECORATE:
                return 1000L;
            default:
                Assert.isTrue(false, "Job has invalid priority: " + priority);
                return 0;
        }
    }

    /**
	 * Performs the scheduling of a job.  Does not perform any notifications.
	 */
    private void doSchedule(InternalJob job, long delay) {
        synchronized (lock) {
            if (job.getPriority() == Job.DECORATE) {
                long minDelay = running.size() * 100;
                delay = Math.max(delay, minDelay);
            }
            if (delay > 0) {
                job.setStartTime(System.currentTimeMillis() + delay);
                changeState(job, Job.SLEEPING);
            } else {
                job.setStartTime(System.currentTimeMillis() + delayFor(job.getPriority()));
                changeState(job, Job.WAITING);
            }
        }
    }

    /**
	 * Shuts down the job manager.  Currently running jobs will be told
	 * to stop, but worker threads may still continue processing.
	 * (note: This implemented IJobManager.shutdown which was removed
	 * due to problems caused by premature shutdown)
	 */
    private void doShutdown() {
        Job[] toCancel = null;
        synchronized (lock) {
            if (active) {
                active = false;
                toCancel = (Job[]) running.toArray(new Job[running.size()]);
                sleeping.clear();
                waiting.clear();
                running.clear();
            }
        }
        if (toCancel != null) {
            for (int i = 0; i < toCancel.length; i++) {
                final Job job = toCancel[i];
                cancel(job);
                String jobName;
                if (job instanceof ThreadJob) {
                    Job realJob = ((ThreadJob) job).realJob;
                    if (realJob != null) jobName = realJob.getClass().getName(); else jobName = "ThreadJob on rule: " + job.getRule();
                } else {
                    jobName = job.getClass().getName();
                }
                String msg = "Job found still running after platform shutdown.  Jobs should be canceled by the plugin that scheduled them during shutdown: " + jobName;
                InternalPlatform.getDefault().log(new Status(IStatus.WARNING, Platform.PI_RUNTIME, Platform.PLUGIN_ERROR, msg, null));
            }
            pool.shutdown();
        }
    }

    /**
	 * Indicates that a job was running, and has now finished.  Note that this method 
	 * can be called under OutOfMemoryError conditions and thus must be paranoid 
	 * about allocating objects.
	 */
    protected void endJob(InternalJob job, IStatus result, boolean notify) {
        InternalJob blocked = null;
        int blockedJobCount = 0;
        long rescheduleDelay = InternalJob.T_NONE;
        synchronized (lock) {
            if (result == Job.ASYNC_FINISH) return;
            if (job.getState() == Job.NONE) return;
            if (JobManager.DEBUG && notify) JobManager.debug("Ending job: " + job);
            job.setResult(result);
            job.setProgressMonitor(null);
            job.setThread(null);
            rescheduleDelay = job.getStartTime();
            changeState(job, Job.NONE);
            blocked = job.previous();
            job.setPrevious(null);
            while (blocked != null) {
                InternalJob previous = blocked.previous();
                changeState(blocked, Job.WAITING);
                blockedJobCount++;
                blocked = previous;
            }
        }
        for (int i = 0; i < blockedJobCount; i++) pool.jobQueued(blocked);
        final boolean reschedule = active && rescheduleDelay > InternalJob.T_NONE && job.shouldSchedule();
        if (notify) jobListeners.done((Job) job, result, reschedule);
        if (reschedule) schedule(job, rescheduleDelay, reschedule);
    }

    public void endRule(ISchedulingRule rule) {
        implicitJobs.end(rule, false);
    }

    public Job[] find(Object family) {
        List members = select(family);
        return (Job[]) members.toArray(new Job[members.size()]);
    }

    /**
	 * Returns a running or blocked job whose scheduling rule conflicts with the 
	 * scheduling rule of the given waiting job.  Returns null if there are no 
	 * conflicting jobs.  A job can only run if there are no running jobs and no blocked
	 * jobs whose scheduling rule conflicts with its rule.
	 */
    protected InternalJob findBlockingJob(InternalJob waitingJob) {
        if (waitingJob.getRule() == null) return null;
        synchronized (lock) {
            if (running.isEmpty()) return null;
            boolean hasBlockedJobs = false;
            for (Iterator it = running.iterator(); it.hasNext(); ) {
                InternalJob job = (InternalJob) it.next();
                if (waitingJob.isConflicting(job)) return job;
                if (!hasBlockedJobs) hasBlockedJobs = job.previous() != null;
            }
            if (!hasBlockedJobs) return null;
            for (Iterator it = running.iterator(); it.hasNext(); ) {
                InternalJob job = (InternalJob) it.next();
                while (true) {
                    job = job.previous();
                    if (job == null) break;
                    if (waitingJob.isConflicting(job)) return job;
                }
            }
        }
        return null;
    }

    public LockManager getLockManager() {
        return lockManager;
    }

    /**
	 * Returns whether the job manager is active (has not been shutdown).
	 */
    protected boolean isActive() {
        return active;
    }

    /**
	 * Returns true if the given job is blocking the execution of a non-system
	 * job.
	 */
    protected boolean isBlocking(InternalJob runningJob) {
        synchronized (lock) {
            if (runningJob.getState() != Job.RUNNING) return false;
            InternalJob previous = runningJob.previous();
            while (previous != null) {
                if (!previous.isSystem()) return true;
                if (previous instanceof ThreadJob && ((ThreadJob) previous).shouldInterrupt()) return true;
                previous = previous.previous();
            }
            return false;
        }
    }

    public boolean isIdle() {
        synchronized (lock) {
            return running.isEmpty() && waiting.isEmpty();
        }
    }

    protected void join(InternalJob job) {
        final IJobChangeListener listener;
        final Semaphore barrier;
        synchronized (lock) {
            int state = job.getState();
            if (state == Job.NONE) return;
            if (suspended && state != Job.RUNNING) return;
            barrier = new Semaphore(null);
            listener = new JobChangeAdapter() {

                public void done(IJobChangeEvent event) {
                    barrier.release();
                }
            };
            job.addJobChangeListener(listener);
        }
        try {
            while (true) {
                lockManager.aboutToWait(job.getThread());
                try {
                    if (barrier.acquire(Long.MAX_VALUE)) break;
                } catch (InterruptedException e) {
                }
            }
        } finally {
            lockManager.aboutToRelease();
            job.removeJobChangeListener(listener);
        }
    }

    public void join(final Object family, IProgressMonitor monitor) throws InterruptedException, OperationCanceledException {
        monitor = monitorFor(monitor);
        IJobChangeListener listener = null;
        final Set jobs;
        int jobCount;
        Job blocking = null;
        synchronized (lock) {
            int states = suspended ? Job.RUNNING : Job.RUNNING | Job.WAITING | Job.SLEEPING;
            jobs = Collections.synchronizedSet(new HashSet(select(family, states)));
            jobCount = jobs.size();
            if (jobCount == 0) return;
            if (jobCount == 1) blocking = (Job) jobs.iterator().next();
            listener = new JobChangeAdapter() {

                public void done(IJobChangeEvent event) {
                    if (!((JobChangeEvent) event).reschedule) jobs.remove(event.getJob());
                }

                public void scheduled(IJobChangeEvent event) {
                    if (((JobChangeEvent) event).reschedule) return;
                    Job job = event.getJob();
                    if (job.belongsTo(family)) jobs.add(job);
                }
            };
            addJobChangeListener(listener);
        }
        try {
            monitor.beginTask(Messages.jobs_blocked0, jobCount);
            monitor.subTask(NLS.bind(Messages.jobs_waitFamSub, Integer.toString(jobCount)));
            reportBlocked(monitor, blocking);
            int jobsLeft;
            int reportedWorkDone = 0;
            while ((jobsLeft = jobs.size()) > 0) {
                int actualWorkDone = Math.max(0, jobCount - jobsLeft);
                if (reportedWorkDone < actualWorkDone) {
                    monitor.worked(actualWorkDone - reportedWorkDone);
                    reportedWorkDone = actualWorkDone;
                    monitor.subTask(NLS.bind(Messages.jobs_waitFamSub, Integer.toString(jobsLeft)));
                }
                if (Thread.interrupted()) throw new InterruptedException();
                if (monitor.isCanceled()) throw new OperationCanceledException();
                lockManager.aboutToWait(null);
                Thread.sleep(100);
            }
        } finally {
            removeJobChangeListener(listener);
            reportUnblocked(monitor);
            monitor.done();
        }
    }

    /**
	 * Returns a non-null progress monitor instance.  If the monitor is null,
	 * returns the default monitor supplied by the progress provider, or a 
	 * NullProgressMonitor if no default monitor is available.
	 */
    private IProgressMonitor monitorFor(IProgressMonitor monitor) {
        if (monitor == null || (monitor instanceof NullProgressMonitor)) {
            if (progressProvider != null) {
                try {
                    monitor = progressProvider.getDefaultMonitor();
                } catch (Exception e) {
                    String msg = NLS.bind(Messages.meta_pluginProblems, Platform.PI_RUNTIME);
                    InternalPlatform.getDefault().log(new Status(IStatus.ERROR, Platform.PI_RUNTIME, Platform.PLUGIN_ERROR, msg, e));
                }
            }
        }
        return Policy.monitorFor(monitor);
    }

    public ILock newLock() {
        return lockManager.newLock();
    }

    /**
	 * Removes and returns the first waiting job in the queue. Returns null if there
	 * are no items waiting in the queue.  If an item is removed from the queue,
	 * it is moved to the running jobs list.
	 */
    private Job nextJob() {
        synchronized (lock) {
            if (suspended) return null;
            long now = System.currentTimeMillis();
            InternalJob job = sleeping.peek();
            while (job != null && job.getStartTime() < now) {
                job.setStartTime(now + delayFor(job.getPriority()));
                changeState(job, Job.WAITING);
                job = sleeping.peek();
            }
            while ((job = waiting.peek()) != null) {
                InternalJob blocker = findBlockingJob(job);
                if (blocker == null) break;
                changeState(job, InternalJob.BLOCKED);
                Assert.isTrue(job.next() == null);
                Assert.isTrue(job.previous() == null);
                blocker.addLast(job);
            }
            if (job != null) {
                changeState(job, InternalJob.ABOUT_TO_RUN);
                if (JobManager.DEBUG) JobManager.debug("Starting job: " + job);
            }
            return (Job) job;
        }
    }

    public void removeJobChangeListener(IJobChangeListener listener) {
        jobListeners.remove(listener);
    }

    /**
	 * Report to the progress monitor that this thread is blocked, supplying
	 * an information message, and if possible the job that is causing the blockage.
	 * Important: An invocation of this method MUST be followed eventually be
	 * an invocation of reportUnblocked.
	 * @param monitor The monitor to report blocking to
	 * @param blockingJob The job that is blocking this thread, or <code>null</code>
	 * @see #reportUnblocked
	 */
    final void reportBlocked(IProgressMonitor monitor, InternalJob blockingJob) {
        if (!(monitor instanceof IProgressMonitorWithBlocking)) return;
        IStatus reason;
        if (blockingJob == null || blockingJob instanceof ThreadJob || blockingJob.isSystem()) {
            reason = new Status(IStatus.INFO, Platform.PI_RUNTIME, 1, Messages.jobs_blocked0, null);
        } else {
            String msg = NLS.bind(Messages.jobs_blocked1, blockingJob.getName());
            reason = new JobStatus(IStatus.INFO, (Job) blockingJob, msg);
        }
        ((IProgressMonitorWithBlocking) monitor).setBlocked(reason);
    }

    /**
	 * Reports that this thread was blocked, but is no longer blocked and is able
	 * to proceed.
	 * @param monitor The monitor to report unblocking to.
	 * @see #reportBlocked
	 */
    final void reportUnblocked(IProgressMonitor monitor) {
        if (monitor instanceof IProgressMonitorWithBlocking) ((IProgressMonitorWithBlocking) monitor).clearBlocked();
    }

    public final void resume() {
        synchronized (lock) {
            suspended = false;
            pool.jobQueued(null);
        }
    }

    /** (non-Javadoc)
	 * @deprecated this method should not be used
	 * @see org.eclipse.core.runtime.jobs.IJobManager#resume(org.eclipse.core.runtime.jobs.ISchedulingRule)
	 */
    public final void resume(ISchedulingRule rule) {
        implicitJobs.resume(rule);
    }

    /**
	 * Attempts to immediately start a given job.  Returns true if the job was
	 * successfully started, and false if it could not be started immediately
	 * due to a currently running job with a conflicting rule.  Listeners will never
	 * be notified of jobs that are run in this way.
	 */
    protected boolean runNow(InternalJob job) {
        synchronized (lock) {
            if (findBlockingJob(job) != null) return false;
            changeState(job, Job.RUNNING);
            job.setProgressMonitor(new NullProgressMonitor());
            job.run(null);
        }
        return true;
    }

    protected void schedule(InternalJob job, long delay, boolean reschedule) {
        if (!active) throw new IllegalStateException("Job manager has been shut down.");
        Assert.isNotNull(job, "Job is null");
        Assert.isLegal(delay >= 0, "Scheduling delay is negative");
        synchronized (lock) {
            if (job.getState() == Job.RUNNING) {
                job.setStartTime(delay);
                return;
            }
            if (job.internalGetState() != Job.NONE) return;
            if (JobManager.DEBUG) JobManager.debug("Scheduling job: " + job);
            changeState(job, InternalJob.ABOUT_TO_SCHEDULE);
        }
        jobListeners.scheduled((Job) job, delay, reschedule);
        doSchedule(job, delay);
        pool.jobQueued(job);
    }

    /**
	 * Adds all family members in the list of jobs to the collection
	 */
    private void select(List members, Object family, InternalJob firstJob, int stateMask) {
        if (firstJob == null) return;
        InternalJob job = firstJob;
        do {
            if ((family == null || job.belongsTo(family)) && ((job.getState() & stateMask) != 0)) members.add(job);
            job = job.previous();
        } while (job != null && job != firstJob);
    }

    /**
	 * Returns a list of all jobs known to the job manager that belong to the given family.
	 */
    private List select(Object family) {
        return select(family, Job.WAITING | Job.SLEEPING | Job.RUNNING);
    }

    /**
	 * Returns a list of all jobs known to the job manager that belong to the given 
	 * family and are in one of the provided states.
	 */
    private List select(Object family, int stateMask) {
        List members = new ArrayList();
        synchronized (lock) {
            if ((stateMask & Job.RUNNING) != 0) {
                for (Iterator it = running.iterator(); it.hasNext(); ) {
                    select(members, family, (InternalJob) it.next(), stateMask);
                }
            }
            if ((stateMask & Job.WAITING) != 0) select(members, family, waiting.peek(), stateMask);
            if ((stateMask & Job.SLEEPING) != 0) select(members, family, sleeping.peek(), stateMask);
        }
        return members;
    }

    public void setLockListener(LockListener listener) {
        lockManager.setLockListener(listener);
    }

    /**
	 * Changes a job priority.
	 */
    protected void setPriority(InternalJob job, int newPriority) {
        synchronized (lock) {
            int oldPriority = job.getPriority();
            if (oldPriority == newPriority) return;
            job.internalSetPriority(newPriority);
            if (job.getState() == Job.WAITING) {
                long oldStart = job.getStartTime();
                job.setStartTime(oldStart + (delayFor(newPriority) - delayFor(oldPriority)));
                waiting.resort(job);
            }
        }
    }

    public void setProgressProvider(ProgressProvider provider) {
        progressProvider = provider;
    }

    public void setRule(InternalJob job, ISchedulingRule rule) {
        synchronized (lock) {
            Assert.isLegal(job.getState() == Job.NONE);
            validateRule(rule);
            job.internalSetRule(rule);
        }
    }

    /**
	 * Puts a job to sleep. Returns true if the job was successfully put to sleep.
	 */
    protected boolean sleep(InternalJob job) {
        synchronized (lock) {
            switch(job.getState()) {
                case Job.RUNNING:
                    if (job.internalGetState() == Job.RUNNING) return false;
                    break;
                case Job.SLEEPING:
                    job.setStartTime(InternalJob.T_INFINITE);
                    changeState(job, Job.SLEEPING);
                    return true;
                case Job.NONE:
                    return true;
                case Job.WAITING:
                    break;
            }
            job.setStartTime(InternalJob.T_INFINITE);
            changeState(job, Job.SLEEPING);
        }
        jobListeners.sleeping((Job) job);
        return true;
    }

    public void sleep(Object family) {
        for (Iterator it = select(family).iterator(); it.hasNext(); ) {
            sleep((InternalJob) it.next());
        }
    }

    /**
	 * Returns the estimated time in milliseconds before the next job is scheduled
	 * to wake up. The result may be negative.  Returns InternalJob.T_INFINITE if
	 * there are no sleeping or waiting jobs.
	 */
    protected long sleepHint() {
        synchronized (lock) {
            if (suspended) return InternalJob.T_INFINITE;
            if (!waiting.isEmpty()) return 0L;
            InternalJob next = sleeping.peek();
            if (next == null) return InternalJob.T_INFINITE;
            return next.getStartTime() - System.currentTimeMillis();
        }
    }

    /**
	 * Returns the next job to be run, or null if no jobs are waiting to run.
	 * The worker must call endJob when the job is finished running.  
	 */
    protected Job startJob() {
        Job job = null;
        while (true) {
            job = nextJob();
            if (job == null) return null;
            if (job.shouldRun()) {
                jobListeners.aboutToRun(job);
                synchronized (lock) {
                    if (job.getState() == Job.RUNNING) {
                        InternalJob internal = job;
                        if (internal.getProgressMonitor() == null) internal.setProgressMonitor(createMonitor(job));
                        internal.internalSetState(Job.RUNNING);
                        break;
                    }
                }
            }
            if (job.getState() != Job.SLEEPING) {
                endJob(job, Status.CANCEL_STATUS, true);
                continue;
            }
        }
        jobListeners.running(job);
        return job;
    }

    public final void suspend() {
        synchronized (lock) {
            suspended = true;
        }
    }

    /** (non-Javadoc)
	 * @deprecated this method should not be used
	 * @see org.eclipse.core.runtime.jobs.IJobManager#suspend(org.eclipse.core.runtime.jobs.ISchedulingRule, org.eclipse.core.runtime.IProgressMonitor)
	 */
    public final void suspend(ISchedulingRule rule, IProgressMonitor monitor) {
        Assert.isNotNull(rule);
        implicitJobs.suspend(rule, monitorFor(monitor));
    }

    public void transferRule(ISchedulingRule rule, Thread destinationThread) {
        implicitJobs.transfer(rule, destinationThread);
    }

    /**
	 * Validates that the given scheduling rule obeys the constraints of
	 * scheduling rules as described in the <code>ISchedulingRule</code>
	 * javadoc specification.
	 */
    private void validateRule(ISchedulingRule rule) {
        if (rule == null) return;
        Assert.isLegal(rule.contains(rule));
        Assert.isLegal(!rule.contains(nullRule));
        Assert.isLegal(rule.isConflicting(rule));
        Assert.isLegal(!rule.isConflicting(nullRule));
    }

    protected void wakeUp(InternalJob job, long delay) {
        Assert.isLegal(delay >= 0, "Scheduling delay is negative");
        synchronized (lock) {
            if (job.getState() != Job.SLEEPING) return;
            doSchedule(job, delay);
        }
        pool.jobQueued(job);
        if (delay == 0) jobListeners.awake((Job) job);
    }

    public void wakeUp(Object family) {
        for (Iterator it = select(family).iterator(); it.hasNext(); ) {
            wakeUp((InternalJob) it.next(), 0L);
        }
    }
}
