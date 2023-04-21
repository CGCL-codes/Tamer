package com.luzan.db.dao;

import com.luzan.db.DatabaseException;

/**
 * An exception that provides information on a database access
 * error or other errors.
 *
 * @author Alexander Kozlov
 */
public class DAOException extends DatabaseException {

    public DAOException() {
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }
}
