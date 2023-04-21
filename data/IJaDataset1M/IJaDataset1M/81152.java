package com.volantis.mcs.runtime.configuration;

import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.JDBCRepository;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * This is an implementation of the DataSourceFactory which
 * provides the ability to create a JDBC specific datasource from the JDBC
 * specific configuration.
 */
public class ConnectionPoolFactory {

    /**
     *  Volantis copyright mark.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(ConnectionPoolFactory.class);

    private JDBCDataSourceFactory jdbcDataSourceFactory = new JDBCDataSourceFactory();

    private MCSDataSourceFactory mcsDataSourceFactory = new MCSDataSourceFactory();

    private JNDIDataSourceFactory jndiDataSourceFactory = new JNDIDataSourceFactory();

    private AnonymousDataSourceFactory anonymousDataSourceFactory = new AnonymousDataSourceFactory();

    /**
     * Create the factory. 
     */
    public ConnectionPoolFactory() {
    }

    /**
     * Create a properties map that may be used by the creation of the
     * datasource.
     *
     * @param  config the JDBCDriverConfiguration object
     * @return        a map of properties where the key is defined in
     *                JDBCRepository constants.
     */
    private Map createPropertiesMap(AnonymousDataSource config) {
        Map properties = new HashMap();
        return properties;
    }

    /**
     * Create the DataSource from the AnonymouseDataSource object
     *
     * @param config            the NamedDataSource object.
     */
    public DataSource createDataSource(ConnectionPoolConfiguration config, JNDIConfiguration jndiConfiguration) {
        DataSource dataSource = null;
        if (config != null) {
            DataSource delegateDataSource = null;
            AnonymousDataSource connectionPoolDataSource = config.getDataSourceConfiguration();
            if (connectionPoolDataSource instanceof JDBCDriverConfiguration) {
                delegateDataSource = jdbcDataSourceFactory.createDataSource((JDBCDriverConfiguration) connectionPoolDataSource);
            } else if (connectionPoolDataSource instanceof MCSDatabaseConfiguration) {
                delegateDataSource = mcsDataSourceFactory.createDataSource((MCSDatabaseConfiguration) connectionPoolDataSource);
            } else if (connectionPoolDataSource instanceof JNDIDataSourceConfiguration) {
                delegateDataSource = jndiDataSourceFactory.createDataSource((JNDIDataSourceConfiguration) connectionPoolDataSource, jndiConfiguration);
            } else if (connectionPoolDataSource instanceof AnonymousDataSourceConfiguration) {
                delegateDataSource = anonymousDataSourceFactory.createDataSource((AnonymousDataSourceConfiguration) connectionPoolDataSource, jndiConfiguration);
            }
            if (delegateDataSource != null) {
                Map connectionPoolProperties = new HashMap();
                connectionPoolProperties.put(JDBCRepository.POOL_ENABLED_PROPERTY, "true");
                connectionPoolProperties.put(JDBCRepository.POOL_MAX_CONNECTIONS_PROPERTY, config.getMaximum());
                connectionPoolProperties.put(JDBCRepository.CONNECTION_POLL_INTERVAL_PROPERTY, config.getPollInterval());
                connectionPoolProperties.put(JDBCRepository.KEEP_CONNECTIONS_ALIVE_PROPERTY, config.getKeepAlive());
                try {
                    dataSource = JDBCRepository.createConnectionPool(connectionPoolProperties, delegateDataSource);
                } catch (RepositoryException re) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Error creating connection pool", re);
                    }
                }
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Cannot create a datasource with a 'null' " + "configuration");
            }
        }
        return dataSource;
    }
}
