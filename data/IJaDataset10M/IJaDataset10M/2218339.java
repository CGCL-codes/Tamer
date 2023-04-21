package org.xactor.tm;

import java.io.File;
import java.net.URL;

/**
 * The interface of the basic <em>typed</em> JBoss server configuration.
 *
 * <p>Clients should use {@link ServerConfigLocator} to get an instance of
 *    {@link ServerConfig} and then use it to get the server's configuration bits.
 *
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @author Scott.Stark@jboss.org 
 * @version $Revision: 61146 $
 */
public interface ServerConfig {

    /** The default partition name */
    String DEFAULT_PARITION_NAME = "DefaultPartition";

    /** The partition name property */
    String PARTITION_NAME_PROPERTY = "jboss.partition.name";

    /** The udp address property */
    String PARTITION_UDP_PROPERTY = "jboss.partition.udpGroup";

    /** The udp port property */
    String PARTITION_UDP_PORT_PROPERTY = "jgroups.udp.mcast_port";

    /** Whether to load native libraries */
    String NATIVE_LOAD_PROPERTY = "jboss.native.load";

    /** The location of native libraries property */
    String NATIVE_DIR_PROPERTY = "jboss.native.dir";

    /**
    * Constant that holds the name of the environment property
    * for specifying a comma seperated list of the basenames of
    * to the boot libraries required load the core system.
    *
    * <p>These libraries will be loaded from <tt>LIBRARY_URL</tt>.
    */
    String BOOT_LIBRARY_LIST = "jboss.boot.library.list";

    /**
    * Constant that holds the name of the environment property
    * for specifying the class type of the server to create.
    */
    String SERVER_TYPE = "jboss.server.type";

    /**
    * Constant that holds the name of the environment property
    * for specifying the root deployment filename (relative to the server
    * config URL that will be deployed to finalize the boot strap process.
    *
    * <p>If not set then the server will default to {@link #DEFAULT_ROOT_DEPLOYMENT_FILENAME}.
    */
    String ROOT_DEPLOYMENT_FILENAME = "jboss.server.root.deployment.filename";

    /**
    * Constant that holds the name of the environment property
    * for specifying the home directory for JBoss.
    */
    String HOME_DIR = "jboss.home.dir";

    /**
    * Constant that holds the name of the environment property
    * for specifying the home URL for JBoss.
    *
    * <p>If not set then the value of HOME_DIR will converted into a URL.
    */
    String HOME_URL = "jboss.home.url";

    /**
    * Constant that holds the name of the environment property
    * for specifying the URL where JBoss will read library files
    * from.
    *
    * <p>Defaults to <tt><em>HOME_URL</em>/lib</tt>/
    */
    String LIBRARY_URL = "jboss.lib.url";

    /**
    * Constant that holds the name of the environment property
    * for specifying the URL where JBoss will read patch library files
    * from.
    *
    * <p>If this value is a <tt>file</tt> URL, then all .zip and .jar files
    * inside will be prepended to the classpath.  Otherwise the URL will be
    * added to the classpath.  If not set then the no patch files will be
    * loaded.
    */
    String PATCH_URL = "jboss.patch.url";

    /**
    * Constant that holds the name of the environment property
    * for specifying the name of the server which will be used to
    * calculate the servers home directory and url.
    *
    * <p>Defaults to <tt>default</tt>.
    */
    String SERVER_NAME = "jboss.server.name";

    /**
    * Constant that holds the name of the environment property
    * for specifying the base directory for calculating server
    * home directories.
    *
    * <p>Defaults to <tt><em>HOME_DIR</em>/server</tt>.
    */
    String SERVER_BASE_DIR = "jboss.server.base.dir";

    /**
    * Constant that holds the name of the environment property
    * for specifying the server home directory for JBoss.
    *
    * <p>Defaults to <tt><em>SERVER_BASE_DIR</em>/<em>SERVER_NAME</em></tt>.
    */
    String SERVER_HOME_DIR = "jboss.server.home.dir";

    /**
    * Constant that holds the name of the environment property
    * for specifying the server log directory for JBoss.
    *
    * <p>Defaults to <tt><em>SERVER_HOME_DIR</em>/<em>log</em></tt>.
    */
    String SERVER_LOG_DIR = "jboss.server.log.dir";

    /**
    * Constant that holds the name of the environment property
    * for specifying the directory which JBoss will use for
    * temporary file storage.
    *
    * <p>Defaults to <tt><em>SERVER_HOME_DIR</em>/tmp</tt> .
    */
    String SERVER_TEMP_DIR = "jboss.server.temp.dir";

