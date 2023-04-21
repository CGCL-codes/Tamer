package com.continuent.tungsten.replicator.pipeline;

import com.continuent.tungsten.replicator.event.ReplDBMSEvent;

/**
 * Denotes a schedule, which monitors and directs task execution.
 * 
 * @author <a href="mailto:robert.hodges@continuent.com">Robert Hodges</a>
 * @version 1.0
 */
public interface Schedule {

    /**
     * Task must call this method before exit to tell the schedule that it has
     * completed.
     */
    public void taskEnd();

    /**
     * Set the last processed event, which triggers checks for watches. If a
     * fulfilled watch directs the task to terminate, the isCancelled call will
     * return true.
     * 
     * @throws InterruptedException Thrown if thread is interrupted.
     */
    public void setLastProcessedEvent(ReplDBMSEvent event) throws InterruptedException;

    /**
     * Returns true if the task is canceled. Tasks must check this each
     * iteration to decide whether to continue.
     */
    public boolean isCancelled();
}
