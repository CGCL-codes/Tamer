package org.jsecurity.session.mgt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsecurity.cache.CacheManager;
import org.jsecurity.cache.CacheManagerAware;
import org.jsecurity.session.InvalidSessionException;
import org.jsecurity.session.Session;
import org.jsecurity.session.mgt.eis.MemorySessionDAO;
import org.jsecurity.session.mgt.eis.SessionDAO;
import org.jsecurity.util.CollectionUtils;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Date;

/**
 * Default business-tier implementation of the {@link ValidatingSessionManager} interface.
 *
 * @author Les Hazlewood
 * @since 0.1
 */
public class DefaultSessionManager extends AbstractValidatingSessionManager implements CacheManagerAware {

    private static final Log log = LogFactory.getLog(DefaultSessionManager.class);

    protected SessionDAO sessionDAO = new MemorySessionDAO();

    public DefaultSessionManager() {
    }

    public void setSessionDAO(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    public SessionDAO getSessionDAO() {
        return this.sessionDAO;
    }

    public void setCacheManager(CacheManager cacheManager) {
        ((CacheManagerAware) getSessionDAO()).setCacheManager(cacheManager);
    }

    protected Session doCreateSession(InetAddress originatingHost) {
        if (log.isTraceEnabled()) {
            log.trace("Creating session for originating host [" + originatingHost + "]");
        }
        Session s = newSessionInstance(originatingHost);
        create(s);
        return s;
    }

    protected Session newSessionInstance(InetAddress inetAddress) {
        return new SimpleSession(inetAddress);
    }

    protected void create(Session session) {
        if (log.isDebugEnabled()) {
            log.debug("Creating new EIS record for new session instance [" + session + "]");
        }
        sessionDAO.create(session);
    }

    protected void onStop(Session session) {
        if (session instanceof SimpleSession) {
            Date stopTs = ((SimpleSession) session).getStopTimestamp();
            ((SimpleSession) session).setLastAccessTime(stopTs);
        }
        super.onStop(session);
    }

    protected void onExpiration(Session session) {
        if (session instanceof SimpleSession) {
            ((SimpleSession) session).setExpired(true);
        }
        onChange(session);
    }

    protected void onChange(Session session) {
        sessionDAO.update(session);
    }

    protected Session retrieveSession(Serializable sessionId) throws InvalidSessionException {
        if (log.isTraceEnabled()) {
            log.trace("Retrieving session with id [" + sessionId + "]");
        }
        Session s = sessionDAO.readSession(sessionId);
        validate(s);
        return s;
    }

    protected Collection<Session> getActiveSessions() {
        Collection<Session> active = sessionDAO.getActiveSessions();
        return active != null ? active : CollectionUtils.emptyCollection(Session.class);
    }
}
