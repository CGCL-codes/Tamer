package uk.org.ogsadai.client.toolkit;

import uk.org.ogsadai.resource.ResourceID;

/**
 * Session operation that specifies that a new session is to be created.
 *
 * @author The OGSA-DAI Project Team
 */
public class CreateSession implements SessionOperation {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2002 - 2007.";

    /** Resource ID of session to create (optional) */
    private ResourceID mResourceID;

    /**
     * Creates a new session.  The session resource ID will be created at the
     * server.
     */
    public CreateSession() {
        mResourceID = null;
    }

    /**
     * Creates a new session with the given session resource ID.
     * 
     * @param resourceID session resource ID.
     */
    public CreateSession(final ResourceID resourceID) {
        mResourceID = resourceID;
    }

    public boolean hasSessionID() {
        return (mResourceID != null);
    }

    public ResourceID getSessionID() {
        return mResourceID;
    }

    public boolean shouldCreateNewSession() {
        return true;
    }
}
