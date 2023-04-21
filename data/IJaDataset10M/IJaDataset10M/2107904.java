package com.hitao.codegen.configs.dao.mapping;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import com.hitao.codegen.configs.DaoServiceConfigurationManager;
import com.hitao.codegen.configs.systemconfig.SystemConfigManager;
import com.hitao.codegen.util.NumberUtils;

/***
 * Utility class for DataSource.
 *
 * @author zhangjun.ht
 * @created 2011-1-13
 * @version $Id: DataSourceManager.java 12 2011-02-20 10:50:23Z guest $
 */
public class DataSourceManager implements IDataSourceManager {

    private static final Logger logger_ = Logger.getLogger(SystemConfigManager.class);

    private static final String SYSTEM_DATASOURCE_MANAGER_PROPERTY = DataSourceManager.class.getName();

    private static IDataSourceManager INSTANCE_;

    private DataSource datasource_ = null;

    public static final String CONNECTION_URL = "connection-url";

    public static final String DRIVER_CLASS = "driver-class";

    public static final String USER_NAME = "user-name";

    public static final String PASSWORD = "password";

    public static final String MIN_POOL_SIZE = "min-pool-size";

    public static final String MAX_POOL_SIZE = "max-pool-size";

    public static final String METADATA = "metadata";

    private boolean isBad_ = false;

    private String meta_ = null;

    static {
        String value = System.getProperty(SYSTEM_DATASOURCE_MANAGER_PROPERTY);
        try {
            INSTANCE_ = (DataSourceManager) Class.forName(value).newInstance();
        } catch (Exception e) {
            INSTANCE_ = new DataSourceManager();
        }
    }

    private DataSourceManager() {
        BasicDataSource baseDataSource = new BasicDataSource();
        baseDataSource.setUrl(DaoServiceConfigurationManager.getProperty(CONNECTION_URL));
        baseDataSource.setDriverClassName(DaoServiceConfigurationManager.getProperty(DRIVER_CLASS));
        baseDataSource.setUsername(DaoServiceConfigurationManager.getProperty(USER_NAME));
        baseDataSource.setPassword(DaoServiceConfigurationManager.getProperty(PASSWORD));
        String maxPoolSize = DaoServiceConfigurationManager.getProperty(MAX_POOL_SIZE);
        String minPoolSize = DaoServiceConfigurationManager.getProperty(MIN_POOL_SIZE);
        baseDataSource.setMaxActive(NumberUtils.stringToInt(maxPoolSize, 10));
        baseDataSource.setInitialSize(NumberUtils.stringToInt(minPoolSize, 0));
        meta_ = DaoServiceConfigurationManager.getProperty(METADATA);
        datasource_ = baseDataSource;
        getConnection();
    }

    public static IDataSourceManager getInstance() {
        return INSTANCE_;
    }

    public Connection getConnection() {
        if (isBad_) {
            return null;
        }
        try {
            return datasource_.getConnection();
        } catch (SQLException e) {
            isBad_ = true;
            logger_.warn("The datasource " + meta_ + " is wrong. Please check it.", e);
        }
        return null;
    }
}
