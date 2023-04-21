package net.sf.borg9.persistence.access;

public class DataAccessException extends Exception {

    public DataAccessException(String aMessage) {
        super(aMessage);
    }

    public DataAccessException(String aMessage, Throwable aThrowable) {
        super(aMessage, aThrowable);
    }
}
