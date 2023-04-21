package javax.util.jcache;

/**
 * An exception was caught or generated within the cache.
 */
public class CacheException extends Exception {

    /** the wrapped exception */
    private final Exception base;

    private String message;

    /**
	 * Creates new <code>CacheException</code> without detail message.
	 */
    public CacheException() {
        this.base = null;
    }

    @Override
    public String getLocalizedMessage() {
        return message == null ? super.getLocalizedMessage() : message;
    }

    @Override
    public String getMessage() {
        return message == null ? super.getMessage() : message;
    }

    /**
	 * Constructs an <code>CacheException</code> with the specified detail
	 * Exception and additional message.
	 * 
	 * @param message the detail message.
	 * @param cause the detail exception.
	 */
    public CacheException(final String message, final Exception cause) {
        super(message);
        this.base = cause;
    }

    /**
	 * Constructs an <code>CacheException</code> with the specified detail
	 * Exception.
	 * 
	 * @param cause the detail exception.
	 */
    public CacheException(final Exception cause) {
        super();
        this.base = cause;
    }

    /**
	 * Constructs an <code>CacheException</code> with the specified detail
	 * message.
	 * 
	 * @param msg the detail message.
	 */
    public CacheException(final String msg) {
        super(msg);
        this.base = null;
    }

    /**
	 * prints the stacktrace for this Exception, and if an wrapped exception is
	 * in place, that stacktrace is also included.
	 * 
	 * @see java.lang.Throwable#printStackTrace()
	 */
    @Override
    public final void printStackTrace() {
        super.printStackTrace();
        if (base != null) {
            System.err.println("CAUSED BY:");
            base.printStackTrace();
        }
    }

    /**
	 * Gets the wrapped exception
	 * 
	 * @return the wrapped exception
	 */
    public final Exception getBase() {
        return base;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
