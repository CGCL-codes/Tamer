package com.teletalk.jserver.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import com.teletalk.jserver.SubComponent;
import com.teletalk.jserver.jdbc.PooledConnection;
import com.teletalk.jserver.property.Property;
import com.teletalk.jserver.property.StringProperty;

/**
 * This class is a specialized pool to handle database connections.
 * 
 * @author Tobias L�fstrand
 * 
 * @since Alpha
 */
public class DBConnectionPool extends ObjectPool {

    private StringProperty driverName;

    private StringProperty dbURL;

    private StringProperty user;

    private StringProperty password;

    private StringProperty connectionValidationQuery;

    /** Flag indicating if the database driver has been initialized. */
    protected boolean driverInitialized = false;

    /**
	 * Constructs a new DBConnectionPool named "DBConnectionPool". The created pool will have a cleanup interval of 60 seconds and an object expiration time
	 * of 120 seconds. The pool min size will be set to 0 and the max size to 10. The default values for the database connection properties (driverName, dbURL, user, password) will be read from property file. 
	 * 
	 * @param parent the parent of this DBConnectionPool.
	 */
    public DBConnectionPool(final SubComponent parent) {
        this(parent, "DBConnectionPool", "", "");
    }

    /**
	 * Constructs a new dynamic size DBConnectionPool. The created pool will have a cleanup interval of 60 seconds and an object expiration time
	 * of 120 seconds. The pool min size will be set to 0 and the max size to 10. The default values for the database connection properties (driverName, dbURL, user, password) will be read from property file.
	 * 
	 * @param parent the parent of this DBConnectionPool.
	 * @param name the name of this DBConnectionPool.
	 */
    public DBConnectionPool(final SubComponent parent, final String name) {
        this(parent, name, "", "");
    }

    /**
	 * Constructs a new DBConnectionPool. The created pool will have a cleanup interval of 60 seconds and an object expiration time
	 * of 120 seconds. The pool min size will be set to 0 and the max size to 10.
	 * 
	 * @param parent the parent of this DBConnectionPool.
	 * @param name the name of this DBConnectionPool.
	 * @param driverName the default name of the database driver. 
	 * @param dbURL the default value for the URL to the database.
	 */
    public DBConnectionPool(final SubComponent parent, final String name, final String driverName, final String dbURL) {
        this(parent, name, driverName, dbURL, 60 * 1000, 120 * 1000);
    }

    /**
	 * Constructs a new DBConnectionPool. The pool min size will be set to 0 and the max size to 10.
	 * 
	 * @param parent the parent of this DBConnectionPool.
	 * @param name the name of this DBConnectionPool.
	 * @param driverName the default name of the database driver. 
	 * @param dbURL the default value for the URL to the database.
	 * @param cleanInterval time in milliseconds between cleanups, used by the associated PoolCleaner.
	 * @param expirationTime default value for the time in milliseconds that has to pass after an object has been created/checked in
	 * til it is considered to be expired.
	 */
    public DBConnectionPool(final SubComponent parent, final String name, final String driverName, final String dbURL, final long cleanInterval, final long expirationTime) {
        this(parent, name, driverName, dbURL, "", "", cleanInterval, expirationTime);
    }

    /**
	 * Constructs a new DBConnectionPool. The min size of the pool will be set to maximum size. 
	 * 
	 * @param parent the parent of this DBConnectionPool.
	 * @param name the name of this DBConnectionPool.
	 * @param driverName the default name of the database driver. 
	 * @param dbURL the default value for the URL to the database.
	 * @param maxSize default value for the maximum size of the pool.
	 * @param cleanInterval time in milliseconds between cleanups, used by the associated PoolCleaner.
	 * @param expirationTime default value for the time in milliseconds that has to pass after an object has been created/checked in
	 * til it is considered to be expired.
	 */
    public DBConnectionPool(final SubComponent parent, final String name, final String driverName, final String dbURL, final int maxSize, final long cleanInterval, final long expirationTime) {
        this(parent, name, driverName, dbURL, "", "", maxSize, cleanInterval, expirationTime);
    }

