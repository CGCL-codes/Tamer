package org.springframework.dao;

/**
 * Exception thrown on a pessimistic locking violation.
 * Thrown by Spring's SQLException translation mechanism
 * if a corresponding database error is encountered.
 *
 * <p>Serves as superclass for more specific exceptions, like
 * CannotAcquireLockException and DeadlockLoserDataAccessException.
 *
 * @author Thomas Risberg
 * @since 1.2
 * @see CannotAcquireLockException
 * @see DeadlockLoserDataAccessException
 * @see OptimisticLockingFailureException
 */
public class PessimisticLockingFailureException extends ConcurrencyFailureException {

    /**
	 * Constructor for PessimisticLockingFailureException.
	 * @param msg message
	 */
    public PessimisticLockingFailureException(String msg) {
        super(msg);
    }

    /**
	 * Constructor for PessimisticLockingFailureException.
	 * @param msg message
	 * @param ex root cause from data access API in use
	 */
    public PessimisticLockingFailureException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
