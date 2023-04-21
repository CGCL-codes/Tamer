package net.sf.babble.events;

import java.util.EventObject;
import net.sf.babble.UserMode;

/**
 * This event models a request for the status of specified user modes.
 * @version $Id: UserModeRequestEvent.java 262 2007-02-06 06:55:29 +0000 (Tue, 06 Feb 2007) speakmon $
 * @author Ben Speakmon
 */
public class UserModeRequestEvent extends EventObject {

    /**
     * Holds value of property modes.
     */
    private UserMode[] modes;

    /**
     * Creates a new UserModeRequestEvent.
     * @param source the <code>UserMode[]</code> array requested for the specified user
     */
    public UserModeRequestEvent(Object source) {
        super(source);
        modes = (UserMode[]) source;
    }

    /**
     * Returns the modes whose status was requested.
     * @return the modes
     */
    public UserMode[] getModes() {
        return this.modes;
    }
}
