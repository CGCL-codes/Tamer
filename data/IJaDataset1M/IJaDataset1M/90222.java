package org.apache.commons.collections;

/**
 * The BufferOverflowException is used when the buffer's capacity has been
 * exceeded.
 *
 * @since Commons Collections 2.1
 * @version $Revision: 646777 $ $Date: 2008-04-10 13:33:15 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Avalon
 * @author <a href="mailto:bloritsch@apache.org">Berin Loritsch</a>
 * @author <a href="mailto:jefft@apache.org">Jeff Turner</a>
 * @author Paul Jack
 * @author Stephen Colebourne
 */
public class BufferOverflowException extends RuntimeException {

    /** The root cause throwable */
    private final Throwable throwable;

    /**
     * Constructs a new <code>BufferOverflowException</code>.
     */
    public BufferOverflowException() {
        super();
        throwable = null;
    }

    /** 
     * Construct a new <code>BufferOverflowException</code>.
     * 
     * @param message  the detail message for this exception
     */
    public BufferOverflowException(String message) {
        this(message, null);
    }

    /** 
     * Construct a new <code>BufferOverflowException</code>.
     * 
     * @param message  the detail message for this exception
     * @param exception  the root cause of the exception
     */
    public BufferOverflowException(String message, Throwable exception) {
        super(message);
        throwable = exception;
    }

    /**
     * Gets the root cause of the exception.
     *
     * @return the root cause
     */
    public final Throwable getCause() {
        return throwable;
    }
}
