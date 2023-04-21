package org.opennfc.service;

/**
 * Task done in separate thread
 * 
 * @param <PARAMETER> Parameter type
 */
public interface ThreadedTask<PARAMETER> {

    /**
     * Call when task turn arrive to execute it
     * 
     * @param taskID The task ID
     * @param parameters The parameters given to the task
     */
    public void excuteTask(int taskID, PARAMETER... parameters);

    /** Call when task is cancel (Typically on timeout) */
    public void cancel();
}
