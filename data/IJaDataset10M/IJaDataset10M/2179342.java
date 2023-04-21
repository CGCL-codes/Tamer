package uk.org.beton.ftpsync.model;

/**
 * Holds a message with an indication of how severe it is.
 *
 * @author Rick Beton
 * @version $Id: Message.java 790 2007-04-09 16:09:30Z rick $
 */
public abstract class Message {

    public static final int FATAL = 0;

    public static final int ERROR = 1;

    public static final int INFO = 2;

    public static final int VERBOSE = 3;

    public static final int DEBUG = 4;

    private final String text;

    private final int level;

    private final Exception exception;

    protected Message(int level, String text, Exception exception) {
        this.text = text;
        this.level = level;
        this.exception = exception;
    }

    /**
     * Gets the level property.
     *
     * @return the level
     */
    public final int getLevel() {
        return level;
    }

    /**
     * Gets the text property.
     *
     * @return the text
     */
    public final String getText() {
        return text;
    }

    /**
     * Gets the exception.
     *
     * @return the exception, usually null
     */
    public Exception getException() {
        return exception;
    }

    @Override
    public String toString() {
        return text;
    }
}
