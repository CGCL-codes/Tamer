package org.moonwave.dconfig.dao.springfw;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.model.ConnectionInfo;
import org.moonwave.dconfig.util.DataSourceUtil;
import org.moonwave.dconfig.util.PropertiesReader;
import org.moonwave.dconfig.util.AppState;
import org.moonwave.dconfig.dao.LibProperties;
import org.moonwave.jdbc.datasource.DriverDataSource;

/**
 * Unified datasource manager for library, editor, and demo.
 *
 * @author Jonathan Luo
 */
public class DataSourceManager {

    private static final Log log = LogFactory.getLog(DataSourceManager.class);

    private static ConnectionInfo connectionInfo;

    private static Properties connProperties;

    /**
     * Creates a new <code>DataSource</code> from config file 
     * conf/dconfig_connection.properties or from <code>ConnectionInfo</code>
     * specified through editor ui.
     */
    public static DataSource getDataSource() {
        DataSource datasource = null;
        if (connectionInfo == null) {
            try {
                DriverManagerDataSourceEx ds = new DriverManagerDataSourceEx();
                Properties properties = getConnectionProperties();
                if (properties != null) {
                    String dataSourceName = LibProperties.getInstance().getProperty("connection.datasource");
                    if (dataSourceName != null) {
                        datasource = DataSourceUtil.getDatasource(dataSourceName);
                    } else {
                        String dsPrefix = "";
                        if (AppState.isLibMode()) {
                            dsPrefix = LibProperties.getInstance().getProperty("active.connection.prefix");
                        } else {
                            if (AppState.isDemoDerby()) dsPrefix = "derby.demo"; else if (AppState.isDemoHsqldb()) dsPrefix = "hsqldb.demo"; else if (AppState.isDemoH2()) dsPrefix = "h2.demo";
                        }
                        ds.setDriverClassName(properties.getProperty(dsPrefix + ".driverClassName"));
                        ds.setUrl(properties.getProperty(dsPrefix + ".url"));
                        ds.setUsername(properties.getProperty(dsPrefix + ".username"));
                        ds.setPassword(properties.getProperty(dsPrefix + ".password"));
                        datasource = ds;
                    }
                }
            } catch (Exception e) {
                log.error(e);
            }
        } else {
            DriverDataSource ds = new DriverDataSource(connectionInfo);
            datasource = ds;
        }
        return datasource;
    }

    protected static Properties getConnectionProperties() {
        if (connProperties == null) {
            String filename = null;
            int i = 0;
            while (true) {
                if (AppState.isLibMode()) {
                    filename = "dconfig_lib.properties";
                    if (i == 1) filename = "conf/dconfig_lib.properties";
                } else filename = "conf/dconfig_connection.properties";
                connProperties = PropertiesReader.getProperties(filename);
                if (connProperties != null) {
                    if (AppState.isVerbose()) System.out.println("load " + filename + " succeeded");
                    log.info("load " + filename + " succeeded");
                    break;
                } else {
                    if (AppState.isVerbose()) System.err.println("load " + filename + " failed");
                    log.error("load " + filename + " failed");
                }
                i++;
                if (i > 1) break;
            }
            if (log.isInfoEnabled() && AppState.isLibMode()) {
                String dsPrefix = LibProperties.getInstance().getProperty("active.connection.prefix");
            }
        }
        return connProperties;
    }

    /**
     * Gets current <code>ConnectionInfo</code>.
     */
    public static ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    /**
     * Sets <code>ConnectionInfo</code> for <code>DataSource</code>.
     */
    public static void setConnectionInfo(ConnectionInfo ci) {
        connectionInfo = ci;
    }
}
