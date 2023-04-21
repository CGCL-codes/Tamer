package com.javaforge.honeycomb.tapestry;

import java.util.Map;

/**
 * Manages a conversation. Can be told to terminate the current conversation.
 * New Conversations will started lazily on the first getCurrentConversation.
 * 
 * @author Marcus Schulte
 */
public interface ConversationManager {

    /**
	 * @return
	 */
    Map<String, Object> getCurrentConversation();

    void terminateConversation();

    void pauseConversation();
}
