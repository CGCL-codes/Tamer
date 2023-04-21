package org.springframework.transaction.support;

/**
 * Simple, generic implementation of the TransactionStatus interface.
 * Derives from AbstractTransactionStatus and adds an explicit "newTransaction" flag.
 *
 * <p>This class is not used by any of Spring's pre-built PlatformTransactionManager
 * implementations. It is mainly provided as a start for custom transaction manager
 * implementations and as a static mock for testing transactional code (either
 * as part of a mock PlatformTransactionManager or as argument passed into a
 * TransactionCallback to be tested).
 *
 * @author Juergen Hoeller
 * @since 1.2.3
 * @see #SimpleTransactionStatus(boolean)
 * @see TransactionCallback
 */
public class SimpleTransactionStatus extends AbstractTransactionStatus {

    private final boolean newTransaction;

    /**
	 * Create a new SimpleTransactionStatus, indicating a new transaction.
	 */
    public SimpleTransactionStatus() {
        this.newTransaction = true;
    }

    /**
	 * Create a new SimpleTransactionStatus.
	 * @param newTransaction whether to indicate a new transaction.
	 */
    public SimpleTransactionStatus(boolean newTransaction) {
        this.newTransaction = newTransaction;
    }

    public boolean isNewTransaction() {
        return newTransaction;
    }
}
