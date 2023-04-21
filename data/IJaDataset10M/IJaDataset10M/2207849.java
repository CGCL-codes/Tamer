package org.mavenit.dbit;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.maven.plugin.AbstractMojo;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.ForwardOnlyResultSetTableFactory;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.datatype.IDataTypeFactory;

/**
 * The base class for all mojos
 */
public abstract class DbitBaseMojo extends AbstractMojo {

    /**
     * @parameter
     */
    private String username;

    /**
     * @parameter
     */
    private String password;

    /**
     * @parameter
     */
    private String url;

    /**
     * @parameter
     */
    private String driver;

    /**
     * @parameter
     */
    private String schema;

    /**
     * Creates a new Connection as using the driver, url, userid and password
     * specified.
     *
     * The calling method is responsible for closing the connection.
     *
     * @return Connection the newly created connection.
     * @throws RuntimeException if the UserId/Password/Url is not set or there
     * is no suitable driver or the driver fails to load.
     */
    protected Connection getConnection() {
        if (username == null) {
            throw new RuntimeException("username attribute must be set!");
        }
        if (password == null) {
            throw new RuntimeException("Password attribute must be set!");
        }
        if (url == null) {
            throw new RuntimeException("Url attribute must be set!");
        }
        try {
            getLog().debug("connecting to " + url);
            Properties info = new Properties();
            info.put("user", username);
            info.put("password", password);
            Driver driverInstance = null;
            try {
                Class dc = Class.forName(driver);
                driverInstance = (Driver) dc.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Driver class not found: " + driver);
            } catch (Exception e) {
                throw new RuntimeException("Failure loading driver: " + driver);
            }
            Connection conn = driverInstance.connect(url, info);
            if (conn == null) {
                throw new SQLException("No suitable Driver for " + url);
            }
            conn.setAutoCommit(true);
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
