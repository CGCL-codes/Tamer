package org.blankie.lang;

public class FailSafeException extends RuntimeException {

    private static final long serialVersionUID = 0L;

    public FailSafeException(String message) {
        super(message);
    }
}