    /**
    * Constant that holds the name of the environment property
    * for specifying the directory which JBoss will use for
    * persistent data file storage.
    *
    * <p>Defaults to <tt><em>SERVER_HOME_DIR</em>/data</tt>.
    */
    String SERVER_DATA_DIR = "jboss.server.data.dir";

    /**
    * Constant that holds the name of the environment property
    * for specifying the base URL for calculating server
    * home URLs.
    *
    * <p>Defaults to <tt><em>HOME_URL</em>/server</tt>.
    */
    String SERVER_BASE_URL = "jboss.server.base.url";

    /**
    * Constant that holds the name of the environment property
    * for specifying the server home URL for JBoss.
    *
    * <p>Defaults to <tt><em>SERVER_BASE_URL</em>/<em>SERVER_NAME</em></tt>.
    */
    String SERVER_HOME_URL = "jboss.server.home.url";

    /**
    * Constant that holds the name of the environment property
    * for specifying the server configuration URL.
    *
    * <p>Defaults to <tt><em>SERVER_HOME_UTL</em>/conf</tt> .
    */
    String SERVER_CONFIG_URL = "jboss.server.config.url";

    /**
    * Constant that holds the name of the environment property
    * for specifying the URL where JBoss will read server specific
    * library files from.
    *
    * <p>Defaults to <tt><em>SERVER_HOME_URL</em>/lib</tt>/
    */
    String SERVER_LIBRARY_URL = "jboss.server.lib.url";

    /**
    * Constant that holds the name of the environment property
    * for specifying the bind address for all jboss services
    *
    */
    String SERVER_BIND_ADDRESS = "jboss.bind.address";

    /**
    * Constant that holds the name of the environment property
    * for specifying whether or not the server should exit the
    * JVM on shutdown.
    *
    * <p>If not set then the server will default to exiting on shutdown.
    */
    String EXIT_ON_SHUTDOWN = "jboss.server.exitonshutdown";

    /**
    * Constant that holds the name of the environment property for
    * specifying whether or not the server should shutdown
    * synchronously (true) or asynchronously (false).
    *
    * <p>If not set then the server will default to asynchronous shutdown.
    */
    String BLOCKING_SHUTDOWN = "jboss.server.blockingshutdown";

    /**
    * Constant that holds the name of the environment property for
    * specifying whether or not the server should log and ignore
    * exceptions when setting the URLStreamHandlerFactory.
    *
    * <p>If not set then the server will default to asynchronous shutdown.
    */
    String REQUIRE_JBOSS_URL_STREAM_HANDLER_FACTORY = "jboss.server.requirejbossurlstreamhandlerfactory";

    /**
    * Constant that holds the name of the environment property for specifying
    * whether or not to use as the main jboss server the MBeanServer returned
    * from ManagementFactory.getPlatformMBeanServer(), when running under jdk1.5+
    * 
    * <p>If not set then jboss will instantiate its own MBeanServer
    */
    String PLATFORM_MBEANSERVER = "jboss.platform.mbeanserver";

    /**
    * The suffix used when generating the default value for {@link #LIBRARY_URL}
    * and {@link #SERVER_LIBRARY_URL}.
    */
    String LIBRARY_URL_SUFFIX = "lib/";

    /**
    * The suffix used when generating the default value for {@link #SERVER_CONFIG_URL}.
    */
    String SERVER_CONFIG_URL_SUFFIX = "conf/";

    /**
    * The suffix used when generating the default value for {@link #SERVER_BASE_DIR}.
    */
    String SERVER_BASE_DIR_SUFFIX = "server";

    /**
    * The suffix used when generating the default value for {@link #SERVER_BASE_URL}.
    */
    String SERVER_BASE_URL_SUFFIX = "server/";

    /**
    * The suffix used when generating the default value for {@link #SERVER_DATA_DIR}.
    */
    String SERVER_DATA_DIR_SUFFIX = "data";

    /**
    * The suffix used when generating the default value for {@link #SERVER_LOG_DIR}.
    */
    String SERVER_LOG_DIR_SUFFIX = "log";

    /**
    * The suffix used when generating the default value for {@link #SERVER_TEMP_DIR}.
    */
    String SERVER_TEMP_DIR_SUFFIX = "tmp";

    /** The default value for {@link #SERVER_NAME}. */
    String DEFAULT_SERVER_NAME = "default";

    /** The default value for {@link #EXIT_ON_SHUTDOWN}. */
    boolean DEFAULT_EXIT_ON_SHUTDOWN = true;

    /** The default value for {@link #BLOCKING_SHUTDOWN}. */
    boolean DEFAULT_BLOCKING_SHUTDOWN = false;

