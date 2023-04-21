package net.sf.jrssserver;

/**
 * @author Gary S. Weaver
 */
public class JRssServerException extends Exception {

    public JRssServerException() {
        super();
    }

    public JRssServerException(String message) {
        super(message);
    }

    public JRssServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public JRssServerException(Throwable cause) {
        super(cause);
    }
}
