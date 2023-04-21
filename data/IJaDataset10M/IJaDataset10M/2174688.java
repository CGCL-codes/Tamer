package org.arch4j.dataaccess;

import java.sql.SQLException;
import org.arch4j.core.BaseApplicationException;

/**
 * <p> This class is a wrapper for <code>SQLException</code>s.  It adds
 * an additional constructor that takes a <code>SQLException</code>, generates
 * an informative message string, and holds onto the original exception.
 *
 * <p> The method that generates the informative message is available for
 * any caller with a <code>SQLException</code>, via the static
 * {@link #getErrorString(SQLException)} method.
 *
 * @author Ross E. Greinke
 * @version 1.0
 */
public class DataAccessException extends BaseApplicationException {

    /**
   * Constructs a <code>DataAccessException</code> with no specified
   * detail message or nested exception.
   */
    public DataAccessException() {
        super();
    }

    /**
   * Constructs a <code>DataAccessException</code> with the specified
   * detail message.
   * @param message The detailed message.
   */
    public DataAccessException(String message) {
        super(message);
    }

    /**
   * Constructs a <code>DataAccessException</code> with the specified
   * nested exception.
   * @param exception The nested exception.
   */
    public DataAccessException(Throwable exception) {
        super(exception);
    }

    /**
   * Constructs a <code>DataAccessException</code> with the specified
   * nested SQL exception.  An informative error string is automatically
   * constructed.
   * @param exception The nested SQL exception.
   * @see   #getErrorString(SQLException)
   */
    public DataAccessException(SQLException exception) {
        this(getErrorString(exception), exception);
    }

    /**
   * Constructs a <code>DataAccessException</code> with the specified
   * detail message and nested exception.
   * @param message The detailed message.
   * @param exception The nested exception.
   */
    public DataAccessException(String message, Throwable exception) {
        super(message, exception);
    }

    /**
   * Creates an informative error string based on the given <code>SQLException</code>,
   * following the chain of additional exceptions (if any).
   * @param ex The original SQLException.
   * @return A <code>String</code> containing the exception's error information.
   */
    public static String getErrorString(SQLException ex) {
        StringBuffer buf = new StringBuffer();
        while (ex != null) {
            buf.append("\nSQL Exception: ");
            buf.append(ex.getMessage());
            buf.append("\nANSI-92 SQL State: ");
            buf.append(ex.getSQLState());
            buf.append("\nVendor Error Code: ");
            buf.append(ex.getErrorCode());
            ex = ex.getNextException();
        }
        return buf.toString();
    }
}