    /**
	 * Constructs a new DBConnectionPool. The min size of the pool will be set to maximum size. 
	 * 
	 * @param parent the parent of this DBConnectionPool.
	 * @param name the name of this DBConnectionPool.
	 * @param driverName the default name of the database driver. 
	 * @param dbURL the default value for the URL to the database.
	 * @param minSize default value for the minimum size of the pool.
	 * @param maxSize default value for the maximum size of the pool.
	 * @param cleanInterval time in milliseconds between cleanups, used by the associated PoolCleaner.
	 * @param expirationTime default value for the time in milliseconds that has to pass after an object has been created/checked in
	 * til it is considered to be expired.
	 * 
	 * @since 2.0 Build 757
	 */
    public DBConnectionPool(final SubComponent parent, final String name, final String driverName, final String dbURL, final int minSize, final int maxSize, final long cleanInterval, final long expirationTime) {
        this(parent, name, driverName, dbURL, "", "", minSize, maxSize, cleanInterval, expirationTime);
    }

    /**
	 * Constructs a new DBConnectionPool with an connection expiration time of 120 seconds. The pool min size will be set to 0 and the max size to 10.
	 * 
	 * @param parent the parent of this DBConnectionPool.
	 * @param name the name of this DBConnectionPool.
	 * @param driverName the default name of the database driver. 
	 * @param dbURL the default value for the URL to the database.
	 * @param user the default value for the username for the database.
	 * @param password the default value for the password for the database.
	 */
    public DBConnectionPool(final SubComponent parent, final String name, final String driverName, final String dbURL, final String user, final String password) {
        this(parent, name, driverName, dbURL, user, password, 60 * 1000, 120 * 1000);
    }

    /**
	 * Constructs a new DBConnectionPool. The pool min size will be set to 0 and the max size to 10.
	 *  
	 * @param parent the parent of this DBConnectionPool.
	 * @param name the name of this DBConnectionPool.
	 * @param driverName the default name of the database driver. 
	 * @param dbURL the default value for the URL to the database.
	 * @param user the default value for the username for the database.
	 * @param password the default value for the password for the database.
	 * @param cleanInterval time in milliseconds between cleanups, used by the associated PoolCleaner.
	 * @param expirationTime default value for the time in milliseconds that has to pass after an object has been created/checked in
	 * til it is considered to be expired.
	 */
    public DBConnectionPool(final SubComponent parent, final String name, final String driverName, final String dbURL, final String user, final String password, final long cleanInterval, final long expirationTime) {
        super(parent, name, cleanInterval, expirationTime);
        this.setPoolObjectFactory(new DBPoolObjectFactory(this));
        initProperties(driverName, dbURL, user, password);
    }

    /**
	 * Constructs a new DBConnectionPool. The min size of the pool will be set to maximum size. 
	 * 
	 * @param parent the parent of this DBConnectionPool.
	 * @param name the name of this DBConnectionPool.
	 * @param driverName the default name of the database driver. 
	 * @param dbURL the default value for the URL to the database.
	 * @param user the default value for the username for the database.
	 * @param password the default value for the password for the database.
	 * @param maxSize default value for the maximum size of the pool.
	 * @param cleanInterval time in milliseconds between cleanups, used by the associated PoolCleaner.
	 * @param expirationTime default value for the time in milliseconds that has to pass after an object has been created/checked in
	 * til it is considered to be expired.
	 */
    public DBConnectionPool(final SubComponent parent, final String name, final String driverName, final String dbURL, final String user, final String password, final int maxSize, final long cleanInterval, final long expirationTime) {
        super(parent, name, maxSize, cleanInterval, expirationTime);
        this.setPoolObjectFactory(new DBPoolObjectFactory(this));
        initProperties(driverName, dbURL, user, password);
    }

    /**
	 * Constructs a new DBConnectionPool. The min size of the pool will be set to maximum size. 
	 * 
	 * @param parent the parent of this DBConnectionPool.
	 * @param name the name of this DBConnectionPool.
	 * @param driverName the default name of the database driver. 
	 * @param dbURL the default value for the URL to the database.
	 * @param user the default value for the username for the database.
	 * @param password the default value for the password for the database.
	 * @param minSize default value for the minimum size of the pool.
	 * @param maxSize default value for the maximum size of the pool.
	 * @param cleanInterval time in milliseconds between cleanups, used by the associated PoolCleaner.
	 * @param expirationTime default value for the time in milliseconds that has to pass after an object has been created/checked in
	 * til it is considered to be expired.
	 * 
	 * @since 2.0 Build 757
	 */
    public DBConnectionPool(final SubComponent parent, final String name, final String driverName, final String dbURL, final String user, final String password, final int minSize, final int maxSize, final long cleanInterval, final long expirationTime) {
        super(parent, name, minSize, maxSize, cleanInterval, expirationTime);
        this.setPoolObjectFactory(new DBPoolObjectFactory(this));
        initProperties(driverName, dbURL, user, password);
    }

