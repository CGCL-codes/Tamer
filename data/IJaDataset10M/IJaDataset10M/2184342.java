package net.sf.buildbox.reactor.impl;

import java.util.*;
import net.sf.buildbox.reactor.api.BuildIdGenerator;
import net.sf.buildbox.reactor.api.JobReactor;
import net.sf.buildbox.reactor.api.JobStoreDao;
import net.sf.buildbox.reactor.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component schedules provided jobs, returns them to the clients and reports a job execution to the event handler.
 *
 * @author zuzcak_m
 * @author Petr Kozelka<pkozelka@gmail.com>
 * @since 27.2.2006
 */
public final class JobReactorImpl implements JobReactor {

    private final Logger logger = LoggerFactory.getLogger(FsJobStore.class);

    private final JobStoreDao jobStoreDao;

    private boolean dedicatedMode = false;

    private BuildIdGenerator buildIdGenerator;

    /**
     * defines timout a job is in progress state if no job progress is reported. after this period, jobs returns back to pool of available jobs if possible.
     */
    private long jobInProgressTimeout = 4 * 60 * 1000;

    private final long schedulerStartTime = System.currentTimeMillis();

    private String dedicatedSiteDefault;

    public void setJobInProgressTimeout(long jobInProgressTimeout) {
        this.jobInProgressTimeout = jobInProgressTimeout;
    }

    /**
     * in dedicated mode, each job needs to specify the target dedicated to perform it.
     * This allows manual control in environment that is unable to distribute some or all of its jobs.
     * <p/>
     * Job param "dedicated.site" is used for this purpose.
     * <p/>
     * If this param is filled, it is respected even in non-dedicated mode - IOW, dedicatedMode only makes this param mandatory
     *
     * @param dedicatedMode true to enable the dedicated mode
     */
    public void setDedicatedMode(boolean dedicatedMode) {
        this.dedicatedMode = dedicatedMode;
        logger.debug("DEDICATED MODE: %s", dedicatedMode);
    }

    /**
     * specifies which worker site receives all non-dedicated jobs
     *
     * @param dedicatedSiteDefault username@host identifying account under which the workers run
     */
    public void setDedicatedSiteDefault(String dedicatedSiteDefault) {
        this.dedicatedSiteDefault = dedicatedSiteDefault;
    }

    public void setBuildIdGenerator(BuildIdGenerator buildIdGenerator) {
        this.buildIdGenerator = buildIdGenerator;
    }

    /**
     * This flag is up when scheduler is seeking a job in another thread.
     * Setting it to false causes very fast termination of that activity, effectively solving a problem with too long
     * wait for entering synchronized block and exceeding a timeout.
     */
    private volatile boolean jobseekFlag = false;

    public JobReactorImpl(JobStoreDao jobStoreDao) {
        this.jobStoreDao = jobStoreDao;
    }

    private String getUnusedBuildId(ReactorJob job) {
        if (buildIdGenerator == null) {
            final AbstractBuildIdGenerator strategy = new SkinnyBuildIdGenerator();
            strategy.setJobStoreDao(jobStoreDao);
            buildIdGenerator = strategy;
        }
        return buildIdGenerator.getUnusedBuildId(job);
    }

    /**
     * Method used to add a new set of jobs to the scheduler. Scheduler combines them with its current reactor state.
     *
     * @param newJobs map od jobs
     */
    public final void scheduleJobs(Iterable<ReactorJob> newJobs) {
        jobseekStop();
        synchronized (this) {
            logger.debug("scheduling jobs: %s", newJobs);
            final Collection<ReactorJob> reactorJobs = getJobs();
            for (ReactorJob newJob : newJobs) {
                if (newJob.getTimeLastImpact() == null) {
                    logger.debug("WARNING: setting empty timeLastImpact on %s", newJob);
                    newJob.setTimeLastImpact(new Date());
                }
                processDependencyTemplate(newJob, reactorJobs, newJobs);
                final JobId newJobId = newJob.getJobId();
                newJob.setBuildId(getUnusedBuildId(newJob));
                final Set<ReactorJob> zombies = new HashSet<ReactorJob>();
                for (ReactorJob reactorJob : reactorJobs) {
                    if (newJobId.equals(reactorJob.getJobId())) {
                        final boolean mustRemove = shouldEliminate(reactorJob);
                        if (mustRemove) {
                            zombies.add(reactorJob);
                        }
                    }
                }
                for (ReactorJob reactorJob : zombies) {
                    logger.debug("removing replaced job %s", reactorJob);
                    jobStoreDao.removeJob(reactorJob);
                }
                if (newJob.getTimeScheduled() == null) {
                    newJob.setTimeScheduled(new Date());
                }
                jobStoreDao.addJob(newJob);
                logger.debug("scheduled: %s params: %s", newJob, newJob.getJobParams());
            }
        }
    }

