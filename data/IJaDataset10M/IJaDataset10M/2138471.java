package org.xmms2;

public class NotADictException extends Exception {

    private static final long serialVersionUID = 4946587346814855824L;

    public NotADictException() {
        super();
    }

    public NotADictException(String message) {
        super(message);
    }

    public NotADictException(Throwable cause) {
        super(cause);
    }

    public NotADictException(String message, Throwable cause) {
        super(message, cause);
    }
}
