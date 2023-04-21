package org.ozoneDB;

import org.ozoneDB.core.Transaction;
import javax.naming.*;
import java.io.IOException;
import java.rmi.Remote;
import java.util.Hashtable;
import java.util.logging.Logger;

/**
 * ExternalTransaction allows an application to explicitly manage transaction
 * boundaries.<p>
 *
 * When programming ozone applications explicite transaction demarcation is
 * needed under rare circumstances only (for example: processing of binary large
 * objects - BLOBs). In fact, in most cases explicite transactions are not
 * really needed while implicite transactions are cleaner and faster. So, every
 * time you are going to use explicite transactions, you should ask yourself if
 * an implicite transaction is maybe a better choice.<p>
 *
 * In case of a deadlock the ordinary behaviour of ozone is to abort one of the
 * locked transactions and restart until all transactions are successfully
 * committed. This is not possible when explicite transactions are used! In case
 * of deadlock an exceptions is thrown and the client has to decide what to do.
 * <p>
 *
 * Note: If an operation that runs under control of this transaction fails, the
 * transaction is set to rollback only.
 *
 *
 * @author <a href="http://www.softwarebuero.de/">SMB</a>
 * @version $Revision: 1.7 $Date: 2004/12/28 16:01:29 $
 */
public final class ExternalTransaction extends AbstractTransaction implements Referenceable {

    /** Status of a transaction: transaction is not active. */
    public static final int STATUS_NONE = 1;

    /** Status of a transaction: transaction has been started. */
    public static final int STATUS_ACTIVE = 2;

    /** Status of a transaction: transaction is about to prepare. */
    public static final int STATUS_PREPARING = 3;

    /** Status of a transaction: transaction has been successfully prepared. */
    public static final int STATUS_PREPARED = 4;

    /** Status of a transaction: transaction is about to commit.*/
    public static final int STATUS_COMMITTING = 5;

    /** Status of a transaction: transaction has been successfully committed. */
    public static final int STATUS_COMMITTED = 6;

    /** Status of a transaction: transaction is about to abort. */
    public static final int STATUS_ROLLINGBACK = 7;

    /** Status of a transaction: transaction has been aborted. */
    public static final int STATUS_ROLLEDBACK = 8;

    protected boolean rollbackOnly = false;

    private static final Logger logger = Logger.getLogger(ExternalTransaction.class.getName());

    public ExternalTransaction(ExternalDatabase _database) {
        super(_database);
    }

    /**
     * Start work on behalf of this transaction and associate it with the current
     * thread.
     *
     *
     * @throws TransactionException If the thread is already associated with a
     * transaction.
     * @throws IOException If the server is not reachable.
     */
    public void begin() throws TransactionException, IOException {
        logger.finest("begin called");
        database.beginTX(this);
    }

    /**
     * Attach the caller's thread to this transaction and detach the thread
     * from any former Transaction the thread may have been associated with.
     */
    public void join() throws TransactionException, IOException {
        logger.finest("join called");
        if (database instanceof LocalDatabase) {
            throw new RuntimeException("Operation not supported: join() on LocalDatabase's.");
        } else {
            database.leaveTX(this);
            database.joinTX(this);
        }
    }

    /**
     * Detach the caller's thread from this <code>Transaction</code>, but do not attach
     * the thread to another <code>Transaction</code>.
     */
    public void leave() throws TransactionException, IOException {
        database.leaveTX(this);
    }

    /**
     * Prepares this transaction. This method is intended to be used by
     * transactional applications that need two-phase commit.
     */
    public void prepare() throws TransactionException, IOException {
        if (rollbackOnly) {
            database.rollbackTX(this);
            throw new TransactionException("Transaction was set to rollback only.", TransactionException.ROLLBACK);
        } else {
            database.prepareTX(this);
        }
    }

    /**
     * Complete this transaction. When this method completes, the thread
     * becomes associated with no transaction. This method can be called by a
     * non-joined thread.
     */
    public void commit() throws TransactionException, IOException {
        commit(true);
    }

