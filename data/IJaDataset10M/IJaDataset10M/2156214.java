package de.ui.sushi.fs;

public class ReflectionException extends Exception {

    public ReflectionException(String msg) {
        super(msg);
    }

    public ReflectionException(String msg, Throwable t) {
        super(msg, t);
    }
}
