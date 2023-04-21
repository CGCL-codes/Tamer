package com.ma_la.myRunning.session;

import com.ma_la.myRunning.*;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author <a href="mailto:mail@myrunning.de">Martin Lang</a>
 */
public abstract class Session {

    private String sessionId;

    private Date createdTs;

    private Date lastAccessedTs;

    private boolean loggedIn = false;

    private User user;

    private Locale locale;

    public Session(String sessionId) {
        this.sessionId = sessionId;
        this.createdTs = new Date();
        this.lastAccessedTs = new Date();
        setLocale(Locale.GERMAN);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setLastAccessedTs() {
        this.lastAccessedTs = new Date();
    }

    public Date getLastAccessedTs() {
        return this.lastAccessedTs;
    }

    public Date getCreatedTs() {
        return this.createdTs;
    }

    public boolean isUserLoggedIn() {
        return loggedIn;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public User getUser() {
        return user;
    }
}