    /**
	 * Constructs a new DBConnectionPool. The min size of the pool will be set to maximum size. 
	 * 
	 * @param parent the parent of this DBConnectionPool.
	 * @param name the name of this DBConnectionPool.
	 * @param driverName the default name of the database driver. 
	 * @param dbURL the default value for the URL to the database.
	 * @param user the default value for the username for the database.
	 * @param password the default value for the password for the database.
	 * @param minSize default value for the minimum size of the pool.
	 * @param maxSize default value for the maximum size of the pool.
	 * @param cleanInterval time in milliseconds between cleanups, used by the associated PoolCleaner.
	 * @param expirationTime default value for the time in milliseconds that has to pass after an object has been created/checked in
	 * til it is considered to be expired.
	 * @param poolObjectFactory the PoolObjectFactory to be used for creation, validation and finalization of database connection (java.sql.Connection) objects. 
	 * If this parameter is <code>null</code> a {@link DBPoolObjectFactory} will be created.
	 * 
	 * @since 2.0 Build 757
	 */
    public DBConnectionPool(final SubComponent parent, final String name, final String driverName, final String dbURL, final String user, final String password, final int minSize, final int maxSize, final long cleanInterval, final long expirationTime, final PoolObjectFactory poolObjectFactory) {
        super(parent, name, minSize, maxSize, cleanInterval, expirationTime, poolObjectFactory);
        if (poolObjectFactory == null) this.setPoolObjectFactory(new DBPoolObjectFactory(this));
        initProperties(driverName, dbURL, user, password);
    }

    private void initProperties(String driverName, String dbURL, String user, String password) {
        this.driverName = new StringProperty(this, "driver", driverName, Property.MODIFIABLE_OWNER_RESTART);
        this.driverName.setDescription("The name (fully qualified class name) of the database driver used by this DBConnectionPool to create database connections.");
        this.dbURL = new StringProperty(this, "databaseURL", dbURL, Property.MODIFIABLE_OWNER_RESTART);
        this.dbURL.setDescription("The URL of the database which this DBConnectionPool should create connections to.");
        this.user = new StringProperty(this, "username", user, Property.MODIFIABLE_OWNER_RESTART);
        this.user.setDescription("The username needed to acces the specified database.");
        this.password = new StringProperty(this, "password", password, Property.MODIFIABLE_OWNER_RESTART);
        this.password.setDescription("The password needed to acces the specified database.");
        this.connectionValidationQuery = new StringProperty(this, "connectionValidationQuery", "", Property.MODIFIABLE_NO_RESTART);
        this.connectionValidationQuery.setDescription("A query to be executed on a connection to validate that it is still open and functional. " + "The query will be executed when validateObject(Object, boolean) is called during the cyclic pool clean up. The query will be executed " + "by creating a statement using Connection.createStatement() and invoking Statement.execute(String) with the query as parameter.");
        addProperty(this.driverName);
        addProperty(this.dbURL);
        addProperty(this.user);
        addProperty(this.password);
        addProperty(this.connectionValidationQuery);
    }

    /**
    * Enables and fills the pool. The pool is filled to
    * maxSize, if maxSize is larger than 0.
    */
    protected void doInitialize() {
        super.doInitialize();
        super.initFromConfiguredProperty(this.dbURL, "database URL", false, true);
    }

    /**
	 * Checkes out and returns a Connection from the pool. If the pool is empty or there are no valid 
	 * items, this method will block the calling thread until a valid item can be produced.
	 * 
	 * @return a Connection, null if the pool is disabled. Null is also returned if there was an error during check out or if the calling thread was interrupted.
	 */
    public Connection getConnectionWait() {
        return (Connection) checkOutWait();
    }

