package com.volantis.mcs.runtime.configuration;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryFactory;
import com.volantis.synergetics.log.LogDispatcher;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * This is an implementation of the DataSourceFactory which
 * provides the ability to create a JDBC specific datasource from the JDBC
 * specific configuration.
 */
public class AnonymousDataSourceFactory {

    /**
     *  Volantis copyright mark.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(AnonymousDataSourceFactory.class);

    private JDBCDataSourceFactory jdbcDataSourceFactory = new JDBCDataSourceFactory();

    private MCSDataSourceFactory mcsDataSourceFactory = new MCSDataSourceFactory();

    private JNDIDataSourceFactory jndiDataSourceFactory = new JNDIDataSourceFactory();

    /**
     * Create the factory. 
     */
    public AnonymousDataSourceFactory() {
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
    public DataSource createDataSource(AnonymousDataSourceConfiguration config, JNDIConfiguration jndiConfiguration) {
        JDBCRepositoryFactory factory = JDBCRepositoryFactory.getDefaultInstance();
        if (logger.isDebugEnabled()) {
            logger.debug("Creating Anonymous DataSource");
        }
        DataSource dataSource = null;
        if (config != null) {
            DataSource delegateDataSource = null;
            AnonymousDataSource anonymousDataSource = config.getDataSourceConfiguration();
            if (anonymousDataSource instanceof JDBCDriverConfiguration) {
                delegateDataSource = jdbcDataSourceFactory.createDataSource((JDBCDriverConfiguration) anonymousDataSource);
            } else if (anonymousDataSource instanceof MCSDatabaseConfiguration) {
                delegateDataSource = mcsDataSourceFactory.createDataSource((MCSDatabaseConfiguration) anonymousDataSource);
            } else if (anonymousDataSource instanceof JNDIDataSourceConfiguration) {
                delegateDataSource = jndiDataSourceFactory.createDataSource((JNDIDataSourceConfiguration) anonymousDataSource, jndiConfiguration);
            }
            if (delegateDataSource != null) {
                dataSource = factory.createAnonymousDataSource(delegateDataSource, config.getUser(), config.getPassword());
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Cannot create a datasource with a 'null' " + "configuration");
            }
        }
        return dataSource;
    }
}
