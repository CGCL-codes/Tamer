package org.datanucleus.store.rdbms;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.PropertyNames;
import org.datanucleus.Transaction;
import org.datanucleus.UserTransaction;
import org.datanucleus.exceptions.ClassNotResolvedException;
import org.datanucleus.exceptions.ConnectionFactoryNotFoundException;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.exceptions.UnsupportedConnectionFactoryException;
import org.datanucleus.store.StoreManager;
import org.datanucleus.store.connection.AbstractConnectionFactory;
import org.datanucleus.store.connection.AbstractManagedConnection;
import org.datanucleus.store.connection.ConnectionFactory;
import org.datanucleus.store.connection.ManagedConnection;
import org.datanucleus.store.rdbms.adapter.RDBMSAdapter;
import org.datanucleus.store.rdbms.datasource.DataNucleusDataSourceFactory;
import org.datanucleus.transaction.TransactionUtils;
import org.datanucleus.util.JavaUtils;
import org.datanucleus.util.Localiser;
import org.datanucleus.util.NucleusLogger;
import org.datanucleus.util.StringUtils;

/**
 * ConnectionFactory for RDBMS datastores.
 * Each instance is a factory of Transactional connection or NonTransactional connection.
 */
public class ConnectionFactoryImpl extends AbstractConnectionFactory {

    /** Localiser for messages. */
    protected static final Localiser LOCALISER_RDBMS = Localiser.getInstance("org.datanucleus.store.rdbms.Localisation", RDBMSStoreManager.class.getClassLoader());

    /** datasources. */
    Object[] dataSource;

    String poolingType = null;

    /**
     * Constructor.
     * @param storeMgr The context
     * @param resourceType either tx or nontx
     */
    public ConnectionFactoryImpl(StoreManager storeMgr, String resourceType) {
        super(storeMgr, resourceType);
        if (resourceType.equals("tx")) {
            String configuredResourceTypeProperty = storeMgr.getStringProperty(DATANUCLEUS_CONNECTION_RESOURCE_TYPE);
            if (configuredResourceTypeProperty != null) {
                options.put(ConnectionFactory.RESOURCE_TYPE_OPTION, configuredResourceTypeProperty);
            }
            String requiredPoolingType = storeMgr.getStringProperty(PropertyNames.PROPERTY_CONNECTION_POOLINGTYPE);
            Object connDS = storeMgr.getConnectionFactory();
            String connJNDI = storeMgr.getConnectionFactoryName();
            String connURL = storeMgr.getConnectionURL();
            initialiseDataSources(connDS, connJNDI, resourceType, requiredPoolingType, connURL);
            if (dataSource == null) {
                throw new NucleusUserException(LOCALISER_RDBMS.msg("047009", "transactional")).setFatal();
            }
        } else {
            String configuredResourceTypeProperty = storeMgr.getStringProperty(DATANUCLEUS_CONNECTION2_RESOURCE_TYPE);
            if (configuredResourceTypeProperty != null) {
                options.put(ConnectionFactory.RESOURCE_TYPE_OPTION, configuredResourceTypeProperty);
            }
            String requiredPoolingType = storeMgr.getStringProperty(PropertyNames.PROPERTY_CONNECTION_POOLINGTYPE2);
            if (requiredPoolingType == null) {
                requiredPoolingType = storeMgr.getStringProperty(PropertyNames.PROPERTY_CONNECTION_POOLINGTYPE);
            }
            Object connDS = storeMgr.getConnectionFactory2();
            String connJNDI = storeMgr.getConnectionFactory2Name();
            String connURL = storeMgr.getConnectionURL();
            initialiseDataSources(connDS, connJNDI, resourceType, requiredPoolingType, connURL);
            if (dataSource == null) {
                connDS = storeMgr.getConnectionFactory();
                connJNDI = storeMgr.getConnectionFactoryName();
                initialiseDataSources(connDS, connJNDI, resourceType, requiredPoolingType, connURL);
            }
            if (dataSource == null) {
                throw new NucleusUserException(LOCALISER_RDBMS.msg("047009", "non-transactional")).setFatal();
            }
        }
    }

