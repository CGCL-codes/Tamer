package org.flightreservationsserver.database;

import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;

/**
 * @author Facundo Manuel Quiroga
 * Feb 2, 2009
 * 
 */
public class TransactionManager {

    static ThreadLocal<Transaction> localTransaction = new ThreadLocal<Transaction>() {

        @Override
        protected Transaction initialValue() {
            return null;
        }
    };

    /**
	 * @return a new transaction
	 */
    protected static Transaction createNewTransaction() {
        assert (DAO.getDatabase() != null);
        Transaction transaction = DAO.getDatabase().newTransaction();
        assert (transaction != null);
        return transaction;
    }

    public static void startTransaction() throws ErrorInServerException {
        if (getTransaction() != null) {
            throw new ErrorInServerException("There should not be a previous transaction");
        }
        localTransaction.set(createNewTransaction());
    }

    public static void commitTransaction() throws TransactionAbortedException {
        try {
            getTransaction().commit();
        } finally {
            clearTransaction();
        }
    }

    public static void abortTransaction() {
        try {
            try {
                getTransaction().abort();
            } catch (TransactionAbortedException e) {
            }
        } finally {
            clearTransaction();
        }
    }

    public static void clearTransaction() {
        localTransaction.set(null);
    }

    /**
	 * @return the transaction that was started for the current thread.
	 */
    public static Transaction getTransaction() {
        return localTransaction.get();
    }
}
