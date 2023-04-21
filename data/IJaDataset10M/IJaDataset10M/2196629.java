package com.diyfiesta.pokimon.dataaccess.datasource;

import javax.sql.DataSource;
import org.apache.commons.configuration.Configuration;
import com.diyfiesta.pokimon.tools.ConfigManager;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

/** {@link DataSourceManager} implmentation specific to mySQL.
 */
public class MySQLDataSourceManager implements DataSourceManager {

    /** The key in the configuration file that is used to lookup the value of the servername. */
    public static final String DATASOURCE_SERVER_KEY = "datasource.mysql.servername";

    /** The key in the configuration file that is used to lookup the value of the database. */
    public static final String DATASOURCE_DATABASE_KEY = "datasource.mysql.database";

    /** The key in the configuration file that is used to lookup the value of the user. */
    public static final String DATASOURCE_USER_KEY = "datasource.mysql.user";

    /** The key in the configuration file that is used to lookup the value of the password. */
    public static final String DATASOURCE_PASS_KEY = "datasource.mysql.password";

    /** The key in the configuration file that is used to lookup the value of the port. */
    public static final String DATASOURCE_PORT_KEY = "datasource.mysql.port";

    /**
	 * @see com.diyfiesta.pokimon.dataaccess.DataSourceFactory#getDataSource()
	 */
    public DataSource getDataSource() throws DataSourceManagerException {
        MysqlConnectionPoolDataSource ds = null;
        try {
            Configuration conf = ConfigManager.getConfig();
            ds = new MysqlConnectionPoolDataSource();
            ds.setServerName(conf.getString(DATASOURCE_SERVER_KEY));
            ds.setPort(Integer.parseInt(conf.getString(DATASOURCE_PORT_KEY)));
            ds.setDatabaseName(conf.getString(DATASOURCE_DATABASE_KEY));
            ds.setUser(conf.getString(DATASOURCE_USER_KEY));
            ds.setPassword(conf.getString(DATASOURCE_PASS_KEY));
        } catch (Exception e) {
            throw new DataSourceManagerException(e);
        }
        return ds;
    }
}