    /**
	 * Checkes out and returns a Connection from the pool. If the pool is empty or 
	 * there are no valid items, this method will block the calling thread until a valid item can be produced or the 
	 * specified <code>waitTime </code> ellapses.
	 * 
	 * @param waitTime maximum wait time in milliseconds.
	 * 
	 * @return an uninitalized Connection, null if the pool is disabled. Null is also returned if there was an error 
	 * during check out or if the calling thread was interrupted.
	 */
    public Connection getConnectionWait(long waitTime) {
        return (Connection) checkOutWait(waitTime);
    }

    /**
	 * Checkes out and returns a Connection from the pool. If the pool is empty or 
	 * if there are no valid items, null will be returned.
	 * 
	 * @return an uninitalized Connection, null if the pool is disabled, empty or if it contained no valid items. Null is also 
	 * returned if there was an error during check out.
	 */
    public Connection getConnectionIfAny() {
        return (Connection) checkOutIfAny();
    }

    /**
	 * Checkes out and returns a Connection from the pool. If the pool is empty or 
	 * if there are no valid items, a new object will be created and returned (though it will not be added to 
	 * the pool when it is eventually checked in).
	 * 
	 * @return an uninitalized Connection, null if the pool is disabled. Null is also returned if there was an error during check out.
	 */
    public Connection getConnection() {
        return (Connection) checkOut();
    }

    /**
	 * Returns a connection to the pool.
	 * 
	 * @param connection a Connection object
	 */
    public void returnConnection(Connection connection) {
        checkIn(connection);
    }

    /**
	 * Called to indicate that the Connection specified by parameter connection was bad.
	 * 
	 * @param connection a bad Connection object.
	 */
    public void badConnection(Connection connection) {
        badObject(connection);
    }

    /**
	 * Called to indicate that the Connection specified by parameter connection was bad and a new one
	 * is required.
	 * 
	 * @param connection a bad Connection object.
	 * 
	 * @return a new Connection object.
	 */
    public Connection badConnectionGetNew(Connection connection) {
        return (Connection) badObjectGetNew(connection);
    }

    /**
    * Creates a new Connection object. Called by {@link #createObject()}.
    * 
    * @return a newly created Connection object.
    */
    public Connection createConnection() throws Exception {
        if (!driverInitialized) {
            if (isDebugMode()) logDebug("(createObject) - loading driver class ('" + driverName.getValueAsString() + "').");
            Class.forName(driverName.getValueAsString()).newInstance();
            driverInitialized = true;
        }
        if (user.getValueAsString().equals("")) {
            if (isDebugMode()) logDebug("(createObject) - Calling DriverManager.getConnection with argument '" + dbURL.getValueAsString() + "'.");
            return DriverManager.getConnection(dbURL.getValueAsString());
        } else {
            if (isDebugMode()) logDebug("(createObject) - Calling DriverManager.getConnection with arguments '" + dbURL.getValueAsString() + "', '" + user.getValueAsString() + "', '" + password.getValueAsString() + "'.");
            return DriverManager.getConnection(dbURL.getValueAsString(), user.getValueAsString(), password.getValueAsString());
        }
    }

    /**
	 * Creates a new Connection object by invoking the method {@link #createConnection()}.
	 * 
	 * @return a newly created Connection object.
	 */
    public Object createObject() throws Exception {
        return this.createConnection();
    }

    /**
	 * Validates a Connection object. This implementation invokes the method {@link #executeConnectionValidationQuery(Connection)} to execute 
    * a query to validate the status of the connection, but only if this method was invoked during clean up (parameter cleanUpValidation true).
	 * 
	 * @param obj the Connection object to validate.
    * @param cleanUpValidation flag indicating if this method was invoked from the {@link #cleanUp() clean up} method of this pool (<code>true<code>).
	 * 
	 * @return true if the Connection object is open, otherwise false.
	 */
    public boolean validateObject(Object obj, boolean cleanUpValidation) {
        try {
            Connection connection = (Connection) obj;
            boolean connectionOk = !connection.isClosed();
            if (connectionOk) {
                if (cleanUpValidation) {
                    this.executeConnectionValidationQuery(connection);
                }
            }
            return connectionOk;
        } catch (Exception e) {
            if (this.isDebugMode()) logDebug("Error while validating connection - " + e + ".");
            return false;
        }
    }

