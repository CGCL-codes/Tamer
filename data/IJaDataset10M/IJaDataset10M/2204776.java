package org.springframework.webflow.execution.repository.support;

import org.springframework.webflow.conversation.Conversation;
import org.springframework.webflow.conversation.ConversationManager;
import org.springframework.webflow.execution.repository.FlowExecutionLock;

/**
 * A flow execution lock that locks a conversation managed by a {@link ConversationManager}.
 * <p>
 * This implementation ensures multiple threads cannot manipulate the same conversation at the same time. The locked
 * conversation is the sole gateway to a flow execution, and a lock on it prevents access to any associated execution.
 * 
 * @see ConversationManager
 * @see Conversation
 * @see Conversation#lock()
 * @see Conversation#unlock()
 * 
 * @author Keith Donald
 */
class ConversationBackedFlowExecutionLock implements FlowExecutionLock {

    /**
	 * The conversation to lock.
	 */
    private Conversation conversation;

    /**
	 * Creates a new conversation-backed flow execution lock.
	 * @param conversation the conversation to lock
	 */
    public ConversationBackedFlowExecutionLock(Conversation conversation) {
        this.conversation = conversation;
    }

    public void lock() {
        conversation.lock();
    }

    public void unlock() {
        conversation.unlock();
    }
}
