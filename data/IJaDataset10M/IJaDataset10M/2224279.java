package com.explosion.utilities.process.threads;

import java.util.Date;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.explosion.utilities.process.ProcessMonitor;

public class UnitisedProcessThread implements ProcessThread {

    private static Logger log = LogManager.getLogger(UnitisedProcessThread.class);

    private int interval = 10;

    private int priority = Thread.NORM_PRIORITY;

    private Thread executionThread;

    private Thread thisThread;

    private boolean finalised = false;

    private com.explosion.utilities.process.threads.UnitisedProcess process;

    private Date startTime = null;

    private Date endTime = null;

    private int status = THREAD_NOT_YET_STARTED;

    private boolean wasKilled = false;

    private boolean isUserProcess = false;

    /**
     * For an explanation on implementing safely stoppable and suspendable
     * threads please see
     * 
     * @see http://java.sun.com/products/jdk/1.2/docs/guide/misc/threadPrimitiveDeprecation.html
     */
    public void run() {
        try {
            thisThread = Thread.currentThread();
            ProcessMonitor.registerProcess(this, isUserProcess);
            checkSuspended();
            process.initialiseProcessing();
            checkSuspended();
            stop: while (executionThread == thisThread && process.getPercentComplete() < 100) {
                checkSuspended();
                if (status == THREAD_STOPPED) break;
                process.beginProcessUnit();
                checkSuspended();
                if (status == THREAD_STOPPED) break;
                while (!process.processUnitComplete()) {
                    checkSuspended();
                    if (status == THREAD_STOPPED) continue stop;
                    process.processUnit();
                    checkSuspended();
                    if (status == THREAD_STOPPED) continue stop;
                }
                process.endProcessUnit();
                checkSuspended();
                if (status == THREAD_STOPPED) break;
                try {
                    if (interval > 0) Thread.sleep(interval);
                } catch (InterruptedException e) {
                }
            }
            process.finaliseProcessing();
        } catch (Exception ex) {
            process.finaliseProcessing(ex);
        }
        status = THREAD_COMPLETED;
    }

    /**
     * Performs suspend logic if the thread has been suspended
     */
    public void checkSuspended() {
        if (status == THREAD_SUSPENDED) {
            synchronized (this) {
                while (status == THREAD_SUSPENDED && executionThread == thisThread) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    /**
     * Starts the job.
     */
    public void start() {
        status = THREAD_STARTED;
        startTime = new Date();
        executionThread = new Thread(this);
        executionThread.setPriority(priority);
        executionThread.start();
    }

    /**
     * Stop the job
     */
    public synchronized void stop() {
        executionThread = null;
        notify();
        endTime = new Date();
        status = THREAD_STOPPED;
    }

    /**
     * Ssuspend the job.
     */
    public synchronized void suspend() {
        status = THREAD_SUSPENDED;
        log.debug("suspended");
    }

    /**
     * Restart the job.
     */
    public synchronized void resume() {
        status = THREAD_STARTED;
        notifyAll();
        log.debug("resumed");
    }

    /**
     * This method allows the user to set the Thread priority of this
     * controllable object. Possible priorities are: a) Thread.NORM_PRIORITY, b)
     * Thread.MAX_PRIORITY c) Thread.MIN_PRIORITY The default priority is
     * Thread.NORM_PRIORITY.
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * This method allows the user to set the interval between each thread loop.
     * Default is 10 (0.001 seconds);
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * Set the UnitisedProcess for this UnitisedProcess
     */
    public void setProcess(com.explosion.utilities.process.threads.Process process) {
        this.process = (UnitisedProcess) process;
    }

    /**
     * Gets the process for this UnitisedProcess
     */
    public com.explosion.utilities.process.threads.Process getProcess() {
        return process;
    }

    /**
     * This method returns the actual thread object upon which this Runnable
     * object is operating.
     */
    public Thread getRunThread() {
        return thisThread;
    }

    /**
     * This method returns status of the thread
     */
    public int getStatus() {
        return status;
    }

    /**
     * Kills the job
     */
    public void kill() {
        stop();
        wasKilled = true;
    }

    /**
     * Returns whether or not the job was killed
     */
    public boolean wasKilled() {
        return wasKilled;
    }

    /**
     * Returns a boolean value indicating whther this is a userThread or not
     * @return
     */
    public boolean isUserThread() {
        return this.isUserProcess;
    }

    /**
     * Sets whtether or not this is a user process.  True means that it is
     * @param truth
     */
    public void setUserThread(boolean truth) {
        this.isUserProcess = true;
    }
}
