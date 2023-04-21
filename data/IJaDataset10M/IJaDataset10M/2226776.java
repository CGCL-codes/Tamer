package net.sf.djback.api;

import java.util.EventObject;

public class Event extends EventObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Message message;

    public Message getMessage() {
        return message;
    }

    public Session getSession() {
        return (Session) getSource();
    }

    public Event(Session source, Message message) {
        super(source);
        this.message = message;
    }
}
