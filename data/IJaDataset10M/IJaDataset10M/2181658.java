package com.continuent.tungsten.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.continuent.tungsten.commons.config.TungstenProperties;

public class Config {

    private static Logger logger = Logger.getLogger(Config.class);

    private static final String APP_SHORT_NAME = "monitor";

    private static final String PROPERTY_HOST_NAME = "host.name";

    private static final String PROPERTY_HOST_PORT = "host.port";

    private static final String PROPERTY_DIRECT_CONNECTION_DRIVER = "direct.connection.driver";

    private static final String PROPERTY_DIRECT_CONNECTION_DRIVER_FILENAME = "direct.connection.driver.filename";

    private static final String PROPERTY_DO_BACKEND_PING = "check.backends.do.ping";

    private static final String PROPERTY_MAX_PING_THREADS = "check.backends.max.threads";

    private static final String PROPERTY_CHECKER_NICKNAME = "checker";

    private static final String PROPERTY_NOTIFIER_NICKNAME = "notifier";

    private static final String PROPERTY_CLUSTER_NAME = "cluster.name";

    private static final String PROPERTY_CLUSTER_MEMBER = "cluster.member";

    private static final String DEFAULT_LOG_CONF_FILENAME = "log4j.properties";

    private static final String DEFAULT_HOST_NAME = "127.0.0.1";

    private static final String DEFAULT_HOST_PORT = "1090";

    private static final String DEFAULT_JDBC_DRIVER = "org.postgresql.Driver";

    private static final String DEFAULT_JDBC_DRIVER_FILENAME = "postgresql-8.2-508.jdbc3.jar";

    private static final String DEFAULT_DO_BACKEND_PING = "false";

    private static final String DEFAULT_MAX_PING_THREADS = "0";

    private static final String DEFAULT_CLUSTER_NAME = "default";

    private String clusterName;

    private String clusterMemberName;

    private String hostName;

    private int hostPort;

    private Driver driver;

    private int checkFrequencyMs;

    private boolean doBackendPing;

    private int maxPingThreads;

    private String checkerClassName;

    private String notifierClassName;

    private TungstenProperties props = new TungstenProperties();

    public Config(String confDir, boolean initLog4j) {
        if (initLog4j) {
            String log4jPropertiesFile = System.getProperty(APP_SHORT_NAME + ".logger", confDir + File.separator + DEFAULT_LOG_CONF_FILENAME);
            PropertyConfigurator.configure(log4jPropertiesFile);
        }
        if (confDir == null) {
            logger.error("Usage: " + APP_SHORT_NAME + " <config-dir>");
            System.exit(-1);
        }
        String configFileName = String.format("%s%smonitor.properties", confDir, File.separator);
        File configFile = new File(configFileName);
        logger.debug("Starting " + APP_SHORT_NAME);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(configFile);
        } catch (FileNotFoundException e) {
            logger.fatal("Cannot find configuration file " + configFile + " - Exiting", e);
            System.exit(1);
        }
        try {
            props.load(fis);
        } catch (IOException e) {
            logger.fatal("File " + configFile + " is corrupt - exiting", e);
            System.exit(2);
        }
        hostName = props.getString(PROPERTY_HOST_NAME, DEFAULT_HOST_NAME, true);
        hostPort = props.getInt(PROPERTY_HOST_PORT, DEFAULT_HOST_PORT, true);
        clusterName = props.getString(PROPERTY_CLUSTER_NAME, DEFAULT_CLUSTER_NAME, true);
        clusterMemberName = props.getString(PROPERTY_CLUSTER_MEMBER, hostName, true);
        String jdbcDriverFilename = props.getString(PROPERTY_DIRECT_CONNECTION_DRIVER_FILENAME, DEFAULT_JDBC_DRIVER_FILENAME, true).trim();
        String jdbcDriverClassname = props.getString(PROPERTY_DIRECT_CONNECTION_DRIVER, DEFAULT_JDBC_DRIVER, true).trim();
        File f = new File(jdbcDriverFilename);
        URL url = null;
        try {
            url = f.toURI().toURL();
        } catch (MalformedURLException e) {
            logger.fatal("Error loading " + jdbcDriverFilename + " - Exiting", e);
            System.exit(3);
        }
        ClassLoader loader = new URLClassLoader(new URL[] { url });
        try {
            driver = (Driver) Class.forName(jdbcDriverClassname, true, loader).newInstance();
        } catch (ClassNotFoundException e) {
            logger.fatal("Cannot load driver " + jdbcDriverClassname + " - Exiting", e);
            System.exit(4);
        } catch (InstantiationException e2) {
            logger.fatal("Cannot load driver " + jdbcDriverClassname + " - Exiting", e2);
            System.exit(5);
        } catch (IllegalAccessException e3) {
            logger.fatal("Cannot load driver " + jdbcDriverClassname + " - Exiting", e3);
            System.exit(6);
        }
        doBackendPing = props.getBoolean(PROPERTY_DO_BACKEND_PING, DEFAULT_DO_BACKEND_PING, true);
        maxPingThreads = props.getInt(PROPERTY_MAX_PING_THREADS, DEFAULT_MAX_PING_THREADS, true);
        notifierClassName = props.getString(PROPERTY_NOTIFIER_NICKNAME + '.' + props.getString(PROPERTY_NOTIFIER_NICKNAME), null, true);
    }

    public int getHostPort() {
        return hostPort;
    }

    public String getHostName() {
        return hostName;
    }

    public Driver getDriver() {
        return driver;
    }

    public int getCheckFrequencyMs() {
        return checkFrequencyMs;
    }

    public boolean doBackendPing() {
        return doBackendPing;
    }

    public int getMaxPingThreads() {
        return maxPingThreads;
    }

    String getCheckerClassName() {
        return checkerClassName;
    }

    String getNotifierClassName() {
        return notifierClassName;
    }

    public TungstenProperties getCheckerConfig() {
        return props.subset(PROPERTY_CHECKER_NICKNAME + '.' + props.getString(PROPERTY_CHECKER_NICKNAME) + '.', true);
    }

    public TungstenProperties getNotifierConfig() {
        return props.subset(PROPERTY_NOTIFIER_NICKNAME + '.' + props.getString(PROPERTY_NOTIFIER_NICKNAME) + '.', true);
    }

    public String getClusterName() {
        return clusterName;
    }

    public String getClusterMemberName() {
        return clusterMemberName;
    }
}
