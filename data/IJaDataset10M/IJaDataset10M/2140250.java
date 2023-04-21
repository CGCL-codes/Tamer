package org.mobicents.timers;

/**
 * 
 * A thread local used to store the a tx context.
 * 
 * @author martins
 * 
 */
public class TransactionContextThreadLocal {

    /**
	 * 
	 */
    private static final ThreadLocal<TransactionContext> transactionContext = new ThreadLocal<TransactionContext>();

    /**
	 * 
	 * @param txContext
	 */
    public static void setTransactionContext(TransactionContext txContext) {
        transactionContext.set(txContext);
    }

    /**
	 * 
	 * @return
	 */
    public static TransactionContext getTransactionContext() {
        return transactionContext.get();
    }
}
