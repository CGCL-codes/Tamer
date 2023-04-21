package net.sf.grobid.common.exception;

/**
 * @author <a href="mailto:dmitry.katsubo@gmail.com">Dmitry Katsubo</a>
 */
public class GrobidResourceException extends RuntimeException {

    public GrobidResourceException() {
    }

    public GrobidResourceException(String message) {
        super(message);
    }

    public GrobidResourceException(Throwable cause) {
        super(cause);
    }

    public GrobidResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
