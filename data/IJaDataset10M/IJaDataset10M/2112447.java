package com.volantis.shared.recovery;

/**
 * This interface provides a common set of methods for those Classes which must
 * be able to:
 * <ul>
 * <li> Capture their state at a known instant.
 * <li> Restore the captured state under error conditions.
 * </ul>
 *
 * Typically implementors will be expected to handle multiply nested
 * recoverable transactions, that is to say they should be able to capture
 * multiple states.  In such cases, under an error condition, the state
 * captured at the start of the currently uncompleted transaction should be
 * restored.
 *
 * Where a nested transaction has been completed implementors should consider
 * that all actions within that transaction are part of the parent transaction.
 */
public interface RecoverableTransaction {

    /**
     * This method will typically be implemented to...
     * <ul>
     * <li> Record the current state of the Object by storing a clone.
     * <li> Initialise any data structures required to record changes to the
     *      state.
     * <li> Mark the Object as being in transaction mode.
     * </ul>
     */
    void startTransaction();

    /**
     * This method will typically be implemented such that...
     * <ul>
     * <li> For an unnested transaction it will release any resources the
     *      object was asked to discard during the transaction.
     * <li> For a nested transaction it will add the actions for the
     *      transaction to the parent transaction.
     * </ul>
     */
    void commitTransaction();

    /**
     * This method will typically be implemented to...
     * <ul>
     * <li> Release an resources added by actions during the transaction.
     * <li> Restore the Object to the state recorded at the start of the
     *      transaction
     * </ul>
     */
    void rollbackTransaction();
}
