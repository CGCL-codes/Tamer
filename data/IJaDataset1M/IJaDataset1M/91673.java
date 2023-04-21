package net.kodeninja.scheduling;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class FIFOScheduler implements Scheduler {

    private static final int WAIT_TIME = 1000;

    private Object syncObj = new Object();

    private volatile Set<Job> jobs = new LinkedHashSet<Job>();

    private volatile boolean running;

    private Thread helperThreads[];

    /**
	 * Creates a scheduler with 1 helper thread.
	 */
    public FIFOScheduler() {
        this(1);
    }

    /**
	 * Creates a scheduler with the specified helper threads.
	 *
	 * @param threadCount
	 *            The amount of helper threads to create & manage.
	 */
    public FIFOScheduler(int threadCount) {
        helperThreads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) helperThreads[i] = new Thread(this, "Scheduler Helper Thread-" + i);
    }

    /**
	 * Adds a job to the scheduler's queue.
	 *
	 * @param job
	 *            The job to add to the scheduler's queue.
	 * @return True if the job was not allready added.
	 */
    public boolean addJob(Job job) {
        synchronized (syncObj) {
            boolean retVal = jobs.add(job);
            syncObj.notify();
            return retVal;
        }
    }

    /**
	 * Removes a job from the scheduler's queue.
	 *
	 * @param job
	 *            The job to remove from the queue.
	 * @return True if the job was found and therefore removed.
	 */
    public boolean removeJob(Job job) {
        synchronized (syncObj) {
            boolean retVal = jobs.remove(job);
            syncObj.notifyAll();
            return retVal;
        }
    }

    /**
	 * Starts the scheduler's process. Blocks until exits.
	 */
    public void start() {
        start(true);
    }

    /**
	 * Starts the scheduler's process. Blocks until exits if block is set to
	 * true.
	 *
	 * @param block
	 *            If set to true, it includes the calling thread in pool and
	 *            doesn't return until a stop has been called.
	 */
    public void start(boolean block) {
        running = true;
        for (Thread element : helperThreads) element.start();
        if (block) run();
    }

    /**
	 * Stops the scheduler, and returns from the block state.
	 */
    public void stop() {
        synchronized (syncObj) {
            running = false;
            syncObj.notifyAll();
        }
    }

    /**
	 * The run procedure for the main and helper threads. Do not call this
	 * procedure directly.
	 */
    public void run() {
        while (running) if (runNextJob() == false) try {
            synchronized (syncObj) {
                syncObj.wait(WAIT_TIME);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Runs other jobs while waiting for the request job to finish.
	 *
	 * @param job
	 *            The job to wait for.
	 */
    public void waitFor(Job job) {
        int oldRunId = job.runId();
        while ((job.runId() == oldRunId) && (running)) runNextJob();
    }

    /**
	 * Runs other jobs while waiting for the requested jobs to finish.
	 *
	 * @param jobsToWaitFor
	 *            The job to wait for.
	 */
    public void waitFor(Map<? extends Job, Integer> jobsToWaitFor) {
        while ((running) && (jobsToWaitFor.size() > 0)) {
            runNextJob();
            synchronized (jobsToWaitFor) {
                Job j = jobsToWaitFor.keySet().iterator().next();
                if (j.runId() != jobsToWaitFor.get(j)) jobsToWaitFor.remove(j);
            }
        }
    }

    /**
	 * Runs the next job, or if non is waiting sleeps for 10ms.
	 */
    private boolean runNextJob() {
        Job nextJob = getNextJob();
        if (nextJob != null) try {
            nextJob.run();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
        return false;
    }

    /**
	 * Retrieves the next job to be run.
	 *
	 * @return The job to be run, or null if none are waiting.
	 */
    private Job getNextJob() {
        Job jobToRun = null;
        synchronized (syncObj) {
            for (Job tmpJob : jobs) {
                if (tmpJob.canRun() == false) continue;
                if (jobToRun == null) jobToRun = tmpJob; else if (tmpJob.isUrgent()) {
                    jobToRun = tmpJob;
                    break;
                }
            }
            if (jobToRun != null) jobs.remove(jobToRun);
        }
        return jobToRun;
    }
}
