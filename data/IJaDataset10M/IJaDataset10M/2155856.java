package net.sourceforge.webskin;

public class WebSkinException extends RuntimeException {

    public WebSkinException(String message) {
        super(message);
    }

    public WebSkinException(String message, Throwable t) {
        super(message, t);
    }
}
