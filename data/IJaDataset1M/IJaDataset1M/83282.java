package org.hsqldb;

import java.io.Serializable;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Properties;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.DataSource;

public class jdbcDataSource implements Serializable, Referenceable, DataSource {

    /**
     * Login timeout
     */
    private int loginTimeout = 0;

    /**
     * Log writer
     */
    private transient PrintWriter logWriter;

    /**
     * Default password to use for connections
     */
    private String password = "";

    /**
     * Default user to use for connections
     */
    private String user = "";

    /**
     * Signature
     */
    private static final String sStartURL = "jdbc:hsqldb:";

    /**
     * Database location
     */
    private String database = "";

    /**
     * Constructor
     */
    public jdbcDataSource() {
    }

    /**
     * Forward with current user/password
     */
    public Connection getConnection() throws java.sql.SQLException {
        return getConnection(user, password);
    }

    /**
     * getConnection method comment.
     */
    public Connection getConnection(String user, String password) throws SQLException {
        Properties props = new Properties();
        if (user != null) {
            props.put("user", user);
        }
        if (password != null) {
            props.put("password", password);
        }
        return new jdbcConnection(database, props);
    }

    /**
     * Return database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * getLoginTimeout method comment.
     */
    public int getLoginTimeout() throws java.sql.SQLException {
        return loginTimeout;
    }

    /**
     * getLogWriter method comment.
     */
    public java.io.PrintWriter getLogWriter() throws java.sql.SQLException {
        return null;
    }

    /**
     * getReference method comment.
     */
    public Reference getReference() throws NamingException {
        String cname = "org.hsqldb.jdbcDataSourceFactory";
        Reference ref = new Reference(getClass().getName(), cname, null);
        ref.add(new StringRefAddr("database", getDatabase()));
        ref.add(new StringRefAddr("user", getUser()));
        ref.add(new StringRefAddr("password", password));
        return ref;
    }

    /**
     * @return user ID for the connection
     */
    public String getUser() {
        return user;
    }

    /**
     * Set database location
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * Not yet implemented
     */
    public void setLoginTimeout(int ignore) throws java.sql.SQLException {
        this.loginTimeout = ignore;
    }

    /**
     * setLogWriter method comment.
     */
    public void setLogWriter(PrintWriter logWriter) throws java.sql.SQLException {
        this.logWriter = logWriter;
    }

    /**
     * Sets the password to use for connecting to the database
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the userid
     * @param user the user id
     */
    public void setUser(String user) {
        this.user = user;
    }
}
