package de.vsis.coordination.coordinator.util.worker;

import java.util.ArrayList;

/**
 * Simple message queue
 * 
 * @author Michael von Riegen
 */
public class MessageQueue {

    private ArrayList<TransactionMessage> queue;

    private static final MessageQueue _instance;

    static {
        try {
            _instance = new MessageQueue();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
	 * Constructor. Set to private in order to implement a singleton
	 */
    private MessageQueue() {
        queue = new ArrayList<TransactionMessage>();
    }

    /**
	 * Returns an instance of the queue
	 * 
	 * @return Instance
	 */
    public static MessageQueue getInstance() {
        return _instance;
    }

    /**
	 * queues a message
	 * 
	 * @param queueElement
	 */
    public synchronized void queue(TransactionMessage queueElement) {
        queue.add(queueElement);
        notifyAll();
    }

    /**
	 * dequeues a message
	 * 
	 * @return Message
	 * @throws InterruptedException
	 */
    public synchronized TransactionMessage dequeue() throws InterruptedException {
        while (queue.size() == 0) {
            wait();
        }
        TransactionMessage msg = queue.remove(0);
        return msg;
    }
}
