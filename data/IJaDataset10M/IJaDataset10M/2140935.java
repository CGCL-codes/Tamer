package oracle.toplink.essentials.internal.ejb.cmp3.transaction.base;

import oracle.toplink.essentials.transaction.AbstractTransactionController;
import oracle.toplink.essentials.exceptions.TransactionException;
import oracle.toplink.essentials.internal.ejb.cmp3.base.*;
import oracle.toplink.essentials.internal.sessions.UnitOfWorkImpl;

/**
 * INTERNAL:
 * Base JTATransactionWrapper
 * The JTATransactionWrapper is used to make in transparent to an EntityManager
 * what kind of transaction is being used.  Transaction type can either be JTATransaction
 * or EntityTransaciton and they are mutually exclusive.  This is the implementation for JTA
 * Transaction
 * 
 * @see oracle.toplink.essentials.internal.ejb.cmp3.transaction.JTATransactionWrapper
 * @see oracle.toplink.essentials.internal.ejb.cmp3.transaction.jdk14.JTATransactionWrapper
 */
public class JTATransactionWrapper extends TransactionWrapperImpl {

    protected AbstractTransactionController txnController;

    public JTATransactionWrapper(EntityManagerImpl entityManager) {
        super(entityManager);
        this.txnController = (AbstractTransactionController) entityManager.getServerSession().getExternalTransactionController();
    }

    /**
     * INTERNAL:
     * This method will be used to check for a transaction and throws exception if none exists.
     * If this methiod returns without exception then a transaction exists.
     * This method must be called before accessing the localUOW.
     */
    public Object checkForTransaction(boolean validateExistence) {
        Object transaction = this.txnController.getTransaction();
        if (validateExistence && (transaction == null)) {
            throwCheckTransactionFailedException();
        }
        return transaction;
    }

    /**
     * INTERNAL:
     * Internal clear the underlying data structures that this transaction owns.
     */
    public void clear() {
        if (txnKey != null && this.entityManager.shouldPropagatePersistenceContext()) {
            this.txnController.getUnitsOfWork().remove(txnKey);
        }
        localUOW.release();
        localUOW = null;
    }

    /**
    * INTERNAL:
    * Mark the current transaction so that the only possible
    * outcome of the transaction is for the transaction to be
    * rolled back.
    * This is an internal method and if the txn is not active will do nothing
    */
    public void setRollbackOnlyInternal() {
        if (txnController.getTransaction() != null) {
            txnController.markTransactionForRollback();
        }
    }

    /**
     * INTERNAL:
     * THis method is used to get the active UnitOfWork.  It is special in that it will
     * return the required RepeatableWriteUnitOfWork required by the EntityManager.  Once 
     * RepeatableWrite is merged into existing UnitOfWork this code can go away.
     */
    public RepeatableWriteUnitOfWork getTransactionalUnitOfWork(Object transaction) {
        if (transaction == null) {
            return null;
        }
        if (this.entityManager.shouldPropagatePersistenceContext()) {
            Object newTxnKey = this.txnController.getTransactionKey(transaction);
            if (this.txnKey == newTxnKey) {
                return (RepeatableWriteUnitOfWork) this.localUOW;
            }
            this.txnKey = newTxnKey;
            this.localUOW = (RepeatableWriteUnitOfWork) this.txnController.lookupActiveUnitOfWork(transaction);
            if (this.localUOW == null) {
                this.localUOW = new RepeatableWriteUnitOfWork(entityManager.getServerSession().acquireClientSession());
                this.localUOW.registerWithTransactionIfRequired();
                this.localUOW.setShouldCascadeCloneToJoinedRelationship(true);
                this.txnController.getUnitsOfWork().put(newTxnKey, this.localUOW);
            }
        } else if (this.localUOW == null) {
            this.localUOW = new RepeatableWriteUnitOfWork(entityManager.getServerSession().acquireClientSession());
            this.localUOW.registerWithTransactionIfRequired();
            this.localUOW.setShouldCascadeCloneToJoinedRelationship(true);
        }
        return (RepeatableWriteUnitOfWork) this.localUOW;
    }

    protected void throwUserTransactionException() {
        throw TransactionException.entityTransactionWithJTANotAllowed();
    }

    protected void throwCheckTransactionFailedException() {
        throw TransactionException.externalTransactionNotActive();
    }

    public void registerUnitOfWorkWithTxn(UnitOfWorkImpl uow) {
        uow.registerWithTransactionIfRequired();
    }

    /**
     * We should only flush the entity manager before the query if the query is
     * joined to a transaction
     */
    public boolean shouldFlushBeforeQuery(UnitOfWorkImpl uow) {
        return uow.isSynchronized();
    }
}
