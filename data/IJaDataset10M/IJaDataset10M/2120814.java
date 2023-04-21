package org.josso.gateway.session.exceptions;

/**
 * @author <a href="mailto:sgonzalez@josso.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id: NoSuchSessionException.java 543 2008-03-18 21:34:58Z sgonzalez $
 */
public class NoSuchSessionException extends SSOSessionException {

    /**
     * @param sessionId the session identifier that is invalid.
     */
    public NoSuchSessionException(String sessionId) {
        super(sessionId);
    }

    public String getMessage() {
        return "JOSSO Session not found : " + super.getMessage();
    }
}
