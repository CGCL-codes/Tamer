package net.siuying.any2rss.core;

/**
 * DOCUMENT ME!
 * 
 * @author Francis Chong
 * @version $Revision: 1.3 $
 */
public class ContentProcessorException extends Exception {

    private static final long serialVersionUID = -4261502118663539234L;

    /**
     * Creates a new ContentProcessorException object.
     */
    public ContentProcessorException() {
        super();
    }

    /**
     * Creates a new ContentProcessorException object.
     * 
     * @param msg DOCUMENT ME!
     */
    public ContentProcessorException(String msg) {
        super(msg);
    }

    /**
     * Creates a new ContentProcessorException object.
     * 
     * @param msg DOCUMENT ME!
     * @param exception DOCUMENT ME!
     */
    public ContentProcessorException(String msg, Throwable exception) {
        super(msg, exception);
    }

    /**
     * Creates a new ContentProcessorException object.
     * 
     * @param exception DOCUMENT ME!
     */
    public ContentProcessorException(Throwable exception) {
        super(exception);
    }
}
