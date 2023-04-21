package com.jramoyo.qfixmessenger.quickfix;

import quickfix.Message;
import quickfix.SessionID;

/**
 * Interface for QuickFIX message listeners
 * 
 * @author jamoyo
 */
public interface QFixMessageListener {

    public static final String SENT = "Sent";

    public static final String RECV = "Received";

    /**
	 * Passes a message to the listener
	 * 
	 * @param direction the direction (Sent/Received)
	 * @param message the QuickFIX message
	 * @param sessionId the QuickFIX session
	 */
    void onMessage(String direction, Message message, SessionID sessionId);
}
