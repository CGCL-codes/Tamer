package com.explosion.expfmodules.rdbmsconn.dbom.sql;

import com.explosion.utilities.exception.EnhancedException;

/**
 * @author Stephen Cowx
 * Created on 28-Oct-2004
 */
public class InvalidUpdateException extends EnhancedException {

    /**
     * 
     */
    public InvalidUpdateException() {
        super();
    }

    /**
     * @param message
     * @param originalException
     */
    public InvalidUpdateException(String message, Exception originalException) {
        super(message, originalException);
    }

    /**
     * @param message
     */
    public InvalidUpdateException(String message) {
        super(message);
    }
}
