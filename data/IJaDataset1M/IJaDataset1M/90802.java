package org.acegisecurity.concurrent;

import org.springframework.util.Assert;
import java.util.Date;

/**
 * Represents a record of a session within the Acegi Security framework.
 * 
 * <p>
 * This is primarily used for concurrent session support.
 * </p>
 * 
 * <p>
 * Sessions have three states: active, expired, and destroyed. A session can
 * that is invalidated by <code>session.invalidate()</code> or via Servlet
 * Container management is considered "destroyed". An "expired" session, on
 * the other hand, is a session that Acegi Security wants to end because it
 * was selected for removal for some reason (generally as it was the least
 * recently used session and the maximum sessions for the user were reached).
 * An "expired" session is removed as soon as possible by a
 * <code>Filter</code>.
 * </p>
 *
 * @author Ben Alex
 * @version $Id: SessionInformation.java,v 1.2 2005/11/17 00:55:56 benalex Exp $
 */
public class SessionInformation {

    private Date lastRequest;

    private Object principal;

    private String sessionId;

    private boolean expired = false;

    public SessionInformation(Object principal, String sessionId, Date lastRequest) {
        Assert.notNull(principal, "Principal required");
        Assert.hasText(sessionId, "SessionId required");
        Assert.notNull(lastRequest, "LastRequest required");
        this.principal = principal;
        this.sessionId = sessionId;
        this.lastRequest = lastRequest;
    }

    private SessionInformation() {
    }

    public boolean isExpired() {
        return expired;
    }

    public Date getLastRequest() {
        return lastRequest;
    }

    public Object getPrincipal() {
        return principal;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void expireNow() {
        this.expired = true;
    }

    /**
     * Refreshes the internal lastRequest to the current date and time.
     */
    public void refreshLastRequest() {
        this.lastRequest = new Date();
    }
}
