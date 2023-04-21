package edu.yale.its.tp.cas.auth.provider;

/** A simple, dummy authenticator. */
public class SampleHandler extends WatchfulPasswordHandler {

    public boolean authenticate(javax.servlet.ServletRequest request, String username, String password) {
        if (username.equals(password)) return true;
        return false;
    }
}
