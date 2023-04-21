package com.jcorporate.expresso.core.db;

import com.jcorporate.expresso.core.misc.StringUtil;
import org.apache.log4j.Logger;

/**
 * Generic database transaction object - hides the implementation
 * details of using jdbc and allows for JDBC message and exceptions to be
 * handled better than by default.
 * DBTransaction are also designed to be used in conjunction with connection
 * pooling, and have special methods to support this.
 *
 * @author Yves Henri AMAIZO
 */
public class DBTransaction {

    /**
     * Constant name for the 'Default' database context
     */
    public static final String DEFAULT_DB_CONTEXT_NAME = "default";

    /**
     * Every connection handled by the pool gets an ID number assigned
     */
    private int transactionId = 0;

    /**
     * last operation
     */
    private int lastCommited = 0;

    /**
     * Description of this connection
     */
    private String myDescription = "no description";

    /**
     * The current data context
     */
    private String dbName = DEFAULT_DB_CONTEXT_NAME;

    /**
     * Is this connection object actually connected to the database yet?
     */
    private boolean isConnected = false;

    /**
     * DB connection pool object
     */
    private DBConnectionPool myPool;

    /**
     * DB connection object
     */
    private DBConnection myConnection;

    /**
     * constant name to help with debugging
     */
    private static final String THIS_CLASS = DBTransaction.class.getName() + ".";

    /**
     * Time that the DBConnection was created.
     */
    private long createdTime = System.currentTimeMillis();

    /**
     * The main Transaction Log
     */
    private static Logger transLog = Logger.getLogger("com.jcorporate.expresso.core.db.TRANSACTION");

    /**
     * The SQL Log  Set this category to debug
     * to see a trace of all sql statements
     * issued against the main database
     */
    private static Logger log = Logger.getLogger(DBTransaction.class);

    /**
     * Constructor
     * Create a new connection object
     *
     * @param pool The identified transaction pool
     *             connection
     * @throws DBException If the information provided cannot be used to
     *                     create a new connection
     */
    public DBTransaction() throws DBException {
        transactionSetup(DEFAULT_DB_CONTEXT_NAME);
    }

    /**
     * Constructor
     * Create a new connection object
     *
     * @param pool The identified transaction pool
     *             connection
     * @throws DBException If the information provided cannot be used to
     *                     create a new connection
     */
    public DBTransaction(DBConnectionPool pool) throws DBException {
        myPool = pool;
        dbName = myPool.getDataContext();
    }

    /**
     * Constructor
     * Create a new connection object
     *
     * @param transactionDataContext The transaction data context
     *                               connection
     * @throws DBException If the information provided cannot be used to
     *                     create a new connection
     */
    public DBTransaction(String transactionDataContext) throws DBException {
        transactionSetup(transactionDataContext);
    }

    /**
     * Sets up the transaction operation on an identitied DBConnectionPool
     *
     * @param transactionDataContext The transaction data context name
     *                               for more information on what this paramter means.
     */
    private void transactionSetup(String transactionDataContext) throws DBException {
        String myName = (THIS_CLASS + "transactionSetup()");
        try {
            myPool = DBConnectionPool.getInstance(transactionDataContext);
            dbName = transactionDataContext;
        } catch (DBException e) {
            myPool = null;
            log.error(myName + "DBException : initialize pool for transaction failed!", e);
        }
    }

    /**
     * Sets up the transaction operation on an identitied DBConnectionPool
     *
     * @param transactionDataContext The transaction data context name
     *                               for more information on what this paramter means.
     */
    public void startTransaction() throws DBException {
        startTransaction(true);
    }

    /**
     * Sets up the transaction operation on an identitied DBConnectionPool
     *
     * @param transactionDataContext The transaction data context name
     *                               for more information on what this paramter means.
     */
    public void startTransaction(boolean immortal) throws DBException {
        String myName = (THIS_CLASS + "startTransaction()");
        try {
            if (!isConnected) {
                if (myPool != null) {
                    myConnection = myPool.getConnection(getDataContext());
                    myConnection.setImmortal(immortal);
                    myConnection.setAutoCommit(false);
                    isConnected = true;
                    setId(myConnection.getId());
                    lastCommited = 0;
                } else {
                    return;
                }
            }
            lastCommited++;
            if (transLog.isDebugEnabled()) {
                transLog.debug(myName + " Transaction " + myConnection.getId() + " Starting:'" + "' on db '" + getDataContext() + "'");
            }
        } catch (DBException e) {
            log.error(myName + "DBException : initialize transaction failed!", e);
            if (myConnection != null) {
                close();
            }
        }
    }

