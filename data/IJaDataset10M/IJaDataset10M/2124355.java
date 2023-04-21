package org.mobicents.slee.runtime.transaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.transaction.CommitListener;
import javax.slee.transaction.RollbackListener;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.transaction.SleeTransaction;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionContext;
import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple;

/**
 * Implementation of SLEE Tx manager.
 * 
 * @author Tim Fox - Complete re-write.
 * @author M. Ranganathan
 * @author J. Deruelle
 * @author Ralf Siedow
 * @author Ivelin Ivanov
 * @author Eduardo Martins version 2
 * 
 */
public class SleeTransactionManagerImpl extends AbstractSleeContainerModule implements SleeTransactionManager {

    private static final Logger logger = Logger.getLogger(SleeTransactionManagerImpl.class);

    /**
	 * the underlying JTA tx manager
	 */
    private final TransactionManager transactionManager;

    /**
	 * an executor service for async operations invoked on {@link SleeTransaction}
	 */
    private ExecutorService executorService;

    private static final boolean doTraceLogs = logger.isTraceEnabled();

    /**
	 * 
	 * @param transactionManager
	 */
    public SleeTransactionManagerImpl(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
	 * 
	 * @return
	 */
    ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newCachedThreadPool();
        }
        return executorService;
    }

    public TransactionManager getRealTransactionManager() {
        return transactionManager;
    }

    public boolean getRollbackOnly() throws SystemException {
        return getStatus() == Status.STATUS_MARKED_ROLLBACK;
    }

    public void mandateTransaction() throws TransactionRequiredLocalException {
        try {
            final Transaction tx = getTransaction();
            if (tx == null) throw new TransactionRequiredLocalException("Transaction Mandatory");
            final int status = tx.getStatus();
            if (status != Status.STATUS_ACTIVE && status != Status.STATUS_MARKED_ROLLBACK) {
                throw new IllegalStateException("There is no active tx, tx is in state: " + status);
            }
        } catch (SystemException e) {
            throw new SLEEException(e.getMessage(), e);
        }
    }

    public boolean requireTransaction() {
        try {
            final Transaction tx = getTransaction();
            if (tx == null) {
                begin();
                return true;
            } else {
                final int status = tx.getStatus();
                if (status != Status.STATUS_ACTIVE && status != Status.STATUS_MARKED_ROLLBACK) {
                    begin();
                    return true;
                }
            }
        } catch (NotSupportedException e) {
            logger.error("Exception creating transaction", e);
        } catch (SystemException e) {
            logger.error("Caught SystemException in checking transaction", e);
        }
        return false;
    }

    public void requireTransactionEnd(boolean terminateTx, boolean doRollback) throws IllegalStateException, SecurityException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        if (terminateTx) {
            if (doRollback) {
                rollback();
            } else {
                commit();
            }
        } else {
            if (doRollback) {
                setRollbackOnly();
            }
        }
    }

    public void asyncCommit(CommitListener commitListener) throws IllegalStateException, SecurityException {
        if (doTraceLogs) {
            logger.trace("asyncCommit( commitListener = " + commitListener + " )");
        }
        try {
            final SleeTransaction sleeTransaction = getSleeTransaction();
            if (sleeTransaction == null) {
                throw new IllegalStateException("no transaction");
            } else {
                sleeTransaction.asyncCommit(commitListener);
            }
        } catch (SystemException e) {
            if (commitListener != null) {
                commitListener.systemException(e);
            }
        }
    }

    public void asyncRollback(RollbackListener rollbackListener) throws IllegalStateException, SecurityException {
        if (doTraceLogs) {
            logger.trace("asyncRollback( rollbackListener = " + rollbackListener + " )");
        }
        try {
            final SleeTransaction sleeTransaction = getSleeTransaction();
            if (sleeTransaction == null) {
                throw new IllegalStateException("no transaction");
            } else {
                sleeTransaction.asyncRollback(rollbackListener);
            }
        } catch (SystemException e) {
            if (rollbackListener != null) {
                rollbackListener.systemException(e);
            }
        }
    }

    public void begin() throws NotSupportedException, SystemException {
        transactionManager.begin();
        getAsSleeTransaction(transactionManager.getTransaction(), true);
    }

    public SleeTransaction beginSleeTransaction() throws NotSupportedException, SystemException {
        transactionManager.begin();
        return getAsSleeTransaction(transactionManager.getTransaction(), true);
    }

    public SleeTransaction getTransaction() throws SystemException {
        return getAsSleeTransaction(transactionManager.getTransaction(), false);
    }

    public SleeTransaction getSleeTransaction() throws SystemException {
        return getAsSleeTransaction(transactionManager.getTransaction(), false);
    }

    private SleeTransaction getAsSleeTransaction(Transaction transaction, boolean transactionCreation) throws SystemException {
        if (transaction != null) {
            TransactionContext transactionContext = null;
            if (transactionCreation) {
                transactionContext = bindToTransaction(transaction);
                if (logger.isDebugEnabled()) {
                    logger.debug("Started tx " + transaction);
                }
            } else {
                transactionContext = TransactionContextThreadLocal.getTransactionContext();
                if (transactionContext == null && transaction.getStatus() == Status.STATUS_ACTIVE) {
                    transactionContext = bindToTransaction(transaction);
                }
            }
            return new SleeTransactionImpl((TransactionImple) transaction, transactionContext, this);
        } else {
            return null;
        }
    }

    private TransactionContext bindToTransaction(Transaction tx) throws IllegalStateException, SystemException {
        final TransactionContextImpl txContext = new TransactionContextImpl();
        try {
            tx.registerSynchronization(new SleeTransactionSynchronization(tx, txContext));
        } catch (RollbackException e) {
            throw new IllegalStateException("Unable to register listener for created transaction. Error: " + e.getMessage());
        }
        TransactionContextThreadLocal.setTransactionContext(txContext);
        return txContext;
    }

    public SleeTransaction asSleeTransaction(Transaction transaction) throws NullPointerException, IllegalArgumentException, SystemException {
        if (transaction == null) {
            throw new NullPointerException("null transaction");
        }
        if (transaction.getClass() == SleeTransactionImpl.class) {
            return (SleeTransaction) transaction;
        }
        if (transaction instanceof TransactionImple) {
            return new SleeTransactionImpl((TransactionImple) transaction, getTransactionContext(), this);
        }
        throw new IllegalArgumentException("unexpected transaction class type " + transaction.getClass());
    }

    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        if (doTraceLogs) {
            logger.trace("Starting commit of tx " + transactionManager.getTransaction());
        }
        transactionManager.commit();
    }

    public int getStatus() throws SystemException {
        return transactionManager.getStatus();
    }

    public void resume(Transaction transaction) throws InvalidTransactionException, IllegalStateException, SystemException {
        if (transaction.getClass() == SleeTransactionImpl.class) {
            final SleeTransactionImpl sleeTransactionImpl = (SleeTransactionImpl) transaction;
            if (doTraceLogs) {
                logger.trace("Resuming tx " + sleeTransactionImpl.getWrappedTransaction());
            }
            transactionManager.resume(sleeTransactionImpl.getWrappedTransaction());
            TransactionContextThreadLocal.setTransactionContext(sleeTransactionImpl.getTransactionContext());
        } else {
            throw new InvalidTransactionException();
        }
    }

    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        if (doTraceLogs) {
            logger.trace("Starting rollback of tx " + transactionManager.getTransaction());
        }
        transactionManager.rollback();
    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("Marking tx " + transactionManager.getTransaction() + " for rollback.");
        }
        transactionManager.setRollbackOnly();
    }

    public void setTransactionTimeout(int seconds) throws SystemException {
        transactionManager.setTransactionTimeout(seconds);
    }

    public Transaction suspend() throws SystemException {
        if (doTraceLogs) {
            logger.trace("Suspending tx " + transactionManager.getTransaction());
        }
        final Transaction tx = getAsSleeTransaction(transactionManager.suspend(), false);
        if (tx != null) {
            TransactionContextThreadLocal.setTransactionContext(null);
            return tx;
        } else {
            return null;
        }
    }

    public TransactionContext getTransactionContext() {
        TransactionContext txContext = TransactionContextThreadLocal.getTransactionContext();
        if (txContext == null) {
            try {
                final Transaction tx = transactionManager.getTransaction();
                if (tx != null && tx.getStatus() == Status.STATUS_ACTIVE) {
                    txContext = bindToTransaction(tx);
                }
            } catch (Throwable e) {
                throw new SLEEException(e.getMessage(), e);
            }
        }
        return txContext;
    }
}
