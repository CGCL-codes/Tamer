package uk.org.ogsadai.database.jdbc;

/**
 * Connection properties for an JDBC database.
 *
 * @author The OGSA-DAI Project Team.
 */
public class JDBCConnectionProperties {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2009.";

    /** JDBC connection URL. */
    private String mConnectionURL;

    /** JDBC driver class name. */
    private String mDriverClass;

    /** Database user name. */
    private String mUserName;

    /** Database password. */
    private String mPassword;

    /**
     * Constructor.
     * 
     * @param connectionURL
     *     Connection URL.
     * @param driverClass
     *     Driver class.
     * @param username
     *     Username.
     * @param password
     *     Password.
     * @throws IllegalArgumentException if <code>connectionURL</code>
     * or <code>driverClass</code> are <code>null</code>.
     */
    public JDBCConnectionProperties(String connectionURL, String driverClass, String username, String password) throws IllegalArgumentException {
        if (connectionURL == null) {
            throw new IllegalArgumentException("connectionURL must not be null");
        }
        if (driverClass == null) {
            throw new IllegalArgumentException("driverClass must not be null");
        }
        mConnectionURL = connectionURL;
        mDriverClass = driverClass;
        mUserName = username;
        mPassword = password;
    }

    /**
     * Gets connection URL.
     *
     * @return connection URL.
     */
    public String getConnectionURL() {
        return mConnectionURL;
    }

    /**
     * Gets driver class name.
     *
     * @return driver class name.
     */
    public String getDriverClass() {
        return mDriverClass;
    }

    /**
     * Gets username.
     *
     * @return username.
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * Gets password
     *
     * @return password.
     */
    public String getPassword() {
        return mPassword;
    }
}