    /**
     * Method to initialise the datasource(s) used by this connection factory.
     * Searches initially for a provided DataSource, then if not found, for JNDI DataSource(s), and finally
     * for the DataSource at a connection URL.
     * @param connDS Factory data source object
     * @param connJNDI DataSource JNDI name(s)
     * @param connURL URL for connections
     */
    private void initialiseDataSources(Object connDS, String connJNDI, String resourceType, String requiredPoolingType, String connURL) {
        if (connDS != null) {
            if (!(connDS instanceof DataSource) && !(connDS instanceof XADataSource)) {
                throw new UnsupportedConnectionFactoryException(connDS);
            }
            dataSource = new DataSource[1];
            dataSource[0] = connDS;
        } else if (connJNDI != null) {
            String[] connectionFactoryNames = StringUtils.split(connJNDI, ",");
            dataSource = new DataSource[connectionFactoryNames.length];
            for (int i = 0; i < connectionFactoryNames.length; i++) {
                dataSource[i] = lookupDataSource(connectionFactoryNames[i]);
            }
        } else if (connURL != null) {
            dataSource = new DataSource[1];
            String poolingType = calculatePoolingType(requiredPoolingType);
            try {
                DataNucleusDataSourceFactory dataSourceFactory = (DataNucleusDataSourceFactory) storeMgr.getNucleusContext().getPluginManager().createExecutableExtension("org.datanucleus.store.rdbms.datasource", "name", poolingType, "class-name", null, null);
                if (dataSourceFactory == null) {
                    throw new NucleusUserException(LOCALISER_RDBMS.msg("047003", poolingType)).setFatal();
                }
                dataSource[0] = dataSourceFactory.makePooledDataSource(storeMgr);
                if (NucleusLogger.CONNECTION.isDebugEnabled()) {
                    NucleusLogger.CONNECTION.debug(LOCALISER_RDBMS.msg("047008", resourceType, poolingType));
                }
            } catch (ClassNotFoundException cnfe) {
                throw new NucleusUserException(LOCALISER_RDBMS.msg("047003", poolingType), cnfe).setFatal();
            } catch (Exception e) {
                if (e instanceof InvocationTargetException) {
                    InvocationTargetException ite = (InvocationTargetException) e;
                    throw new NucleusException(LOCALISER_RDBMS.msg("047004", poolingType, ite.getTargetException().getMessage()), ite.getTargetException()).setFatal();
                } else {
                    throw new NucleusException(LOCALISER_RDBMS.msg("047004", poolingType, e.getMessage()), e).setFatal();
                }
            }
        }
    }

    /**
     * Method to create a new ManagedConnection.
     * @param poolKey the object that is bound the connection during its lifecycle (if any)
     * @param transactionOptions Transaction options
     * @return The ManagedConnection
     */
    public ManagedConnection createManagedConnection(Object poolKey, Map transactionOptions) {
        ManagedConnectionImpl mconn = new ManagedConnectionImpl(transactionOptions);
        if (resourceType.equalsIgnoreCase("nontx")) {
            boolean releaseAfterUse = storeMgr.getBooleanProperty(PropertyNames.PROPERTY_CONNECTION_NONTX_RELEASE_AFTER_USE);
            if (!releaseAfterUse) {
                mconn.setPooled();
            }
        }
        return mconn;
    }

    class ManagedConnectionImpl extends AbstractManagedConnection {

        int isolation;

        boolean needsCommitting = false;

        ConnectionProvider connProvider = null;

