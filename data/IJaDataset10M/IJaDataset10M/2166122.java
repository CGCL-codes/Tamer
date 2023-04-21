package com.ihafer.jargx;

/**
 * A runtime exception thrown by HelpHandler when the user requests help.  It
 * is a subclass of ArgumentException to allow the calling code to catch a
 * single type of exception but distinguish between intentional and accidental
 * reasons to provide help to the user.
 */
public class HelpRequestedException extends ArgumentException {

    public HelpRequestedException() {
    }

    public HelpRequestedException(String message) {
        super(message);
    }

    public HelpRequestedException(String message, Throwable cause) {
        super(message, cause);
    }

    public HelpRequestedException(Throwable cause) {
        super(cause);
    }
}
