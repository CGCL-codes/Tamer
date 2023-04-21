package com.volantis.shared.recovery;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.pipeline.localization.LocalizationFactory;
import java.util.Stack;

/**
 * This abstract class provides common functionality to those classes that
 * implement RecoverableTransaction.
 */
public abstract class AbstractRecoverableTransaction implements RecoverableTransaction, Cloneable {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(AbstractRecoverableTransaction.class);

    /**
     * This stack is used to store clones of AbstractRecoverableTransaction
     * as a means of maintaining nestable snapshots of the Object state.
     */
    protected Stack clones;

    /**
     * This flag allows us to determine whether the we are currently processing
     * a recoverable transaction.
     */
    protected boolean inTransaction = false;

    /**
     * Create a new instance of AbstractRecoverableTransaction
     */
    public AbstractRecoverableTransaction() {
        clones = new Stack();
    }

    protected Object clone() throws CloneNotSupportedException {
        AbstractRecoverableTransaction clone = null;
        try {
            clone = (AbstractRecoverableTransaction) getClass().newInstance();
        } catch (Exception e) {
            throw new CloneNotSupportedException(e.getMessage());
        }
        clone.inTransaction = inTransaction;
        clone.clones = (Stack) clones.clone();
        return clone;
    }

    public final void startTransaction() {
        Object clone = null;
        try {
            clone = clone();
        } catch (CloneNotSupportedException e) {
            logger.error("recoverable-transaction-clone-failure", new Object[] { getClass().getName(), AbstractRecoverableTransaction.class.getName() });
            final String errorMsg = "Could not clone " + getClass().getName() + " because: " + e.getMessage() + ".  Specialisations of " + AbstractRecoverableTransaction.class.getName() + " must be cloneable.  Cannot store state required " + "to facilitate a recoverable transaction.";
            throw new IllegalStateException(errorMsg);
        }
        clones.push(clone);
        inTransaction = true;
        startTransactionImpl();
    }

    /**
     * Implementation method for {@link #startTransaction}.  Typically this
     * will be implemented to perform and additional processing required to
     * initialize the state to begin a recoverable transaction.
     */
    protected abstract void startTransactionImpl();

    public final void commitTransaction() {
        if (!inTransaction) {
            logger.error("transaction-required-to-perform-commit");
            throw new IllegalStateException("Must be in a transaction to commit it.");
        }
        AbstractRecoverableTransaction clone = (AbstractRecoverableTransaction) clones.pop();
        commitTransactionImpl(clone);
    }

    /**
     * Implementation method for {@link #commitTransaction}.  Typically this
     * will be implemented to perform any additional processing required to
     * complete the transaction and update the Object state.
     *
     * @param poppedState This AbstractRecoverableTransaction contains the
     * state stored at the last call to {@link #startTransaction}
     */
    protected abstract void commitTransactionImpl(AbstractRecoverableTransaction poppedState);

    public final void rollbackTransaction() {
        if (!inTransaction) {
            logger.error("transaction-required-to-perform-rollback");
            throw new IllegalStateException("Must be in a transaction to rollback.");
        }
        AbstractRecoverableTransaction clone = (AbstractRecoverableTransaction) clones.pop();
        rollbackTransactionImpl(clone);
        inTransaction = clone.inTransaction;
        clones = clone.clones;
    }

    /**
     * Implementation method for {@link #rollbackTransaction}.  Typically this
     * will be implemented to perform any additional processing required to
     * revert Object back to the state recorded at the last call to the
     * method {@link #startTransaction}
     *
     * @param poppedState This AbstractRecoverableTransaction contains the
     * state stored at the last call to {@link #startTransaction}
     */
    protected abstract void rollbackTransactionImpl(AbstractRecoverableTransaction poppedState);

    public boolean inTransaction() {
        return inTransaction;
    }
}
