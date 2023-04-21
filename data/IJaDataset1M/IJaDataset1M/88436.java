package com.android.im.engine;

/**
 * Interface that allows the implementing classes to listen for ChatSession
 * creation. The typical implementation will register MessageListener with the
 * created session so that it will be notified when new message arrived to the
 * session. Listeners are registered with ChatSessionManager.
 */
public interface ChatSessionListener {

    /**
     * Called when a new ChatSession is created.
     *
     * @param session the created ChatSession.
     */
    public void onChatSessionCreated(ChatSession session);
}
