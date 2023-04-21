package br.gov.component.demoiselle.hibernate;

/**
 * Exception class used by hibernate component.
 * 
 * @author CETEC/CTJEE
 * @see RuntimeException
 */
public class PersistenceHibernateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor.
	 */
    public PersistenceHibernateException() {
        super();
    }

    /**
     * Constructor with message.
     * 
     * @param message string message
     */
    public PersistenceHibernateException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause.
     * 
     * @param message string message
     * @param cause exception cause
     */
    public PersistenceHibernateException(String message, Throwable cause) {
        super(message, cause);
    }
}
