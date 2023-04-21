package org.itracker.web.util;

import java.io.Serializable;
import java.util.Date;

/**
 * What's this for? Please comment!
 * 
 * @author ready
 */
public class SessionTracker implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Date now;

    private String login;

    private String sessionId;

    public SessionTracker() {
        now = new Date();
    }

    public SessionTracker(String login, String sessionId) {
        this();
        this.login = login;
        this.sessionId = sessionId;
    }

    protected void finalize() throws Throwable {
        SessionManager.invalidateSession(this.login);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getNow() {
        if (null == now) return null;
        return new Date(now.getTime());
    }

    public void setNow(Date now) {
        if (null == now) this.now = null; else this.now = new Date(now.getTime());
    }
}
