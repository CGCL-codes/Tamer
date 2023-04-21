package net.sourceforge.javautil.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import net.sourceforge.javautil.common.exception.ThrowableManagerRegistry;
import net.sourceforge.javautil.common.password.IPassword;
import net.sourceforge.javautil.common.password.impl.UnencryptedPassword;

/**
 * A simple data source implementation that will use common driver, url, username and password
 * settings for generating connections. It will wrap connections so that closing of them can be
 * controlled and monitored.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: SimpleDataSource.java 2297 2010-06-16 00:13:14Z ponderator $
 */
public class SimpleDataSource implements DataSource {

    protected final String driverClass;

    protected final String connectionURL;

    protected String defaultUsername;

    protected IPassword defaultPassword;

    protected int loginTimeout;

    protected PrintWriter logWriter = new PrintWriter(System.out, true);

    protected List<Connection> connections = new ArrayList<Connection>();

    public SimpleDataSource(IDataSourceDescriptor dsd, IPassword password) {
        this(dsd.getDriverClassName(), dsd.getConnectionURL(), dsd.getUsername(), password);
    }

    public SimpleDataSource(String driverClass, String connectionURL) {
        this(driverClass, connectionURL, null, null);
    }

    public SimpleDataSource(String driverClass, String connectionURL, String defaultUsername, IPassword defaultPassword) {
        this.driverClass = driverClass;
        this.connectionURL = connectionURL;
        this.defaultUsername = defaultUsername;
        this.defaultPassword = defaultPassword;
    }

    public Connection getConnection() throws SQLException {
        return this.createConnection(this.defaultUsername, this.defaultPassword);
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return this.getConnection(username, new UnencryptedPassword(password));
    }

    public Connection getConnection(String username, IPassword password) throws SQLException {
        return this.createConnection(username, password);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public int getLoginTimeout() throws SQLException {
        return this.loginTimeout;
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        this.loginTimeout = seconds;
    }

    public PrintWriter getLogWriter() throws SQLException {
        return this.logWriter;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        this.logWriter = out;
    }

    /**
	 * @return The default username specified for this data source, or null if not specified
	 */
    public String getDefaultUsername() {
        return defaultUsername;
    }

    public void setDefaultUsername(String defaultUsername) {
        this.defaultUsername = defaultUsername;
    }

    /**
	 * @return The default password specified for this data source, or null if not specified
	 */
    public IPassword getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(IPassword defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    /**
	 * @return The driver class for this data source
	 */
    public String getDriverClass() {
        return driverClass;
    }

    /**
	 * @return The connection URL for this data source
	 */
    public String getConnectionURL() {
        return connectionURL;
    }

    /**
	 * This will cleanup any connections that were not closed.
	 */
    public void cleanup() {
        if (this.connections.size() > 0) this.logWriter.println("Cleaning up connections that were not closed: " + this.connections.size());
        for (Connection connection : this.connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                this.logWriter.println("Could not close connection");
                e.printStackTrace(this.logWriter);
            }
        }
    }

    /**
	 * @param username The username to use for the connection, or null if no username to be used
	 * @param password The password to use for the connection, only used if username is provided
	 * @return A new connection
	 * @throws SQLException
	 */
    protected Connection createConnection(String username, IPassword password) throws SQLException {
        try {
            Class.forName(this.driverClass);
        } catch (ClassNotFoundException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
        if (username != null) {
            return new MonitoredConnection(this, DriverManager.getConnection(this.connectionURL, username, password == null ? null : new String(password.getPassword())));
        } else {
            return new MonitoredConnection(this, DriverManager.getConnection(this.connectionURL));
        }
    }
}
