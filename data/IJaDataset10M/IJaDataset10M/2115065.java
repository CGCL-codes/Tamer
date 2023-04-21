package org.archive.crawler.frontier.precedence;

import org.archive.crawler.frontier.WorkQueue;
import org.archive.state.Module;

/**
 * Superclass for QueuePrecedencePolicies, which set a integer precedence value 
 * on uri-queues inside the frontier when the uri-queue is first created, and 
 * before the uri-queue is placed on a new internal queue-of-queues. 
 */
public abstract class QueuePrecedencePolicy implements Module {

    /**
     * Set an appropriate initial precedence value on the given
     * newly-created WorkQueue.
     * 
     * @param wq WorkQueue to modify
     */
    public abstract void queueCreated(WorkQueue wq);

    /**
     * Update an appropriate initial precedence value on the given
     * already-existing WorkQueue.
     * @param wq WorkQueue to modify
     */
    public abstract void queueReevaluate(WorkQueue wq);
}
