package oracle.toplink.essentials.internal.ejb.cmp3.transaction.base;

import oracle.toplink.essentials.exceptions.TransactionException;
import oracle.toplink.essentials.internal.ejb.cmp3.base.*;
import oracle.toplink.essentials.internal.sessions.UnitOfWorkImpl;

public abstract class TransactionWrapperImpl {

    protected EntityManagerImpl entityManager = null;

    protected RepeatableWriteUnitOfWork localUOW;

    protected Object txnKey;

    public TransactionWrapperImpl(EntityManagerImpl entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * INTERNAL:
     * This method will be used to check for a transaction and throws exception if none exists.
     * If this methiod returns without exception then a transaction exists.
     * This method must be called before accessing the localUOW.
     */
    public abstract Object checkForTransaction(boolean validateExistence);

    /**
     * INTERNAL:
     * Clears the transactional UnitOfWork
     */
    public void clear() {
        if (this.localUOW != null) {
            this.localUOW.clear(true);
        }
    }

    /**
     * INTERNAL:
     * THis method is used to get the active UnitOfWork.  It is special in that it will
     * return the required RepeatableWriteUnitOfWork required by the EntityManager.  Once
     * RepeatableWrite is merged into existing UnitOfWork this code can go away.
     * @param transaction
     * @return
     */
    public abstract RepeatableWriteUnitOfWork getTransactionalUnitOfWork(Object transaction);

    public abstract void registerUnitOfWorkWithTxn(UnitOfWorkImpl uow);

    public UnitOfWorkImpl getLocalUnitOfWork() {
        return localUOW;
    }

    public void setLocalUnitOfWork(RepeatableWriteUnitOfWork uow) {
        this.localUOW = uow;
    }

    /**
    * Mark the current transaction so that the only possible
    * outcome of the transaction is for the transaction to be
    * rolled back.
    * This is an internal method and if the txn is not active will do nothing
    */
    public abstract void setRollbackOnlyInternal();

    /**
     * This method will be called when a query is executed.  If changes in the entity manager
     * should be flushed this method should return true
     */
    public abstract boolean shouldFlushBeforeQuery(UnitOfWorkImpl uow);
}