    /**
     * Clear all result sets and statements associated with this connection
     */
    public void clear() throws DBException {
        if (myConnection != null) {
            rollback();
        }
    }

    /**
     * Send a COMMIT to the database, closing the current transaction  If the
     * database driver claims it doesn't support transactions, then we skip
     * this.
     *
     * @throws DBException If the commit does not succeed
     */
    public void commit() throws DBException {
        String myName = (THIS_CLASS + "commit()");
        try {
            if (!myConnection.isClosed()) {
                myConnection.commit();
                if (log.isDebugEnabled()) {
                    log.debug(myName + " Transaction " + myConnection.getId() + " Commited:'" + "' on db '" + getDataContext() + "'");
                }
                close();
            }
        } catch (DBException se) {
            close();
            throw new DBException(myName + ":Could not commit  (" + myDescription + ")", se.getMessage());
        }
    }

    /**
     * Clear all result sets and statements associated with this connection
     */
    private void close() throws DBException {
        String myName = (THIS_CLASS + "close()");
        myConnection.setImmortal(false);
        if (log.isDebugEnabled()) {
            log.debug(myName + " Transaction " + myConnection.getId() + " Closed:'" + "' on db '" + getDataContext() + "'");
        }
        myPool.release(myConnection);
        myConnection = null;
        myPool = null;
        setId(0);
        dbName = "";
        isConnected = false;
        lastCommited = -1;
    }

    /**
     * <p/>
     * Low level function that allows you to retrieve the JDBC connection associated
     * with this DBConnection.  This allows you to do several fancy things that would
     * not normally happen within Expresso itself such as prepared statements, and
     * other low level entities.  </p>
     * <p/>
     * <b>NOTE:</b>  Once you grab the connection yourself, you are on your own
     * as far as the framework is concerned.  It doesn't help you to get your
     * connection back in place.  So, for example, if you close the connection manually,
     * it will mess up the connection pool.  Or if you fail to free a prepared statement,
     * it will register as a resource leak.  Be sure to restore your connection to
     * pristine order before releasing this DBConnection back into the pool.
     * </p>
     *
     * @return a java.sql.Connection JDBC connection.
     */
    public DBConnection getTransactionConnection() {
        if (myConnection == null) {
            return null;
        }
        return myConnection;
    }

    /**
     * Returns the text description of this connection. When the connection
     * is put in use by a client program a description is set, this method
     * retrieves the description.
     *
     * @return String A text description of this database connection's purpose
     */
    public String getDescription() {
        return myDescription;
    }

    /**
     * When this connection has lost its connection to the server,
     * tell whether or not it is available to be re-allocated
     *
     * @return boolean True if the connection has been lost,
     *         false if it is still usable
     */
    public boolean isClosed() throws DBException {
        String myName = (THIS_CLASS + "isClosed()");
        try {
            return myConnection.isClosed();
        } catch (DBException se) {
            throw new DBException(myName + "isClosed()" + ":Cannot access connection to " + "database via (driver or JNDI) '" + myConnection.getDBDriver() + "' and URL '" + myConnection.getDBURL() + "' (" + myDescription + ")", se.getMessage());
        }
    }

    /**
     * When this connection has lost its connection to the server,
     * tell whether or not it is available to be re-allocated
     *
     * @return boolean True if the connection has been lost,
     *         false if it is still usable
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Roll back the current transaction, as if it were never requested.  If
     * the JDBC driver claims it doesn't support transactions, then this
     * method is a NOOP except for touching the connection
     *
     * @throws DBException If the rollback encounters an error
     */
    public void rollback() throws DBException {
        String myName = (THIS_CLASS + "rollback()");
        try {
            if (!myConnection.isClosed()) {
                myConnection.rollback();
                if (log.isDebugEnabled()) {
                    log.debug(myName + " Transaction " + myConnection.getId() + " Rollback:'" + "' on db '" + getDataContext() + "'");
                }
                close();
            }
        } catch (DBException se) {
            close();
            throw new DBException(myName + " Transaction" + ":Could not rollback" + " (" + myDescription + ")", se.getMessage());
        }
    }

    /**
     * Set a description for this database connection. When status is requested
     * from the connection pool, this is used to describe the connection.
     * Any client requesting a connection from the pool should set the
     * description of the connection as soon as it's returned
     *
     * @param newDescription Description of this connection
     */
    public void setDescription(String newDescription) {
        if (newDescription != null) {
            myDescription = newDescription;
        }
    }

    /**
     * Sets the data context for the connection.
     *
     * @param newDBName the datacontext name to use
     */
    public void setDataContext(String newDBName) {
        if (StringUtil.notNull(newDBName).length() == 0) {
            dbName = DEFAULT_DB_CONTEXT_NAME;
        } else {
            dbName = newDBName;
        }
    }

