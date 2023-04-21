package pl.edu.agh.iosr.ftpserverremote.dataaccess;

/**
 * This is a <code>DaoException</code> class created for DAO purposes.
 * 
 * @author Tomasz Sadura
 *
 */
public class DaoException extends RuntimeException {

    private static final long serialVersionUID = -5862997179811886659L;

    /**
   * Creates a new <code>DaoException</code>.
   * @param cause cause of the exception 
   */
    public DaoException(final Throwable cause) {
        super(cause);
    }

    /**
   * Creates a new <code>DaoException</code>.
   */
    public DaoException() {
        super();
    }

    /**
   * Creates a new <code>DaoException</code>.
   * @param message message of the exception
   */
    public DaoException(final String message) {
        super(message);
    }

    /**
   * Creates a new <code>DaoException</code>.
   * @param message message of the exception
   * @param cause cause of the exception
   */
    public DaoException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