    private Collection<ReactorJob> getJobs() {
        return jobStoreDao.getReactorJobs(null, null, null, null, null, false);
    }

    /**
     * This method decides about parallel execution of multiple jobs with same {@link JobId}.
     * When the job is locked, new execution is allowed; otherwise the one currently present in reactor is replaced.
     *
     * @param reactorJob -
     * @return true to eliminate, false to keep running/scheduled
     */
    protected boolean shouldEliminate(ReactorJob reactorJob) {
        return reactorJob.getLockingKey() == null;
    }

    private static void processDependencyTemplate(ReactorJob newJob, Iterable<ReactorJob> reactorJobs, Iterable<ReactorJob> newJobs) {
        final Set<ReactorJob> newJobDeps = newJob.getDependencies();
        for (JobId depTemplate : newJob.getDependencyTemplates()) {
            final ReactorJob internalDep = JobUtils.findFirstInCollection(newJobs, depTemplate);
            if (internalDep != null) {
                newJobDeps.add(internalDep);
                continue;
            }
            final ReactorJob reactorDep = JobUtils.findFirstInCollection(reactorJobs, depTemplate);
            if (reactorDep != null) {
                newJobDeps.add(reactorDep);
            } else {
            }
        }
    }

    /**
     * If job seeking is in progress by another thread, this method requests its fast termination.
     * This is useful for reducing the risk that a synchronization blocking will exceed keepalive timeout,
     * and also useful because typically new data that may potentially influence jobseeking results are coming.
     */
    private void jobseekStop() {
        if (jobseekFlag) {
            jobseekFlag = false;
            logger.warn("requested to stop seeking a job");
        }
    }

