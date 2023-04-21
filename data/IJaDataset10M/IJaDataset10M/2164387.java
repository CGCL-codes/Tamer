package com.telstra.dynamicrva.lock.database.spi;

import com.telstra.ess.*;
import com.telstra.ess.logging.*;
import com.telstra.ess.alarming.*;
import com.telstra.ess.configuration.*;
import java.sql.*;

/**
 *
 * @author c957258
 */
public class LockDatabaseConnectionManager {

    private static final String CONNECTION_FACTORY_CLASSNAME_PARAM_NAME = "dynamicrvaupload.lock.database.connectionfactory.classname";

    private static final String DEFAULT_CONNECTION_FACTORY_CLASSNAME = "com.telstra.dynamicrva.lock.database.spi.jndi.JndiLockDatabaseConnectionFactory";

    private EssComponent mgrComponent = new EssServiceComponent("LockDatabaseConnectionManager");

    private EssLogger logger = null;

    private ConfigurationManager confMgr = null;

    /** Creates a new instance of LockDatabaseConnectionManager */
    public LockDatabaseConnectionManager() {
        logger = LoggingManager.getEssLoggerInstance(mgrComponent);
    }

    public Connection getConnection() throws LockDatabaseConnectionManagerException {
        logger.debug("Retrieving database connection...");
        if (confMgr == null) {
            confMgr = ConfigurationManager.getInstance(mgrComponent);
        }
        LockDatabaseConnectionFactory factory = getConnectionFactory();
        if (factory != null) {
            try {
                return factory.getConnection();
            } catch (Exception e) {
                logger.error("Could not acquire connection", e);
                throw new LockDatabaseConnectionManagerException("Could not create connection", e);
            }
        } else {
            logger.error("Could not acquire factory!");
            throw new LockDatabaseConnectionManagerException("Could not create connection factory");
        }
    }

    private LockDatabaseConnectionFactory getConnectionFactory() {
        LockDatabaseConnectionFactory connFactory = null;
        if (confMgr == null) {
            confMgr = ConfigurationManager.getInstance(mgrComponent);
        }
        try {
            String connFactoryClassName = confMgr.getConfigurationItem(CONNECTION_FACTORY_CLASSNAME_PARAM_NAME, null);
            if (connFactoryClassName != null) {
                try {
                    Object obj = Class.forName(connFactoryClassName).newInstance();
                    if (obj instanceof LockDatabaseConnectionFactory) {
                        connFactory = (LockDatabaseConnectionFactory) obj;
                    } else {
                        connFactory = getDefaultConnectionFactory();
                    }
                } catch (Exception e) {
                    logger.warn("Could not create specified connection factory: " + connFactoryClassName, e);
                    connFactory = getDefaultConnectionFactory();
                }
            } else {
                logger.warn("Lock database connection factory not specified, using default");
                connFactory = getDefaultConnectionFactory();
            }
        } catch (Throwable t) {
            logger.error("Could not create connection factory", t);
            connFactory = getDefaultConnectionFactory();
        }
        return connFactory;
    }

    private LockDatabaseConnectionFactory getDefaultConnectionFactory() {
        try {
            logger.debug("Creating default connection factory: " + DEFAULT_CONNECTION_FACTORY_CLASSNAME);
            Object obj = Class.forName(DEFAULT_CONNECTION_FACTORY_CLASSNAME).newInstance();
            if (obj instanceof LockDatabaseConnectionFactory) {
                return (LockDatabaseConnectionFactory) obj;
            } else {
                logger.error("Default connection factory: " + DEFAULT_CONNECTION_FACTORY_CLASSNAME + " is not a valid lock database connection factory!");
            }
        } catch (Throwable t) {
            logger.error("Could not create default connection factory", t);
        }
        return null;
    }
}