        ManagedConnectionImpl(Map transactionOptions) {
            if (transactionOptions != null && transactionOptions.get(Transaction.TRANSACTION_ISOLATION_OPTION) != null) {
                isolation = ((Number) transactionOptions.get(Transaction.TRANSACTION_ISOLATION_OPTION)).intValue();
            } else {
                isolation = TransactionUtils.getTransactionIsolationLevelForName(storeMgr.getStringProperty(PropertyNames.PROPERTY_TRANSACTION_ISOLATION));
            }
            try {
                connProvider = (ConnectionProvider) storeMgr.getNucleusContext().getPluginManager().createExecutableExtension("org.datanucleus.store.rdbms.connectionprovider", "name", storeMgr.getStringProperty("datanucleus.rdbms.connectionProviderName"), "class-name", null, null);
                if (connProvider == null) {
                    throw new NucleusException(LOCALISER_RDBMS.msg("050000", storeMgr.getStringProperty("datanucleus.rdbms.connectionProviderName"))).setFatal();
                }
                connProvider.setFailOnError(storeMgr.getBooleanProperty("datanucleus.rdbms.connectionProviderFailOnError"));
            } catch (Exception e) {
                throw new NucleusException(LOCALISER_RDBMS.msg("050001", storeMgr.getStringProperty("datanucleus.rdbms.connectionProviderName"), e.getMessage()), e).setFatal();
            }
        }

        /**
         * Release this connection.
         * Releasing this connection will allow this managed connection to be used one or more times
         * inside the same transaction. If this managed connection is managed by a transaction manager,
         * release is a no-op, otherwise the physical connection is closed
         */
        @Override
        public void release() {
            if (commitOnRelease) {
                try {
                    Connection conn = getSqlConnection();
                    if (conn != null && !conn.isClosed() && !conn.getAutoCommit()) {
                        ((RDBMSStoreManager) storeMgr).getSQLController().processConnectionStatement(this);
                        this.needsCommitting = false;
                        conn.commit();
                        if (NucleusLogger.CONNECTION.isDebugEnabled()) {
                            NucleusLogger.CONNECTION.debug(LOCALISER_RDBMS.msg("052005", conn.toString()));
                        }
                    }
                } catch (SQLException sqle) {
                    throw new NucleusDataStoreException(sqle.getMessage(), sqle);
                }
            }
            super.release();
        }

        /**
         * Obtain an XAResource which can be enlisted in a transaction
         */
        @Override
        public XAResource getXAResource() {
            if (getConnection() instanceof Connection) {
                return new EmulatedXAResource((Connection) getConnection());
            } else {
                try {
                    return ((XAConnection) getConnection()).getXAResource();
                } catch (SQLException e) {
                    throw new NucleusDataStoreException(e.getMessage(), e);
                }
            }
        }

