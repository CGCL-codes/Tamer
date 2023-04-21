package org.t2framework.commons.transaction;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import org.t2framework.commons.transaction.exception.TransactionalIllegalStateException;

/**
 * The implementation of JTA {@link TransactionManager}.
 * 
 * @author shot
 * 
 */
public class TransactionManagerImpl implements TransactionManager {

    protected final ThreadLocal<TransactionInfo> threadTx = new ThreadLocal<TransactionInfo>() {

        @Override
        protected TransactionInfo initialValue() {
            return new TransactionInfo();
        }
    };

    protected long timeout = 1000 * 5;

    public TransactionManagerImpl() {
    }

    /**
	 * Create and associate a transaction to this thread.
	 */
    @Override
    public void begin() throws NotSupportedException, SystemException {
        final TransactionInfo info = getTransactionInfo();
        if (info.transaction != null) {
            throw new NotSupportedException();
        }
        try {
            final Transaction tx = createTransaction();
            associate(tx);
        } catch (Throwable t) {
            throw createSystemException(t);
        }
    }

    /**
	 * Commit associated transaction, then disassociate.
	 */
    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        final TransactionInfo info = getTransactionInfo();
        final Transaction transaction = info.transaction;
        assertTransactionExist(transaction);
        try {
            transaction.commit();
        } finally {
            disassociate(info);
        }
    }

    protected void disassociate(TransactionInfo info) {
        synchronized (info) {
            info.transaction = null;
        }
    }

    @Override
    public int getStatus() throws SystemException {
        try {
            final TransactionInfo info = getTransactionInfo();
            final Transaction transaction = info.transaction;
            if (transaction == null) {
                return Status.STATUS_NO_TRANSACTION;
            } else {
                return transaction.getStatus();
            }
        } catch (Throwable t) {
            throw createSystemException(t);
        }
    }

    @Override
    public Transaction getTransaction() throws SystemException {
        try {
            final TransactionInfo info = getTransactionInfo();
            final Transaction transaction = info.transaction;
            if (transaction == null || transaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
                return null;
            }
            return transaction;
        } catch (Throwable t) {
            throw createSystemException(t);
        }
    }

    @Override
    public void resume(Transaction transaction) throws InvalidTransactionException, IllegalStateException, SystemException {
        final TransactionInfo info = getTransactionInfo();
        try {
            if (info.resumable) {
                ((Resumable) transaction).resume(transaction);
            }
        } finally {
            disassociate(info);
        }
    }

    @Override
    public Transaction suspend() throws SystemException {
        final TransactionInfo info = getTransactionInfo();
        final Transaction transaction = info.transaction;
        if (transaction == null) {
            return null;
        }
        try {
            if (info.suspendable) {
                ((Suspendable) transaction).suspend();
            }
        } finally {
            disassociate(info);
        }
        return transaction;
    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        final TransactionInfo info = getTransactionInfo();
        final Transaction transaction = info.transaction;
        assertTransactionExist(transaction);
        try {
            transaction.rollback();
        } finally {
            disassociate(info);
        }
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        final TransactionInfo info = getTransactionInfo();
        final Transaction transaction = info.transaction;
        assertTransactionExist(transaction);
        if (transaction == null) {
            throw new TransactionalIllegalStateException("ECMN0508");
        }
        transaction.setRollbackOnly();
    }

    @Override
    public void setTransactionTimeout(int seconds) throws SystemException {
        getTransactionInfo().timeout = seconds * 1000;
    }

    protected void associate(Transaction transaction) {
        synchronized (transaction) {
            TransactionInfo info = getTransactionInfo();
            info.transaction = transaction;
            info.suspendable = (transaction instanceof Suspendable);
            info.resumable = (transaction instanceof Resumable);
            threadTx.set(info);
        }
    }

    protected void assertTransactionExist(final Transaction transaction) {
        if (transaction == null) {
            throw new TransactionalIllegalStateException("ETransaction0016");
        }
    }

    protected TransactionInfo getTransactionInfo() {
        TransactionInfo info = threadTx.get();
        info.timeout = timeout;
        return info;
    }

    private static class TransactionInfo {

        @SuppressWarnings("unused")
        long timeout;

        Transaction transaction;

        boolean suspendable = false;

        boolean resumable = false;
    }

    protected Transaction createTransaction() {
        return new TransactionImpl();
    }

    protected SystemException createSystemException(Throwable t) {
        return new SystemException(t.getMessage());
    }
}
