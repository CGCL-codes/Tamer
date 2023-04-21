package org.tm4j.admintool;

/**
 * TopicMap Commander processing exception
 */
public class TMCException extends Exception {

    private Throwable m_cause;

    public TMCException(String msg) {
        super(msg);
    }

    public TMCException(String msg, Throwable cause) {
        super(msg + "Cause: " + cause.getMessage());
        m_cause = cause;
    }
}
