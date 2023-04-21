package com.subshell.persistence.runner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.subshell.persistence.database.DatabaseFactory;
import com.subshell.persistence.exception.ErrorInRunnableException;
import com.subshell.persistence.exception.PersistenceException;
import com.subshell.persistence.transaction.Transaction;
import com.subshell.persistence.transaction.TransactionContext;

/**
 * Runs a {@link Runnable} and provides it with a transaction context. This class will take care
 * of setting up/destroying the transaction context. Additionally, it will commit/roll back the
 * {@link Transaction} as necessary.
 *
 * <p><strong>Note: The transaction must not be committed/rolled back by the Runnable.</strong></p>
 *
 * <p><strong>Instances of this class are thread-safe, as must be instances of subclasses of
 * this class.</strong></p>
 *
 * @author Maik Schreiber (schreiber@subshell.com)
 */
public class SimpleTransactionContextRunner implements TransactionContextRunner {

    private static final Log log = LogFactory.getLog(SimpleTransactionContextRunner.class);

    /**
	 * <p>Runs the specified Runnable and provides it with a transaction context. This method
	 * takes care of setting up/destroying the transaction context. It will also
	 * commit/roll back the transaction as necessary.</p>
	 * 
	 * <p><strong>Note: The transaction must not be committed/rolled back by the
	 * Runnable.</strong></p>
	 * 
	 * @throws ErrorInRunnableException when the Runnable itself has thrown an exception
	 * @throws PersistenceException when there is a persistence-related error
	 */
    public final void run(Runnable runnable) throws ErrorInRunnableException, PersistenceException {
        if (runnable == null) {
            throw new IllegalArgumentException("no runnable specified");
        }
        Transaction tx = null;
        boolean transactionContextSet = false;
        try {
            log.debug("opening transaction");
            tx = openTransaction();
            if (tx == null) {
                log.error("no transaction returned");
                throw new PersistenceException("no transaction returned");
            }
            log.debug("setting up transaction context");
            TransactionContext context = new TransactionContext(tx);
            TransactionContext.setCurrentContext(context);
            transactionContextSet = true;
            log.debug("executing runnable");
            try {
                runnable.run();
            } catch (RuntimeException e) {
                throw new ErrorInRunnableException("error while executing runnable", e);
            }
            log.debug("committing transaction");
            tx.commit();
        } finally {
            if (transactionContextSet) {
                log.debug("destroying transaction context");
                TransactionContext.clearCurrentContext();
            }
            if ((tx != null) && !tx.isCommitted()) {
                log.debug("rolling back transaction");
                tx.rollback();
            }
        }
    }

    /**
	 * <p>Opens and returns a new transaction.</p>
	 * 
	 * <p>This method obtains a database from the database factory and uses it to open the
	 * transaction. Subclasses may override this method to provide an alternative
	 * implementation.</p>
	 * 
	 * @throws PersistenceException when there is a persistence-related error
	 */
    protected Transaction openTransaction() throws PersistenceException {
        return DatabaseFactory.getDatabase().openTransaction();
    }
}
