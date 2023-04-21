package com.armatiek.infofuze.result;

/**
 * An interface that must be implemented by all Result classes that support
 * transactions.
 * 
 * @author Maarten Kroon
 */
public interface TransactionableIf {

    /**
   * Commits the transaction.
   */
    public void commitTransaction();

    /**
   * Tests whether the transaction is already committed or rolled back.
   */
    public boolean isCommitted();

    /**
   * Rolls back the transaction.
   */
    public void rollbackTransaction();

    /**
   * Starts the transaction.
   */
    public void startTransaction();
}
