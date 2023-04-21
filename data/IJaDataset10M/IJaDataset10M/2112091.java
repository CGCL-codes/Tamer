package net.sf.jrelay.exception;

/**
 * Problems while inserting a record into a database table.
 */
public class JrInsertException extends JrException {

    /**
     *
     */
    public JrInsertException() {
        super();
    }

    /**
     * @param message
     */
    public JrInsertException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param throwable
     */
    public JrInsertException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * @param throwable
     */
    public JrInsertException(Throwable throwable) {
        super(throwable);
    }
}
