package net.sourceforge.pergamon.parser;

/**
 * Thrown when dealing with a content type not mapped.
 */
public class ContentTypeNotMappedException extends Exception {

    private static final long serialVersionUID = -4939180266989096154L;

    /**
	 * @see Exception#Exception()
	 */
    public ContentTypeNotMappedException() {
    }

    /**
	 * @see Exception#Exception(String)
	 */
    public ContentTypeNotMappedException(final String message) {
        super(message);
    }

    /**
	 * @see Exception#Exception(Throwable)
	 */
    public ContentTypeNotMappedException(final Throwable cause) {
        super(cause);
    }

    /**
	 * @see Exception#Exception(String, Throwable)
	 */
    public ContentTypeNotMappedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
