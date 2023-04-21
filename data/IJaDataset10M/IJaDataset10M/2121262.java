package com.aptana.ide.scripting.events;

import com.aptana.ide.editors.profiles.ProfileURI;

/**
 * @author Paul Colton
 */
public class ProfilesDeleteEvent extends Event {

    private static final long serialVersionUID = 7769730133838362691L;

    /**
	 * This event's type name
	 */
    public static final String eventType = "ProfilesDeleteEvent";

    /**
	 * @param target
	 * @param id
	 * @param uris
	 */
    public ProfilesDeleteEvent(Object target, int id, ProfileURI[] uris) {
        super(eventType, target);
        this.defineProperty("id", new Integer(id), READONLY | PERMANENT);
        this.defineProperty("uris", uris, READONLY | PERMANENT);
    }
}
