package com.jcorporate.expresso.core.misc;

import com.jcorporate.expresso.core.cache.CacheManager;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerFactory;
import com.jcorporate.expresso.core.controller.ExpressoActionServlet;
import com.jcorporate.expresso.core.db.DBConnectionPool;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.DBObject;
import com.jcorporate.expresso.core.dbobj.Schema;
import com.jcorporate.expresso.core.dbobj.SchemaFactory;
import com.jcorporate.expresso.core.dbobj.SecuredDBObject;
import com.jcorporate.expresso.core.job.ServerException;
import com.jcorporate.expresso.core.jsdkapi.GenericDispatcher;
import com.jcorporate.expresso.core.utility.JobHandler;
import com.jcorporate.expresso.kernel.LogManager;
import com.jcorporate.expresso.kernel.digester.SaxParserConfigurer;
import com.jcorporate.expresso.kernel.util.FastStringBuffer;
import com.jcorporate.expresso.services.dbobj.DBOtherMap;
import com.jcorporate.expresso.services.dbobj.SchemaList;
import com.jcorporate.expresso.services.dbobj.Setup;
import com.jcorporate.expresso.services.test.TestSystemInitializer;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * ConfigManager is a static class that utilizes the Struts Digester
 * utility to read an XML file of configuration parameters when
 * Expresso starts up. These parameters are then available during the
 * execution of the application.
 *
 * @author Adam Rossi
 * @version $Revision: 3 $ $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 */
public final class ConfigManager {

    /**
     * constant path for DTD file; we expect DTD to be placed here, probably by a ANT copy before running servlet engine
     * searched relative to class dir
     */
    public static final String EXPRESSO_DTD_COPY_LOCATION = "/com/jcorporate/expresso/core/expresso-config_4_0.dtd";

    /**
     * name of servlet param which controls logging directory
     */
    public static final String LOG_DIR_PARAM_NAME = "logDir";

    /**
     * Hashtable of current logins sitting in the system.
     */
    private static Hashtable currentLogins = new Hashtable();

    private static Hashtable allContexts = new Hashtable();

    private static String configDir = "";

    private static String servletAPIVersion = "2_2";

    /**
     * Map collection to associate module names with action mappings
     * The default module has a prefix of "".
     */
    private static Map mapModuleMapping = new HashMap();

    private static ConfigExpresso myConfig = null;

    private static Hashtable dbOtherMap = new Hashtable();

    private static Logger log = Logger.getLogger(ConfigManager.class);

    private static boolean xmlConfigOk = false;

    private static boolean configurationFailed = false;

    /**
     * List of currently running job handlers
     */
    private static Hashtable jobHandlers = new Hashtable();

    /**
     * If configuration failed on initial startup, we keep the exception
     * that caused the failure available for reference
     */
    private static Throwable configurationFailureException = null;

    private static HttpServletRequest myRequest = null;

    /**
     * Singleton implementation function.
     */
    private static ConfigManager theInstance = null;

    private static ControllerFactory controllerFactory = null;

    /**
     * Adds to the 'we have X many logins' hashtable.
     * @param newLogin the new login
     */
    public static void addSession(CurrentLogin newLogin) {
        if (currentLogins == null) {
            currentLogins = new Hashtable();
        }
        if (newLogin == null) {
            return;
        }
        String sessionid = newLogin.getSessionId();
        if (sessionid == null) {
            return;
        }
        if (currentLogins.containsKey(newLogin.getSessionId())) {
            currentLogins.remove(newLogin.getSessionId());
        }
        currentLogins.put(newLogin.getSessionId(), newLogin);
    }

    public static void setConfigurationFailureException(Throwable ee) {
        configurationFailureException = ee;
    }

    public static Throwable getConfigurationFailureException() {
        return configurationFailureException;
    }

    /**
     * Removes a stored session.  Used for tracking the number of people
     * currently logged in.
     * @param sessionId the session id that we are receiving the signal for
     */
    public static void removeSession(String sessionId) {
        if (sessionId == null) {
            return;
        }
        if (currentLogins == null) {
            currentLogins = new Hashtable();
        }
        if (currentLogins.containsKey(sessionId)) {
            currentLogins.remove(sessionId);
        }
        if (currentLogins.size() == 0) {
            System.gc();
            System.runFinalization();
        }
    }

    /**
     * Returns a hashtable of the current login objects
     * @return a Hashtable containing everybody logged in.
     */
    public static Hashtable getCurrentLogins() {
        return (Hashtable) currentLogins.clone();
    }

    /**
     * Returns a specific job handler.
     * @param contextName The dbcontext to get the jobhandler from. ex: <code>
     * default</code>
     * @return the JobHandler for that specific context
     */
    public static JobHandler getJobHandler(String contextName) {
        return (JobHandler) jobHandlers.get(contextName);
    }

    /**
     * Gets all job handlers in the system
     * @return a hashtable of all Job Handlers
     */
    public static Hashtable getAllJobHandlers() {
        return jobHandlers;
    }

    /**
     * Default Constructer
     */
    public ConfigManager() {
        super();
    }

    /**
     * Queries the current servletAPI defined in the configuration system
     *
     * @return A string listing the current servletAPIVersion being used
     */
    public static String getServletAPIVersion() {
        return servletAPIVersion;
    }

    /**
     * Remove our database pool(s)
     *
     * Closes all connections so that Hypersonic can handle multiple test
     * suites from multiple VM's thrown at it as long as each test suite
     * calls dbUninitialize in the teardown method.
     *
     * @throws DBException
     */
    public static synchronized void dbUninitialize() throws DBException {
        if (log.isInfoEnabled()) {
            log.info("Tearing down connection pools");
        }
        DBConnectionPool.reInitialize();
    }

