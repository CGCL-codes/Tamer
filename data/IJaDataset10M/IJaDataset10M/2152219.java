package ludo.exceptions;

public class NoMoreMedalsException extends Exception {

    public NoMoreMedalsException(String msg, Throwable exc) {
        super(msg, exc);
    }

    public NoMoreMedalsException(String msg) {
        super(msg);
    }
}
