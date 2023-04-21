package com.google.code.timetrail.presenter;

import com.google.code.timetrail.backend.Event;

/**
 * The backend for the EventFrame should only be instantiated if the next event
 * is a dummy event PRECONDTION: THE CONTROL'S NEXT EVENT HAS BEEN PASSED IN AND
 * THE DUMMY HAS BEEN SET
 * 
 * @author Steven
 * @version 1.1
 */
public class EventFrameBackend {

    /**
	 * The message text for this class
	 */
    private final String messageText;

    /**
	 * Creates the constructor for this class
	 * 
	 * @param nextEvent the next event
	 */
    public EventFrameBackend(Event nextEvent) {
        messageText = nextEvent.getMessageText();
    }

    /**
	 * Gets the text for the message
	 * 
	 * @return the text for the string
	 */
    public String getMessage() {
        return messageText;
    }

    /**
	 * The name of the class
	 * 
	 * @return the name of the class
	 */
    @Override
    public String toString() {
        return "EventFrameBackend";
    }
}