    /**
    * Executes a query to validate that the connection is ok. This method is invoked from {@link #validateObject(Object, boolean)}, when 
    * called from the clean up method of the pool.<br>
    * <br>
    * This implementation checks if a {@link #getConnectionValidationQuery() connection validation query} has been set for this pool, and if 
    * so, attempts to execute it (by creating a statement using the statement Connection.createStatement() and invoking Statement.execute(String)).
    * 
    * @since 2.0.1 (20041201)
    */
    protected void executeConnectionValidationQuery(final Connection connection) throws Exception {
        if ((this.connectionValidationQuery.stringValue() != null) && (this.connectionValidationQuery.stringValue().trim().length() > 0)) {
            if (this.isDebugMode()) logDebug("Executing connection validation query: " + this.connectionValidationQuery.stringValue() + " on connection " + connection + ".");
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.execute(this.connectionValidationQuery.stringValue());
            } finally {
                try {
                    statement.close();
                } catch (Throwable t) {
                }
            }
        }
    }

    /**
	 * Finalizes a Connection object by closing it.
	 * 
	 * @param obj an Connection object to be finalized.
	 */
    public void finalizeObject(final Object obj) {
        final boolean debugModeFlag = this.isDebugMode();
        Runnable connectionCloser = new Runnable() {

            public void run() {
                try {
                    if (obj instanceof PooledConnection) {
                        ((PooledConnection) obj).getTargetConnection().close();
                    } else {
                        ((Connection) obj).close();
                    }
                } catch (Exception e) {
                    if (debugModeFlag) logDebug("Error in finalizeObject() - " + e + ".");
                }
            }
        };
        super.fireGlobalEvent(connectionCloser);
    }

    /**
	 */
    public synchronized void connectionErrorOccurred() {
        super.reinitialize();
    }

    /**
	 * Called when a property owned by this ObjectPool has changed.
	 * 
	 * @param property the property that has changed.
	 */
    public void propertyModified(Property property) {
        if (property == driverName) {
            driverInitialized = false;
        }
        super.propertyModified(property);
    }

    /**
	 * Sets the driver name to be used when ceating database connections.
	 * 
	 * @param driverName the database driver name.
	 */
    public void setDriverName(String driverName) {
        this.driverName.setValue(driverName);
    }

    /**
	 * Gets the driver name currently used when ceating database connections.
	 * 
	 * @return a String containing the database driver name.
	 */
    public String getDriverName() {
        return this.driverName.stringValue();
    }

    /**
	 * Sets the URL to the database.
	 * 
	 * @param dbURL the URL to the database.
	 */
    public void setDbURL(String dbURL) {
        this.dbURL.setValue(dbURL);
    }

    /**
	 * Gets the URL to the database.
	 * 
	 * @return the URL to the database.
	 */
    public String getDbURL() {
        return this.dbURL.stringValue();
    }

    /**
	 * Sets the user name to be used when connecting to a database.
	 * 
	 * @param user the user name.
	 */
    public void setUser(String user) {
        this.user.setValue(user);
    }

    /**
	 * Gets the user name used when connecting to a database.
	 * 
	 * @return a String containing the user name.
	 */
    public String getUser() {
        return this.user.stringValue();
    }

    /**
	 * Sets the password to be used when connecting to a database.
	 * 
	 * @param password the password.
	 */
    public void setPassword(String password) {
        this.password.setValue(password);
    }

    /**
	 * Gets the password used when connecting to a database.
	 * 
	 * @return a String containing the password.
	 */
    public String getPassword() {
        return this.password.stringValue();
    }

    /**
    * Gets the query used for validating the status of a connection. A return value of <code>null</code> means that 
    * no validation query will be executed.
    * 
    * @return the query used for validating the status of a connection, or <code>null</code> if none is specified.
    * 
    * @since 1.3
    */
    public String getConnectionValidationQuery() {
        return this.connectionValidationQuery.stringValue();
    }

    /**
    * Sets the query used for validating the status of a connection. A value of <code>null</code> means that 
    * no validation query will be executed.
    * 
    * @param connectionValidationQuery the query to be used for validating the status of a connection. 
    * 
    * @since 1.3
    */
    public void setConnectionValidationQuery(String connectionValidationQuery) {
        this.connectionValidationQuery.setValue(connectionValidationQuery);
    }
}
