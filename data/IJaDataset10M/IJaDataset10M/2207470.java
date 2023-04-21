package org.afk.job;

import java.util.*;
import java.util.logging.*;
import org.afk.util.*;

/**
 * JobHandler is a Class for delayed execution of Jobs like the Command pattern.
 * 
 * You just initialize the JobHandler
 * 
 * JobHandler myHandler = new JobHandler(x);
 * where x is the number of Jobs that should be executed in parallel.
 * 
 * now start the handler with
 * myHandler.start("myJobs");
 * 
 * which creates a new Thread with name JobHandler_myJobs
 * 
 * now you can register a listener if you are interested in the results of the jobs.
 * myHandler.addJobListener(myListener)
 * 
 * Now you can add Jobs (which extend org.afk.job.Job) to the Handler
 * myHandler.addJob(myJob)
 * 
 * The listener will receive events about starting and finishing the Jobs.
 * 
 *  
 * @author axl
 */
public class JobHandler implements Runnable {

    Logger logger = Logger.getLogger("org.afk.job");

    Vector runner = new Vector();

    JobQueue queue = new JobQueue();

    Vector listener = new Vector();

    private int paused = 0;

    private int sleepTime = 500;

    private JobRunner[] running;

    private boolean stop;

    private boolean stopped;

    private Thread thread;

    private Vector errorListener = new Vector();

    private boolean finishAndStop;

    /**
	 * @param sleepTime the time that'll be waited before checking if there is a job to bes started. must be > 0
	 * 
	 */
    public JobHandler(int parallelJobAmount) {
        if (parallelJobAmount <= 0) throw new IllegalArgumentException("parallelJobAmount must be greater than zero : " + parallelJobAmount);
        running = new JobRunner[parallelJobAmount];
    }

    /**
	 * the time to sleep when waiting for new Jobs or no free slots. default is 500 (ms)
	 */
    public void setSleepTime(int sleepTime) {
        if (sleepTime <= 0) throw new IllegalArgumentException("Sleeptime must be greater than zero : " + sleepTime);
        this.sleepTime = sleepTime;
    }

    public void removeJobListener(JobListener jobListener) {
        if (this.listener.contains(jobListener)) {
            Vector newListener = (Vector) this.listener.clone();
            newListener.remove(jobListener);
            this.listener = newListener;
        }
    }

    public void addJobListener(JobListener jobListener) {
        if (!this.listener.contains(jobListener)) {
            Vector newListener = (Vector) this.listener.clone();
            newListener.add(jobListener);
            this.listener = newListener;
        }
    }

    /**
	 * adds the job to the waiting queue
	 * @param job
	 */
    public void addJob(Job job) {
        if (stopped) throw new IllegalStateException("JobHandler has stopped");
        if (stop || finishAndStop) throw new IllegalStateException("JobHandler is going to stop");
        queue.put(job);
    }

    /**
	 * removes a job from the waiting queue
	 * @param job the job to be removed
	 * @return the removed job or null if job is not waiting
	 */
    public Job removeJob(Job job) {
        boolean remove = queue.remove(job);
        return remove ? job : null;
    }

    /**
	 * counts the number of jobs that wait to be executed
	 * @return
	 */
    public int countWaitingJobs() {
        return queue.size();
    }

    /**
	 * counts the number of jobs that are currently running
	 * @return number of runnign Jobs
	 */
    public int countRunningJobs() {
        return countRunning();
    }

    private int countRunning() {
        int runningJobs = 0;
        for (int i = 0; i < running.length; i++) {
            if (running[i] != null && !running[i].isfinished() && !running[i].isFree()) runningJobs++;
        }
        return runningJobs;
    }

    /**
	 * return the currently free Job slots (how many jobs could be started without beeing queued)
	 * @return
	 */
    public int countFreeRunSlots() {
        int frreeeeeSlots = 0;
        for (int i = 0; i < running.length; i++) {
            if (running[i] == null || running[i].isfinished() || running[i].isFree()) frreeeeeSlots++;
        }
        return frreeeeeSlots;
    }

    /**
	 * counts running and waiting jobs
	 * 
	 * @return the number of jobs currently in the system
	 */
    public int countAllJobs() {
        return countRunning() + queue.size();
    }

    /**
	 * increments pause value. while pause > 0 isPaused() returns true and no new jobs are started
	 */
    public synchronized void pause() {
        paused++;
    }

    /**
	 * decrements pause value. while pause <= 0 isPaused() returns false and new jobs are started if possible
	 */
    public synchronized void resume() {
        paused--;
    }

