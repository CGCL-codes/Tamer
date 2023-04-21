package org.mobicents.media.server.scheduler;

/**
 * Defines interface for monitoring task chain execution process.
 * 
 * @author kulikov
 */
public interface TaskChainListener {

    /**
     * Called when task has been executed successfully
     */
    public void onTermination();

    /**
     * Called when task has been failed.
     * @param e 
     */
    public void onException(Exception e);
}
