package net.sf.repbot.db;

@SuppressWarnings("serial")
public class NoSuchUserException extends Exception {

    public NoSuchUserException() {
        super();
    }

    public NoSuchUserException(String s) {
        super(s);
    }
}
