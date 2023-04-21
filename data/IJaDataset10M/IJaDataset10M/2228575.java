package com.excilys.sugadroid.services.exceptions;

/**
 * Exception thrown when the session token is not valid anymore, and the user
 * needs to login again
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public class InvalidSessionException extends ServiceException {

    private static final long serialVersionUID = 1L;

    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public InvalidSessionException() {
    }

    public InvalidSessionException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidSessionException(Throwable throwable) {
        super(throwable);
    }

    public InvalidSessionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidSessionException(String error, String description) {
        super(error, description);
    }

    public InvalidSessionException(String error, String description, String errorNumber) {
        super(error, description, errorNumber);
    }

    @Override
    public String getDescription() {
        if (sessionId != null) {
            return super.getDescription() + " | Session id: " + sessionId;
        } else {
            return super.getDescription();
        }
    }
}
