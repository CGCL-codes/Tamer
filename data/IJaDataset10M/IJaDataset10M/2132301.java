package ru.unislabs.dbtier;

/**
 * Interface for data access operations with user managed transaction
 * @see DAO
 * @see DAOException
 * @author Michael Golovanov
 *
 */
public interface UserTransactionDAO extends DAO {

    /**
	 * Starting transaction.
	 * <br>Throw DAOException if transaction already started.
	 * @see DAOException
	 */
    public void beginTransaction();

    /**
	 * Check transaction started
	 * @return transaction started status
	 */
    public boolean isTransactionRunning();

    /**
	 * Commit started transaction.
	 * <br>Throw DAOException if transaction is not started.
	 * @see DAOException
	 */
    public void commit();

    /**
	 * Rollback started transaction.
	 * <br>Throw DAOException if transaction is not started.
	 * @see DAOException
	 */
    public void rollback();
}
