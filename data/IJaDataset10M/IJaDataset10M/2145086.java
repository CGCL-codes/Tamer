package org.xaware.server.engine.controller.transaction;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.xaware.server.engine.IChannelKey;
import org.xaware.server.engine.ITransactionContext;
import org.xaware.server.engine.ITransactionalChannel;
import org.xaware.server.engine.enums.TransactionPropagation;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This class maintains the state for a single transaction.
 * 
 * @author Tim Uttormark
 */
public class TransactionContext implements ITransactionContext {

    private static MasterTransactionManager transactionManager = new MasterTransactionManager();

    private static final XAwareLogger lf = XAwareLogger.getXAwareLogger(TransactionContext.class.getName());

    private static final String CLASS_NAME = "TransactionContext";

    private final TransactionStatus transactionStatus;

    private final String transactionName;

    private final boolean isActiveTransaction;

    /**
     * Creates a new TransactionContext instance. Made private to force use of
     * factory method startNewTransactionIfNeeded().
     * 
     * @param txStatus
     *            the TransactionStatus object returned when the transaction was
     *            created.
     * @param transactionName
     *            the name of the transaction. Typically this is the path to the
     *            element which originated the transaction.
     * @param isActiveTransaction
     *            indicates whether there an actual transaction exists during
     *            the scope of this TransactionContext.
     */
    private TransactionContext(TransactionStatus txStatus, String transactionName, boolean isActiveTransaction) {
        this.transactionStatus = txStatus;
        this.transactionName = transactionName;
        this.isActiveTransaction = isActiveTransaction;
    }

    /**
     * Factory method for creating new TransactionContext instances as needed.
     * If needed based on the transactionPropagationLevel, a new Transaction is
     * started and the existing transaction (if any) is suspended. If a new
     * transaction is not required, then the previousTxContext is returned.
     * 
     * @param transactionPropagationLevel
     *            the transaction propagation level, e.g., REQUIRED,
     *            REQUIRES_NEW, SUPPORTS, etc.
     * @param previousTxContext
     *            the inherited TransactionContext previously in use.
     * @param transactionName
     *            the transaction name to use when a new TransactionContext is
     *            created.
     * @return the TransactionContext to use next, which may be the
     *         previousTxContext or a newly created one.
     * @throws XAwareTransactionException
     *             if an unsupported propagation level is used.
     */
    public static ITransactionContext startNewTransactionIfNeeded(TransactionPropagation transactionPropagationLevel, ITransactionContext previousTxContext, String transactionName) throws XAwareTransactionException {
        DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
        txDef.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        txDef.setName(transactionName);
        if (previousTxContext == null) {
            int propagationLevel = (transactionPropagationLevel == null) ? TransactionDefinition.PROPAGATION_NOT_SUPPORTED : transactionPropagationLevel.ordinal();
            if (propagationLevel == TransactionDefinition.PROPAGATION_NESTED) {
                throw new XAwareTransactionException("Nested transactions are not supported.");
            }
            txDef.setPropagationBehavior(propagationLevel);
            TransactionStatus txStatus = transactionManager.getTransaction(txDef);
            if (txStatus.isNewTransaction()) {
                lf.fine("Started new transaction.", CLASS_NAME, "startNewTransactionIfNeeded");
            }
            return new TransactionContext(txStatus, transactionName, transactionManager.isActualTransactionActive());
        }
        if (transactionPropagationLevel == null) {
            return previousTxContext;
        }
        txDef.setPropagationBehavior(transactionPropagationLevel.ordinal());
        TransactionStatus txStatus = transactionManager.getTransaction(txDef);
        if (txStatus.isNewTransaction()) {
            lf.fine("Started new transaction.", CLASS_NAME, "startNewTransactionIfNeeded");
            return new TransactionContext(txStatus, transactionName, true);
        }
        if (!previousTxContext.isActiveTransaction()) {
            return previousTxContext;
        }
        if (transactionManager.isActualTransactionActive()) {
            return previousTxContext;
        }
        lf.fine("Suspended existing transaction.", CLASS_NAME, "startNewTransactionIfNeeded");
        return new TransactionContext(txStatus, transactionName, false);
    }

    /**
     * Gets the name of the transaction. Typically this is the path to the
     * element which originated the transaction.
     * 
     * @return the name of the transaction.
     */
    public final String getTransactionName() {
        return this.transactionName;
    }

    /**
     * Returns a boolean indicating whether this TransactionContext is tied to
     * an active transaction. False indicates that it is running outside of any
     * transaction.
     * 
     * @return a boolean indicating whether this TransactionContext is tied to
     *         an active transaction.
     */
    public boolean isActiveTransaction() {
        return this.isActiveTransaction;
    }

    /**
     * Gets the transaction scoped ITransactionalChannel instance which matches
     * the type and key provided.
     * 
     * @param key
     *            The key for the specific transactional resource instance
     *            desired.
     * 
     * @return the ITransactionalChannel instance in the transaction scoped pool
     *         which matches the type and key provided, or null if no matching
     *         ITransactionalChannel is cached yet.
     * @throws XAwareException
     *             if any error occurs finding the specified channel
     */
    public ITransactionalChannel getTransactionalChannel(IChannelKey key) throws XAwareException {
        return transactionManager.getTransactionalChannel(key);
    }

    /**
     * Adds the transactionalChannel instance provided into the transaction
     * scoped cache.
     * 
     * @param key
     *            The key for the specific transactional channel instance
     *            desired.
     * @param transactionalChannel
     *            the transactionalChannel instance to be stored in transaction
     *            scoped cache.
     * @throws XAwareException
     *             if any error occurs enlisting the specified channel in a
     *             transaction.
     */
    public void setTransactionalChannel(IChannelKey key, ITransactionalChannel transactionalChannel) throws XAwareException {
        transactionManager.setTransactionalChannel(key, transactionalChannel);
    }

    /**
     * Completes the transaction (if any) associated with this context, and
     * frees up any transaction-scoped resources. Resumes previously suspended
     * transaction, if any,
     * 
     * @param success
     *            a boolean indicating whether to commit the transaction. True
     *            indicates commit; false indicates rollback.
     * @param processingComplete
     *            a boolean indicating whether processing of the BizView session
     *            is complete.
     * @throws XAwareTransactionException
     *             if any error occurs completing the transaction
     */
    public void complete(boolean success, boolean processingComplete) throws XAwareTransactionException {
        try {
            if (isActiveTransaction) {
                if (success) {
                    lf.fine("Committing transaction.", CLASS_NAME, "complete");
                    transactionManager.commit(transactionStatus);
                } else {
                    lf.fine("Rolling back transaction.", CLASS_NAME, "complete");
                    transactionManager.rollback(transactionStatus);
                }
            } else {
                if (!processingComplete) {
                    lf.fine("Resuming suspended transaction.", CLASS_NAME, "complete");
                }
                transactionManager.commit(transactionStatus);
            }
        } catch (TransactionException e) {
            throw new XAwareTransactionException(e);
        }
    }

    /**
     * Accessor for the MasterTransactionManager instance.
     * Intended for unit testing purposes; should not be needed otherwise.
     *
     * @return the transactionManager
     */
    public static MasterTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * Returns a String representation of this object.
     * 
     * @return a String representation of this object.
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("TransactionContext isActiveTransaction: ").append(this.isActiveTransaction()).append("\n                   demarcated by element: ").append(this.transactionName);
        return buf.toString();
    }
}