        /**
         * Create a connection to the resource
         */
        public Object getConnection() {
            if (this.conn == null) {
                Connection cnx = null;
                try {
                    RDBMSStoreManager rdbmsMgr = (RDBMSStoreManager) storeMgr;
                    boolean readOnly = storeMgr.getBooleanProperty(PropertyNames.PROPERTY_DATASTORE_READONLY);
                    if (rdbmsMgr.getDatastoreAdapter() != null) {
                        RDBMSAdapter rdba = (RDBMSAdapter) rdbmsMgr.getDatastoreAdapter();
                        int reqdIsolationLevel = isolation;
                        if (rdba.getRequiredTransactionIsolationLevel() >= 0) {
                            reqdIsolationLevel = rdba.getRequiredTransactionIsolationLevel();
                        }
                        cnx = connProvider.getConnection((DataSource[]) dataSource);
                        boolean succeeded = false;
                        try {
                            if (cnx.isReadOnly() != readOnly) {
                                NucleusLogger.CONNECTION.debug("Setting readonly=" + readOnly + " to connection: " + cnx.toString());
                                cnx.setReadOnly(readOnly);
                            }
                            if (reqdIsolationLevel == UserTransaction.TRANSACTION_NONE) {
                                if (!cnx.getAutoCommit()) {
                                    NucleusLogger.CONNECTION.debug("Setting autocommit=true to connection: " + cnx.toString());
                                    cnx.setAutoCommit(true);
                                }
                            } else {
                                if (cnx.getAutoCommit()) {
                                    NucleusLogger.CONNECTION.debug("Setting autocommit=false to connection: " + cnx.toString());
                                    cnx.setAutoCommit(false);
                                }
                                if (rdba.supportsTransactionIsolation(reqdIsolationLevel)) {
                                    int currentIsolationLevel = cnx.getTransactionIsolation();
                                    if (currentIsolationLevel != reqdIsolationLevel) {
                                        NucleusLogger.CONNECTION.debug("Setting transaction isolation " + TransactionUtils.getNameForTransactionIsolationLevel(reqdIsolationLevel) + " to connection: " + cnx.toString());
                                        cnx.setTransactionIsolation(reqdIsolationLevel);
                                    }
                                } else {
                                    NucleusLogger.CONNECTION.warn(LOCALISER_RDBMS.msg("051008", reqdIsolationLevel));
                                }
                            }
                            if (NucleusLogger.CONNECTION.isDebugEnabled()) {
                                NucleusLogger.CONNECTION.debug(LOCALISER_RDBMS.msg("052002", cnx.toString(), TransactionUtils.getNameForTransactionIsolationLevel(reqdIsolationLevel)));
                            }
                            if (reqdIsolationLevel != isolation && isolation == UserTransaction.TRANSACTION_NONE) {
                                if (!cnx.getAutoCommit()) {
                                    NucleusLogger.CONNECTION.debug("Setting autocommit=true to connection: " + cnx.toString());
                                    cnx.setAutoCommit(true);
                                }
                            }
                            succeeded = true;
                        } catch (SQLException e) {
                            throw new NucleusDataStoreException(e.getMessage(), e);
                        } finally {
                            if (!succeeded) {
                                try {
                                    cnx.close();
                                } catch (SQLException e) {
                                }
                                if (NucleusLogger.CONNECTION.isDebugEnabled()) {
                                    String cnxStr = cnx.toString();
                                    NucleusLogger.CONNECTION.debug(LOCALISER_RDBMS.msg("052003", cnxStr));
                                }
                            }
                        }
                    } else {
                        cnx = ((DataSource) dataSource[0]).getConnection();
                        if (cnx == null) {
                            String msg = LOCALISER_RDBMS.msg("052000", dataSource[0]);
                            NucleusLogger.CONNECTION.error(msg);
                            throw new NucleusDataStoreException(msg);
                        }
                        if (NucleusLogger.CONNECTION.isDebugEnabled()) {
                            NucleusLogger.CONNECTION.debug(LOCALISER_RDBMS.msg("052001", cnx.toString()));
                        }
                    }
                } catch (SQLException e) {
                    throw new NucleusDataStoreException(e.getMessage(), e);
                }
                this.conn = cnx;
            }
            needsCommitting = true;
            return this.conn;
        }

