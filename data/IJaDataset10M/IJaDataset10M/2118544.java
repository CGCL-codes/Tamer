package org.jsecurity.ri.session;

import org.jsecurity.session.ExpiredSessionException;
import org.jsecurity.session.InvalidSessionException;
import org.jsecurity.session.Session;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Date;

/**
 * Default business-tier implementation of the {@link ValidatingSessionManager} interface.
 *
 * @since 0.1
 * @author Les Hazlewood
 * @author Jeremy Haile
 */
public class DefaultSessionManager extends AbstractSessionManager implements ValidatingSessionManager {

    /**
     * Validator used to validate sessions on a regular basis.
     * By default, the session manager will use Quartz to schedule session validation, but this
     * can be overridden by calling {@link #setSessionValidationScheduler(SessionValidationScheduler)}
     */
    protected SessionValidationScheduler sessionValidationScheduler = new QuartzSessionValidationScheduler(this);

    public DefaultSessionManager() {
        super.setSessionClass(SimpleSession.class);
    }

    public void init() {
        super.init();
        if (sessionValidationScheduler != null) {
            sessionValidationScheduler.startSessionValidation();
        } else {
            if (log.isWarnEnabled()) {
                log.warn("No session validation scheduler is configured, so sessions may not be validated.");
            }
        }
    }

    public void setSessionValidationScheduler(SessionValidationScheduler sessionValidationScheduler) {
        this.sessionValidationScheduler = sessionValidationScheduler;
    }

    protected void onStop(Session session) {
        if (log.isTraceEnabled()) {
            log.trace("Updating stop time of session with id [" + session.getSessionId() + "]");
        }
        ((SimpleSession) session).setStopTimestamp(new Date());
    }

    protected void onExpire(Session session) {
        if (log.isTraceEnabled()) {
            log.trace("Updating stop time and expiration status of session with id " + session.getSessionId() + "]");
        }
        SimpleSession ss = (SimpleSession) session;
        ss.setStopTimestamp(new Date());
        ss.setExpired(true);
    }

    protected void onTouch(Session session) {
        if (log.isTraceEnabled()) {
            log.trace("Updating last access time of session with id [" + session.getSessionId() + "]");
        }
        ((SimpleSession) session).setLastAccessTime(new Date());
    }

    protected void init(Session newInstance, InetAddress hostAddr) {
        if (newInstance instanceof SimpleSession) {
            SimpleSession ss = (SimpleSession) newInstance;
            ss.setHostAddress(hostAddr);
        }
    }

    /**
     * @see org.jsecurity.ri.session.ValidatingSessionManager#validateSessions()
     */
    public void validateSessions() {
        if (log.isInfoEnabled()) {
            log.info("Validating all active sessions...");
        }
        int invalidCount = 0;
        Collection<Session> activeSessions = getSessionDAO().getActiveSessions();
        if (activeSessions != null && !activeSessions.isEmpty()) {
            for (Session s : activeSessions) {
                try {
                    validate(s);
                } catch (InvalidSessionException e) {
                    if (log.isDebugEnabled()) {
                        boolean expired = (e instanceof ExpiredSessionException);
                        String msg = "Invalidated session with id [" + s.getSessionId() + "]" + (expired ? " (expired)" : " (stopped)");
                        log.debug(msg);
                    }
                    invalidCount++;
                }
            }
        }
        if (log.isInfoEnabled()) {
            String msg = "Finished session validation.";
            if (invalidCount > 0) {
                msg += "  [" + invalidCount + "] sessions were stopped.";
            } else {
                msg += "  No sessions were stopped.";
            }
            log.info(msg);
        }
    }

    public void validateSession(Serializable sessionId) {
        retrieveAndValidateSession(sessionId);
    }
}
