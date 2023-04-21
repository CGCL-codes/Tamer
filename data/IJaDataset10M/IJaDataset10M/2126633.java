package com.google.code.timetrail.backend;

import java.io.Serializable;

/**
 * This event will act as a temporary wrapper for an event's text it might
 * eventually do actions, but that's not gonna be implemented now BOUNUS! IT
 * FORMATS THE TEXT #wow #whoa
 * 
 * @author Steven
 * @version 1.0
 */
public class Event implements Serializable {

    /** Message for this class */
    protected String messageText;

    /**
	 * the version ID of Event, CHANGE IT if control gets incompatibly changed
	 **/
    private static final long serialVersionUID = 50L;

    /**
	 * dummyEvent - the standard event text, when there is no event.
	 */
    private static final String DUMMYEVENT = "NO_CURRENT_EVENT";

    /**
	 * Number for using modulus at 78
	 */
    private static final int MODONE = 78;

    /**
	 * Number for using modulus at 79
	 */
    private static final int MODTWO = 79;

    /**
	 * gameControl - the instance of the game to be acted upon by the event
	 */
    private Control gameControl;

    /**
	 * This is a custom event, where the event text is input, and no action is
	 * performed. It is assumed there is no action, or it had already happened.
	 * 
	 * @param messageText
	 *            the text that describes the event
	 * @param gameControl
	 *            the instance of the game to be acted upon by the event
	 */
    public Event(String messageText, Control gameControl) {
        this.gameControl = gameControl;
        this.messageText = "";
        formatMessage(messageText);
    }

    /**
	 * This is a dummy event. There is no action performed, and the message text
	 * implies there was no event
	 * 
	 * @param gameControl
	 *            the instance of the game to be acted upon by the event
	 */
    public Event(Control gameControl) {
        this.gameControl = gameControl;
        messageText = DUMMYEVENT;
    }

    /**
	 * this is a blank event, the message is the dummy message There is no
	 * action
	 */
    public Event() {
        messageText = DUMMYEVENT;
    }

    /**
	 * Formats the text in Message String to be 80 characters per line. It then
	 * sets the Event's messageText field to be the formatted string
	 * 
	 * @param messageText
	 */
    public final void formatMessage(String messageText) {
        this.messageText = "";
        final StringBuffer messageString = new StringBuffer("");
        for (int i = 0; i < messageText.length(); i++) {
            if (i != 0 && i % MODONE == 0 && messageText.charAt(i) != ' ') {
                messageString.append('-');
            } else if (i != 0 && i % MODTWO == 0) {
                messageString.append('\n');
            } else {
                messageString.append(messageText.charAt(i));
            }
        }
        this.messageText = messageString.toString();
    }

    /**
	 * @return the standard message text for no event
	 */
    public final String getDummyText() {
        return DUMMYEVENT;
    }

    /**
	 * @return the instance of the game that is to be acted upon
	 */
    public Control getControl() {
        return gameControl;
    }

    /**
	 * The action to be performed upon the Control. This is what should happen,
	 * based upon the text in the messageText
	 */
    public void action() {
        for (int i = 0; i < 0; i++) {
            i++;
        }
    }

    /**
	 * returns the message text for future use
	 * 
	 * @return the formatted message text
	 */
    public String getMessageText() {
        return messageText;
    }

    /**
	 * The toString for this class
	 * @return the string as the class name
	 */
    @Override
    public String toString() {
        return "vent";
    }
}