    /**
     * Retrieve the data context name we're associated with
     *
     * @return java.lang.String of the current data Context
     */
    public String getDataContext() {
        return dbName;
    }

    /**
     * <p>Releases the DBConnection back into the parent DBConnectionPool.  Allows
     * for syntax like:</p>
     * <code>
     * <p/>
     * DBConnection connection = DBConnectionPool.getInstance("default").getConnection(); <br/>
     * [do stuff]<br />
     * connection.release(); <br />
     * </code>
     * </p>
     */
    public void release() throws DBException {
        String myName = (THIS_CLASS + "release()");
        if (myConnection != null) {
            myConnection.setImmortal(false);
            if (log.isDebugEnabled()) {
                log.debug(myName + " Transaction " + myConnection.getId() + " Released:'" + "' on db '" + getDataContext() + "'");
            }
            myPool.release(myConnection);
            myConnection = null;
            myPool = null;
        } else {
            log.warn(myName + "No parent connection pool defined for this DBConnection");
        }
    }

    /**
     * @return
     */
    public DBConnectionPool getPool() {
        return myPool;
    }

    /**
     * @param pool
     */
    public void setPool(DBConnectionPool pool) {
        myPool = pool;
    }

    /**
     * Set EXPRESSO transaction mode for the current DBConnection.
     * <p/>
     * author Yves Henri AMAIZO, Mon Dec 22 10:30:59 2003
     *
     * @since Expresso 5.3
     */
    public void setTransactionReadOnlyMode() throws DBException {
        String myName = (THIS_CLASS + "setTransactionReadOnlyMode()");
        try {
            myConnection.setTransactionCommittedMode();
        } catch (DBException se) {
            throw new DBException(myName + "Transaction " + myDescription + "." + myConnection.getId() + " Transaction Committed Mode :'" + "' on db '" + getDataContext() + "'" + se);
        }
    }

    /**
     * Set EXPRESSO transaction mode for the current DBConnection.
     * <p/>
     * author Yves Henri AMAIZO, Mon Dec 22 10:30:59 2003
     *
     * @since Expresso 5.3
     */
    public void setTransactionDirtyMode() throws DBException {
        String myName = (THIS_CLASS + "setTransactionDirtyMode()");
        try {
            myConnection.setTransactionUncommittedMode();
        } catch (DBException se) {
            throw new DBException(myName + "Transaction " + myDescription + "." + myConnection.getId() + " Transaction Uncommitted Mode :'" + "' on db '" + getDataContext() + "'" + se);
        }
    }

    /**
     * Set EXPRESSO transaction mode for the current DBConnection.
     * <p/>
     * author Yves Henri AMAIZO, Mon Dec 22 10:30:59 2003
     *
     * @since Expresso 5.3
     */
    public void setTransactionRestrictiveMode() throws DBException {
        String myName = (THIS_CLASS + "setTransactionExclusiveMode()");
        try {
            myConnection.setTransactionRepeatableMode();
        } catch (DBException se) {
            throw new DBException(myName + "Transaction " + myDescription + "." + myConnection.getId() + " Transaction Repeatable Mode :'" + "' on db '" + getDataContext() + "'" + se);
        }
    }

    /**
     * Set EXPRESSO transaction mode for the current DBConnection.
     * <p/>
     * author Yves Henri AMAIZO, Mon Dec 22 10:30:59 2003
     *
     * @since Expresso 5.3
     */
    public void setTransactionExclusiveMode() throws DBException {
        String myName = (THIS_CLASS + "setTransactionExclusiveMode()");
        try {
            myConnection.setTransactionSerializableMode();
        } catch (DBException se) {
            throw new DBException(myName + " Transaction " + myDescription + "." + myConnection.getId() + " Transaction Serialisable Mode :'" + "' on db '" + getDataContext() + "'" + se);
        }
    }

    /**
     * Get transaction id
     *
     * @return int
     *         <p/>
     *         author Yves Henri AMAIZO, Mon Dec 22 10:30:59 2003
     * @since Expresso 5.3
     */
    public int getId() {
        return transactionId;
    }

    /**
     * Set transaction id
     *
     * @param newTransactionID
     */
    private void setId(int newTransactionID) {
        transactionId = newTransactionID;
    }

    /**
     * Get transaction identifier which is myDescription + transactionId
     *
     * @return String
     *         <p/>
     *         author Yves Henri AMAIZO, Mon Dec 22 10:30:59 2003
     * @since Expresso 5.3
     */
    public String getTransactionIdentifier() {
        return myDescription + "." + Integer.toString(transactionId);
    }
}