    public void run() {
        stop = false;
        stopped = false;
        paused = 0;
        while (true) {
            if (stop) {
                break;
            }
            if (finishAndStop && queue.size() == 0) {
                break;
            }
            try {
                while (paused > 0) {
                    suspend();
                }
                JobRunner runner = getFreeRunner();
                if (runner != null) {
                    Job job = queue.pop();
                    if (job != null) {
                        runner.setJob(job);
                        runner.setJobHandler(this);
                        runner.setFree(false);
                        runner.start();
                        continue;
                    }
                }
            } catch (Exception x) {
                fireWarning(x);
            } catch (Error t) {
                fireCrash(t);
                break;
            }
            suspend();
        }
        waitForRunning();
        thread = null;
        stopped = true;
    }

    private void waitForRunning() {
        for (int i = 0; i < running.length; i++) {
            JobRunner runner = running[i];
            while (!runner.isfinished() && !runner.isFree()) {
                synchronized (runner) {
                    try {
                        runner.join();
                    } catch (InterruptedException e) {
                        logger.fine("Ignored: " + AfkLib.stackTraceToString(e));
                    }
                }
            }
        }
    }

    /**
	 * the Handler stops working. when no more job is running isStopped returns true
	 */
    public void stop() {
        stop = true;
    }

    /**
	 * the Handler stops working after having processed ll remeining jobs. when no more job is running isStopped returns true
	 */
    public void FinishAndStop() {
        finishAndStop = true;
    }

    /**
	 * as long the method returns false, the handler and/or jobs are running  
	 * @return true if handler and jobs have stopped
	 */
    public boolean isStopped() {
        return stopped;
    }

    /**
	 * return true if no more jobs are allowed to be started
	 * @return true if pause > 0
	 */
    public boolean isPaused() {
        return paused > 0;
    }

    private void suspend() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            logger.fine("Ignored: " + AfkLib.stackTraceToString(e));
        }
    }

    private JobRunner getFreeRunner() {
        JobRunner freeRunner = null;
        for (int i = 0; i < running.length; i++) {
            JobRunner runner = running[i];
            if (runner == null || runner.isfinished()) {
                freeRunner = runner = running[i] = new JobRunner();
            } else if (runner.isFree()) {
                freeRunner = runner;
            } else {
            }
        }
        return freeRunner;
    }

    /**
	 * @param job
	 * @param result
	 */
    public void fireFinished(Job job, ExecutionResult result) {
        for (Iterator iter = listener.iterator(); iter.hasNext(); ) {
            JobListener listeningOne = (JobListener) iter.next();
            listeningOne.jobFinished(job, result);
        }
    }

    /**
	 * @param job
	 * @param e
	 */
    public void fireException(Job job, Throwable e) {
        ExecutionResult result = new ExecutionResult();
        if (e == null) result.setResult(ExecutionResult.NULL_RESULT); else result.setResult(ExecutionResult.EXCEPTION);
        for (Iterator iter = listener.iterator(); iter.hasNext(); ) {
            JobListener listeningOne = (JobListener) iter.next();
            listeningOne.jobFinished(job, result);
        }
    }

    /**
	 * @param job
	 */
    public void fireStart(Job job) {
        for (Iterator iter = listener.iterator(); iter.hasNext(); ) {
            JobListener listeningOne = (JobListener) iter.next();
            listeningOne.jobStarts(job);
        }
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
	 * starts the JobHnadler if not allready running
	 * @param name of the Thread (Will be JobHandler_name)
	 */
    public void start(String name) {
        if (thread == null) {
            thread = new Thread(this, "JobHandler_" + name);
            thread.start();
        } else {
            throw new IllegalStateException("Job already running. stop it first!");
        }
    }

    public void removeErrorListener(ErrorListener listener) {
        if (this.errorListener.contains(listener)) {
            Vector newListener = (Vector) this.errorListener.clone();
            newListener.remove(listener);
            this.errorListener = newListener;
        }
    }

    public void addErrorListener(ErrorListener jobListener) {
        if (!this.errorListener.contains(listener)) {
            Vector newListener = (Vector) this.errorListener.clone();
            newListener.add(listener);
            this.errorListener = newListener;
        }
    }

    protected void fireWarning(Throwable t) {
        if (errorListener.size() == 0) {
            logger.severe(AfkLib.stackTraceToString(t));
        } else {
            for (Iterator iter = errorListener.iterator(); iter.hasNext(); ) {
                ErrorListener listeningOne = (ErrorListener) iter.next();
                listeningOne.caught(this, t);
            }
        }
    }

    protected void fireCrash(Throwable t) {
        if (errorListener.size() == 0) {
            logger.severe(AfkLib.stackTraceToString(t));
        } else {
            for (Iterator iter = errorListener.iterator(); iter.hasNext(); ) {
                ErrorListener listeningOne = (ErrorListener) iter.next();
                listeningOne.crashed(this, t);
            }
        }
    }
}
