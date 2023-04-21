package org.smslib;

import java.io.Serializable;

/**
 * Class representing the different types of messages.
 */
public class MessageTypes implements Serializable {

    private static final long serialVersionUID = -5031471839787113248L;

    private final String s;

    private MessageTypes(String s) {
        this.s = s;
    }

    public String toString() {
        return s;
    }

    /**
	 * Inbound message.
	 */
    public static final MessageTypes INBOUND = new MessageTypes("INBOUND");

    /**
	 * Outbound message.
	 */
    public static final MessageTypes OUTBOUND = new MessageTypes("OUTBOUND");

    /**
	 * Status (delivery) report message
	 */
    public static final MessageTypes STATUSREPORT = new MessageTypes("STATUSREPORT");

    /**
	 * Outbound WAP SI message.
	 */
    public static final MessageTypes WAPSI = new MessageTypes("WAPSI");

    /**
	 * Unhandled / unknown message.
	 */
    public static final MessageTypes UNKNOWN = new MessageTypes("UNKNOWN");
}
