package com.volantis.mps.session.pool;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mps.localization.LocalizationFactory;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import org.apache.commons.pool.BaseObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.smpp.Session;

/**
 * Common superclass for both the actual and unpooled (i.e. creates a new
 * session for each request) session pool implementations.
 *
 * @mock.generate
 */
public abstract class SessionPool extends BaseObjectPool {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER = LocalizationFactory.createLogger(SessionPool.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER = LocalizationFactory.createExceptionLocalizer(SessionPool.class);

    /**
     * Set containing all the active sessions in this pool.
     */
    protected Set active = new HashSet();

    /**
     * Set containing all the available (i.e. idle) sessions in this pool.
     */
    protected Set available = new HashSet();

    /**
     * Used to create {@link Session} instances.
     */
    protected PoolableObjectFactory objectFactory;

    /**
     * Indicates whether each session should be validated (using
     * {@link PoolableObjectFactory#validateObject(Object)} before being
     * returned from {@link #borrowObject}
     */
    protected boolean testOnBorrow;

    /**
     * Indicates that this session pool can have unlimited active sessions.
     */
    public static final int UNLIMITED_ACTIVE_SESSIONS = -1;

    public synchronized void returnObject(Object object) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Attempting to return a session to the pool");
        }
        if (!active.remove(object)) {
            throw new SessionException(EXCEPTION_LOCALIZER.format("object-not-from-session-pool", object));
        }
        available.add(object);
    }

    public int getNumActive() throws UnsupportedOperationException {
        return active.size();
    }

    public int getNumIdle() throws UnsupportedOperationException {
        return available.size();
    }

    public synchronized void clear() throws Exception, UnsupportedOperationException {
        for (Iterator i = available.iterator(); i.hasNext(); ) {
            Session s = (Session) i.next();
            s.close();
        }
        available.clear();
    }

    public synchronized void close() throws Exception {
        for (Iterator i = active.iterator(); i.hasNext(); ) {
            Session s = (Session) i.next();
            s.close();
        }
        active.clear();
        clear();
    }

    public synchronized void invalidateObject(Object object) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Attempting to invalidate a session");
        }
        if (!available.remove(object) && !active.remove(object)) {
            throw new SessionException(EXCEPTION_LOCALIZER.format("object-not-from-session-pool", object));
        }
    }

    public void setFactory(PoolableObjectFactory poolableObjectFactory) throws IllegalStateException, UnsupportedOperationException {
        this.objectFactory = poolableObjectFactory;
    }

    /**
     * Return a iterator which iterates over a copy of the underlying pool of
     * sessions. Modifying this will not impact the underlying pool.
     *
     * @return Iterator to iterate over the sessions in the pool
     */
    protected abstract Iterator iterator();

    /**
     * Create a new active session (validating it if validation on borrowing is
     * required).
     *
     * @return Session new active session
     * @throws Exception if there was a problem creating the new session.
     */
    protected Session createNewSession() throws Exception {
        Session session = (Session) objectFactory.makeObject();
        active.add(session);
        if (!validateIfRequired(session)) {
            final String key = "cannot-create-session";
            LOGGER.fatal(key);
            throw new SessionException(EXCEPTION_LOCALIZER.format(key));
        }
        return session;
    }

    /**
     * Ensure that the session is valid, and if not invalidate it. If this
     * method returns false, the session will be nulled.
     *
     * @param session   to validate
     * @return true if the session is valid or if validation is not required,
     * false if the session is not valid (and has now been invalidated)
     * @throws Exception if there was a problem validating the session
     */
    protected boolean validateIfRequired(Session session) throws Exception {
        boolean valid = true;
        if (testOnBorrow) {
            valid = objectFactory.validateObject(session);
            if (!valid) {
                invalidateObject(session);
                session = null;
            }
        }
        return valid;
    }

    /**
     * Return true if the objects returned by this pool will be validated when
     * borrowed, and false otherwise
     *
     * @return true if the objects returned by this pool will be validated when
     * borrowed, and false otherwise
     */
    public boolean isValidatingOnBorrow() {
        return testOnBorrow;
    }
}
