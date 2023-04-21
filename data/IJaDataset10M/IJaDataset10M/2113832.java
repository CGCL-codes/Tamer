package jaxlib.sql.datasource;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import jaxlib.util.CheckArg;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: DriverDataSource.java 2271 2007-03-16 08:48:23Z joerg_wassmer $
 */
public class DriverDataSource extends AbstractDataSource implements DataSource, Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    private final Driver driver;

    private final Properties properties;

    private final String url;

    public DriverDataSource(final Driver driver, final String url, final Map<?, ?> properties) {
        super();
        CheckArg.notNull(driver, "driver");
        CheckArg.notBlank(url, "url");
        this.driver = driver;
        this.properties = new Properties();
        this.url = url;
        if (properties != null) this.properties.putAll(properties);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.driver.connect(this.url, this.properties);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Properties properties = this.properties;
        if ((username != null) && (username.length() > 0) && !username.equals(properties.get("user"))) {
            properties = (Properties) properties.clone();
            properties.put("user", username);
        }
        if ((password != null) && (password.length() > 0) && !password.equals(properties.get("password"))) {
            if (properties == this.properties) properties = (Properties) properties.clone();
            properties.put("password", password);
        }
        return this.driver.connect(this.url, properties);
    }

    public Driver getDriver() {
        return this.driver;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    public Properties getProperties() {
        return (Properties) this.properties.clone();
    }

    public String getUrl() {
        return this.url;
    }

    @Override
    public void setLoginTimeout(final int seconds) throws SQLException {
    }

    @Override
    public void setLogWriter(final PrintWriter out) throws SQLException {
    }
}
