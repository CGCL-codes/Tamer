package org.asteriskjava.manager.action;

/**
 * The QueueResetAction resets all statistical data of a given queue or all queues.<p>
 * It is implemented in <code>apps/app_queue.c</code><p>
 * Available since Asterisk 1.6.
 *
 * @author Sebastian
 * @since 1.0.0
 */
public class QueueResetAction extends AbstractManagerAction {

    private static final long serialVersionUID = 1L;

    private String queue;

    /**
     * Creates a new QueueResetAction that resets statistical data of all queues.
     */
    public QueueResetAction() {
    }

    /**
     * Creates a new QueueResetAction that resets statistical data of the given queue.
     *
     * @param queue the name of the queue to reset.
     */
    public QueueResetAction(String queue) {
        this.queue = queue;
    }

    @Override
    public String getAction() {
        return "QueueReset";
    }

    /**
     * Returns the name of the queue to reset.
     *
     * @return the name of the queue to reset or <code>null</code> for all queues.
     */
    public String getQueue() {
        return queue;
    }

    /**
     * Sets the name of the queue to reset.
     *
     * @param queue the name of the queue to reset or <code>null</code> for all queues.
     */
    public void setQueue(String queue) {
        this.queue = queue;
    }
}
