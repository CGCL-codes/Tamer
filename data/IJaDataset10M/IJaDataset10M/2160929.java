package org.beanopen.f.exception;

public class ActionAccessException extends Exception {

    private static final long serialVersionUID = 1L;

    public ActionAccessException() {
    }

    public ActionAccessException(String arg0) {
        super(arg0);
    }

    public ActionAccessException(Throwable arg0) {
        super(arg0);
    }

    public ActionAccessException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
