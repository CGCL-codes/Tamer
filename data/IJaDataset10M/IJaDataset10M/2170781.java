package de.searchworkorange.lib.databasecache;

/**
 *
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class NoDBCacheAvailableException extends Exception {

    /**
     * Creates a new instance of <code>NoDBCacheAvailableException</code> without detail message.
     */
    public NoDBCacheAvailableException() {
    }

    /**
     * Constructs an instance of <code>NoDBCacheAvailableException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NoDBCacheAvailableException(String msg) {
        super(msg);
    }
}
