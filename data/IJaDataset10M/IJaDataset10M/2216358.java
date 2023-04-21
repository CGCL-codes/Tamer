package gov.sns.apps.jeri.tools.database;

import gov.sns.tools.database.DatabaseException;
import java.util.*;
import java.util.logging.*;
import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.BLOB;
import javax.sql.DataSource;

/**
 * Provides a concrete subclass of <CODE>CachingDatabaseAdaptor</CODE> for 
 * implementing methods specifically for the Oracle database.
 *
 * @author Chris Fowlkes
 */
public class OracleCachingDatabaseAdaptor extends CachingDatabaseAdaptor {

    protected Map arrayDescriptorMap;

    /**
   * Holds the <CODE>DataSource</CODE> used to connect to the database.
   */
    private OracleDataSource dataSource;

    /**
   * Holds the description for the adaptor. This is used by the 
   * <CODE>toString()</CODE> method.
   */
    private String description;

    /**
   * Used to determine if a connection has been made.
   */
    private boolean connected = false;

    /**
   * Holds the <CODE>CachingDatabaseAdaptor</CODE> used if the first connect 
   * fails.
   */
    private OracleCachingDatabaseAdaptor failOverDatabase;

    /**
   * Flag used to determine if the failover database is being used.
   */
    private boolean usingFailOver = false;

    /**
	 * Public Constructor. Creates the <CODE>DataSource</CODE> and turns on 
   * caching.
   * 
   * @throws gov.sns.tools.database.DatabaseException Thrown on database error.
	 */
    public OracleCachingDatabaseAdaptor() throws gov.sns.tools.database.DatabaseException {
        try {
            arrayDescriptorMap = new HashMap();
            dataSource = new OracleDataSource();
            dataSource.setConnectionCachingEnabled(true);
        } catch (SQLException exception) {
            Logger.getLogger("global").log(Level.SEVERE, "Error creating the data source object.", exception);
            throw new DatabaseException("Exception creating the data source.", this, exception);
        }
    }

    /**
	 * Get a new database connection. This method will initialize the 
   * <CODE>DataSource</CODE> if this is the first time the method has been 
   * invoked, or the URL has changed since the last time.
   * 
	 * @param urlSpec The URL to which to connect
	 * @param user The user loggin into the database
	 * @param password the user's password
	 * @throws gov.sns.tools.database.DatabaseException if a database exception is thrown
	 */
    @Override
    public Connection getConnection(String urlSpec, String user, String password) throws DatabaseException {
        OracleDataSource dataSource = getOracleDataSource();
        if (!urlSpec.equals(getURL())) {
            close();
            setURL(urlSpec);
            setUser(user);
            setPassword(password);
        }
        return getConnection(user, password);
    }

    /**
	 * Get a new database connection. This method will tries to return a 
   * <CODE>Connection</CODE> to the database using the supplied credentials.
   * 
	 * @param urlSpec The URL to which to connect
	 * @param user The user loggin into the database
	 * @param password the user's password
	 * @throws gov.sns.tools.database.DatabaseException if a database exception is thrown
	 */
    @Override
    public Connection getConnection(String user, String password) throws gov.sns.tools.database.DatabaseException {
        try {
            OracleDataSource cache = getOracleDataSource();
            if (cache.getUser() == null) {
                cache.setUser(user);
                cache.setPassword(password);
            }
            Connection connection = cache.getConnection(user, password);
            connected = true;
            usingFailOver = false;
            return connection;
        } catch (java.sql.SQLException ex) {
            try {
                CachingDatabaseAdaptor failOver = getFailOverDatabase();
                if (connected || failOver == null) throw ex;
                Connection connection = failOver.getConnection(user, password);
                usingFailOver = true;
                connected = true;
                return connection;
            } catch (java.sql.SQLException exc) {
                Logger.getLogger("global").log(Level.SEVERE, "Error connecting to the database as user: " + user, exc);
                throw new DatabaseException("Exception connecting to the database.", this, exc);
            }
        }
    }

    /**
   * Closes all of the connections in the cache.
   * 
   * @throws gov.sns.tools.database.DatabaseException Thrown on database error.
   */
    public void close() throws gov.sns.tools.database.DatabaseException {
        try {
            getOracleDataSource().close();
            connected = false;
            usingFailOver = false;
        } catch (SQLException exception) {
            Logger.getLogger("global").log(Level.SEVERE, "Error closing database connections.", exception);
            throw new DatabaseException("Exception closing database connections.", this, exception);
        }
    }

    /**
   * Returns a <CODE>Connection</CODE> to the database using the credentials 
   * last supplied.
   * 
   * @return A <CODE>Connection</CODE> to the database.
   * @throws gov.sns.tools.database.DatabaseException Thrown on SQL error.
   */
    @Override
    public Connection getConnection() throws DatabaseException {
        try {
            Connection connection = getDataSource().getConnection();
            connected = true;
            usingFailOver = false;
            return connection;
        } catch (SQLException ex) {
            try {
                CachingDatabaseAdaptor failOver = getFailOverDatabase();
                if (connected || failOver == null) throw ex;
                Connection connection = failOver.getConnection();
                usingFailOver = true;
                connected = true;
                return connection;
            } catch (java.sql.SQLException exc) {
                Logger.getLogger("global").log(Level.SEVERE, "Error connecting to the database.", exc);
                throw new DatabaseException("Exception connecting to the database.", this, exc);
            }
        }
    }