    /**
     * Call this to destroy all items that ConfigManager uses.
     * Initially not everythiny is implemented.  Must be tested  If there
     * is an error the system prints a stack trace to System.err instead of the
     * log since logging may have shut down by now.
     *
     */
    public static synchronized void destroy() {
        if (theInstance == null) {
            return;
        }
        ConfigManager cm = ConfigManager.getInstance();
        try {
            cm.stopJobHandler();
            cm.dbUninitialize();
            com.jcorporate.expresso.core.dbobj.NextNumber.destroy();
            CacheManager.destroy();
            com.jcorporate.expresso.core.security.CryptoManager.getInstance().destroy();
            com.jcorporate.expresso.core.security.DelayThread.kill();
            LogManager.destroy();
            for (Enumeration e = cm.getAllConfigKeys(); e.hasMoreElements(); ) {
                String oneKey = (String) e.nextElement();
                try {
                    ConfigJdbc jdbcConfig = cm.getContext(oneKey).getJdbc();
                    if (jdbcConfig.getDriver().equals("org.hsqldb.jdbcDriver")) {
                        DBConnectionPool pool = DBConnectionPool.getInstance(oneKey);
                        pool.executeExclusiveUpdate("SHUTDOWN");
                    }
                } catch (DBException ex) {
                    System.err.println("Error shutting down hypersonic: " + ex.getMessage());
                }
            }
            theInstance = null;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return;
    }

    /**
     * Initialize our database pool(s)
     *
     * @throws ConfigurationException if there is an error getting the config
     * values from the config beans.
     */
    public static synchronized void dbInitialize() throws ConfigurationException {
        if (log.isInfoEnabled()) {
            log.info("Initializing connection pool(s)");
        }
        String oneConfigKey = null;
        for (Enumeration e = getAllConfigKeys(); e.hasMoreElements(); ) {
            oneConfigKey = (String) e.nextElement();
            if (log.isInfoEnabled()) {
                log.info("Initializing pool for context '" + oneConfigKey + "'");
            }
            ConfigJdbc myConfig = getJdbc(oneConfigKey);
            if (myConfig != null) {
                try {
                    DBConnectionPool myPool = DBConnectionPool.getInstance(oneConfigKey);
                    if (!StringUtil.notNull(myConfig.getDbTest()).equalsIgnoreCase("")) {
                        myPool.setTestQuery(myConfig.getDbTest());
                        if (log.isInfoEnabled()) {
                            log.info("DB connection testing enabled");
                        }
                    } else {
                        if (log.isInfoEnabled()) {
                            log.info("DB connection testing disabled");
                        }
                    }
                } catch (DBException de) {
                    log.error("Initialize failed for context '" + oneConfigKey + "'. This pool will not be available.", de);
                }
            }
        }
        if (log.isInfoEnabled()) {
            log.info("All connection pools initialized");
        }
        if (log.isInfoEnabled()) {
            log.info("Initializing all registered DBObjets");
        }
    }

    /**
     * Iterate through all schemas and instantiate a DBObject.  Yes, this by
     * and large may get thrown away later, but constructing blank objects allows
     * various objects to set up listener relations with other classes.
     * This is especially important for UserListener information.
     */
    public static synchronized void initializeAllDBObjects() {
        boolean expressoInitialized = false;
        for (Iterator i = allContexts.keySet().iterator(); i.hasNext(); ) {
            try {
                String oneDBName = (String) i.next();
                ConfigContext oneContext = ConfigManager.getContext(oneDBName);
                if (oneContext.hasSetupTables()) {
                    try {
                        SchemaList sl = new SchemaList(SecuredDBObject.SYSTEM_ACCOUNT);
                        sl.setDataContext(oneDBName);
                        sl.count();
                    } catch (DBException e) {
                        if (log.isInfoEnabled()) {
                            log.info("Schema is not initialized yet- run dbcreate for context: " + oneDBName);
                        }
                        continue;
                    }
                    if (expressoInitialized == false) {
                        initializeOneSchema(com.jcorporate.expresso.core.ExpressoSchema.class.getName());
                        expressoInitialized = true;
                    }
                    SchemaList sl = new SchemaList(SecuredDBObject.SYSTEM_ACCOUNT);
                    sl.setDataContext(oneDBName);
                    ArrayList al = sl.searchAndRetrieveList();
                    for (Iterator schemas = al.iterator(); schemas.hasNext(); ) {
                        SchemaList oneSchema = (SchemaList) schemas.next();
                        try {
                            initializeOneSchema(oneSchema.getField(SchemaList.FLD_SCHEMA_CLASS));
                        } catch (Exception ex) {
                            log.error("Error initializing schema: " + oneSchema.getField(SchemaList.FLD_SCHEMA_CLASS), ex);
                        }
                    }
                }
            } catch (DBException dbe) {
                log.warn("Unable to initialize dbobjects... SchemaList may not be initialized yet", dbe);
            } catch (Exception e) {
                log.error("Error in initializeAllDBObjects", e);
            }
        }
    }

    protected static synchronized void initializeOneSchema(String className) {
        if (className == null) {
            log.warn("initializeOneSchema: Received null className");
            return;
        }
        Schema schema = SchemaFactory.getInstance().getSchema(className);
        if (schema == null) {
            log.error("initializeOneSchema: cannot find schema: " + className);
            return;
        }
        Enumeration enum_ = schema.getMembers();
        while (enum_.hasMoreElements()) {
            DBObject obj = (DBObject) enum_.nextElement();
            try {
                obj.setSchema(schema);
            } catch (Exception e) {
                log.warn("cannot set schema for: " + obj.getClass().getName() + " because of: " + e.getMessage());
            }
        }
        return;
    }

    /**
     * Return an enumeration of all of the valid configuration keys.
     * There is one key for each property file read.
     *
     * @return  java.util.Enumeration
     */
    public static Enumeration getAllConfigKeys() {
        return allContexts.keys();
    }

    /**
     * Return the reference instance of the config manager  Use this in preference
     * to the singleton APIs.  So now you have:
     * <code>ConfigManager.getInstace().getLogDirectory()</code>
     * for instance. instead of <code>ConfigManager.getLogDirectory();
     *
     * @return an instance of ConfigManager
     */
    public static synchronized ConfigManager getInstance() {
        if (theInstance == null) {
            theInstance = new ConfigManager();
        }
        return theInstance;
    }

    /**
     * Returns the directory to put all log files into.
     * @return String containing the Expresso logging directory
     * @deprecated Use getConfig().getLogDirectory() instead
     */
    public static String getLogDirectory() {
        return getConfig().getLogDirectory();
    }

    /**
     * Is the ConfigManager initialized yet?
     *
     * @return true if the xml configuration parsed correctly
     */
    public static boolean isInitialized() {
        return xmlConfigOk;
    }

    /**
     * Return the top-level configuration object, an instance of the ConfigExpresso
     * class. This class contains all the setup options common to all contexts
     * @return a ConfigExpresso bean
     */
    public static ConfigExpresso getConfig() {
        return myConfig;
    }

    /**
     * Returns a hashmap keyed by filename, and data ='s a series of input
     * streams corresponding to xml files of the desired doctype. All files are
     * located in the Expresso configuration directory
     *
     * @param filterDocType - The doctype that you need for configuration files
     * @return HashMap of InputSources of xml files of the appropriate types.
     * @throws ConfigurationException if an error occurs while checking xml file
     * types
     */
    public static HashMap getConfigInputSources(String filterDocType) throws ConfigurationException {
        final String myName = "ConfigManager.getConfigInputSources(String)";
        HashMap returnSources = new HashMap();
        String configDir = ConfigManager.getConfigDir();
        if (configDir == null) {
            throw new ConfigurationException(myName + ": There is no config Directory Set");
        }
        InputSource inputSource = null;
        XmlDocTypeFilter xmldtf = new XmlDocTypeFilter();
        if ('/' == configDir.charAt(0) == false) {
            configDir = "/" + configDir;
        }
        if (log.isDebugEnabled()) {
            log.debug("Getting Input Sources in directory: " + configDir);
        }
        File propDir = new File(configDir);
        if (!propDir.isDirectory()) {
            throw new ConfigurationException(myName + ": " + configDir + " is not a directory");
        }
        String[] flist = propDir.list();
        for (int i = 0; i < flist.length; i++) {
            if (log.isDebugEnabled()) {
                log.debug("Checking file: " + flist[i]);
            }
            if (flist[i].endsWith(".xml")) {
                if (log.isDebugEnabled()) {
                    log.debug("File ends with xml");
                }
                try {
                    String propFileName = configDir + "/" + flist[i];
                    FileInputStream fis = new FileInputStream(propFileName);
                    try {
                        inputSource = new InputSource(fis);
                        inputSource.setSystemId(configDir + "/");
                        if (xmldtf.isProperDocType(filterDocType, inputSource) == true) {
                            if (log.isDebugEnabled()) {
                                log.debug(propFileName + " is doctype " + filterDocType + " adding as inputsource");
                            }
                            fis.close();
                            inputSource = new InputSource(new FileInputStream(propFileName));
                            inputSource.setSystemId(configDir + "/");
                            returnSources.put(flist[i], inputSource);
                        } else {
                            if (log.isDebugEnabled()) {
                                log.debug(propFileName + " is not of doctype " + filterDocType);
                            }
                        }
                    } finally {
                        fis.close();
                    }
                } catch (java.io.FileNotFoundException fnfe) {
                    throw new ConfigurationException(fnfe);
                } catch (java.io.IOException ioe) {
                    throw new ConfigurationException(ioe);
                }
            }
        }
        return returnSources;
    }

    /**
     * Return the context configuration object for the named context. This object
     * contains all of the configuration info for a specific context/database
     * @param contextName the data context to retrieve the configuration information
     * for
     * @throws ConfigurationException if there's an error retrieving the ConfigContext
     * bean for the specified context
     * @return the ConfigContextBean for the data context specified
     */
    public static ConfigContext getContext(String contextName) throws ConfigurationException {
        StringUtil.assertNotBlank(contextName, "You must specify a context name");
        if (allContexts == null) {
            throw new ConfigurationException("No contexts available");
        }
        ConfigContext oneContext = (ConfigContext) allContexts.get(contextName);
        if (oneContext == null) {
            throw new ConfigurationException("No such configuration context as '" + contextName + "'");
        }
        return oneContext;
    }

    /**
     * Return the Class dealing with the named ClassHandler
     * @param handlerName the 'service name' of the classhandler to retrieve
     * @return the name of the class to use for this 'category'
     */
    public static String getClassHandler(String handlerName) {
        ConfigClassHandler cch = (ConfigClassHandler) myConfig.getClassHandlers().get(handlerName);
        if (cch == null) {
            return null;
        }
        return cch.getClassHandler();
    }

    /**
     * Returns the given parameter for a class handler.
     * @param handlerName the 'service name' of the classhandler to retrieve
     * @param parameter The name of the parameter to get
     * @return the value of the parameter for this class handler
     */
    public static String getClassHandlerParameter(String handlerName, String parameter) {
        return ((ConfigClassHandler) myConfig.getClassHandlers().get(handlerName)).getParameter(parameter);
    }

    /**
     * Get the Jdbc configuration (if any) for the named context
     * @param contextName the data context to get the JDBC configuration bean for
     * @throws ConfigurationException if there is an error retrieving the context
     * @return a ConfigJdbc Configuration Bean
     */
    public static ConfigJdbc getJdbc(String contextName) throws ConfigurationException {
        ConfigContext oneContext = getContext(contextName);
        if (oneContext != null) {
            return oneContext.getJdbc();
        }
        return null;
    }

    /**
     * Get a Jdbc configuration object, throwing an exception if there is not one
     * for the specified context
     * @param contextName the data context to get the JDBC configuration bean for
     * @throws ConfigurationException if there is an error retrieving the context
     * @return a ConfigJdbc Configuration Bean
     */
    public static ConfigJdbc getJdbcRequired(String contextName) throws ConfigurationException {
        ConfigJdbc myJdbc = getJdbc(contextName);
        if (myJdbc == null) {
            throw new ConfigurationException("Context '" + contextName + "' is not configured for JDBC access");
        }
        return myJdbc;
    }

    /**
     * Get the actual filesystem directory that is the root of this web-app
     *
     * @return The filesystem directory that is the root of this webapp.
     */
    public static String getWebAppDir() {
        String webAppDir = SystemMacros.getInstance().getWebAppDir();
        if (webAppDir == null) {
            try {
                throw new Exception("Getting requests for web application directory, but system is deployed in a " + "compressed war file.  There is no such thing");
            } catch (Exception ex) {
                log.warn("getWebAppDir path error", ex);
            }
        }
        return webAppDir;
    }

    /**
     * Checks to see if any given field or tablename fed to this qualifies
     * as a known reserved word for various databases.
     * @todo Convert this to isReservedWord() and get all dbobjects converted
     * over.
     * @param testWord The word to check against the list of known reserved
     * words.
     * @return true if the test word is a reserved word.
     */
    public static boolean isReservedWord(String testWord) {
        return ReservedWords.getInstance().isExpressoReservedWord(testWord);
    }

    /**
     * Checks to see if a controller parameter may be a reserved word.  This
     * helps prevent any weird behavior when posting objects.
     * @param testWord The word to check against the list of known reserved
     * words.
     * @return true if the test word is a reserved word.
     */
    public static boolean isParameterReservedWord(String testWord) {
        return ReservedWords.getInstance().isExpressoReservedWord(testWord);
    }

    /**
     * Start job handlers
     * The job handler for each DB context is started if the appropriate
     * configuration entry is found for that context
     *
     *
     * @throws ConfigurationException if there's a config error reading job values or
     * getting job values for a particular context.
     */
    public static synchronized void startJobHandler() throws ConfigurationException {
        if (log.isInfoEnabled()) {
            log.info("Starting job handler(s)");
        }
        String oneConfigKey = null;
        for (Enumeration e = getAllConfigKeys(); e.hasMoreElements(); ) {
            oneConfigKey = (String) e.nextElement();
            if (!getContext(oneConfigKey).startJobHandler()) {
                log.warn("Job handler for configuration '" + oneConfigKey + "' not enabled (because of the setting in expresso-config.xml).");
            } else {
                if (log.isInfoEnabled()) {
                    log.info("Job handler for configuration '" + oneConfigKey + "' starting...");
                }
                try {
                    JobHandler ts = new JobHandler(oneConfigKey);
                    ts.setDaemon(true);
                    ts.start();
                    jobHandlers.put(oneConfigKey, ts);
                    if (log.isInfoEnabled()) {
                        log.info("Job handler for '" + oneConfigKey + "' running.");
                    }
                } catch (DBException de) {
                    log.error("Job handler start failed for config key '" + oneConfigKey + "'. Jobs for this config will " + "will not be run.", de);
                } catch (ServerException se) {
                    log.error("Job handler start failed for config key ' " + oneConfigKey + "'. Jobs for this config will will " + "not be run.", se);
                }
            }
        }
        if (log.isInfoEnabled()) {
            log.info("All job handlers started");
        }
    }

    /**
     * Stops all job handlers
     * @throws DBException if there's an error starting the job handler from
     * the DBContext point of view.
     * @throws ConfigurationException if there's an error getting the config
     * information beans.
     */
    protected void stopJobHandler() throws DBException, ConfigurationException {
        if (log.isInfoEnabled()) {
            log.info("Stops job handler(s)");
        }
        String oneConfigKey = null;
        for (Enumeration e = getAllConfigKeys(); e.hasMoreElements(); ) {
            oneConfigKey = (String) e.nextElement();
            if (getContext(oneConfigKey).startJobHandler()) {
                if (log.isInfoEnabled()) {
                    log.info("Job handler for configuration '" + oneConfigKey + "' interrupting...");
                }
                JobHandler ts = (JobHandler) jobHandlers.get(oneConfigKey);
                if (ts != null) {
                    ts.shutDown();
                }
            }
        }
        if (log.isInfoEnabled()) {
            log.info("All job handlers interrupted");
        }
    }

    /**
     * Set the "Web application directory" - this is used for translating
     * the %web-app% 'macro' that can be used in property and setup
     * values
     *
     * @param   newDir the new directory for the webAppDirectory 'environment
     * variable'
     */
    public static void setWebAppDir(String newDir) {
        SystemMacros.getInstance().setWebAppDir(newDir);
    }

    /**
     * @deprecated not used; deprecated march 03 since Expresso 5.1
     * @param newLevel the new debug level 0-9
     */
    public static void setConfigDebugLevel(int newLevel) {
    }

    /**
     * Set the "context path" - this is used for translating
     * the %context% 'macro' that can be used in property and setup
     * values
     *
     * @param   newContextPath the new contextPath value to use
     */
    public static void setContextPath(String newContextPath) {
        SystemMacros.getInstance().setContextPath(newContextPath);
    }

    /**
     * set the controller factory to use.
     * @see com.jcorporate.expresso.core.controller.ExpressoActionServlet for
     * an example use of this.
     * @param cf the new Controller Factory to use for the running system.
     */
    public static synchronized void setControllerFactory(ControllerFactory cf) {
        controllerFactory = cf;
    }

    /**
     * Get the web-app context path for this web application
     *
     * @return the context path string.
     */
    public static String getContextPath() {
        if (!isInitialized()) {
            throw new IllegalArgumentException("ConfigManager not initialized");
        }
        return SystemMacros.getInstance().getContextPath();
    }

    /**
     * Called by the initial load servlet to initialize the entire system
     *
     * @param   c The servlet engine configuration
     * @throws  ServletException Servlet exception if an error occurs initializing
     * the configuration system.
     */
    public static synchronized void config(ServletConfig c) throws ServletException {
        if (configurationFailed) {
            throw new ServletException("ConfigManager: Configuration has already been attempted and has failed - cannot re-run");
        }
        if (c == null) {
            ServletException se = new ServletException("ConfigManager: ServletConfig may not be null");
            setConfigurationFailureException(se);
            throw se;
        }
        try {
            ServletContext sc = c.getServletContext();
            servletAPIVersion = new Integer(sc.getMajorVersion()).toString() + "_" + new Integer(sc.getMinorVersion()).toString();
            configDir = StringUtil.notNull(c.getInitParameter("configDir"));
            if (configDir.equals("")) {
                configDir = StringUtil.notNull(sc.getInitParameter("configDir"));
            }
            if (configDir.equals("")) {
                ServletException se = new ServletException("ConfigManager: No 'configDir' initial " + "parameter was read - unable to initialize. " + "Check web.xml to ensure the configDir parameter is set " + "to a non-blank value");
                setConfigurationFailureException(se);
                throw se;
            }
            String rootDir = StringUtil.notNull(sc.getRealPath("/"));
            if (rootDir.equals("")) {
                rootDir = System.getProperty("expresso.home", "");
                if (rootDir.length() == 0) {
                    log.warn("Deployed inside WAR file and no expresso.home " + "directory set.  %WEB-APP% will expand to null");
                } else {
                    configDir = FileUtil.makeAbsolutePath(rootDir, configDir);
                    if (!('/' == configDir.charAt(0))) {
                        configDir = "/" + configDir;
                    }
                    setWebAppDir(rootDir);
                }
            } else {
                configDir = FileUtil.makeAbsolutePath(rootDir, configDir);
                if (!('/' == configDir.charAt(0))) {
                    configDir = "/" + configDir;
                }
                if (configDir.indexOf(' ') != -1) {
                    System.err.println("Bad name for installation path: Reinstall in directory without a space in name.");
                }
                setWebAppDir(rootDir);
            }
            initLogManager(c);
            load(configDir);
        } catch (Throwable thrown) {
            if (log != null) {
                log.error(thrown);
            }
            setConfigurationFailureException(thrown);
            System.err.println("ConfigManager: Expresso configuration encountered an exception:");
            thrown.printStackTrace(System.err);
            configurationFailed = true;
            throw new ServletException(thrown);
        }
    }

    /**
     * Initialize Log Manager based upon the LogDirectory.
     * @param sc the servlet configuration.
     */
    private static void initLogManager(ServletConfig sc) {
        String configDir = sc.getServletContext().getInitParameter("configDir");
        String logDir = sc.getServletContext().getInitParameter(LOG_DIR_PARAM_NAME);
        String logConfig = null;
        if (!('/' == configDir.charAt(0))) {
            configDir = getWebAppDir() + configDir;
        }
        if (logDir != null && logDir.length() > 0 && !('/' == logDir.charAt(0))) {
            logDir = getWebAppDir() + logDir;
        }
        if (configDir != null && configDir.length() > 0) {
            logConfig = configDir + "/expressoLogging.xml";
            java.io.File f = new java.io.File(logConfig);
            if (f == null || !f.exists()) {
                logConfig = null;
            }
        }
        new LogManager(logConfig, logDir);
    }

    public static synchronized void load(String theConfigDir) throws DBException, ConfigurationException {
        StringUtil.assertNotBlank(theConfigDir, "No configuration directory specified");
        Logger.getLogger(ConfigManager.class);
        ConfigManager.configDir = theConfigDir;
        ConfigManager instance = ConfigManager.getInstance();
        instance.readXMLConfig(configDir);
        if (log.isInfoEnabled()) {
            log.info("Initializing connection pool(s)");
        }
        instance.dbInitialize();
        instance.mapOtherDBs();
        if (log.isInfoEnabled()) {
            log.info("Reading setup values");
        }
        Setup.readSetups();
        if (log.isInfoEnabled()) {
            log.info("Initializing Schema Objects");
        }
        initializeAllDBObjects();
        com.jcorporate.expresso.core.dbobj.NextNumber.getInstance();
        CacheManager.getInstance();
        if (log.isInfoEnabled()) {
            log.info("Starting job handler(s)");
        }
        instance.startJobHandler();
        if (log.isInfoEnabled()) {
            log.info("Expresso initialization complete");
        }
    }

    /**
     * Method to "expand" some simple "macro" codes allowed in
     * property file and Setup values.
     *
     * @param   propValue The 'property value' to expand.
     * @return The 'expanded' value of the property value
     */
    public static String expandValue(String propValue) {
        return SystemMacros.getInstance().expandValue(propValue);
    }

    /**
     * Return the pathname of the configuration directory (specified
     * as "configDir"
     *
     * @return The configuration directory that is used by expresso
     */
    public static String getConfigDir() {
        return configDir;
    }

    /**
     * Returns the controller factory object.  Instantiates the default
     * controller factory if one doesn't exist.
     * @return a ControllerFactory object to use for instantiating controllers
     */
    public static synchronized ControllerFactory getControllerFactory() {
        if (controllerFactory == null) {
            controllerFactory = new com.jcorporate.expresso.core.controller.DefaultControllerFactory();
        }
        return controllerFactory;
    }

    /**
     * Get the cached "other db" location for a specific object
     * Creation date: (1/5/01 6:57:06 PM)
     * author: Adam Rossi, PlatinumSolutions
     *
     * @param  dbName java.lang.String
     * @param objectName the name to located
     * @return A String giving the data context for the other location
     */
    public static String getOtherDbLocation(String dbName, String objectName) {
        FastStringBuffer fsb = FastStringBuffer.getInstance();
        try {
            fsb.append(dbName);
            fsb.append("|");
            fsb.append(objectName);
            return (String) dbOtherMap.get(fsb.toString());
        } finally {
            fsb.release();
        }
    }

    /**
     * Return a cloned Hashtable containing all of the DBOtherMap entries
     * author  Adam Rossi, PlatinumSolutions
     * @return java.util.Hashtable
     */
    public static Hashtable getOtherDBLocations() {
        return (Hashtable) dbOtherMap.clone();
    }

    /**
     * Map otherdb locations into a hashtable stored in memory.
     *
     * Creation date: (1/5/01 6:48:11 PM)
     * author: Adam Rossi, PlatinumSolutions
     * @throws ConfigurationException if there is an error mapping these other
     * databases.
     */
    public static synchronized void mapOtherDBs() throws ConfigurationException {
        String oneKey = "";
        dbOtherMap.clear();
        for (Enumeration ek = getAllConfigKeys(); ek.hasMoreElements(); ) {
            oneKey = (String) ek.nextElement();
            ConfigContext oneContext = getContext(oneKey);
            if (oneContext.hasSetupTables() && oneContext.isActive()) {
                if (log.isInfoEnabled()) {
                    log.info("Reading DBObject mappings to other databases, context '" + oneKey + "'...");
                }
                try {
                    SchemaList sl = new SchemaList(SecuredDBObject.SYSTEM_ACCOUNT);
                    sl.setDataContext(oneKey);
                    sl.count();
                } catch (DBException e) {
                    if (log.isInfoEnabled()) {
                        log.info("Context: " + oneKey + " does not have a SchemaList yet.  Skipping for now");
                        continue;
                    }
                }
                int i = 0;
                try {
                    DBOtherMap otherDB = new DBOtherMap();
                    otherDB.setDataContext(oneKey);
                    DBOtherMap oneOtherDB = null;
                    FastStringBuffer fsb = FastStringBuffer.getInstance();
                    try {
                        for (Iterator allOtherDB = otherDB.searchAndRetrieveList().iterator(); allOtherDB.hasNext(); ) {
                            oneOtherDB = (DBOtherMap) allOtherDB.next();
                            fsb.append(oneKey);
                            fsb.append("|");
                            fsb.append(oneOtherDB.getField("DBObjName"));
                            dbOtherMap.put(fsb.toString(), oneOtherDB.getField("DBConnName"));
                            fsb.clear();
                            i++;
                        }
                    } finally {
                        fsb.release();
                    }
                    if (log.isInfoEnabled()) {
                        log.info("Added " + i + " entries to the DBObject Other Database Map.");
                    }
                } catch (DBException de) {
                    if (oneKey.equalsIgnoreCase(TestSystemInitializer.getTestContext())) {
                        log.warn("Didn't read test database mapping context: " + de.getMessage());
                    } else {
                        log.error("Unable to read database mapping entries for context '" + oneKey + "'", de);
                    }
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info("DB/Context '" + oneKey + "' is not an Expresso database, or is not active - not reading mapping entries from this context");
                }
            }
        }
    }

    /**
     * Re-read all properties and other values, re-initialize everything
     *
     * @param   req The servlet request to use
     * @param   c The servlet config handed to us in the init() method.
     * @throws  ServletException
     */
    public static void reInitialize(HttpServletRequest req, ServletConfig c) throws ServletException {
        try {
            DBConnectionPool.reInitialize();
            checkInitialized(req, c);
        } catch (DBException de) {
            System.err.println("ConfigManager: Error re-initializing:");
            de.printStackTrace(System.err);
        }
    }

    /**
     * Check if the configuration info needs to be read, and read
     * it if so.<br>
     * Immediately returns if the config manager has already been
     * initialized successfully.<br>
     * Any servlet can check this to make sure Expresso has been
     * started up successfully - if not, it calls load as needed
     * to perform/re-perform the startup.
     *
     * @param   req The servlet request given by the servlet container
     * @param   c The servlet configuration
     * @throws  ServletException
     */
    public static void checkInitialized(HttpServletRequest req, ServletConfig c) throws ServletException {
        if (!isInitialized()) {
            System.err.println("ConfigManager: Expresso was not initialized - initializing now");
            config(c);
        }
        setRequest(req);
    }

    /**
     * This function is used by webservers to set global variables such as
     * server prefix, or context path.  This is usually set by checkLogin(),
     * or any other first pages that are reached.
     * @param req a HttpServletRequest
     */
    public static synchronized void setRequest(HttpServletRequest req) {
        if (myRequest == null) {
            myRequest = req;
            SystemMacros sm = SystemMacros.getInstance();
            sm.setContextPath(GenericDispatcher.getContextPath(req));
            sm.setServerPrefix(req.getServerName() + ":" + req.getServerPort());
        }
    }

    /**
     * Store the action mappings per module to allow fast reverse based
     * lookup based on the controller name.
     *
     * <p>
     * It is possible that the same controller is used across
     * action mappings and also across module (sub applications). So
     * this data structure takes account of the multiple mappings to
     * module names and save a list of possible action mappings
     * related to a controller class name.
     * </p>
     *
     * <p>
     * <b>PLEASE NOTE</b> from Struts 1.1 the old
     * <code>org.apache.struts.action.ActionMapping</code> is deprecated
     * and has been replaced with <code>org.apache.struts.config.ActionConfig</code>
     * which is part of <code>org.apache.struts.config.ModuleConfig</code>
     *
     * </p>
     */
    public static void storeModuleActionConfig(ModuleConfig moduleConfig) {
        String prefixName = moduleConfig.getPrefix();
        ActionConfig actionConfig[] = moduleConfig.findActionConfigs();
        if (log.isDebugEnabled()) {
            log.debug("ConfigManager.saveModuleActionConfig() prefix=`" + prefixName + "'");
        }
        synchronized (mapModuleMapping) {
            Map mapMappings = new HashMap();
            for (int j = 0; j < actionConfig.length; ++j) {
                String controllerName = actionConfig[j].getType();
                List list = (List) mapMappings.get(controllerName);
                if (list == null) list = new ArrayList();
                list.add(actionConfig[j]);
                mapMappings.put(controllerName, list);
                if (log.isDebugEnabled()) {
                    log.debug("   " + controllerName + " => { `" + actionConfig[j].getPath() + "' " + "name:`" + actionConfig[j].getName() + "' " + "scope:`" + actionConfig[j].getScope() + "' " + "input:`" + actionConfig[j].getInput() + "' " + "attribute:`" + actionConfig[j].getAttribute() + "' " + "parameter:`" + actionConfig[j].getParameter() + "' " + "validate:`" + actionConfig[j].getValidate() + "' " + "}");
                }
            }
            mapModuleMapping.put(prefixName, mapMappings);
        }
    }

    /**
     * Gets the action mapping associated with the controller and default root  module.
     * Perform a 'reverse-mapping', that is, locate a Struts action mapping
     * by looking for the given controller name (and optional state name)
     *
     * @param controllerName the classname of the controller to look up.
     * @param stateName the name of the state to lookup within that controller
     * @return the ActionConfig associated with the parameters specified
     *
     * @see #getActionConfig( String,String,String )
     */
    public static ActionConfig getActionConfig(String controllerName, String stateName) {
        return getActionConfig("", controllerName, stateName);
    }

    /**
     * Gets the action mapping associated with the controller and the module name.
     * Perform a 'reverse-mapping', that is, locate a Struts action mapping
     * by looking for the given controller name (and optional state name).
     * If the stateName is <code>null</code> this method will attempt to
     * dynamically load the controller class, instantiate an object instance
     * and retrieve the controller class's initial state.
     *
     * <p>
     *
     * The action mapping is determined by looking at
     * <code>ActionConfig</code> records in Struts configuration,
     * retrieving all the <em>local</em> action forward and comparing
     * its name to the state forward.  This means that in your Struts
     * XML configuration you must define a local action forward with
     * the same name as the state Method.
     *
     * </p>
     *
     * <p>
     *
     * If no action mapping can be determined by�looking for a
     * particular matching local action forward, then the first action
     * mapping is returned if exists. Please note the first action
     * mapping is not necessarily the first action mapping declared in
     * the XML configuration. This cannot be guaranteed, because the
     * implementation may changed in the future.
     *
     * </p>
     *
     * <pre>
     *  &lt;action path="/Register"
     *		type="com.jcorporate.expresso.services.controller.SimpleRegistration"
     *		name="default" scope="request" validate="false" &gt;
     *          &lt;forward name="showDBMenu"
     *			path="/expresso/jsp/register/dbmenu.jsp" /&gt;
     *          &lt;forward name="promptAddRecord"
     *			path="/expresso/jsp/register/regAdd.jsp" /&gt;
     *          &lt;forward name="promptUpdateRecord"
     *			path="/expresso/jsp/register/regAdd.jsp" /&gt;
     *		...
     * &lt;/action&gt;
     * <pre>
     *
     * <p>
     * In order for the above action mapping "/Register" to be returned then
     * you need to declare a <em>unique</em> local action forward name, if you
     * use the same controller class for multiple actions. For example "showDBMenu"
     * could be the unique local forward.
     * </p>
     *
     * @param moduleName the Struts module name
     * @param controllerName the classname of the controller to look up.
     * @param stateName the name of the state to lookup within that controller
     *
     * @return the ActionConfig associated with the parameters specified
     *
     * @see #storeModuleActionConfig( ModuleConfig moduleConfig)
     */
    public static ActionConfig getActionConfig(String moduleName, String controllerName, String stateName) {
        if (log.isDebugEnabled()) {
            log.debug("CF.getActionConfig() moduleName=`" + moduleName + "', controllerName=`" + controllerName + "', stateName=`" + stateName + "'");
        }
        if (moduleName == null) {
            throw new IllegalArgumentException("ConfigManager.getActionMapping(): " + "parameter moduleName cannot be null");
        }
        if (controllerName == null) {
            throw new IllegalArgumentException("ConfigManager.getActionMapping(): " + "parameter controllerName cannot be null");
        }
        if (stateName == null || stateName.length() == 0) {
            try {
                com.jcorporate.expresso.core.controller.Controller c = ConfigManager.getControllerFactory().getController(moduleName, controllerName);
                stateName = c.getInitialState();
                if (stateName == null || stateName.length() == 0) {
                    throw new IllegalArgumentException("ConfigManager.getMapping(): " + "stateName was null and there is no defined initial state for: " + controllerName);
                }
                if (log.isDebugEnabled()) {
                    log.debug("using initial state =`" + stateName + "'");
                }
            } catch (ControllerException ex) {
                throw new IllegalArgumentException("ConfigManager.getMapping(): " + "stateName was null and can't construct controller of name: " + controllerName);
            } catch (ClassCastException ex) {
                log.error("Error getting controller", ex);
                throw new IllegalArgumentException("ClassCastException getting controller");
            }
        }
        synchronized (mapModuleMapping) {
            Map mapMappings = (Map) mapModuleMapping.get(moduleName);
            if (mapMappings != null) {
                List list = (List) mapMappings.get(controllerName);
                if (list != null) {
                    for (int j = 0; j < list.size(); ++j) {
                        ActionConfig oneConfig = (ActionConfig) list.get(j);
                        if (StringUtil.notNull(oneConfig.getParameter()).equals(stateName)) {
                            if (log.isDebugEnabled()) {
                                log.debug("CM.getActionConfig() *FOUND* parameter=`" + oneConfig.getParameter() + "'  actionConfig:" + oneConfig.getName() + "," + oneConfig.getPath());
                            }
                            return oneConfig;
                        }
                        ForwardConfig aForward = findForwardConfig(oneConfig, stateName);
                        if (aForward != null) {
                            if (log.isDebugEnabled()) {
                                log.debug("CM.getActionConfig() *FOUND* forwards=`" + aForward.getName() + "',`" + aForward.getPath() + "'  actionConfig:" + oneConfig.getName() + "," + oneConfig.getPath());
                            }
                            return oneConfig;
                        }
                    }
                    if (list.size() > 0) {
                        ActionConfig oneConfig = (ActionConfig) list.get(0);
                        if (log.isDebugEnabled()) {
                            log.debug("CM.getActionConfig() *FOUND* using the FIRST actionConfig:" + oneConfig.getName() + "," + oneConfig.getPath());
                        }
                        return oneConfig;
                    }
                }
            }
        }
        return (null);
    }

    /**
     * Find a forward config given an action config and a state name  Does not
     * return higher level forwards, just the one for the given state IF it exists
     * @param oneConfig the ActionConfig for the controller
     * @param stateName the state name for the controller
     * @return ForwardConfig instance for that parameter combination
     */
    public static final ForwardConfig findForwardConfig(ActionConfig oneConfig, String stateName) {
        ForwardConfig forwards[] = oneConfig.findForwardConfigs();
        for (int k = 0; k < forwards.length; ++k) {
            if (forwards[k].getName().equals(stateName)) {
                return forwards[k];
            }
        }
        return null;
    }

    /**
     * Gets <em>all</em> the action mappings associated with the
     * controller and the default module.  Perform a
     * 'reverse-mapping', that is, locate a Struts action mappings by
     * looking for the given controller name.
     *
     * @param controllerName the classname of the controller to look up.
     * @return the ActionConfig associated with the parameters specified
     *
     * @see #getActionConfigList( String,String )
     */
    public static List getActionConfigList(String controllerName) {
        return getActionConfigList("", controllerName);
    }

    /**
     * Gets <em>all</em> the action mappings associated with the controller
     * and the module name.
     * Perform a 'reverse-mapping', that is, locate a Struts action mapping
     * by looking for the given controller name.
     *
     *
     * @param moduleName the Struts module name
     * @param controllerName the classname of the controller to look up.
     *
     * @return the ActionConfig list associated with the parameters specified
     *
     * @see #storeModuleActionConfig( ModuleConfig moduleConfig)
     */
    public static List getActionConfigList(String moduleName, String controllerName) {
        if (moduleName == null) {
            throw new IllegalArgumentException("ConfigManager.getActionConfigList(): " + "parameter moduleName cannot be null");
        }
        if (controllerName == null) {
            throw new IllegalArgumentException("ConfigManager.getActionConfigList(): " + "parameter controllerName cannot be null");
        }
        List result = new ArrayList();
        synchronized (mapModuleMapping) {
            Map mapMappings = (Map) mapModuleMapping.get(moduleName);
            if (mapMappings != null) {
                List mappings = (List) mapMappings.get(controllerName);
                if (mappings != null) {
                    result.addAll(mappings);
                }
            }
        }
        return result;
    }

    /**
     * Store the Struts Forwards for lookups.
     *
     * @param theActionServlet the one and only Expresso Action Servlet
     *
     * @deprecated this method is now deprecated and does no anything since Struts 1.1
     * {@link #storeModuleActionConfig( ModuleConfig moduleConfig)}
     *
     */
    public static synchronized void setForwards(ExpressoActionServlet theActionServlet) {
    }

    /**
     * Perform a 'reverse-mapping', that is, locate a Struts action mapping
     * by looking for the given controller name (and optional state name)
     * @param controllerName the classname of the controller to look up.
     * @param stateName the name of the state to lookup within that controller
     * @return the ActionMapping associated with the parameters specified
     * @deprecated this method is now deprecated and does no anything since Struts 1.1
     * {@link #getActionConfig(String moduleName, String controllerName, String stateName )}
     */
    public static ActionMapping getMapping(String controllerName, String stateName) {
        ActionMapping mapping = null;
        ActionConfig config = getActionConfig("", controllerName, stateName);
        if (config != null) {
            mapping = new ActionMapping();
            try {
                BeanUtils.copyProperties(mapping, config);
            } catch (Exception e) {
                log.error("BeanUtils failed to copy properties", e);
                e.printStackTrace();
            }
        }
        return mapping;
    }

    /**
     * Read the Expresso-config.xml file, populating the configuration tree as we go
     *
     * This method utilizes the Strut's XML digester to perform it's configuration
     * readings.  When Struts 1.1 is eventually integrated, the package will move
     * to apache.commons package.
     *
     * @param configDir The directory that the expresso-config.xml resides in.
     * @throws ConfigurationException if something goes wrong in reading the XML
     *          file.
     */
    private void readXMLConfig(String configDir) throws ConfigurationException {
        Logger setupLog = Logger.getLogger(ConfigManager.class);
        setupLog.info("ConfigManager: Reading XML config");
        setSAXParser();
        Digester digester = new Digester();
        URL url = ConfigManager.class.getResource(EXPRESSO_DTD_COPY_LOCATION);
        if (url != null) {
            digester.register("-//Jcorporate Ltd//DTD Expresso Configuration 4.0//EN", url.toString());
        } else {
            ConfigurationException ce = new ConfigurationException("ConfigManager: Unable to get URL to the expresso-config dtd from: " + EXPRESSO_DTD_COPY_LOCATION + " relative to classes directory (DTD should be copied there).");
            setConfigurationFailureException(ce);
            throw ce;
        }
        myConfig = new ConfigExpresso();
        ConfigErrorHandler myHandler = new ConfigErrorHandler();
        digester.setErrorHandler(myHandler);
        digester.setUseContextClassLoader(true);
        this.setDigesterRules(myConfig, digester);
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(configDir + "/expresso-config.xml");
        } catch (FileNotFoundException fe) {
            ConfigurationException ce = new ConfigurationException("No such file as '" + configDir + "/expresso-config.xml");
            setConfigurationFailureException(ce);
            throw ce;
        }
        try {
            digester.parse(fin);
        } catch (IOException ie) {
            System.err.println("ConfigManager: IOException reading expresso-config.xml:");
            ie.printStackTrace(System.err);
            configurationFailed = true;
            setConfigurationFailureException(ie);
            throw new ConfigurationException("IO Exception reading expresso-config.xml");
        } catch (SAXException se) {
            System.err.println("ConfigManager: SAXException reading expresso-config.xml:");
            se.printStackTrace(System.err);
            setConfigurationFailureException(se);
            if (se.getException() != null) {
                System.err.println("ConfigManager: Nested SAX Exception:");
                se.getException().printStackTrace(System.err);
            }
            configurationFailed = true;
            throw new ConfigurationException("SAX Exception reading expresso-config.xml");
        } catch (IllegalArgumentException ia) {
            configurationFailed = true;
            setConfigurationFailureException(ia);
            throw new ConfigurationException("Illegal Setup Value Specified", ia);
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException ex) {
                    setupLog.error("Error closing file input stream", ex);
                }
            }
        }
        digester = null;
        System.gc();
        if (myHandler.anyErrorsOrWarnings()) {
            Exception oneException = null;
            Vector warnings = myHandler.getWarnings();
            if (warnings.size() > 0) {
                System.err.println("ConfigManager: XML warnings:");
                for (Enumeration ee2 = warnings.elements(); ee2.hasMoreElements(); ) {
                    oneException = (Exception) ee2.nextElement();
                    oneException.printStackTrace(System.err);
                    setConfigurationFailureException(oneException);
                }
            }
            Vector errors = myHandler.getErrors();
            if (errors.size() > 0) {
                System.err.println("ConfigManager: XML errors:");
                for (Enumeration ee = errors.elements(); ee.hasMoreElements(); ) {
                    oneException = (Exception) ee.nextElement();
                    oneException.printStackTrace(System.err);
                    setConfigurationFailureException(oneException);
                }
            }
            Exception fatal = myHandler.getFatalException();
            if (fatal != null) {
                System.err.println("ConfigManager: Fatal XML exception:");
                setConfigurationFailureException(oneException);
                fatal.printStackTrace(System.err);
            }
            configurationFailed = true;
            throw new ConfigurationException("Unable to parse expresso-config.xml successfully. " + "Please see your 'system.err' log file for details of the problem");
        }
        System.err.println("ConfigManager: Done reading XML config - no errors or warnings");
        ConfigContext oneContext = null;
        Vector contexts = myConfig.getContexts();
        int contextsize = contexts.size();
        for (int i = 0; i < contextsize; i++) {
            oneContext = (ConfigContext) contexts.elementAt(i);
            if (oneContext.isActive() == true) {
                allContexts.put(oneContext.getName(), oneContext);
            } else {
                contexts.removeElementAt(i);
                contextsize -= 1;
                i--;
            }
        }
        xmlConfigOk = true;
    }

    /**
     * Set up the rules for the digester
     * @param myConfig The root of the bean accepting the digester input
     * @param digester an instantiated Digester ready to accept the rules.
     */
    protected void setDigesterRules(ConfigExpresso myConfig, Digester digester) {
        digester.push(myConfig);
        digester.addSetProperties("expresso-config");
        digester.addCallMethod("expresso-config/logDirectory", "setLogDirectory", 0);
        digester.addCallMethod("expresso-config/strongCrypto", "setStrongCrypto", 0);
        digester.addCallMethod("expresso-config/cryptoKey", "setCryptoKey", 0);
        digester.addCallMethod("expresso-config/servletAPI", "setServletAPI", 0);
        digester.addCallMethod("expresso-config/encryptMode", "setEncryptMode", 0);
        digester.addCallMethod("expresso-config/httpPort", "setHttpPort", 0);
        digester.addCallMethod("expresso-config/sslPort", "setSslPort", 0);
        digester.addObjectCreate("expresso-config/cacheManager", "com.jcorporate.expresso.core.misc.ConfigCacheManager");
        digester.addSetProperties("expresso-config/context/cacheManager");
        digester.addSetNext("expresso-config/cacheManager", "addCacheManager", "com.jcorporate.expresso.core.misc.ConfigCacheManager");
        digester.addObjectCreate("expresso-config/class-handlers/class-handler", "com.jcorporate.expresso.core.misc.ConfigClassHandler");
        digester.addSetProperties("expresso-config/class-handlers/class-handler");
        digester.addSetNext("expresso-config/class-handlers/class-handler", "addClassHandler", "com.jcorporate.expresso.core.misc.ConfigClassHandler");
        digester.addObjectCreate("expresso-config/class-handlers/class-handler/handler-parameter", "com.jcorporate.expresso.core.misc.ConfigClassHandlerParameter");
        digester.addSetProperties("expresso-config/class-handlers/class-handler/handler-parameter");
        digester.addSetNext("expresso-config/class-handlers/class-handler/handler-parameter", "addParameter", "com.jcorporate.expresso.core.misc.ConfigClassHandlerParameter");
        digester.addObjectCreate("expresso-config/context", "com.jcorporate.expresso.core.misc.ConfigContext");
        digester.addSetProperties("expresso-config/context");
        digester.addCallMethod("expresso-config/context/description", "setDescription", 0);
        digester.addCallMethod("expresso-config/context/images", "setImages", 0);
        digester.addCallMethod("expresso-config/context/startJobHandler", "setStartJobHandler", 0);
        digester.addCallMethod("expresso-config/context/showStackTrace", "setShowStackTrace", 0);
        digester.addCallMethod("expresso-config/context/mailDebug", "setMailDebug", 0);
        digester.addCallMethod("expresso-config/context/expressoDir", "setExpressoDir", 0);
        digester.addCallMethod("expresso-config/context/hasSetupTables", "setHasSetupTables", 0);
        digester.addSetNext("expresso-config/context", "addContext", "com.jcorporate.expresso.core.misc.ConfigContext");
        digester.addObjectCreate("expresso-config/context/jdbc", "com.jcorporate.expresso.core.misc.ConfigJdbc");
        digester.addCallMethod("expresso-config/context/jdbc/dbWildcard", "addWildcard", 0);
        digester.addSetProperties("expresso-config/context/jdbc");
        digester.addSetNext("expresso-config/context/jdbc", "addJdbc", "com.jcorporate.expresso.core.misc.ConfigJdbc");
        digester.addObjectCreate("expresso-config/context/jdbc/jndi", "com.jcorporate.expresso.core.misc.ConfigJndi");
        digester.addSetProperties("expresso-config/context/jdbc/jndi");
        digester.addSetNext("expresso-config/context/jdbc/jndi", "addJndi", "com.jcorporate.expresso.core.misc.ConfigJndi");
        digester.addObjectCreate("expresso-config/context/path-mappings/path-mapping", "com.jcorporate.expresso.core.misc.ConfigPathMapping");
        digester.addSetProperties("expresso-config/context/path-mappings/path-mapping");
        digester.addSetNext("expresso-config/context/path-mappings/path-mapping", "addPathMapping", "com.jcorporate.expresso.core.misc.ConfigPathMapping");
        digester.addCallMethod("expresso-config/context/path-mappings/path-mapping/url-pattern", "setUrlPattern", 0);
        digester.addCallMethod("expresso-config/context/path-mappings/path-mapping/path", "setPath", 0);
        digester.addObjectCreate("expresso-config/context/path-mappings/path-mapping/param", "com.jcorporate.expresso.core.misc.ConfigPathParam");
        digester.addSetNext("expresso-config/context/path-mappings/path-mapping/param", "addParam", "com.jcorporate.expresso.core.misc.ConfigPathParam");
        digester.addObjectCreate("expresso-config/context/path-mappings/path-mapping/fixed-param", "com.jcorporate.expresso.core.misc.ConfigPathFixedParam");
        digester.addSetNext("expresso-config/context/path-mappings/path-mapping/fixed-param", "addFixedParam", "com.jcorporate.expresso.core.misc.ConfigPathFixedParam");
        digester.addCallMethod("expresso-config/context/path-mappings/path-mapping/param/param-name", "setName", 0);
        digester.addCallMethod("expresso-config/context/path-mappings/path-mapping/param/param-number", "setNumber", 0);
        digester.addCallMethod("expresso-config/context/path-mappings/path-mapping/fixed-param/param-name", "setName", 0);
        digester.addCallMethod("expresso-config/context/path-mappings/path-mapping/fixed-param/param-value", "setValue", 0);
        digester.addObjectCreate("expresso-config/context/ldap", "com.jcorporate.expresso.core.misc.ConfigLdap");
        digester.addSetProperties("expresso-config/context/ldap");
        digester.addSetNext("expresso-config/context/ldap", "addLdap", "com.jcorporate.expresso.core.misc.ConfigLdap");
        digester.addObjectCreate("expresso-config/context/setupDefault", "com.jcorporate.expresso.core.misc.ConfigSetupDefault");
        digester.addSetProperties("expresso-config/context/setupDefault");
        digester.addSetNext("expresso-config/context/setupDefault", "addSetupDefault", "com.jcorporate.expresso.core.misc.ConfigSetupDefault");
        digester.addObjectCreate("expresso-config/context/type-mapping", "com.jcorporate.expresso.core.misc.ConfigTypeMapping");
        digester.addSetNext("expresso-config/context/type-mapping", "addTypeMapping", "com.jcorporate.expresso.core.misc.ConfigTypeMapping");
        digester.addCallMethod("expresso-config/context/type-mapping/java-type", "setJavaType", 0);
        digester.addCallMethod("expresso-config/context/type-mapping/db-type", "setDBType", 0);
        digester.addCallMethod("expresso-config/context/type-mapping/expresso-type", "setExpressoType", 0);
        digester.addObjectCreate("expresso-config/context/customProperty", "com.jcorporate.expresso.core.misc.ConfigCustomProperty");
        digester.addSetNext("expresso-config/context/customProperty", "addCustomProperty", "com.jcorporate.expresso.core.misc.ConfigCustomProperty");
        digester.addSetProperties("expresso-config/context/customProperty");
        digester.addCallMethod("expresso-config/context/customProperty/name", "setName", 0);
        digester.addCallMethod("expresso-config/context/customProperty/value", "setValue", 0);
        digester.setValidating(true);
    }

    protected void setSAXParser() {
        new SaxParserConfigurer();
    }
}
