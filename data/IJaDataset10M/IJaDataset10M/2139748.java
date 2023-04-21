package com.jcorporate.expresso.kernel;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;
import com.jcorporate.expresso.kernel.digester.ExpressoServicesConfig;
import com.jcorporate.expresso.kernel.exception.ConfigurationException;
import com.jcorporate.expresso.kernel.management.ExpressoRuntimeMap;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the default root container interface.  It is essentially the root of
 * the Expresso Runtime tree.
 *
 * @author Michael Rimov
 * @version $Revision: 3 $ on  $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 */
public class RootContainer extends ContainerComponentBase implements RootContainerInterface {

    private Boolean showStackTrace;

    private LogManager logManager = null;

    private Map setupValues = new HashMap();

    private String httpPort;

    private String servletAPI;

    private String sslPort;

    /**
     * The location of the services file so we can reload the config if
     * necessary
     */
    private java.net.URL servicesFile;

    /**
     * Weak Reference that contains the expresso configuration file. To save
     * memory, we allow the tree to be gc'ed if nobody else maintains a
     * reference to it.  We reload the system upon request at that point.
     */
    private WeakReference systemConfiguration = null;

    /**
     * Default constructor
     */
    public RootContainer() {
    }

    /**
     * Set the ExpressoServicesConfig bean.  This is usually done by the System
     * factory, or possibly the management tools.
     *
     * @param theConfig the new Expresso services config file.
     */
    public synchronized void setExpressoServicesConfig(ExpressoServicesConfig theConfig) {
        this.systemConfiguration = new WeakReference(theConfig);
    }

    /**
     * Retrieve the current ExpressoServicesConfiguration file bean.
     *
     * <p>
     * <b>Note</b> An important aspect of memory saving that RootContainer does
     * is that it maintains the local copy of the ExpressoServicesConfig as a
     * weak reference.  It then reloads from scratch if absolutely necessary.
     * Because of this, you cannot modify ExpressoServicesConfig, lose your
     * reference, and call getExpressoServicesConfig() again to retrieve your
     * changes.  Maintain a reference yourself if you want to save changes
     * later!
     * </p>
     *
     * @return the Root ExpressoServicesconfig
     */
    public synchronized ExpressoServicesConfig getExpressoServicesConfig() {
        ExpressoServicesConfig sc;
        if (systemConfiguration.get() == null) {
            sc = new ExpressoServicesConfig();
            sc.setExpressoServicesFile(servicesFile);
            sc.loadExpressoServices();
            systemConfiguration = new WeakReference(sc);
        }
        return (ExpressoServicesConfig) systemConfiguration.get();
    }

    /**
     * Set the HTTP port of the server
     *
     * @param httpPort the new value for this configuration setting
     */
    public synchronized void setHttpPort(String httpPort) {
        this.httpPort = httpPort;
    }

    /**
     * Retrieve the HTTP port of this server
     *
     * @return java.lang.String
     */
    public synchronized String getHttpPort() {
        return httpPort;
    }

    /**
     * Set the log manager for the entire expresso system
     *
     * @param newManager the new LogManager for the system
     */
    public synchronized void setLogManager(LogManager newManager) {
        logManager = newManager;
    }

    /**
     * Retrieve the LogManager object for the system
     *
     * @return the LogManager for the system
     */
    public synchronized LogManager getLogManager() {
        return this.logManager;
    }

    /**
     * Sets the ULR location of the services file.  This is usually called by
     * the SystemFactory
     *
     * @param url the location of the services file.
     */
    public synchronized void setServicesFileLocation(java.net.URL url) {
        servicesFile = url;
    }

    /**
     * Retrieves the URL location of the services file location.  May be inside
     * a jar/war file
     *
     * @return java.net.URL
     */
    public synchronized java.net.URL getServicesFileLocation() {
        return servicesFile;
    }

    /**
     * Set the servlet API of the system
     *
     * @param servletAPI the new value for the system
     */
    public synchronized void setServletAPI(String servletAPI) {
        this.servletAPI = servletAPI;
    }

    /**
     * Retrieve the servlet API of the system
     *
     * @return the current servlet API string
     */
    public synchronized String getServletAPI() {
        return servletAPI;
    }

    /**
     * Retrieve a specific setup value for the container.
     *
     * @param key the key for the global setup value
     *
     * @return java.lang.String
     */
    public synchronized String getSetupValue(String key) {
        return (String) setupValues.get(key);
    }

    /**
     * Retrieve all the setup values for the root container.
     *
     * @return Map of all setup values.
     */
    public synchronized Map getSetupValues() {
        return Collections.synchronizedMap(setupValues);
    }

    /**
     * Sets whether stack traces should be shown or not.
     *
     * @param showStackTrace the new value.
     *
     * @throws IllegalArgumentException if showStackTrace is null
     */
    public synchronized void setShowStackTrace(Boolean showStackTrace) {
        if (showStackTrace == null) {
            this.showStackTrace = Boolean.FALSE;
        }
        this.showStackTrace = showStackTrace;
    }

    /**
     * Get the show stack trace.  This is backwards compatible with the old
     * method of doing things in misc.ConfigManager.
     *
     * @return java.lang.Boolean
     */
    public synchronized Boolean getShowStackTrace() {
        return showStackTrace;
    }

    /**
     * Retrieve whether stack traces should be shown or not.
     *
     * @return boolean true if stacktrace should be shown
     */
    public synchronized boolean isShowStackTrace() {
        return showStackTrace.booleanValue();
    }

    /**
     * Set the SSL port property of this root container
     *
     * @param sslPort the new location for the SSL port
     */
    public synchronized void setSslPort(String sslPort) {
        this.sslPort = sslPort;
    }

    /**
     * Retrieve the location of the Expresso SSL port.  This method is to
     * eventually be removed to another separate component
     *
     * @return java.lang.String
     */
    public synchronized String getSslPort() {
        return sslPort;
    }

    /**
     * Configure the global container including type conversion for default
     * values.
     *
     * @param newConfig The Configuration 'dynabean'
     *
     * @throws ConfigurationException upon error
     */
    public synchronized void configure(Configuration newConfig) throws ConfigurationException {
        Map setupMap = newConfig.getMappedProperties("SetupValue");
        if (setupMap != null) {
            setupValues = new ConcurrentReaderHashMap(setupMap);
        }
        setShowStackTrace((Boolean) newConfig.get("ShowStackTrace"));
        setHttpPort((String) newConfig.get("HttpPort"));
        setServletAPI((String) newConfig.get("ServletAPI"));
        setSslPort((String) newConfig.get("SslPort"));
    }

    /**
     * Destroys the container and unregisters itself from the
     * ExpressoRuntimeMap
     */
    public synchronized void destroy() {
        ExpressoRuntimeMap.unregisterRuntime(this.getExpressoServicesConfig().getName());
        this.getContainerImplementation().destroyContainer();
    }

    /**
     * Default Initializer
     */
    public synchronized void initialize() {
        return;
    }

    /**
     * Reconfigure the global container
     *
     * @param newConfig the new configuration.
     *
     * @throws ConfigurationException upon reconfiguration error
     */
    public synchronized void reconfigure(Configuration newConfig) throws ConfigurationException {
        setShowStackTrace(null);
        setHttpPort(null);
        setServletAPI(null);
        setSslPort(null);
        setupValues = new HashMap();
        configure(newConfig);
    }
}
