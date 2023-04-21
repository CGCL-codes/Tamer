package org.speakmon.babble.events;

import java.util.EventObject;
import org.speakmon.babble.UserInfo;

/**
 * This event models a user leaving the server.
 * @version $Id: QuitEvent.java 239 2004-07-28 05:09:17Z speakmon $
 * @author Ben Speakmon
 */
public class QuitEvent extends EventObject {

    /**
     * Holds value of property user.
     */
    private UserInfo user;

    /**
     * Holds value of property reason.
     */
    private String reason;

    /**
     * Creates a new QuitEvent.
     * @param source the <code>UserInfo</code> for the user who left
     */
    public QuitEvent(Object source) {
        super(source);
        user = (UserInfo) source;
    }

    /**
     * Returns the <code>UserInfo</code> for the user who left.
     * @return the <code>UserInfo</code> for the user who left
     */
    public UserInfo getUser() {
        return this.user;
    }

    /**
     * Returns the user's reason for leaving.
     * @return the reason for leaving
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Sets the reason for leaving.
     * @param reason the reason for leaving
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
}
