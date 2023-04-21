package protopeer.queues;

import java.util.*;
import protopeer.network.*;

/**
 * 
 * Filters messages by their Java class and splits the messages into two
 * separate queues: matching and unmatching.
 * 
 */
public class FilteringQueue extends MessageQueue {

    private Set<Class<?>> filteredMessageClasses;

    private MessageQueue queueForMatchingMessages;

    private MessageQueue queueForNonmatchingMessages;

    /**
	 * true is appended if messageAvaiable called for queueForMatchingMessages
	 * false is appended if messageAvaiable called for
	 * queueForNonmatchingMessages
	 */
    private LinkedList<Boolean> dequeuingOrder = new LinkedList<Boolean>();

    /**
	 * 
	 * @param queueForMatchingMessages
	 *            the user-supplied queue for
	 * @param queueForNonmatchingMessages
	 * @param filteredMessageClasses
	 *            if a messsage matches one of these classes it will be put on
	 *            the <code>queueForMatchingMessages</code> otherwise on the
	 *            <code>queueForNonmatchingMessages</code>
	 */
    public FilteringQueue(MessageQueue queueForMatchingMessages, MessageQueue queueForNonmatchingMessages, Set<Class<?>> filteredMessageClasses) {
        super();
        if (filteredMessageClasses == null || queueForMatchingMessages == null || queueForNonmatchingMessages == null) {
            throw new IllegalArgumentException();
        }
        this.filteredMessageClasses = new HashSet<Class<?>>(filteredMessageClasses);
        this.queueForMatchingMessages = queueForMatchingMessages;
        this.queueForNonmatchingMessages = queueForNonmatchingMessages;
        setUpListeners();
    }

    /**
	 * Same as the other contructor, but the <code>filteredMessageClasses</code>
	 * is initially empty and can be modified by calling the
	 * <code>addFilteredMessageClass</code>
	 * 
	 * @param queueForMatchingMessages
	 * @param queueForNonmatchingMessages
	 */
    public FilteringQueue(MessageQueue queueForMatchingMessages, MessageQueue queueForNonmatchingMessages) {
        super();
        if (queueForMatchingMessages == null || queueForNonmatchingMessages == null) {
            throw new IllegalArgumentException();
        }
        this.filteredMessageClasses = new HashSet<Class<?>>();
        this.queueForMatchingMessages = queueForMatchingMessages;
        this.queueForNonmatchingMessages = queueForNonmatchingMessages;
        setUpListeners();
    }

    private class PullingQueueListener implements MessageQueueListener {

        private MessageQueue queue;

        public PullingQueueListener(MessageQueue queue) {
            this.queue = queue;
        }

        public void messageAvailable() {
            if (queueForMatchingMessages == queue) {
                dequeuingOrder.addFirst(true);
            } else {
                dequeuingOrder.addFirst(false);
            }
            fireMessageAvailable();
        }

        public void messageDropped(Message message) {
            fireMessageDropped(message);
        }
    }

    ;

    private void setUpListeners() {
        queueForMatchingMessages.addMessageQueueListener(new PullingQueueListener(queueForMatchingMessages));
        queueForNonmatchingMessages.addMessageQueueListener(new PullingQueueListener(queueForNonmatchingMessages));
    }

    /**
	 * Adds the <code>class</code> to the list of message classes that are
	 * enqueued on the <code>queueForMatchingMessages</code>
	 * 
	 * @param clazz
	 */
    public void addFilteredMessageClass(Class<?> clazz) {
        filteredMessageClasses.add(clazz);
    }

    @Override
    public void enqueue(Message message) {
        synchronized (dequeuingOrder) {
            if (filteredMessageClasses.contains(message.getClass())) {
                queueForMatchingMessages.enqueue(message);
            } else {
                queueForNonmatchingMessages.enqueue(message);
            }
        }
    }

    public MessageQueue getQueueForMatchingMessages() {
        return queueForMatchingMessages;
    }

    public MessageQueue getQueueForNonmatchingMessages() {
        return queueForNonmatchingMessages;
    }

    public Set<Class<?>> getFilteredMessageClasses() {
        return Collections.unmodifiableSet(filteredMessageClasses);
    }

    @Override
    public Message dequeue() {
        synchronized (dequeuingOrder) {
            Boolean whichOne = dequeuingOrder.pollLast();
            if (whichOne != null) {
                if (whichOne.booleanValue()) {
                    return queueForMatchingMessages.dequeue();
                } else {
                    return queueForNonmatchingMessages.dequeue();
                }
            }
        }
        return null;
    }

    public int size() {
        synchronized (dequeuingOrder) {
            return queueForMatchingMessages.size() + queueForNonmatchingMessages.size();
        }
    }

    public void dropAll() {
        synchronized (dequeuingOrder) {
            queueForMatchingMessages.dropAll();
            queueForNonmatchingMessages.dropAll();
            dequeuingOrder.clear();
        }
    }
}
