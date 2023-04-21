package com.teletalk.jserver.queue;

/**
 * Interface used by owners of queues.
 * 
 * @see Queue
 * 
 * @author Tobias L�fstrand
 * 
 * @since Beta 1
 */
public interface QueueOwner {

    /**
	 * This methd is called when a external operation is called in the associated queue.
	 * 
	 * @param operationName the name of the operation that was called.
	 * @param keys indices of items in a queue for which the operation was called.
	 */
    public void externalQueueOperationCalled(String operationName, String[] keys);
}
