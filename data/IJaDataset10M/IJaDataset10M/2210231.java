package com.tscribble.bitleech.core.download.auth;

import org.apache.log4j.Logger;

public abstract class Authentication {

    /**
	 * Logger for this class
	 */
    private static final Logger log = Logger.getLogger("Authentication");

    private String user;

    private String pass;

    private String site;

    private IAuthType authType;

    private boolean authorized;

    public Authentication() {
        user = "";
        pass = "";
        site = "";
    }

    public Authentication(String site, String user, String pass) {
        this.site = site;
        this.user = user;
        this.pass = pass;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean valid) {
        this.authorized = valid;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSite() {
        return site;
    }

    public void setPassword(String pass) {
        this.pass = pass;
    }

    public String getPassword() {
        return pass;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public IAuthType getType() {
        return authType;
    }

    public void setType(IAuthType authType) {
        this.authType = authType;
    }

    @Override
    public String toString() {
        return "[type: " + getType() + ",site: " + site + ", user: " + user + ", pass: " + pass + ", authorized: " + authorized + "]";
    }
}
