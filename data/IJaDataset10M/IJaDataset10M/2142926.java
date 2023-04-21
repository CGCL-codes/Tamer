package org.t2framework.commons.transaction.util;

import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import org.t2framework.commons.transaction.Messages;
import org.t2framework.commons.transaction.exception.SystemRuntimeException;

/**
 * 
 * {@.en }
 * 
 * <br />
 * 
 * {@.ja }
 * 
 * @author shot
 * 
 */
public class TransactionManagerUtil {

    /**
	 * 
	 * {@.en }
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @param transactionManager
	 */
    public static void setRollbackOnly(final TransactionManager transactionManager) {
        assertTransactionManagerNotNull(transactionManager);
        try {
            transactionManager.setRollbackOnly();
        } catch (IllegalStateException e) {
            throw e;
        } catch (SystemException e) {
            throw new SystemRuntimeException(e);
        }
    }

    public static void assertTransactionManagerNotNull(TransactionManager transactionManager) {
        if (transactionManager == null) {
            throw new NullPointerException(Messages.ETransaction0021);
        }
    }

    /**
	 * 
	 * {@.en }
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @param transactionManager
	 * @return
	 */
    public static int getStatus(TransactionManager transactionManager) {
        assertTransactionManagerNotNull(transactionManager);
        try {
            return transactionManager.getStatus();
        } catch (SystemException e) {
            throw new SystemRuntimeException(e);
        }
    }

    /**
	 * 
	 * {@.en }
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @param <T>
	 * @param transactionManager
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public static <T extends Transaction> T getTransaction(final TransactionManager transactionManager) {
        assertTransactionManagerNotNull(transactionManager);
        try {
            return (T) transactionManager.getTransaction();
        } catch (SystemException e) {
            throw new SystemRuntimeException(e);
        }
    }
}
