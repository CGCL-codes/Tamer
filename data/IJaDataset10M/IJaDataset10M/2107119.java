package com.ewansilver.raindrop.demo.pushserver;

import java.util.LinkedList;
import java.util.List;

/**
 * Generates a rotating list of messages.
 * 
 * @author Ewan Silver
 */
public class RotatingMessageFactory implements MessageFactory {

    private List messages;

    /**
	 * Constructor.
	 */
    public RotatingMessageFactory() {
        super();
        messages = new LinkedList();
        messages.add("Message 1\r\n");
        messages.add("Message 2 - the new one\r\n");
        messages.add("Message 3 - guess what this is new too!\r\n");
        messages.add("Message 4\r\n");
    }

    public String message() {
        String message = (String) messages.remove(0);
        messages.add(message);
        return message;
    }
}
