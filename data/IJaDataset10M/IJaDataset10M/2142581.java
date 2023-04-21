package com.cube42.util.thread;

/**
 * Represents a queue of runnable jobs.
 * <p>
 * NOTE: This will be used with threads, so make sure the implmentation is
 * thread safe
 *
 * @author  Matt Paulin
 * @version $Id: JobQueue.java,v 1.8 2003/03/12 00:27:39 zer0wing Exp $
 */
public interface JobQueue {

    /**
     * Returns the number of jobs in the queue
     *
     * @return  the number of jobs in the Queue
     */
    public int getNumJobs();

    /**
     * Returns the next job in the queue, does not remove the job from the
     * queue
     *
     * @return  the next job in the queue, null if there is none
     */
    public Job getNextJob();

    /**
     * Returns the next job in the queue, removing the job from the queue
     *
     * @return  the next job in the queue, null if there is none
     */
    public Job removeNextJob();

    /**
     * Sets the thread manager for the job queue
     *
     * @param   threadManager   The thread manager running this job queue
     * @throws  Cube42RuntimeException if the thread manager is set
     *          more than once
     */
    public void setThreadManager(ThreadManager threadManager);
}
