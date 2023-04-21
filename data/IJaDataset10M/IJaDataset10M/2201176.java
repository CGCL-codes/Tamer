package net.sf.unruly.util;

import net.sf.unruly.Session;
import net.sf.unruly.UnrulyException;
import net.sf.unruly.impl.SessionObject;

/**
 * Session access utilities for use by rules objects.
 * These utilities should be used from within rules objects instead of accessing 
 * session interfaces directly.
 */
public abstract class SessionUtils {

    public <T> T create(final Class<T> mappedClass, Object parent) {
        return getSession(parent).create(mappedClass);
    }

    private static Session getSession(Object object) {
        if (object instanceof SessionObject) {
            Session session = ((SessionObject) object).getSession();
            if (session == null) {
                throw new UnrulyException("the session object is not associated with a session : " + object);
            }
            return session;
        } else {
            throw new UnrulyException("the object is not session-aware it was not created by a session : " + object);
        }
    }
}