    public final WorkerJob getWorkToDo(WorkerSpec workerSpec) {
        synchronized (this) {
            final List<ReactorJob> availableJobs = new ArrayList<ReactorJob>();
            jobseekFlag = true;
            final Date now = new Date();
            final String workerName = workerSpec.getName();
            for (ReactorJob job : getJobs()) {
                if (!jobseekFlag) {
                    logger.warn("seeking a job for worker '%s' stopped");
                    return null;
                }
                if (job.isFailed()) {
                } else if (now.before(job.getTimeScheduled())) {
                } else {
                    if (job.getWorkerId() != null) {
                        long lastResponseTime = schedulerStartTime;
                        if (job.getTimeStatusReported() != null && lastResponseTime < job.getTimeStatusReported().getTime()) {
                            lastResponseTime = job.getTimeStatusReported().getTime();
                        }
                        if (job.getTimeStarted() != null && lastResponseTime < job.getTimeStarted().getTime()) {
                            lastResponseTime = job.getTimeStarted().getTime();
                        }
                        if (System.currentTimeMillis() > lastResponseTime + jobInProgressTimeout) {
                            final String message = String.format("%s - not responding since %tFT%<tT.%<tL (%s), rescheduling as available again", job, lastResponseTime, job.getStatusMessage());
                            logger.error(message);
                            job.setWorkerId(null);
                            job.setProgress(null);
                            job.setStatusMessage(message);
                            job.setTimeStatusReported(new Date());
                            job.setTimeStarted(null);
                            jobStoreDao.updateJob(job);
                        }
                    }
                    if (job.getDependencies().isEmpty() && job.getWorkerId() == null) {
                        availableJobs.add(job);
                    }
                }
            }
            if (!availableJobs.isEmpty()) {
                Collections.sort(availableJobs, new Comparator<ReactorJob>() {

                    public int compare(ReactorJob o1, ReactorJob o2) {
                        final int priorityDiff = o2.getPriority() - o1.getPriority();
                        if (priorityDiff != 0) {
                            return priorityDiff;
                        }
                        return o1.getTimeScheduled().compareTo(o2.getTimeScheduled());
                    }
                });
                final String workerSite = getWorkerSite(workerSpec);
                for (ReactorJob availableJob : availableJobs) {
                    final String dedicatedSite = availableJob.getJobParams().get("dedicated.site");
                    if (workerSite.equals(dedicatedSite)) {
                        logger.debug("worker %s receives job dedicated for %s", workerName, dedicatedSite);
                    } else if (dedicatedMode) {
                        if (dedicatedSite == null && workerSite.equals(dedicatedSiteDefault)) {
                            logger.debug("worker %s receives undedicated job as it is on default site: %s", workerName, dedicatedSiteDefault);
                        } else {
                            logger.debug("worker {} cannot get job {} dedicated for {}", new Object[] { workerName, availableJob, dedicatedSite });
                            continue;
                        }
                    }
                    if (workerCanPerform(workerSpec, availableJob)) {
                        availableJob.getStatusProperties().clear();
                        availableJob.setStatusMessage("reserved by worker " + workerName);
                        availableJob.setTimeStatusReported(new Date());
                        availableJob.setWorkerId(workerName);
                        final WorkerJob newWorkerJob = WorkerJob.create(availableJob.getJobId(), availableJob.getBuildId());
                        logger.info("worker {} reserves work on job {}", workerName, availableJob);
                        jobseekFlag = false;
                        final String workerSiteUrl = workerSpec.getProperties().get("worker.site.url");
                        if (workerSiteUrl != null) {
                            availableJob.getJobParams().put("worker.site.url", workerSiteUrl);
                        }
                        final String buildCatalogUrl = workerSpec.getProperties().get("build.catalog.url");
                        if (buildCatalogUrl != null) {
                            availableJob.getJobParams().put("build.catalog.url", buildCatalogUrl);
                        }
                        return newWorkerJob;
                    }
                }
            }
            jobseekFlag = false;
            return null;
        }
    }

    private static String getWorkerSite(WorkerSpec workerSpec) {
        final Map<String, String> workerProperties = workerSpec.getProperties();
        final String workerSite = workerProperties.get("worker.site");
        if (workerSite != null) {
            return workerSite;
        }
        final String workerHostName = workerProperties.get("worker.host.name");
        return workerHostName == null ? "UNKNOWN_HOST_NAME" : workerHostName;
    }

    private boolean workerCanPerform(WorkerSpec workerSpec, ReactorJob availableJob) {
        return workerSpec.supportsAll(availableJob.getRequiredCapabilities());
    }

    public ReactorJob jobAccepted(WorkerJob workerJob) {
        final ReactorJob reactorJob = JobUtils.findInJobs(getJobs(), workerJob);
        logger.info("%s - job accepted by %s; params: %s", new Object[] { reactorJob, reactorJob.getWorkerId(), reactorJob.getJobParams() });
        reactorJob.setProgress(0L);
        reactorJob.setStatusMessage("Job Accepted");
        reactorJob.setTimeStatusReported(new Date());
        jobStoreDao.updateJob(reactorJob);
        reactorJob.setTimeStarted(new Date());
        return reactorJob;
    }