        /**
         * Close the connection
         */
        public void close() {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).managedConnectionPreClose();
            }
            Connection conn = getSqlConnection();
            if (conn != null) {
                try {
                    String connStr = conn.toString();
                    if (commitOnRelease && needsCommitting) {
                        if (!conn.isClosed() && !conn.getAutoCommit()) {
                            SQLController sqlController = ((RDBMSStoreManager) storeMgr).getSQLController();
                            if (sqlController != null) {
                                sqlController.processConnectionStatement(this);
                            }
                            conn.commit();
                            needsCommitting = false;
                            if (NucleusLogger.CONNECTION.isDebugEnabled()) {
                                NucleusLogger.CONNECTION.debug(LOCALISER_RDBMS.msg("052005", connStr));
                            }
                        }
                    }
                    if (!conn.isClosed()) {
                        conn.close();
                        if (NucleusLogger.CONNECTION.isDebugEnabled()) {
                            NucleusLogger.CONNECTION.debug(LOCALISER_RDBMS.msg("052003", connStr));
                        }
                    } else {
                        if (NucleusLogger.CONNECTION.isDebugEnabled()) {
                            NucleusLogger.CONNECTION.debug(LOCALISER_RDBMS.msg("052004", connStr));
                        }
                    }
                } catch (SQLException sqle) {
                    throw new NucleusDataStoreException(sqle.getMessage(), sqle);
                }
            }
            try {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).managedConnectionPostClose();
                }
            } finally {
                listeners.clear();
            }
            this.conn = null;
        }

        /**
         * Convenience accessor for the java.sql.Connection in use (if any).
         * @return SQL Connection
         */
        private Connection getSqlConnection() {
            if (this.conn != null && this.conn instanceof Connection) {
                return (Connection) this.conn;
            } else if (this.conn != null && this.conn instanceof XAConnection) {
                try {
                    return ((XAConnection) this.conn).getConnection();
                } catch (SQLException e) {
                    throw new NucleusDataStoreException(e.getMessage(), e);
                }
            }
            return null;
        }
    }

    /**
     * Emulate the two phase protocol for non XA
     */
    static class EmulatedXAResource implements XAResource {

        Connection conn;

        EmulatedXAResource(Connection conn) {
            this.conn = conn;
        }

        public void commit(Xid xid, boolean onePhase) throws XAException {
            NucleusLogger.CONNECTION.debug("Managed connection " + this.toString() + " is committing for transaction " + xid.toString() + " with onePhase=" + onePhase);
            try {
                conn.commit();
                NucleusLogger.CONNECTION.debug("Managed connection " + this.toString() + " committed connection for transaction " + xid.toString() + " with onePhase=" + onePhase);
            } catch (SQLException e) {
                NucleusLogger.CONNECTION.debug("Managed connection " + this.toString() + " failed to commit connection for transaction " + xid.toString() + " with onePhase=" + onePhase);
                XAException xe = new XAException(StringUtils.getStringFromStackTrace(e));
                xe.initCause(e);
                throw xe;
            }
        }

        public void end(Xid xid, int flags) throws XAException {
            NucleusLogger.CONNECTION.debug("Managed connection " + this.toString() + " is ending for transaction " + xid.toString() + " with flags " + flags);
        }

        public void forget(Xid arg0) throws XAException {
        }

        public int getTransactionTimeout() throws XAException {
            return 0;
        }

        public boolean isSameRM(XAResource xares) throws XAException {
            return (this == xares);
        }

        public int prepare(Xid xid) throws XAException {
            NucleusLogger.CONNECTION.debug("Managed connection " + this.toString() + " is preparing for transaction " + xid.toString());
            return 0;
        }

        public Xid[] recover(int flags) throws XAException {
            throw new XAException("Unsupported operation");
        }

        public void rollback(Xid xid) throws XAException {
            NucleusLogger.CONNECTION.debug("Managed connection " + this.toString() + " is rolling back for transaction " + xid.toString());
            try {
                conn.rollback();
                NucleusLogger.CONNECTION.debug("Managed connection " + this.toString() + " rolled back connection for transaction " + xid.toString());
            } catch (SQLException e) {
                NucleusLogger.CONNECTION.debug("Managed connection " + this.toString() + " failed to rollback connection for transaction " + xid.toString());
                XAException xe = new XAException(StringUtils.getStringFromStackTrace(e));
                xe.initCause(e);
                throw xe;
            }
        }

        public boolean setTransactionTimeout(int arg0) throws XAException {
            return false;
        }

        public void start(Xid xid, int flags) throws XAException {
            NucleusLogger.CONNECTION.debug("Managed connection " + this.toString() + " is starting for transaction " + xid.toString() + " with flags " + flags);
        }
    }

    /**
     * Looks up a DataSource object in JNDI. This only permits lookup for DataSources locally
     * For remote DataSources usage, configure the PersistenceConfiguration as an object (ConnectionFactory),
     * instead of name (ConnectionFactoryName)
     * @param name The JNDI name of the DataSource.
     * @return The DataSource object.
     * @exception ConnectionFactoryNotFoundException If a JNDI lookup failure occurs.
     * @exception UnsupportedConnectionFactoryException If the object is not a javax.sql.DataSource.
     */
    private Object lookupDataSource(String name) {
        Object obj;
        try {
            obj = new InitialContext().lookup(name);
        } catch (NamingException e) {
            throw new ConnectionFactoryNotFoundException(name, e);
        }
        if (!(obj instanceof DataSource) && !(obj instanceof XADataSource)) {
            throw new UnsupportedConnectionFactoryException(obj);
        }
        return obj;
    }

    /**
     * Accessor for the pooling type (if any).
     * @return Pooling type to use (name of a pool type, or null)
     */
    public String getPoolingType() {
        if (poolingType == null) {
            poolingType = "datasource";
        }
        return poolingType;
    }

    /**
     * Method to set the connection pooling type (if any).
     * Tries to use any user-provided value if possible, otherwise will fallback to something
     * available in the CLASSPATH (if any), else use the builtin DBCP.
     * @param requiredPoolingType Pooling type requested by the user
     * @return Pooling type to use (name of a pool type)
     */
    protected String calculatePoolingType(String requiredPoolingType) {
        if (poolingType != null) {
            return poolingType;
        }
        poolingType = requiredPoolingType;
        ClassLoaderResolver clr = storeMgr.getNucleusContext().getClassLoaderResolver(null);
        if (poolingType != null) {
            if (poolingType.equalsIgnoreCase("DBCP") && !dbcpPresent(clr)) {
                NucleusLogger.CONNECTION.warn("DBCP specified but not present in CLASSPATH (or one of dependencies)");
                poolingType = null;
            } else if (poolingType.equalsIgnoreCase("C3P0") && !c3p0Present(clr)) {
                NucleusLogger.CONNECTION.warn("C3P0 specified but not present in CLASSPATH (or one of dependencies)");
                poolingType = null;
            } else if (poolingType.equalsIgnoreCase("Proxool") && !proxoolPresent(clr)) {
                NucleusLogger.CONNECTION.warn("Proxool specified but not present in CLASSPATH (or one of dependencies)");
                poolingType = null;
            } else if (poolingType.equalsIgnoreCase("BoneCP") && !bonecpPresent(clr)) {
                NucleusLogger.CONNECTION.warn("BoneCP specified but not present in CLASSPATH (or one of dependencies)");
                poolingType = null;
            }
        }
        if (poolingType == null && dbcpPresent(clr)) {
            poolingType = "DBCP";
        }
        if (poolingType == null && c3p0Present(clr)) {
            poolingType = "C3P0";
        }
        if (poolingType == null && proxoolPresent(clr)) {
            poolingType = "Proxool";
        }
        if (poolingType == null && bonecpPresent(clr)) {
            poolingType = "BoneCP";
        }
        if (poolingType == null) {
            if (JavaUtils.isJRE1_6OrAbove()) {
                poolingType = "dbcp-builtin";
            } else {
                poolingType = "None";
            }
        }
        return poolingType;
    }

    protected boolean dbcpPresent(ClassLoaderResolver clr) {
        try {
            clr.classForName("org.apache.commons.pool.ObjectPool");
            clr.classForName("org.apache.commons.dbcp.ConnectionFactory");
            return true;
        } catch (ClassNotResolvedException cnre) {
            return false;
        }
    }

    protected boolean c3p0Present(ClassLoaderResolver clr) {
        try {
            clr.classForName("com.mchange.v2.c3p0.ComboPooledDataSource");
            return true;
        } catch (ClassNotResolvedException cnre) {
            return false;
        }
    }

    protected boolean proxoolPresent(ClassLoaderResolver clr) {
        try {
            clr.classForName("org.logicalcobwebs.proxool.ProxoolDriver");
            clr.classForName("org.apache.commons.logging.Log");
            return true;
        } catch (ClassNotResolvedException cnre) {
            return false;
        }
    }

    protected boolean bonecpPresent(ClassLoaderResolver clr) {
        try {
            clr.classForName("com.jolbox.bonecp.BoneCPDataSource");
            clr.classForName("org.slf4j.Logger");
            clr.classForName("com.google.common.collect.Multiset");
            return true;
        } catch (ClassNotResolvedException cnre) {
            return false;
        }
    }
}