    /** The default value for {@link #REQUIRE_JBOSS_URL_STREAM_HANDLER_FACTORY}. */
    boolean DEFAULT_REQUIRE_JBOSS_URL_STREAM_HANDLER_FACTORY = true;

    /** The default value for {@link #PLATFORM_MBEANSERVER}. */
    boolean DEFAULT_PLATFORM_MBEANSERVER = false;

    /** The default value for {@link ROOT_DEPLOYMENT_FILENAME}. */
    String DEFAULT_ROOT_DEPLOYMENT_FILENAME = "jboss-service.xml";

    /**
    * Get the local home directory which the server is running from.
    *
    * @return    The local server home directory.
    */
    File getHomeDir();

    /**
    * Get the home URL which the server is running from.
    *
    * @return    The home URL which the server is running from.
    */
    URL getHomeURL();

    /**
    * Get the library URL for the server.
    *
    * @return    The library URL for the server.
    */
    URL getLibraryURL();

    /**
    * Get the patch URL for the server.
    *
    * @return    The patch URL for the server.
    */
    URL getPatchURL();

    /**
    * Get the name of the server.
    *
    * @return    The name of the server.
    */
    String getServerName();

    /**
    * Get the base directory for calculating server home directories.
    *
    * @return    Base server home directory.
    */
    File getServerBaseDir();

    /**
    * Get the server home directory.
    *
    * @return    Server home directory.
    */
    File getServerHomeDir();

    /**
    * Get the directory where log files will be stored.
    *
    * @return    The directory where the server writes log files.
    */
    File getServerLogDir();

    /**
    * Get the directory where temporary files will be stored.
    *
    * @return    The directory where the server stores temporary files.
    */
    File getServerTempDir();

    /**
    * Get the directory where local data will be stored.
    *
    * @return    The directory where the server stores local data.
    */
    File getServerDataDir();

    /**
    * Get the base directory for calculating server home URLs.
    *
    * @return    Base server home URL.
    */
    URL getServerBaseURL();

    /**
    * Get the server home URL.
    *
    * @return    Server home URL.
    */
    URL getServerHomeURL();

    /**
    * Get the server library URL.
    *
    * @return    Server library URL.
    */
    URL getServerLibraryURL();

    /**
    * Get the server configuration URL.
    *
    * @return    Server configuration URL.
    */
    URL getServerConfigURL();

    /**
    * Get the current value of the flag that indicates if we are
    * using the platform MBeanServer as the main jboss server.
    * Both the {@link ServerConfig.PLATFORM_MBEANSERVER}
    * property must be set, and the jvm must be jdk1.5+
    * 
    * @return true if jboss runs on the jvm platfrom MBeanServer
    */
    boolean getPlatformMBeanServer();

    /**
    * Enable or disable exiting the JVM when {@link Server#shutdown} is called.
    * If enabled, then shutdown calls {@link Server#exit}.  If disabled, then
    * only the shutdown hook will be run.
    *
    * @param flag    True to enable calling exit on shutdown.
    */
    void setExitOnShutdown(boolean flag);

    /**
    * Get the current value of the exit on shutdown flag.
    *
    * @return    The current value of the exit on shutdown flag.
    */
    boolean getExitOnShutdown();

    /**
    * Get the BlockingShutdown value.
    * @return the BlockingShutdown value.
    */
    boolean getBlockingShutdown();

    /**
    * Set the BlockingShutdown value.
    * @param blockingShutdown The new BlockingShutdown value.
    */
    void setBlockingShutdown(boolean blockingShutdown);

    /**
    * Get the RequireJBossURLStreamHandlerFactory value.
    * @return the RequireJBossURLStreamHandlerFactory value.
    */
    boolean getRequireJBossURLStreamHandlerFactory();

    /**
    * Set the RequireJBossURLStreamHandlerFactory value.
    * @param requireJBossURLStreamHandlerFactory The new RequireJBossURLStreamHandlerFactory value.
    */
    void setRequireJBossURLStreamHandlerFactory(boolean requireJBossURLStreamHandlerFactory);

    /**
    * Set the filename of the root deployable that will be used to finalize
    * the bootstrap process.
    *
    * @param filename    The filename of the root deployable.
    */
    void setRootDeploymentFilename(String filename);

    /**
    * Get the filename of the root deployable that will be used to finalize
    * the bootstrap process.
    *
    * @return    The filename of the root deployable.
    */
    String getRootDeploymentFilename();

    /**
    * Get the native dir for unpacking
    * 
    * @return the directory
    */
    File getServerNativeDir();

    /**
    * Get the temporary deployment dir for unpacking
    * 
    * @return the directory
    */
    File getServerTempDeployDir();

    /**
    * Get the server Specification-Version
    */
    String getSpecificationVersion();
}
