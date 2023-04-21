package org.ikasan.connector.base.command;

import org.hibernate.HibernateException;

/**
 * Exception representing a problem within the persitence layer for
 * TransactionalResourceCommands
 * 
 * @author Ikasan Development Team
 *
 */
public class TransactionalResourceCommandPersistenceException extends Exception {

    /**
     * Constructor
     * 
     * @param message
     * @param cause
     */
    public TransactionalResourceCommandPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor
     * @param e
     */
    public TransactionalResourceCommandPersistenceException(HibernateException e) {
        super(e);
    }

    /**
     * Constructor
     * @param message
     */
    public TransactionalResourceCommandPersistenceException(String message) {
        super(message);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
}
