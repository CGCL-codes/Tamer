package kiff.util.transaction;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import kiff.exception.TransactionException;
import com.google.inject.Inject;

/**
 * Wraps a JTA UserTransaction.
 * @author Adam
 * @version $Id: JtaTransaction.java 62 2008-10-31 04:51:50Z a.ruggles $
 * 
 * Created on Oct 9, 2008 at 12:17:28 PM 
 */
public class JtaTransaction implements Transaction {

    /**
	 * Entity Manager, used to join the entity manager to the JTA Transaction.
	 */
    private EntityManager entityManager;

    /**
	 * JTA Transaction being wrapped.
	 */
    private final UserTransaction utx;

    /**
	 * Constructs a JTA Transaction with a UserTransaction.
	 * @param userTransaction The UserTransaction.
	 */
    @Inject
    public JtaTransaction(final UserTransaction userTransaction) {
        this.utx = userTransaction;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.util.transaction.Transaction#begin()
	 */
    public void begin() {
        try {
            utx.begin();
            if (entityManager != null) {
                entityManager.joinTransaction();
            }
        } catch (NotSupportedException e) {
            throw new TransactionException("JTA Begin NotSupportedException", e);
        } catch (SystemException e) {
            throw new TransactionException("JTA Begin SystemException", e);
        }
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.util.transaction.Transaction#commit()
	 */
    public void commit() {
        try {
            utx.commit();
        } catch (SecurityException e) {
            throw new TransactionException("JTA Commit SecurityException", e);
        } catch (IllegalStateException e) {
            throw new TransactionException("JTA Commit IllegalStateException", e);
        } catch (RollbackException e) {
            throw new TransactionException("JTA Commit RollbackException", e);
        } catch (HeuristicMixedException e) {
            throw new TransactionException("JTA Commit HeuristicMixedException", e);
        } catch (HeuristicRollbackException e) {
            throw new TransactionException("JTA Commit HeuristicRollbackException", e);
        } catch (SystemException e) {
            throw new TransactionException("JTA Commit SystemException", e);
        }
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.util.transaction.Transaction#end()
	 */
    public void end() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.util.transaction.Transaction#getRollbackOnly()
	 */
    public boolean getRollbackOnly() {
        try {
            int status = utx.getStatus();
            return (status == Status.STATUS_MARKED_ROLLBACK || status == Status.STATUS_ROLLEDBACK);
        } catch (SystemException e) {
            throw new TransactionException("JTA Status SystemException", e);
        }
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.util.transaction.Transaction#isActive()
	 */
    public boolean isActive() {
        try {
            int status = utx.getStatus();
            return (status == Status.STATUS_ACTIVE);
        } catch (SystemException e) {
            throw new TransactionException("JTA Status SystemException", e);
        }
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.util.transaction.Transaction#rollback()
	 */
    public void rollback() {
        try {
            utx.rollback();
        } catch (IllegalStateException e) {
            throw new TransactionException("JTA Rollback IllegalStateException", e);
        } catch (SecurityException e) {
            throw new TransactionException("JTA Rollback SecurityException", e);
        } catch (SystemException e) {
            throw new TransactionException("JTA Rollback SystemException", e);
        }
    }

    /**
	 * Sets entityManager.
	 * @param entityManager the entityManager to set.
	 */
    @Inject
    public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.util.transaction.Transaction#setRollbackOnly()
	 */
    public void setRollbackOnly() {
        try {
            utx.setRollbackOnly();
        } catch (IllegalStateException e) {
            throw new TransactionException("JTA Rollback IllegalStateException", e);
        } catch (SystemException e) {
            throw new TransactionException("JTA Rollback SystemException", e);
        }
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.util.transaction.Transaction#setTransactionTimeout(int)
	 */
    public void setTransactionTimeout(final int seconds) {
        try {
            utx.setTransactionTimeout(seconds);
        } catch (SystemException e) {
            throw new TransactionException("JTA Timeout SystemException", e);
        }
    }
}
