package de.ibis.permoto.loganalyzer.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.prefs.Preferences;
import javax.sql.DataSource;
import oracle.jdbc.pool.OracleDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import de.ibis.permoto.loganalyzer.exception.JdbcNoConnection;

/**
 * JDBC DataSource Factory Manager
 * @author Andreas Schamberger
 */
public class JdbcDataSourceFactory {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(JdbcDataSourceFactory.class);

    /** db preferences */
    private static Preferences prefs = null;

    /** HashMapt that holds the references to the DataSources */
    private static HashMap<String, DataSource> poolingDataSource = new HashMap<String, DataSource>();

    /**
	 * return the database URL for the given alias
	 * @param name
	 * @return the connection URL
	 */
    public static String getConnectionUrl(String name) {
        if (prefs == null) {
            prefs = Preferences.userRoot().node("/de/ibis/permoto/loganalyzer/db/JdbcDataSource");
        }
        return prefs.node("databases").get(prefs.get(name, "local"), "");
    }

    /**
	 * get configured driver name
	 * @return driver name
	 */
    public static String getDriverName() {
        if (prefs == null) {
            prefs = Preferences.userRoot().node("/de/ibis/permoto/loganalyzer/db/JdbcDataSource");
        }
        return prefs.get("driver", "oracle.jdbc.OracleDriver");
    }

    /**
	 * reset datasource in order to address changed config
	 */
    public static void resetJdbcDataSource() {
        poolingDataSource = new HashMap<String, DataSource>();
    }

    /**
	 * get a DataSource for DB connectivity
	 * @param jdbcConnectionURL
	 * @param pooling
	 * @return DataSource object
	 * @throws JdbcNoConnection
	 */
    public static DataSource setupDataSource(String jdbcConnectionURL, boolean pooling) throws JdbcNoConnection {
        return setupDataSource(jdbcConnectionURL, pooling, false);
    }

    /**
	 * get a DataSource for DB connectivity
	 * @param jdbcConnectionURL
	 * @param pooling
	 * @param getUrlFromConfig
	 * @return DataSource object
	 * @throws JdbcNoConnection
	 */
    public static DataSource setupDataSource(String jdbcConnectionURL, boolean pooling, boolean getUrlFromConfig) throws JdbcNoConnection {
        String driver = null;
        if (getUrlFromConfig == true) {
            jdbcConnectionURL = getConnectionUrl(jdbcConnectionURL);
            driver = getDriverName();
        }
        if (pooling && poolingDataSource.containsKey(jdbcConnectionURL)) {
            return poolingDataSource.get(jdbcConnectionURL);
        }
        DataSource ds = setupDataSource(jdbcConnectionURL, driver, pooling);
        if (pooling && !driver.equals("org.sqlite.JDBC")) {
            poolingDataSource.put(jdbcConnectionURL, ds);
        }
        return ds;
    }

    /**
	 * setup a DataSource for DB connectivity
	 * @param jdbcConnectionURL
	 * @param driver
	 * @param pooling
	 * @return DataSource
	 * @throws JdbcNoConnection
	 */
    private static DataSource setupDataSource(String jdbcConnectionURL, String driver, boolean pooling) throws JdbcNoConnection {
        if (jdbcConnectionURL.equals("")) {
            throw new JdbcNoConnection("empty JDBC connection url");
        }
        if (jdbcConnectionURL.startsWith("jdbc:mysql:")) {
            jdbcConnectionURL = jdbcConnectionURL.replace("password", "&password");
        }
        driver = loadDriver(driver, jdbcConnectionURL);
        DataSource ds = null;
        if (pooling) {
            if (logger.isDebugEnabled()) {
                logger.debug("setupDataSource() - connection pooling is enabled ...");
            }
            if (driver.equals("org.sqlite.JDBC")) {
                ds = new SqliteDataSource();
                ((SqliteDataSource) ds).setURL(jdbcConnectionURL);
            } else {
                ObjectPool connectionPool = new GenericObjectPool(null);
                ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(jdbcConnectionURL, null);
                @SuppressWarnings("unused") PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
                ds = new PoolingDataSource(connectionPool);
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("setupDataSource() - connection pooling is NOT enabled ...");
            }
            if (driver.equals("oracle.jdbc.OracleDriver") || driver.equals("oracle.jdbc.driver.OracleDriver")) {
                try {
                    ds = new OracleDataSource();
                    ((OracleDataSource) ds).setURL(jdbcConnectionURL);
                } catch (SQLException e) {
                    throw new JdbcNoConnection("can't create datasource", e);
                }
            } else if (driver.equals("com.mysql.jdbc.Driver")) {
                ds = new MysqlDataSource();
                ((MysqlDataSource) ds).setURL(jdbcConnectionURL);
            } else if (driver.equals("org.sqlite.JDBC")) {
                ds = new SqliteDataSource();
                ((SqliteDataSource) ds).setURL(jdbcConnectionURL);
            } else if (driver.equals("org.apache.derby.jdbc.ClientDataSource")) {
                jdbcConnectionURL = jdbcConnectionURL.replace("jdbc:derby:", "");
                String path = "";
                ds = new EmbeddedDataSource();
                if (jdbcConnectionURL.contains(";")) {
                    int semikolon = jdbcConnectionURL.indexOf(";");
                    path = jdbcConnectionURL.substring(0, semikolon - 1);
                    String attribute = jdbcConnectionURL.substring(semikolon + 1);
                    ((EmbeddedDataSource) ds).setConnectionAttributes(attribute);
                } else {
                    path = jdbcConnectionURL;
                }
                ((EmbeddedDataSource) ds).setDatabaseName(path);
            } else {
                throw new JdbcNoConnection("no known driver, can't create datasource");
            }
        }
        return ds;
    }

    /**
	 * Load given JDBC driver or guess from jdbcConnectionURL.
	 * @param driver
	 * @param guess
	 * @return loaded driver
	 */
    private static String loadDriver(String driver, String jdbcConnectionURL) throws JdbcNoConnection {
        if (jdbcConnectionURL.substring(0, 12).equals("jdbc:oracle:")) {
            driver = "oracle.jdbc.OracleDriver";
        }
        if (jdbcConnectionURL.substring(0, 11).equals("jdbc:mysql:")) {
            driver = "com.mysql.jdbc.Driver";
        }
        if (jdbcConnectionURL.substring(0, 11).equals("jdbc:derby:")) {
            driver = "org.apache.derby.jdbc.ClientDataSource";
        }
        if (jdbcConnectionURL.substring(0, 12).equals("jdbc:sqlite:")) {
            driver = "org.sqlite.JDBC";
        }
        if (driver == null) {
            driver = "oracle.jdbc.OracleDriver";
        }
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new JdbcNoConnection("JDBC driver not found", e);
        }
        return driver;
    }
}