    public void reportWorkDone(WorkerJob workerJob, WorkerResultCode workerResultCode, String resultMessage, String workerId) {
        jobseekStop();
        synchronized (this) {
            final ReactorJob reactorJob = JobUtils.findInJobs(getJobs(), workerJob);
            if (reactorJob == null) {
                logger.warn("unknown job finished: workerJob:%s, workerResultCode:%s, message:%s, workerName:%s", new Object[] { workerJob, workerResultCode, resultMessage, workerId });
                return;
            } else {
                logger.info("job finished: workerJob:%s, workerResultCode:%s, message:%s, workerName:%s", new Object[] { workerJob, workerResultCode, resultMessage, workerId });
                final Collection<ReactorJob> dependants = JobUtils.getDependantJobs(getJobs(), reactorJob.getJobId(), reactorJob.getBuildId());
                reactorJob.setStatusMessage(resultMessage);
                reactorJob.setTimeStatusReported(new Date());
                switch(workerResultCode) {
                    case OK:
                        for (ReactorJob dependant : dependants) {
                            dependant.getDependencies().remove(reactorJob);
                        }
                        jobStoreDao.removeJob(reactorJob);
                        break;
                    case REJECTED:
                    case IOERROR:
                    case FAILED:
                        reactorJob.setProgress(null);
                        reactorJob.setFailed(true);
                        jobStoreDao.updateJob(reactorJob);
                        break;
                    case INTERRUPTED:
                    case WORKER_DIED:
                    default:
                        reactorJob.setProgress(null);
                        reactorJob.setFailed(true);
                        reactorJob.setWorkerId(null);
                        jobStoreDao.updateJob(reactorJob);
                        break;
                }
            }
            if (reactorJob.getTimeStarted() == null) {
                logger.debug("cannot convert reactor job to a finished job because timeStarted is null: %s", reactorJob);
                return;
            }
            final FinishedJob finishedJob = FinishedJob.createFrom(reactorJob);
            finishedJob.setResultCode(workerResultCode);
            finishedJob.setStatusMessage(resultMessage);
            finishedJob.setWorkerId(workerId);
            finishedJob.setTimeFinished(new Date());
            jobStoreDao.addFinishedJob(finishedJob);
        }
    }

    public final boolean queryContinue(WorkerJob workerJob) {
        return JobUtils.findInJobs(getJobs(), workerJob) != null;
    }

    public final ReactorJob reportJobProgress(WorkerJob workerJob, String statusKey, String statusValue, Long progress) {
        final ReactorJob reactorJob = JobUtils.findInJobs(getJobs(), workerJob);
        if (reactorJob != null) {
            logger.debug("{} - {}: {}", new Object[] { reactorJob, statusKey, statusValue });
            reactorJob.setProgress(progress);
            reactorJob.setStatusProperty(statusKey, statusValue);
            reactorJob.setTimeStatusReported(new Date());
            jobStoreDao.updateJob(reactorJob);
        } else {
            logger.warn("%s (unknown job status report) - %s: %s", new Object[] { workerJob, statusKey, statusValue });
        }
        return reactorJob;
    }

    public int unscheduleJobs(Collection<ReactorJob> jobs) {
        int cnt = 0;
        for (ReactorJob job : jobs) {
            logger.info("unscheduling job %s requested by client", job);
            cnt += unscheduleJob(job);
        }
        return cnt;
    }

    private int unscheduleJob(ReactorJob job) {
        logger.debug("unscheduling job %s", job);
        jobStoreDao.removeJob(job);
        int cnt = 1;
        final String buildId = job.getBuildId();
        final JobId jobId = job.getJobId();
        final Collection<ReactorJob> jobsToUnschedule = JobUtils.getDependantJobs(getJobs(), jobId, buildId);
        logger.debug("%s: unscheduling dependant jobs - %s", jobId, jobsToUnschedule);
        for (ReactorJob dependant : jobsToUnschedule) {
            logger.info("unscheduling dependant job %s caused by %s", dependant, jobId);
            cnt += unscheduleJob(dependant);
        }
        return cnt;
    }

    /**
     * Returns scheduled jobs
     *
     * @return collection of scheduled jobs
     */
    public final Iterable<ReactorJob> getReactorJobs() {
        return getJobs();
    }

    public Collection<ReactorJob> getReactorJobs(String projectId, String activityName, String activitySpec, String buildId, String lockingKey, boolean activeOnly) {
        return jobStoreDao.getReactorJobs(projectId, activityName, activitySpec, buildId, lockingKey, activeOnly);
    }
}
