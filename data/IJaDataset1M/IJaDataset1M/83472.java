package com.eis.ds.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Description: This class is a global registry to one-time-load all the critical configuration in resources.
 * 
 * @author zhong wen qing
 * @version 1.0
 */
public class CoreRegistry implements CoreConstants {

    private static CoreRegistry coreReg = null;

    /**
     * core keeps content of core.properties
     */
    private static Properties core = null;

    /**
     * masterCommandReg keeps content of commandregistry.properties
     */
    private static Properties masterCommandReg = null;

    /**
     * jdbcEngineReg keeps content of jdbcengine.properties
     */
    private static Properties jdbcEngineReg = null;

    /**
     * objectPersistenceEngine keeps content of XXXengine.properties, it is according to the value of
     * object_persistence_engine in core configuration
     */
    private static Properties objectPersistenceEngine = null;

    /**
     * applicationServerConfig keeps content of xxx.properties, it is according to the application_server_config in core
     * configuration.
     */
    private static Properties applicationServerConfig = null;

    /**
     * Description: Constructor, Load all the resources on business tier.
     * 
     * @author zhong wen qing
     * @param coreConfigFile
     *            entry properties file (core.properties)
     */
    private CoreRegistry(String coreConfigFile) {
        core = getProperties(coreConfigFile);
        masterCommandReg = getProperties(core.getProperty(CoreConstants.KEY_MASTER_COMMAND_REGISTRY));
        jdbcEngineReg = getProperties(core.getProperty(CoreConstants.KEY_JDBC_DATA_ENGINE));
        objectPersistenceEngine = getProperties(core.getProperty(KEY_OBJECT_PERSISTENCE_ENGINE));
        applicationServerConfig = getProperties(core.getProperty(KEY_APPLICATION_SERVER_CONFIG));
    }

    public static CoreRegistry getInstantce() {
        if (coreReg == null) {
            coreReg = new CoreRegistry(CORE_CONFIG_FILE);
        }
        return coreReg;
    }

    public static synchronized CoreRegistry getInstantce(String coreConfigFile) {
        if (coreReg == null) {
            coreReg = new CoreRegistry(coreConfigFile);
        }
        return coreReg;
    }

    /**
     * Description: This method gets content of core.properties.
     * 
     * @author zhong wen qing
     * @return Properties
     */
    public Properties getCore() {
        return core;
    }

    /**
     * Description: This method gets content of commandregistry.properties.
     * 
     * @author zhong wen qing
     * @return Properties
     */
    public Properties getMasterCommandReg() {
        return masterCommandReg;
    }

    /**
     * Description: This method gets content of jdbcengine.properties.
     * 
     * @author zhong wen qing
     * @return Properties
     */
    public Properties getJdbcEngineReg() {
        return jdbcEngineReg;
    }

    /**
     * Description: This method gets content of XXXengine.properties according to the value of object_persistence_engine
     * in core.properties.
     * 
     * @author zhong wen qing
     * @return Properties
     */
    public Properties getObjectPersistenceEngine() {
        return objectPersistenceEngine;
    }

    /**
     * Description: This method gets content of xxx.properties, according to the application_server_config in
     * core.properties.
     * 
     * @author zhong wen qing
     * @return Properties
     */
    public Properties getApplicationServerConfig() {
        return applicationServerConfig;
    }

    private Properties getProperties(String fileName) {
        Properties p = null;
        InputStream inputStream = null;
        try {
            inputStream = CoreRegistry.class.getResourceAsStream("/" + fileName);
            if (inputStream == null) {
                System.out.println("Property file [" + fileName + "] is not found");
            } else {
                p = new Properties();
                p.load(inputStream);
                System.out.println("Have loaded core property file [ " + fileName + ":" + core.size() + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable t) {
        } finally {
            try {
                inputStream.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return p;
    }
}