    /**
     * Complete this transaction. When this method completes, the thread
     * becomes associated with no transaction. This method is intended to be
     * used by transactional applications that need two-phase commit.
     */
    public void commit(boolean onePhase) throws TransactionException, IOException {
        logger.finest("commit called");
        if (rollbackOnly) {
            database.rollbackTX(this);
            throw new TransactionException("Transaction was set to rollback only.", TransactionException.ROLLBACK);
        } else {
            database.commitTX(this, onePhase);
        }
    }

    /**
     * Checkpoint this transaction. This method can also be called by a
     * non-joined thread.
     */
    public void checkpoint() throws TransactionException, IOException {
        database.checkpointTX(this);
    }

    /**
     * Rollback the transaction associated with the current thread. When this
     * method completes, the thread becomes associated with no transaction.
     * Calling this method when the transaction is not opened doe not throw
     * an exception.
     * <p>
     * This method can be called by any threads.
     */
    public void rollback() throws TransactionException, IOException {
        logger.finest("rollback called");
        database.rollbackTX(this);
    }

    /**
     * Modify the transaction associated with the current thread such that the
     * only possible outcome of the transaction is to roll back the transaction.
     */
    public synchronized void setRollbackOnly() throws TransactionException, IOException {
        rollbackOnly = true;
    }

    /**
     * Obtain the status of the transaction associated with the current thread.
     */
    public int getStatus() throws TransactionException, IOException {
        int internal = database.getStatusTX(this);
        switch(internal) {
            case Transaction.STATUS_NONE:
                return STATUS_NONE;
            case Transaction.STATUS_STARTED:
                return STATUS_ACTIVE;
            case Transaction.STATUS_PREPARING:
                return STATUS_PREPARING;
            case Transaction.STATUS_PREPARED:
                return STATUS_PREPARED;
            case Transaction.STATUS_COMMITTING:
                return STATUS_COMMITTING;
            case Transaction.STATUS_COMMITTED:
                return STATUS_COMMITTED;
            case Transaction.STATUS_ABORTING:
                return STATUS_ROLLINGBACK;
            case Transaction.STATUS_ABORTED:
                return STATUS_ROLLEDBACK;
            default:
                throw new RuntimeException("Unknown internal transaction status: " + internal);
        }
    }

    /** this method is used for debugging purpose only */
    public static String explainStatus(int status) {
        switch(status) {
            case STATUS_NONE:
                return "STATUS_NONE - transaction is not active.";
            case STATUS_ACTIVE:
                return "STATUS_ACTIVE - transaction has been started. ";
            case STATUS_PREPARING:
                return "STATUS_PREPARING - transaction is about to prepare.";
            case STATUS_PREPARED:
                return "STATUS_PREPARED - transaction has been successfully prepared.";
            case STATUS_COMMITTING:
                return "STATUS_COMMITTING - transaction is about to commit.";
            case STATUS_COMMITTED:
                return "STATUS_COMMITTED - transaction has been successfully committed.";
            case STATUS_ROLLINGBACK:
                return "STATUS_ROLLINGBACK - transaction is about to abort.";
            case STATUS_ROLLEDBACK:
                return "STATUS_ROLLEDBACK - transaction has been aborted.";
            default:
                return "Unknown transaction status: " + status;
        }
    }

    /**
     * Modify the value of the timeout value that is associated with the
     * transactions started by the current thread with the begin method.
     *
     * If an application has not called this method, the transaction service
     * uses some default value for the transaction timeout.
     *
     *
     * @param seconds The value of the timeout in seconds. If the value is zero,
     * the transaction service restores the default value
     */
    public void setTransactionTimeout(int seconds) throws TransactionException, IOException {
        throw new RuntimeException("setTransactionTimeout() is not yet implemented.");
    }

    /**
     * Retrieves the JNDI Reference of this object.
     * @return The non-null Reference of this object.
     */
    public Reference getReference() throws NamingException {
        throw new RuntimeException("getReference() is not yet implemented.");
    }

    public Object getObjectInstance(Object refObj, Name name, Context nameCtx, Hashtable env) {
        if (refObj instanceof Reference) {
            return this;
        } else if (refObj instanceof Remote) {
            return refObj;
        } else {
            return null;
        }
    }

    public static ExternalTransaction getInstance() {
        throw new RuntimeException("ExternalTransaction.getInstance() not implemented yet.");
    }
}
