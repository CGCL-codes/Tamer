package com.hs.mail.web.exception;

/**
 * 
 * @author Won Chul Doh
 * @since Sep 1, 2010
 *
 */
public class SessionRequiredException extends KeyedException {

    private static final long serialVersionUID = -4916305753429395487L;

    public SessionRequiredException(String key) {
        super(key);
    }
}