    /**
	 * Instantiate an empty Blob.
	 * @param connection the database connection
	 * @return a new instance of a Blob appropriate for this adaptor.
	 */
    @Override
    public Blob newBlob(final Connection connection) {
        try {
            return BLOB.createTemporary(connection, true, BLOB.DURATION_SESSION);
        } catch (SQLException exception) {
            throw new DatabaseException("Exception instantiating a Blob.", this, exception);
        }
    }

    /**
	 * Get an SQL Array given an SQL array type, connection and a primitive array
	 * @param type An SQL array type identifying the type of array
	 * @param connection An SQL connection
	 * @param array The primitive Java array
	 * @return the SQL array which wraps the primitive array
	 * @throws gov.sns.tools.database.DatabaseException if a database exception is thrown
	 */
    @Override
    public Array getArray(String type, Connection connection, Object array) throws DatabaseException {
        try {
            final ArrayDescriptor descriptor = getArrayDescriptor(type, connection);
            return new ARRAY(descriptor, connection, array);
        } catch (SQLException exception) {
            Logger.getLogger("global").log(Level.SEVERE, "Error instantiating an SQL array of type: " + type, exception);
            throw new DatabaseException("Exception generating an SQL array.", this, exception);
        }
    }

    /**
	 * Get the array descriptor for the specified array type
	 * @param type An SQL array type
	 * @param connection A database connection
	 * @return the array descriptor for the array type
	 * @throws java.sql.SQLException if a database exception is thrown
	 */
    private ArrayDescriptor getArrayDescriptor(final String type, final Connection connection) throws SQLException {
        if (arrayDescriptorMap.containsKey(type)) {
            return (ArrayDescriptor) arrayDescriptorMap.get(type);
        } else {
            ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor(type, connection);
            arrayDescriptorMap.put(type, descriptor);
            return descriptor;
        }
    }

    /**
   * Finalizes the instance. Makes sure all connections are closed.
   * 
   * @throws java.lang.Throwable Thrown on error.
   */
    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    /**
   * Allows the user to interact directly with the JDBC data source.
   * 
   * @return The <CODE>DataSource</CODE> for the guven URL.
   */
    @Override
    public final DataSource getDataSource() {
        return getOracleDataSource();
    }

    /**
   * Allows the user to interact directly with the <CODE>OracleDataSource</CODE>
   * without having to cast.
   * 
   * @return The <CODE>DataSource</CODE> for the guven URL.
   */
    public OracleDataSource getOracleDataSource() {
        if (isUsingFailOver()) return getFailOverDatabase().getOracleDataSource(); else return dataSource;
    }

    /**
   * Determines if the database being used is the fail over or the main 
   * database.
   * 
   * @return <CODE>true</CODE> if the database being used is the main one, <CODE>false</CODE> if it is the fail over.
   */
    public boolean isUsingFailOver() {
        return usingFailOver;
    }

    /**
   * Gets the name of the server to which all connections using the adaptor are 
   * made.
   * 
   * @return The name of the server to which connection are made.
   * @throws gov.sns.tools.database.DatabaseException Thrown on database error.
   */
    public String getServerName() throws gov.sns.tools.database.DatabaseException {
        return getOracleDataSource().getServerName();
    }

    /**
   * Sets the name of the server to which all connections using the adaptor are 
   * made.
   * 
   * @param serverName The name of the server to which connection are to be made.
   * @throws gov.sns.tools.database.DatabaseException Thrown on database error.
   */
    public void setServerName(String serverName) {
        getOracleDataSource().setServerName(serverName);
    }

    /**
   * Gets the name of the database to which all connections using the adaptor
   * are made.
   * 
   * @return The name of the database to which connections are made.
   * @throws gov.sns.tools.database.DatabaseException Thrown on database error.
   */
    public String getDatabaseName() throws gov.sns.tools.database.DatabaseException {
        return getOracleDataSource().getDatabaseName();
    }

    /**
   * Sets the name of the database to which all connections using the adaptor
   * are made.
   * 
   * @return The name of the database to which connections will be made.
   * @throws gov.sns.tools.database.DatabaseException Thrown on database error.
   */
    public void setDatabaseName(String databaseName) {
        getOracleDataSource().setDatabaseName(databaseName);
    }

    /**
   * Gets the port number through which all database connections are made.
   * 
   * @return The port number through which all database connections are made.
   */
    public int getPortNumber() {
        return getOracleDataSource().getPortNumber();
    }

    /**
   * Sets the port number through which all database connections are made.
   * 
   * @param portNumber The port number through which all database connections will be made.
   */
    public void setPortNumber(int portNumber) {
        getOracleDataSource().setPortNumber(portNumber);
    }

