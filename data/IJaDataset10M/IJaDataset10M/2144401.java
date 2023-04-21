package ch.fusun.baron.printing.service.impl;

import java.util.Date;
import ch.fusun.baron.core.rmi.User;

/**
 * Holds the messager and the message
 */
public class GameMessage {

    private User user;

    private String text;

    private long timeStamp;

    /**
	 * Kryo constructor
	 */
    public GameMessage() {
    }

    /**
	 * @param user
	 *            The uttering user
	 * @param text
	 *            The uttered text
	 */
    public GameMessage(User user, String text) {
        this.user = user;
        this.text = text;
        this.timeStamp = new Date().getTime();
    }

    /**
	 * @return The user
	 */
    public User getUser() {
        return user;
    }

    /**
	 * @return The message
	 */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return user + ": " + text;
    }

    /**
	 * @return The time the message was created
	 */
    public long getTimeStamp() {
        return timeStamp;
    }
}
