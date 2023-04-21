package com.avaje.ebean.server.core;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import com.avaje.ebean.server.lib.ConfigProperties;

/**
 * DataSource retrieved from JNDI lookup.
 *
 */
public class JndiDataSourceFactory implements DataSourceFactory {

    private static final String DEFAULT_PREFIX = "java:comp/env/jdbc/";

    public JndiDataSourceFactory() {
    }

    private String getJndiPrefix(ConfigProperties config) {
        return config.getProperty("ebean.datasource.jndi.prefix", DEFAULT_PREFIX);
    }

    /**
	 * Return the DataSource by JNDI lookup.
	 * <p>
	 * If name is null the 'default' dataSource is returned.
	 * </p>
	 */
    public DataSource createDataSource(String name, ConfigProperties config) {
        if (name == null) {
            String dflt = config.getProperty("ebean.datasource.default");
            name = config.getProperty("datasource.default", dflt);
            if (name == null) {
                String msg = "datasource.default has not be defined in system.properties";
                throw new PersistenceException(msg);
            }
        }
        try {
            Context ctx = new InitialContext();
            String lookupName = getJndiPrefix(config) + name;
            DataSource ds = (DataSource) ctx.lookup(lookupName);
            if (ds == null) {
                throw new PersistenceException("JNDI DataSource [" + lookupName + "] not found?");
            }
            return ds;
        } catch (NamingException ex) {
            throw new PersistenceException(ex);
        }
    }
}