    /**
   * Gets the driver type used to create connections to the database.
   * 
   * @return The driver type used to create connections to the database.
   */
    public String getDriverType() {
        return getOracleDataSource().getDriverType();
    }

    /**
   * Sets the driver type used to create connections to the database.
   * 
   * @param driverType The driver type to be used to create connections to the database.
   */
    public void setDriverType(String driverType) {
        getOracleDataSource().setDriverType(driverType);
    }

    /**
   * Gets the user name used to connect to the database.
   * 
   * @return The user name used to connect to the database.
   */
    public String getUser() {
        return getOracleDataSource().getUser();
    }

    /**
   * Sets the user name used to connect to the database.
   * 
   * @param user The user name used to be used to connect to the database.
   */
    public void setUser(String user) {
        getOracleDataSource().setUser(user);
    }

    /**
   * Sets the password to be used to connect to the database.
   * 
   * @param password The password to be used to connect to the database.
   */
    public void setPassword(String password) {
        getOracleDataSource().setPassword(password);
    }

    /**
   * Gets the URL used to create database connections.
   * 
   * @return The URL used to connect to the database.
   * @throws gov.sns.tools.database.DatabaseException Thrown on SQL error.
   */
    public String getURL() throws gov.sns.tools.database.DatabaseException {
        try {
            return getOracleDataSource().getURL();
        } catch (SQLException exception) {
            Logger.getLogger("global").log(Level.SEVERE, "Error getting URL for database.", exception);
            throw new DatabaseException("Exception getting database URL.", this, exception);
        }
    }

    /**
   * Sets the URL used to connect to the database.
   * 
   * @param url The URL to the database.
   */
    public void setURL(String url) {
        getOracleDataSource().setURL(url);
    }

    /**
   * Gets the name of the connection cache being used.
   * 
   * @return The name of the connection cache currently being used.
   */
    public String getConnectionCacheName() {
        try {
            return getOracleDataSource().getConnectionCacheName();
        } catch (SQLException exception) {
            Logger.getLogger("global").log(Level.SEVERE, "Error getting the connection cache name.", exception);
            throw new DatabaseException("Exception getting cache name.", this, exception);
        }
    }

    /**
   * Sets the name of the connection cache to be used to obtain connaections to 
   * the database.
   * 
   * @param connectionCacheName The name of the connection cache.
   */
    public void setConnectionCacheName(String connectionCacheName) {
        try {
            getOracleDataSource().setConnectionCacheName(connectionCacheName);
        } catch (SQLException exception) {
            Logger.getLogger("global").log(Level.SEVERE, "Error setting the connection cache name.", exception);
            throw new DatabaseException("Exception setting cache name.", this, exception);
        }
    }

    /**
   * Gets the description of the instance.
   * 
   * @return The description of the adaptor instance.
   */
    public String getDescription() {
        if (isUsingFailOver()) return getFailOverDatabase().getDescription(); else return description;
    }

    /**
   * Gets the description of the instance.
   * 
   * @param description The description of the instance.
   */
    public void setDescription(String description) {
        if (isUsingFailOver()) getFailOverDatabase().setDescription(description); else this.description = description;
    }

    /**
   * returns the value of the description property if it has been set.
   * 
   * @return The value of the description property if it has been set.
   */
    @Override
    public String toString() {
        String description = getDescription();
        if (description == null) return super.toString(); else return description;
    }

    /**
   * Sets the <CODE>CachingDatabaseAdaptor</CODE> used if the first connect 
   * attempt fails.
   * 
   * @param failOverDatabase The <CODE>CachingDatabaseAdaptor</CODE> used if the first connect fails.
   */
    public void setFailOverDatabase(OracleCachingDatabaseAdaptor failOverDatabase) {
        this.failOverDatabase = failOverDatabase;
    }

    /**
   * Returns the <CODE>CachingDatabaseAdaptor</CODE> used of the first connect 
   * fails.
   * 
   * @return The <CODE>CachingDatabaseAdaptor</CODE> for the fail over database.
   */
    public OracleCachingDatabaseAdaptor getFailOverDatabase() {
        return failOverDatabase;
    }

    /**
   * Returns the roles to which a user belongs. If user roles are not supported
   * by the RDB, an empty array should be returned.
   * 
   * @return The database roles to which a user belongs.
   */
    @Override
    public String[] getUserRoles() throws gov.sns.tools.database.DatabaseException {
        ArrayList roles = new ArrayList();
        try {
            Connection oracleConnection = getConnection();
            try {
                Statement query = oracleConnection.createStatement();
                try {
                    String sql = "SELECT ROLE FROM SESSION_ROLES";
                    ResultSet result = query.executeQuery(sql);
                    try {
                        while (result.next()) roles.add(result.getString("ROLE"));
                        return (String[]) roles.toArray(new String[roles.size()]);
                    } finally {
                        result.close();
                    }
                } finally {
                    query.close();
                }
            } finally {
                oracleConnection.close();
            }
        } catch (java.sql.SQLException ex) {
            Logger.getLogger("global").log(Level.SEVERE, "Error getting roles from the RDB.", ex);
            throw new DatabaseException("Exception getting roles.", this, ex);
        }
    }
}
