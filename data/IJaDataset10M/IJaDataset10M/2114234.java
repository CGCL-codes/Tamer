package sc.fgrid.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.GZIPInputStream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hsqldb.DatabaseManager;
import org.hsqldb.Server;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import sc.fgrid.common.Constants;
import sc.fgrid.common.JavaVersion;
import sc.fgrid.common.JaxbValidationEventHandler;
import sc.fgrid.common.Status;
import sc.fgrid.common.Util;
import sc.fgrid.config.ConfigType;
import sc.fgrid.config.DBServerModeEnum;
import sc.fgrid.config.LogLevelEnum;
import sc.fgrid.script.AccessEnum;
import sc.fgrid.script.NextType;
import sc.fgrid.script.TaskExitAgentCommandEnum;
import sc.fgrid.types.ActionEnum;
import sc.fgrid.types.ActionResponse;
import sc.fgrid.types.InstanceCredType;
import sc.fgrid.types.InstanceInfo;
import sc.fgrid.types.Notification;
import sc.fgrid.types.NotificationEventEnum;
import sc.fgrid.types.ServiceDescription;
import sc.fgrid.types.VariableValue;
import com.ice.tar.TarArchive;

/**
 * The class which represents the sc.fgrid Engine from an outside. Design goal
 * is that everything done on this object should be persistent. Currently only
 * Instances are persistent. Agents are not persisted right now, means after a
 * restart Enging thinks no agents are running (this is true since right now
 * only local agents).
 * 
 * Engine is a singleton.
 * 
 */
public class Engine {

    /**
     * This will check for unused NotificationHolders, which have not been
     * touched for a while, and clean them up. There is one HolderCleanupTask
     * object for each session, but there should be only one Timer (thus thread)
     * for all of them together for better performance.
     */
    class HolderCleanupTask extends TimerTask {

        long deadtime;

        String clientID;

        /**
         * @param deadtime
         *            the time after last access when a holder can be removed
         * @param clientID
         */
        HolderCleanupTask(long deadtime, String clientID) {
            this.deadtime = deadtime;
            this.clientID = clientID;
        }

        public void run() {
            synchronized (notificationLock) {
                NotificationHolder holder = notificationHolders.get(clientID);
                if (holder == null) {
                    log.warn("Holder was null!");
                    return;
                }
                long now = new Date().getTime();
                long access = holder.accessTime.getTime();
                if ((!holder.isPolling()) && (now - access) > deadtime) {
                    cancel();
                    notificationHolders.remove(clientID);
                    log.debug("Removed notification holder for session " + clientID);
                }
            }
        }
    }

    ;

    private static Logger log = Logger.getLogger(Engine.class);

    private String contextPath = null;

    private URL agentBaseURL = null;

    private File fg_root;

    /**
     * This is where the servicescript is stored. Could become a path instead of
     * a File on day.
     */
    private File servicescriptdir;

    /**
     * A directory to sort temporary instance data, as generated scripts.
     */
    private File workdir;

    /**
     * The directory where, if applicable, the database file is. Database can
     * also be in memory or server, then this is null.
     */
    private File databaseDir;

    private org.hsqldb.Server dbServer;

    private String dbURL = null;

    private final String dbUser = "sa";

    private EntityManagerFactory entityManagerFactory;

    private ConfigType config = null;

    private ConcurrentHashMap<String, Service> services = new ConcurrentHashMap<String, Service>();

    /**
     * All currently started or running agents. key is AgentID.
     */
    private ConcurrentHashMap<String, AgentProxy> agentProxies = new ConcurrentHashMap<String, AgentProxy>();

    private ConcurrentHashMap<Long, ReentrantReadWriteLock> instanceLocks = new ConcurrentHashMap<Long, ReentrantReadWriteLock>();

    private ConcurrentHashMap<String, NotificationHolder> notificationHolders = new ConcurrentHashMap<String, NotificationHolder>();

    private final Object notificationLock = new Object();

    private final Timer notificationHolderCleanupTimer = new Timer("notification-holder-cleanup-timer", true);

    /**
     * @return the configuration object from the engine.
     */
    public ConfigType getConfig() {
        return config;
    }

    /**
     * Construct an Engine which is not accessible from Web-Service (has no
     * context path). A shortcut with contactPath==null;
     */
    public Engine(File fgRoot) throws ConfigException {
        this(null, fgRoot);
    }

    /**
     * Construct an Engine which is accessible from Web-Service (has context
     * path). Engine is supposed to be singleton. The constructor may be called
     * only once, but will throw an exception otherwise. One needs to call
     * init() after constructor. One can get the stack trace option after
     * constructing, this is why init() is separated.
     * 
     * @param contextPath
     *            see getAgentContextURL, this is the part after the
     *            AgentBaseURL (usually 'fgrid').
     */
    public Engine(String contextPath, File fgRoot) throws ConfigException {
        this.contextPath = contextPath;
        String error_message = JavaVersion.checkRequiredVersion();
        if (error_message != null) {
            log.fatal(error_message);
            throw new ConfigException(error_message);
        }
        fg_root = fgRoot;
        readConfig();
    }

    /**
     * To be called after constructor Engine(). This is separated, because the
     * constructor of Engine() shall only read configuration, which then is
     * needed to tell stack trace option.
     */
    public void init() throws InternalException {
        setupDB();
        setupWorkdir();
        readServicescript();
    }

    /**
     * Read config file and set some of the default. Some other methods, as
     * setupDB do also change the config object.
     */
    private void readConfig() throws ConfigException {
        Unmarshaller configUnmarshaller;
        try {
            javax.xml.bind.JAXBContext jaxbcontext = javax.xml.bind.JAXBContext.newInstance("sc.fgrid.config");
            configUnmarshaller = jaxbcontext.createUnmarshaller();
            java.net.URL schemaURL = this.getClass().getClassLoader().getResource("sc/fgrid/config.xsd");
            javax.xml.validation.SchemaFactory sf = javax.xml.validation.SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
            javax.xml.validation.Schema schema = sf.newSchema(schemaURL);
            configUnmarshaller.setSchema(schema);
            ValidationEventHandler veh = new JaxbValidationEventHandler(null, log, null);
            configUnmarshaller.setEventHandler(veh);
        } catch (Exception ex) {
            throw new RuntimeException("Problem creating unmarshaler for config.xsd!", ex);
        }
        File configFile = getConfigFile();
        if (configFile.exists()) {
            try {
                javax.xml.bind.JAXBElement<ConfigType> root = configUnmarshaller.unmarshal(new javax.xml.transform.stream.StreamSource(configFile), ConfigType.class);
                config = root.getValue();
            } catch (JAXBException ex) {
                throw new ConfigException("when reading config file " + configFile.getAbsolutePath(), ex);
            }
            log.info("Config file " + configFile.getAbsolutePath() + " is read in.");
        } else {
            sc.fgrid.config.ObjectFactory of = new sc.fgrid.config.ObjectFactory();
            config = of.createConfigType();
            log.fatal("Config file '" + configFile.getAbsolutePath() + "' not found!");
            throw new ConfigException("Config file '" + configFile.getAbsolutePath() + "' not found!");
        }
        if (config.getLogLevel() == null) {
            config.setLogLevel(LogLevelEnum.INFO);
        }
        log.info("Set log level to " + config.getLogLevel().value());
        Level newLevel = Level.toLevel(config.getLogLevel().value().toUpperCase());
        Logger.getLogger("sc.fgrid").setLevel(newLevel);
        if (config.isStackTraceToClient() == null) {
            config.setStackTraceToClient(false);
        }
        if (config.isStackTraceToClient()) {
            System.setProperty("com.sun.xml.ws.fault.SOAPFaultBuilder." + "disableCaptureStackTrace", "false");
        } else {
            System.setProperty("com.sun.xml.ws.fault.SOAPFaultBuilder." + "disableCaptureStackTrace", "true");
        }
        if (config.getServerAliveInterval() == null) {
            double timeout = Constants.serverDefaultNotificationTimeoutSeconds;
            config.setServerAliveInterval(timeout);
            log.debug("Set notification timeout value to default");
        }
        log.debug("Notification timeout = " + config.getServerAliveInterval() + " seconds.");
        if (config.getServerHostnameFromAgent() == null) {
            try {
                java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
                String localHost = localMachine.getHostName();
                config.setServerHostnameFromAgent(localHost);
            } catch (UnknownHostException ex) {
                throw new ConfigException("Configuration variable ServerHostnameFromAgent " + "neither set nor able to find it automatically", ex);
            }
        }
        if (config.getServerPortFromAgent() == null) {
            int port = Constants.defaultServerPortFromAgent;
            config.setServerPortFromAgent(port);
        }
        try {
            agentBaseURL = new URL("http", config.getServerHostnameFromAgent(), config.getServerPortFromAgent(), "");
            log.debug("host part of URL for agent to connect the server = " + agentBaseURL.toString());
        } catch (MalformedURLException ex) {
            throw new ConfigException("URL for agent is invalid, please check: \n" + "  serverHostnameFromAgent = " + config.getServerHostnameFromAgent() + "\n" + "  serverPortFromAgent     = " + config.getServerPortFromAgent(), ex);
        }
        if (config.getWorkDir() == null) {
            String workDir = Constants.defaultEngineWorkDir;
            config.setWorkDir(workDir);
        }
        String wdir = config.getWorkDir();
        workdir = (new File(wdir).isAbsolute()) ? new File(wdir) : new File(fg_root, wdir);
        log.debug("WorkDir = " + workdir.getAbsolutePath());
        if (config.getServicescriptDir() == null) {
            String instanceDir = Constants.defaultServicesDir;
            config.setServicescriptDir(instanceDir);
        }
        String fsp = config.getServicescriptDir();
        servicescriptdir = (new File(fsp).isAbsolute()) ? new File(fsp) : new File(fg_root, fsp);
        log.debug("servicescriptDir = " + servicescriptdir.getAbsolutePath());
        String python_home = System.getProperty("python.home");
        if (python_home == null) {
            String jython = Constants.defaultPythonHomeDir;
            python_home = new File(fg_root, jython).getAbsolutePath();
            System.setProperty("python.home", python_home);
            log.info("Setting python.home to " + python_home);
        } else {
            log.info("Using predefined python.home: " + python_home);
        }
        String python_cachedir = System.getProperty("python.cachedir");
        if (python_cachedir == null) {
            String pc_string = Constants.defaultPythonCacheDir;
            python_cachedir = new File(workdir, pc_string).getAbsolutePath();
            System.setProperty("python.cachedir", python_cachedir);
            log.info("Setting python.cachedir to " + python_cachedir);
        } else {
            log.info("Using predefined python.cachedir: " + python_cachedir);
        }
    }

    private void setupWorkdir() throws ConfigException {
        if (workdir.exists()) {
            if (!workdir.isDirectory()) throw new ConfigException("workdir " + workdir.getAbsolutePath() + " is not a directory!");
        } else {
            workdir.mkdir();
        }
        File agentdir = new File(getAgentPythonPath());
        if (!agentdir.exists()) {
            InputStream istream = this.getClass().getClassLoader().getResourceAsStream("sc/fgrid/agent.tar.gz");
            try {
                GZIPInputStream gzipInputStream;
                gzipInputStream = new GZIPInputStream(istream);
                TarArchive tarArchive = new TarArchive(gzipInputStream);
                tarArchive.extractContents(agentdir);
            } catch (IOException ex) {
                throw new ConfigException("Could not extract agent.tar.gz to " + agentdir.getAbsolutePath(), ex);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setupDB() throws InternalException {
        if (false && log.isDebugEnabled()) {
            log.debug("--- List of all Java System Properties ---");
            Properties props = System.getProperties();
            Enumeration propenum = props.propertyNames();
            while (propenum.hasMoreElements()) {
                String ps = (String) propenum.nextElement();
                log.debug(ps + "=" + props.getProperty(ps));
            }
            log.debug("--- End of List of all Java System Properties ---");
        }
        if (config.getDBServerMode() == null) {
            config.setDBServerMode(DBServerModeEnum.FILE);
        }
        log.debug("DBServerMode=" + config.getDBServerMode().value());
        if (config.getDBDir() == null) {
            config.setDBDir(Constants.defaultDBDir);
        }
        log.debug("DBDir=" + config.getDBDir());
        if (config.getDBWriteDelayMillis() == null) {
            config.setDBWriteDelayMillis(Constants.defaultDBWriteDelayMillis);
        }
        log.debug("DBWriteDelayMillis=" + config.getDBWriteDelayMillis());
        if (config.getDBHost() == null) {
            config.setDBHost(Constants.defaultDBHost);
        }
        log.debug("DBHost=" + config.getDBHost());
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Driver class not found. " + ex);
        }
        String databasePath = null;
        String ddl_generation = null;
        DBServerModeEnum dbServerMode = config.getDBServerMode();
        databaseDir = fg_root;
        switch(dbServerMode) {
            case FILE:
            case INTERN:
                {
                    String db_dir = config.getDBDir();
                    databaseDir = (new File(db_dir).isAbsolute()) ? new File(db_dir) : new File(fg_root, db_dir);
                    String fileBase = Constants.defaultDBFile;
                    databasePath = new File(databaseDir, fileBase).getAbsolutePath();
                    log.debug("DatabaseDir=" + databaseDir.getAbsolutePath());
                    log.debug("DatabasePath=" + databasePath);
                    if (dbServerMode == DBServerModeEnum.FILE) dbURL = new String("jdbc:hsqldb:file:" + databasePath); else if (dbServerMode == DBServerModeEnum.INTERN) dbURL = "jdbc:hsqldb:hsql://localhost"; else throw new RuntimeException("dbServerMode invalid");
                    File databaseFile = new File(databasePath + ".properties");
                    File databasePasswordFile = new File(databasePath + ".password");
                    if (databaseFile.exists()) {
                        ddl_generation = "none";
                        if (!databasePasswordFile.exists()) {
                            throw new InternalException("Database password file " + databasePasswordFile.getAbsolutePath() + " is missing for existing database!\n" + "Hint: remove all database files and start again.");
                        }
                        try {
                            String dbPassword = new BufferedReader(new FileReader(databasePasswordFile)).readLine();
                            config.setDBPassword(dbPassword);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException("existence of db-passwd should have been checked", ex);
                        } catch (IOException ex) {
                            throw new ConfigException("Problem reading database password file", ex);
                        }
                        log.debug("read database passwort from " + databasePasswordFile.getPath());
                    } else {
                        log.warn("Database file " + databaseFile.getAbsolutePath() + " does not exist, create a new (empty) one");
                        ddl_generation = "create-tables";
                        if (config.getDBPassword() == null) {
                            config.setDBPassword(Long.toHexString(new Random().nextLong()));
                        }
                        try {
                            Connection conn = DriverManager.getConnection(dbURL, dbUser, "");
                            String delay = config.getDBWriteDelayMillis().toString();
                            log.info("Set database write delay to " + delay + " milliseconds.");
                            Statement st = conn.createStatement();
                            st.execute("SET WRITE_DELAY " + 0 + " MILLIS");
                            FileWriter fw = new FileWriter(databasePasswordFile);
                            fw.write(config.getDBPassword());
                            fw.close();
                            st = conn.createStatement();
                            st.execute("ALTER USER sa SET PASSWORD\"" + config.getDBPassword() + "\"");
                            st.execute("SET WRITE_DELAY " + delay + " MILLIS");
                            conn.close();
                        } catch (SQLException ex) {
                            throw new RuntimeException("Problem to configure the database!", ex);
                        } catch (IOException ex) {
                            throw new ConfigException("Problem writing database password file!", ex);
                        }
                        log.info("Database configured.");
                    }
                }
                break;
            case MEMORY:
                ddl_generation = "create-tables";
                dbURL = "jdbc:hsqldb:mem:fgdb";
                config.setDBPassword("");
                break;
            case EXTERN:
                ddl_generation = "none";
                dbURL = "jdbc:hsqldb:hsql://" + config.getDBHost();
                if (config.getDBPassword() == null) {
                    throw new ConfigException("DBServerMode=external requires password in options file");
                }
                break;
            default:
                throw new RuntimeException("serverMode unknown!");
        }
        log.debug("dbURL=" + dbURL);
        if (config.getDBServerMode() == DBServerModeEnum.INTERN) {
            log.info("Starting database server ...");
            log.info("databasePath=" + databasePath);
            dbServer = new Server();
            dbServer.setSilent(true);
            dbServer.setDatabasePath(0, databasePath);
            dbServer.setPort(Constants.internalDBServerPort);
            dbServer.setDatabaseName(0, "");
            dbServer.start();
            log.info("dbServer Address = " + dbServer.getAddress());
            log.info("dbServer DatabaseName(0,true) = " + dbServer.getDatabaseName(0, true));
            log.info("dbServer DatabaseName(0,false) = " + dbServer.getDatabaseName(0, false));
            log.info("dbServer DatabasePath(0,true) = " + dbServer.getDatabasePath(0, true));
            log.info("dbServer DatabasePath(0,false) = " + dbServer.getDatabasePath(0, false));
            log.info("dbServer DatabaseType(0) = " + dbServer.getDatabaseType(0));
            log.info("dbServer Port = " + dbServer.getPort());
            log.info("dbServer Protocol = " + dbServer.getProtocol());
            log.info("dbServer NoSystemExit = " + dbServer.isNoSystemExit());
            log.info("dbServer RestartOnShutdown = " + dbServer.isRestartOnShutdown());
            log.info("dbServer Silent = " + dbServer.isSilent());
            log.info("dbServer TLS = " + dbServer.isTls());
            log.info("dbServer Trace = " + dbServer.isTrace());
            log.info("... database server started");
        }
        Map<String, String> persistanceProps = new HashMap<String, String>();
        persistanceProps.put("toplink.jdbc.user", dbUser);
        persistanceProps.put("toplink.ddl-generation.output-mode", "database");
        if (dbServerMode == DBServerModeEnum.FILE || dbServerMode == DBServerModeEnum.INTERN) {
            persistanceProps.put("toplink.application-location", databaseDir.getAbsolutePath());
        }
        persistanceProps.put("toplink.jdbc.password", config.getDBPassword());
        persistanceProps.put("toplink.jdbc.url", dbURL);
        persistanceProps.put("toplink.ddl-generation", ddl_generation);
        entityManagerFactory = Persistence.createEntityManagerFactory("persistence1", persistanceProps);
        {
            EntityManager em = entityManagerFactory.createEntityManager();
            em.close();
        }
    }

    /** Get the location for the auth file (from config file or default) */
    public File getAuthFile() {
        if (config.getAuthFile() == null) {
            String confDir = Constants.engine_confDir;
            String defaultAuthFile = Constants.engine_defaultAuthFile;
            config.setAuthFile(new File(confDir, defaultAuthFile).getPath());
        }
        String afile = config.getAuthFile();
        File authFile = (new File(afile).isAbsolute()) ? new File(afile) : new File(fg_root, afile);
        return authFile;
    }

    /**
     * This method is called if the service is shut doen, e.g. if the server
     * stopped. Thus this method will be called exactly once.
     */
    public void shutDown() {
        log.info("Closing database connection ...");
        boolean closed = false;
        int sleeptime = Constants.engineShutDownLoopSleepSeconds;
        while (!closed) {
            try {
                entityManagerFactory.close();
                closed = true;
            } catch (IllegalStateException ex) {
                log.error("Close of EntityManager (for database) failed.\n" + "Likely that some transactions are going on.\n" + "I will try again in " + sleeptime + " seconds");
                try {
                    Thread.sleep(1000 * sleeptime);
                } catch (InterruptedException ex2) {
                    log.error("sleep interrupeted");
                }
            }
            log.debug("hsqldb timer shutdown");
            DatabaseManager.getTimer().shutDown();
        }
        log.info("Database connection closed, now " + "shutting down database (Server) ... ");
        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUser, config.getDBPassword());
            if (!conn.isClosed()) {
                if (config.getDBServerMode() != DBServerModeEnum.EXTERN) {
                    Statement st = conn.createStatement();
                    st.execute("SHUTDOWN");
                }
                conn.close();
            }
        } catch (SQLException ex) {
            log.error("Exception during shutdown of database! ", ex);
        }
        try {
            synchronized (notificationLock) {
                notificationHolderCleanupTimer.cancel();
                for (NotificationHolder holder : notificationHolders.values()) {
                    String clientID = holder.clientID;
                    long now = new Date().getTime();
                    long access = holder.accessTime.getTime();
                    double age = (now - access) / 1000.0;
                    log.info("Removed notification task from user " + holder.user + " age = " + age + " seconds, polling=" + holder.isPolling() + " notification holder clientID=" + clientID);
                }
                notificationHolders.clear();
            }
        } finally {
        }
        if (false) {
            double sleepseconds = 0.3;
            log.info("... Lifecycle shutdown, give timers " + sleepseconds + " seconds, then end of loggin. ");
            log.info("Good by!");
            LogManager.shutdown();
            log.debug("LogManager-shutdown vorbei!");
            try {
                Thread.sleep((int) (sleepseconds * 1000));
            } catch (Exception e) {
            }
            log.debug("sleep vorbei");
        }
    }

    public synchronized String readServicescript() throws ServicescriptException {
        StringBuffer sb = new StringBuffer();
        Unmarshaller fsUnmarshaller;
        try {
            javax.xml.bind.JAXBContext jaxbcontext = javax.xml.bind.JAXBContext.newInstance("sc.fgrid.script");
            fsUnmarshaller = jaxbcontext.createUnmarshaller();
            java.net.URL schemaURL = this.getClass().getClassLoader().getResource("sc/fgrid/script.xsd");
            javax.xml.validation.SchemaFactory sf = javax.xml.validation.SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
            javax.xml.validation.Schema schema = sf.newSchema(schemaURL);
            fsUnmarshaller.setSchema(schema);
            ValidationEventHandler veh = new JaxbValidationEventHandler(sb, log, null);
            fsUnmarshaller.setEventHandler(veh);
        } catch (JAXBException ex) {
            throw new RuntimeException("Problem creating unmarshaller for servicescript schema!", ex);
        } catch (SAXException ex) {
            throw new RuntimeException("Problem creating unmarshaller for servicescript schema!", ex);
        }
        boolean recursive = true;
        String extensions[] = Constants.serviceScriptFileExtensions;
        Collection<File> scripts = FileUtils.listFiles(servicescriptdir, extensions, recursive);
        if (scripts.isEmpty()) {
            String message = "Found not even one instance script. \n" + "If this is not intended, check servicescriptDir";
            log.info(message);
            sb.append("<p>" + message + "</p>");
        }
        for (File iscript : scripts) {
            File relfile = Util.absoluteFile2relativeFile(servicescriptdir, iscript);
            String serviceID = null;
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(iscript);
                Element element = document.getDocumentElement();
                String nodeName = element.getNodeName();
                String name = nodeName.substring(nodeName.lastIndexOf(':') + 1);
                if (!Constants.servicescriptTag.equals(name)) {
                    String message = "Skipping because it contains no <" + Constants.servicescriptTag + "> element: " + iscript.getAbsolutePath();
                    log.info(message);
                    sb.append("<p>" + message + "</p>");
                    continue;
                }
                Attr attr_ID = element.getAttributeNode(Constants.xmlAttributeID);
                if (attr_ID != null) {
                    serviceID = attr_ID.getValue();
                } else {
                    String message = "Skipping because it contains no " + Constants.xmlAttributeID + " attribute: " + iscript.getAbsolutePath();
                    log.error(message);
                    sb.append("<p>" + message + "</p>");
                    continue;
                }
            } catch (Exception ex) {
                String message = "Could not parse XML file " + iscript.getAbsolutePath() + "\n" + ex.getMessage();
                log.error(message, ex);
                sb.append("<p>" + message + "</p>");
                continue;
            }
            if (services.containsKey(serviceID)) {
                String message = "Found servicescript " + relfile.toString() + " with serviceID=" + serviceID + " already read in, skipped!";
                log.info(message);
                sb.append("<p>" + message + "</p>");
                continue;
            }
            Service ft = null;
            try {
                ft = new Service(iscript, fsUnmarshaller);
            } catch (JAXBException ex) {
                String message = "servicescript " + relfile.toString() + " ignored because could not parse it (JAXB)!?!";
                log.error(message, ex);
                sb.append("<p>" + message + "</p>");
            } catch (ServicescriptException ex) {
                String message = "Error in servicescript " + relfile.toString() + "\n";
                if (config.isStackTrace()) {
                    message += Util.getStackTrace(ex);
                } else {
                    message += Util.getMessageChain(ex);
                }
                message += "\nservicescript " + relfile.toString() + " ignored because of errors!";
                log.error(message, ex);
                sb.append("<p>" + message + "</p>");
            }
            if (ft != null) {
                services.put(ft.getID(), ft);
                String message = "Found new servicescript " + iscript.getName() + " with serviceID=" + ft.getID() + ", read in!";
                log.info(message);
                sb.append("<p>" + message + "</p>");
            }
        }
        Notification notification = new Notification();
        notification.setEvent(NotificationEventEnum.SERVICES_CHANGED);
        synchronized (notificationLock) {
            for (NotificationHolder holder : notificationHolders.values()) {
                holder.put(notification);
            }
        }
        return sb.toString();
    }

    /**
     * Returns the 'workdir', where instances can temporarily store stuff, i.e.
     * scripts
     * 
     * @return always a directory
     */
    public File getWorkdir() {
        return workdir;
    }

    /**
     * Directory where the database files are, if any (may be null)
     * 
     * @return null or a directory
     */
    public File getDatabesDir() {
        return databaseDir;
    }

    public String getAgentPythonPath() {
        return new File(workdir, "agent").getAbsolutePath();
    }

    /**
     * The root directory for the configuration, where config, work, instances,
     * and so on is, but not necessarily the installation directory.
     * 
     * @return FG_ROOT, or what is used for it.
     */
    public File getFgRoot() {
        return fg_root;
    }

    public File getServicescriptDir() {
        return servicescriptdir;
    }

    public File getConfigFile() {
        String confDir = Constants.engine_confDir;
        String confFile = Constants.engine_confFile;
        return new File(new File(fg_root, confDir), confFile);
    }

    /**
     * The returned EntityManager has an 'extended persistence context'. The
     * entities are still managed between two transactions (unless you call
     * entityManager.clear() in between).
     */
    public EntityManager createEM() {
        return entityManagerFactory.createEntityManager();
    }

    /** Get a set with the unique IDs of all servicees. */
    public Set<String> getServiceIDs() {
        return services.keySet();
    }

    /** Get the object which represents this service. */
    public Service getService(String serviceID) {
        return services.get(serviceID);
    }

    /**
     * Get a short description.
     */
    public ServiceDescription getServiceDescription(String serviceID) {
        Service service = services.get(serviceID);
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setDescription(service.getDescription());
        serviceDescription.setName(service.getName());
        serviceDescription.setServiceID(serviceID);
        return serviceDescription;
    }

    /**
     * @return the documentation which is in the config file in the
     *         Documentation entry.
     */
    public String getDocumentation() {
        String docu = config.getDocumentation();
        return docu == null ? "There is no documentation for this server." : docu;
    }

    /**
     * Is this engine running in a server which can be accessed from outside by
     * web services?
     */
    private boolean isServer() {
        return contextPath != null;
    }

    /**
     * A relative path to the Client Port. Prepend getContextURL() to get the
     * full path, i.e. new URL(getContextURL()+"/"+ getServiceURLPath()), which
     * could result in http://localhost:8080/fgrid/client
     * 
     * @return the relative path of the client port.
     */
    public String getClientURLPath() throws IOException {
        return Constants.wsClientURLPath;
    }

    /**
     * A relative path to the data Port. Prepend getContextURL() to get the full
     * path, i.e. new URL(getContextURL()+"/"+ getDataURLPath()), which could
     * result in http://localhost:8080/fgrid/data
     * 
     * @return the relative path of the data port.
     */
    public String getDataURLPath() throws IOException {
        return Constants.wsDataURLPath;
    }

    /**
     * A relative path to the Agent Port. Prepend getAgentURLPath() to get the
     * full path, i.e. new URL(getContextURL()+"/"+ getAgentURLPath()), which
     * could result in http://localhost:8080/fgrid/agent
     * 
     * @return the relative path of the agent port.
     */
    public String getAgentURLPath() throws IOException {
        return Constants.wsAgentURLPath;
    }

    /**
     * A relative path to the Web Interface. Prepend getContextURL() to get the
     * full path, i.e. new URL(getContextURL() +"/"+ getWebURLPath()), which
     * could result in http://localhost:8080/fgrid/index.html
     * 
     * @return the reative path ot the web.
     */
    public String getWebURLPath() throws IOException {
        return Constants.webURLPath;
    }

    public String getInstanceURLPath() throws IOException {
        return Constants.instanceURLPath;
    }

    /**
     * In case of several network interfaces, this should be used by the Agent.
     * Return the part of the service URL which is the same for all services in
     * the war-file. It is the most precise base for all other URLs in this web
     * application. I.e if the service interface is
     * http://saentis:8080/fgrid/services, then this method returns
     * http://saentis:8080/fgrid
     * 
     * It is not inteded for other clients beside the agent. Those shall connect
     * to an URL relative to the one by which they did the request.
     * 
     * @throws IOException
     *             if no valid URL could be created, which indicates
     *             missconfiguration by the administrator in config.xml .
     */
    public URL getAgentContextURL() throws ConfigException {
        if (contextPath == null) {
            throw new ConfigException("This engine has no way for Agents to connect (internal)" + " and does not allows external agents (yet)!");
        }
        try {
            return new URL(agentBaseURL.toString() + contextPath);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Problem with this URL:" + agentBaseURL.toString() + contextPath);
        }
    }

    public double getTimeout() {
        return config.getServerAliveInterval();
    }

    /**
     * Does not change the status of a task but changes the status variable
     * only. This is more then a conveninace method which sets the corresponding
     * variable, because it will will also do event handling of the 'next' here,
     * i.e. if another task shall be started if this one is finished.
     * 
     * @param taskName
     *            is name of task
     * @param new_status
     *            see class Task for status.
     */
    public void setTaskStatus(String agentID, InstanceCredType instanceCred, String taskName, Status new_status) throws EngineException {
        InstanceRunableSetTaskStatus runable = new InstanceRunableSetTaskStatus();
        runable.agentID = agentID;
        runable.taskName = taskName;
        runable.new_status = new_status;
        boolean write = true;
        runWithInstance(instanceCred, runable, write);
    }

    private class InstanceRunableSetTaskStatus extends Engine.InstanceRunnable {

        String agentID;

        String taskName;

        Status new_status;

        public void run() throws EngineException {
            setTaskStatus(em, agentID, instance, taskName, new_status);
        }
    }

    private void setTaskStatus(EntityManager em, String agentID, Instance instance, String taskName, Status new_status) throws EngineException {
        String var_name = Constants.VAR_STATUS_TASK_PREFIX + taskName;
        VarValue new_value = new VarValue(var_name, new_status.ordinal());
        VarValue old_value = instance.getVarValue(var_name, Caller.INTERNAL);
        long old_status_long = old_value.getLongValue();
        Status old_status = Status.fromOrdinal((int) old_status_long);
        if (new_status == old_status) return;
        updateVariable(em, instance, new_value, Caller.INTERNAL);
        String agentName = AgentProxy.extractAgentName(agentID);
        Status agent_status = getAgentStatus(instance, agentName);
        Status agent_status_fallback = StatusTransistions.agentStatusFallback(agent_status, new_status);
        if (agent_status_fallback != agent_status) {
            throw new EngineException("Agent status inconsistend? agent_status=" + agent_status + " task_status=" + new_status);
        }
        boolean tmp_was_QR = old_status == Status.QUEUED || old_status == Status.RUNNING;
        boolean tmp_is_FFT = new_status == Status.FINISHED || new_status == Status.FAILED || new_status == Status.TERMINATED;
        if (tmp_was_QR && tmp_is_FFT) {
            Service service = instance.getService();
            Task task = service.getTask(taskName);
            TaskExitAgentCommandEnum topAgentCommand = TaskExitAgentCommandEnum.DEFAULT;
            Set<String> nextTaskSet = new HashSet<String>();
            for (NextType next : task.getNext()) {
                boolean condition = true;
                String conditionVar = next.getCondition();
                if (conditionVar != null) {
                    VarValue vv = instance.getVarValue(conditionVar);
                    condition = vv.getBooleanValue();
                }
                boolean x1 = next.isIfFinished() && new_status == Status.FINISHED;
                boolean x2 = next.isIfTerminated() && new_status == Status.TERMINATED;
                boolean x3 = next.isIfFailed() && new_status == Status.FAILED;
                boolean doAction = condition && (x1 || x2 || x3);
                if (doAction) {
                    String next_task = next.getTask();
                    if (next_task != null) {
                        nextTaskSet.add(next_task);
                    }
                    TaskExitAgentCommandEnum agentCommand = next.getAgentCommand();
                    if (agentCommand != null) {
                        if (agentCommand.ordinal() < topAgentCommand.ordinal()) {
                            topAgentCommand = agentCommand;
                        }
                    }
                }
            }
            if (topAgentCommand.ordinal() <= TaskExitAgentCommandEnum.FINISH.ordinal()) {
                nextTaskSet.clear();
            }
            Set<String> agentTasks = service.getTaskNames(agentName);
            if (topAgentCommand.ordinal() <= TaskExitAgentCommandEnum.FINISH.ordinal()) {
                boolean removed = nextTaskSet.removeAll(agentTasks);
                if (removed) {
                    log.warn("in <next /> removed 'next task' elements because FINISH at same time.");
                }
            }
            for (String next_task : nextTaskSet) {
                queueTask(em, next_task, service, instance);
            }
            AgentProxy agentProxy = agentProxies.get(agentID);
            if (agentProxy == null) {
                throw new RuntimeException("agentProxy == null");
            }
            switch(topAgentCommand) {
                case KILL:
                case STOP:
                    if (!nextTaskSet.isEmpty()) throw new RuntimeException("nextTaskSet not empty!");
                case FINISH:
                    {
                        String agent_command;
                        if (topAgentCommand == TaskExitAgentCommandEnum.STOP) agent_command = Agent.STOP; else if (topAgentCommand == TaskExitAgentCommandEnum.FINISH) agent_command = Agent.FINISH; else if (topAgentCommand == TaskExitAgentCommandEnum.KILL) agent_command = Agent.KILL; else throw new RuntimeException("missing case in switch!");
                        agentProxy.queueAction(agent_command);
                    }
                    break;
                case CONTINUE:
                    break;
                case DEFAULT:
                    {
                        outer: {
                            for (String agentTask : agentTasks) {
                                Status taskStatus = getTaskStatus(instance, agentTask);
                                if (taskStatus == Status.QUEUED || taskStatus == Status.RUNNING) {
                                    break outer;
                                }
                            }
                            agentProxy.queueAction(Agent.FINISH);
                        }
                    }
                    break;
                default:
                    throw new RuntimeException("Invalid case!");
            }
        }
    }

    /**
     * Get the status of a task. A conveninace method which gets the
     * corresponding variable.
     * 
     */
    private Status getTaskStatus(Instance instance, String task) throws EngineException {
        String var_name = Constants.VAR_STATUS_TASK_PREFIX + task;
        VarValue value = instance.getVarValue(var_name);
        return Status.fromOrdinal(value.getLongValue());
    }

    /**
     * Convenciance wrapper only. Take a single value only, and this one is
     * VarValue, not VariableValue.
     */
    public void updateVariable_synced(InstanceCredType instanceCred, VarValue value, Caller caller) throws EngineException {
        List<VariableValue> values = new LinkedList<VariableValue>();
        values.add(value.asVariableValue());
        updateVariables_synced(instanceCred, values, caller);
    }

    private void updateVariable(EntityManager em, Instance instance, VarValue value, Caller caller) throws EngineException {
        List<VariableValue> values = new LinkedList<VariableValue>();
        values.add(value.asVariableValue());
        updateVariables(em, instance, values, caller);
    }

    /**
     * @param caller
     *            for access rights
     * 
     */
    public void updateVariables_synced(InstanceCredType instanceCred, List<VariableValue> values, Caller caller) throws EngineException {
        InstanceRunableUpdateVariables runable = new InstanceRunableUpdateVariables();
        runable.values = values;
        runable.caller = caller;
        boolean write = true;
        runWithInstance(instanceCred, runable, write);
    }

    private class InstanceRunableUpdateVariables extends Engine.InstanceRunnable {

        List<VariableValue> values;

        Caller caller;

        public void run() throws EngineException {
            updateVariables(em, instance, values, caller);
        }
    }

    private void updateVariables(EntityManager em, Instance instance, List<VariableValue> values, Caller caller) throws EngineException {
        long instanceID = instance.getInstanceID();
        Map<String, VarAttributes> attributesMap = instance.getAttributesMap();
        Map<String, Long> old_versions = new HashMap<String, Long>();
        for (VariableValue value : values) {
            String name = value.getName();
            long old_version = instance.getVarValue(name).getVersion();
            old_versions.put(name, old_version);
            instance.updateVariableValue(value, caller);
        }
        em.getTransaction().commit();
        em.getTransaction().begin();
        Notification notification = new Notification();
        notification.setEvent(NotificationEventEnum.VARIABLES_CHANGE);
        notification.setInstanceID(instanceID);
        for (VariableValue value : values) {
            String name = value.getName();
            VarAttributes va = attributesMap.get(name);
            if (va.getAccess() != AccessEnum.NONE) {
                VarValue versioned_value = instance.getVarValue(name);
                long new_version = versioned_value.getVersion();
                long old_version = old_versions.get(name);
                if (new_version > old_version) {
                    notification.getValue().add(versioned_value.asVariableValue());
                }
            }
        }
        synchronized (notificationLock) {
            for (NotificationHolder holder : notificationHolders.values()) {
                if (holder.instanceIDs.contains(instanceID)) {
                    holder.put(notification);
                }
            }
        }
    }

    /**
     * Add a message to the output of a job. Warning: be aware of possible
     * conflicts or deadlocks if this method is called from other methods which
     * act on the same instance. This method is intended to be called from
     * outside, i.e. from agent. *
     * 
     * @param message
     *            The message to append to the output
     */
    public void write(InstanceCredType instanceCred, String message) throws EngineException {
        InstanceRunableWrite runable = new InstanceRunableWrite();
        runable.message = message;
        boolean write = true;
        runWithInstance(instanceCred, runable, write);
    }

    /**
     * TODO this method works, but the implementation is not optimal because it
     * re-reads the full output even if only one character is added.
     */
    private void write(EntityManager em, Instance instance, String message) throws EngineException {
        VarValue value = instance.getVarValue(Constants.VAR_STDOUT);
        String stdout = value.getStringValue();
        stdout = stdout + message;
        value.setStringValue(stdout);
        updateVariable(em, instance, value, Caller.INTERNAL);
    }

    private class InstanceRunableWrite extends Engine.InstanceRunnable {

        String message;

        public void run() throws EngineException {
            write(em, instance, message);
        }
    }

    /**
     * A wrapper arround write(...) which adds a newline.
     * 
     * @param message
     *            The message to append to the output
     */
    public void println(InstanceCredType instanceCred, String message) throws EngineException {
        write(instanceCred, message + "\n");
    }

    /**
     * Get an OutputStream which can be used to write to the stdout of a job.
     * Wrapps write(...).
     */
    public OutputStream getInstanceOutputStream(InstanceCredType instanceCred) {
        return new InstanceOutputStream(this, instanceCred);
    }

    /**
     * Similar as getVarValues, but get exactly one variable. Syncronizes on a
     * instance and should be called only from outside, but not from an other
     * method which syncs on instance, as it would cause deadlock.
     * 
     * @param varname
     * @param caller
     *            for acces rights
     * @return the variable
     */
    public VarValue getVarValue_synced(InstanceCredType instanceCred, String varname, Caller caller) throws EngineException {
        InstanceRunableGetVarValue runable = new InstanceRunableGetVarValue();
        runable.varname = varname;
        runable.caller = caller;
        boolean write = false;
        runWithInstance(instanceCred, runable, write);
        return runable.value;
    }

    private class InstanceRunableGetVarValue extends Engine.InstanceRunnable {

        String varname;

        Caller caller;

        VarValue value;

        public void run() throws EngineException {
            value = instance.getVarValue(varname, caller);
        }
    }

    /**
     * @param varnames
     * @param caller
     *            for access rights
     * @throws EngineException
     */
    public Collection<VarValue> getVarValues_synced(InstanceCredType instanceCred, Collection<String> varnames, Caller caller) throws EngineException {
        InstanceRunableGetVarValues runable = new InstanceRunableGetVarValues();
        runable.varnames = varnames;
        runable.caller = caller;
        boolean write = false;
        runWithInstance(instanceCred, runable, write);
        return runable.values;
    }

    private class InstanceRunableGetVarValues extends Engine.InstanceRunnable {

        Collection<String> varnames;

        Caller caller;

        Set<VarValue> values;

        public void run() throws EngineException {
            values = instance.getVarValues(varnames, caller);
        }
    }

    /**
     * Create a new instance. The instance is already persisted.
     * 
     * @return A detached (from EntityManager) instance form which one can geht
     *         the instanceID and the key of the newly created Instance.
     */
    public InstanceCredType createInstance(String serviceID, String instanceName, String user) throws EngineException {
        InstanceCredType instanceCred = null;
        EntityManager em = createEM();
        EntityTransaction tx = null;
        try {
            Service service = getService(serviceID);
            if (service == null) {
                throw new EngineException("ID=" + serviceID);
            }
            long instanceID;
            {
                tx = em.getTransaction();
                tx.begin();
                Instance instance = null;
                instance = service.createInstance(user, instanceName, this);
                em.persist(instance);
                instanceID = instance.getInstanceID();
                instanceCred = instance.getInstanceCred();
                try {
                    instance.setupInstanceDir();
                } catch (IOException ex) {
                    throw new EngineException("When creating instanceDir for instanceID=" + instance.getInstanceID(), ex);
                }
                tx.commit();
            }
            Notification notification = new Notification();
            notification.setEvent(NotificationEventEnum.INSTANCE_ADDED);
            notification.setInstanceID(instanceID);
            synchronized (notificationLock) {
                for (NotificationHolder holder : notificationHolders.values()) {
                    if (holder.user.equals(user)) {
                        holder.put(notification);
                    }
                }
            }
        } catch (RuntimeException re) {
            throw new RuntimeException("Failed during creating Instance " + instanceName, re);
        } finally {
            if (tx != null && tx.isActive()) tx.rollback();
            em.close();
        }
        return instanceCred;
    }

    /**
     * For Internal use, delete an instance, needs to be run within a entity
     * manager context and synchronized on instance with writeLock
     */
    private void deleteInstance(Instance instance, EntityManager em) {
        instance.cleanUp();
        em.remove(instance);
        instanceLocks.remove(instance.getInstanceID());
        Long instanceID = instance.getInstanceID();
        Notification notification = new Notification();
        notification.setEvent(NotificationEventEnum.INSTANCE_DELETED);
        notification.setInstanceID(instance.getInstanceID());
        synchronized (notificationLock) {
            for (NotificationHolder holder : notificationHolders.values()) {
                if (holder.user.equals(instance.getCreator()) || holder.instanceIDs.contains(instanceID)) {
                    holder.put(notification);
                }
            }
        }
    }

    /**
     * Get Information about a subset of all Instances. If creator_arg and
     * serviceID_arg are both set, then only those instances which fullfill both
     * requirements at once are returned, so it is a logical 'AND' of them.
     * 
     * @param creator_arg
     *            the creator to be searched for, or null
     * @param serviceID_arg
     *            the serviceID to be searched for, or null
     * @param othersInstances
     *            optionally (may be null) List with instances from other users
     * @return basic (static) information for all matching instances.
     * 
     */
    public List<InstanceInfo> getInstanceInfo(String creator_arg, String serviceID_arg, Collection<InstanceCredType> othersInstances) throws EngineException {
        List<InstanceInfo> instanceList = null;
        EntityManager em = createEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            String query_where = "";
            if (creator_arg != null && serviceID_arg != null) {
                String w1 = " i.creator" + " = " + "'" + creator_arg + "'";
                String w2 = " i.serviceID" + " = " + "'" + serviceID_arg + "'";
                query_where = w1 + " AND " + w2;
            } else if (creator_arg != null && serviceID_arg == null) {
                query_where = " i.creator" + " = " + "'" + creator_arg + "'";
            } else if (creator_arg == null && serviceID_arg != null) {
                query_where = " i.serviceID" + " = " + "'" + serviceID_arg + "'";
            } else {
                query_where = " i.serviceID = ''";
            }
            String query = "select i.name, i.instanceID, i.key, i.serviceID from Instance i " + "WHERE" + query_where;
            @SuppressWarnings("unchecked") List<Object[]> iList = em.createQuery(query).getResultList();
            int size = iList.size();
            instanceList = new Vector<InstanceInfo>(size);
            Set<Long> instanceIDs = new HashSet<Long>();
            for (Object[] objects : iList) {
                String name = (String) objects[0];
                long instanceID = (Long) objects[1];
                String key = (String) objects[2];
                String serviceID = (String) objects[3];
                InstanceInfo iinfo = new InstanceInfo();
                iinfo.setName(name);
                InstanceCredType instanceCred = new InstanceCredType();
                instanceCred.setId(instanceID);
                instanceCred.setKey(key);
                iinfo.setInstanceCred(instanceCred);
                iinfo.setServiceID(serviceID);
                instanceList.add(iinfo);
                instanceIDs.add(instanceID);
            }
            if (othersInstances != null) {
                for (InstanceCredType instanceCred : othersInstances) {
                    long instanceID = instanceCred.getId();
                    if (instanceIDs.contains(instanceID)) continue;
                    Instance instance = em.find(Instance.class, instanceID);
                    if (instance == null) continue;
                    instance.setEngine(this);
                    if (!instance.getKey().equals(instanceCred.getKey())) {
                        continue;
                    }
                    InstanceInfo iinfo = new InstanceInfo();
                    iinfo.setName(instance.getName());
                    iinfo.setInstanceCred(instanceCred);
                    iinfo.setServiceID(instance.getServiceID());
                    instanceList.add(iinfo);
                }
            }
            tx.commit();
        } catch (RuntimeException re) {
            throw new RuntimeException("Failed during getting instance info for " + "creator=" + creator_arg + " serviceID=" + serviceID_arg + "!", re);
        } finally {
            if (tx != null && tx.isActive()) tx.rollback();
            em.close();
        }
        return instanceList;
    }

    /**
     * Get a detached (from EntityManager) instance. detached means changes on
     * this instance are not persisted.
     * 
     * @return the detached instance, or null if it could not be retrieved
     * @param instanceCred
     *            the credential for the instance
     */
    public Instance getInstance(InstanceCredType instanceCred) throws ClientException {
        Instance instance = null;
        EntityManager em = createEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            long instanceID = instanceCred.getId();
            instance = em.find(Instance.class, instanceID);
            if (instance != null && (!instance.getKey().equals(instanceCred.getKey()))) {
                throw new ClientException(instanceID, "Illegal key for instance with ID=" + instanceID);
            }
            if (instance != null) instance.setEngine(this);
            tx.commit();
        } catch (RuntimeException re) {
            throw new RuntimeException("Failed during getting instance, instanceID=" + instanceCred.getId(), re);
        } finally {
            if (tx != null && tx.isActive()) tx.rollback();
            em.close();
        }
        return instance;
    }

    /**
     * To be called when the agent program is about to be stoped or killed. This
     * will not start or stop or terminate or kill an agent. This will remove
     * the agentProxy and set the status for this agent, thus no operations will
     * be possible anymore after this on this agent. This should only be called
     * if it is certain that the agent will not run anymore. It will also
     * correct the task statuses in case those are not valid anymore (i.e.
     * finished agent means task can't be running anymore.
     * 
     * Or to be called if status has changed to Agent.QUEUED or Agent.RUNNING.
     * 
     * Internal note: This method makes a consistency check of old status with
     * proxy object. In case proxy is created or deleted, on needs to first set
     * the agent status, then create or delete the agent proxy object.
     * 
     */
    public void setAgentStatus(InstanceCredType instanceCred, String agentID, Status new_status) throws EngineException {
        InstanceRunableSetAgentStatus runable = new InstanceRunableSetAgentStatus();
        runable.agentID = agentID;
        runable.new_status = new_status;
        boolean write = true;
        runWithInstance(instanceCred, runable, write);
    }

    private class InstanceRunableSetAgentStatus extends Engine.InstanceRunnable {

        String agentID;

        Status new_status;

        public void run() throws EngineException {
            setAgentStatus(em, instance, agentID, new_status);
        }
    }

    private void setAgentStatus(EntityManager em, Instance instance, String agentID, Status new_status) throws EngineException {
        String agentName = AgentProxy.extractAgentName(agentID);
        String var_name = Constants.VAR_STATUS_AGENT_PREFIX + agentName;
        VarValue old_value = instance.getVarValue(var_name);
        Status old_status = Status.fromOrdinal(old_value.getLongValue());
        if (new_status == old_status) return;
        if (!StatusTransistions.isAllowedTransistion(old_status, new_status)) {
            throw new EngineException("Agent status transition from " + old_status + " to " + new_status + " is forbidden!");
        }
        boolean proxy_exists = agentProxies.containsKey(agentID);
        boolean proxy_should_exist_old = (old_status == Status.QUEUED || old_status == Status.RUNNING);
        if (proxy_exists ^ proxy_should_exist_old) {
            String message = "Inconsistent agent proxy status: proxy_exists=" + proxy_exists + " status=" + old_status;
            if (new_status != Status.FAILED) {
                throw new EngineException(message);
            } else {
                log.warn(message + " Is auto-corrected!");
            }
        }
        VarValue new_value = new VarValue(var_name, new_status.ordinal());
        updateVariable(em, instance, new_value, Caller.INTERNAL);
        Agent agent = instance.getService().getAgent(agentName);
        Set<String> taskNames = agent.getTasks().keySet();
        for (String task_name : taskNames) {
            Status task_status = getTaskStatus(instance, task_name);
            Status agent_status = new_status;
            Status fallback_task_status = StatusTransistions.taskStatusFallback(agent_status, task_status);
            if (fallback_task_status != task_status) {
                setTaskStatus(em, agentID, instance, task_name, fallback_task_status);
            }
        }
        boolean proxy_should_exist_new = (new_status == Status.QUEUED || new_status == Status.RUNNING);
        if (proxy_exists && (!proxy_should_exist_new)) {
            AgentProxy ap = agentProxies.get(agentID);
            agentProxies.remove(agentID);
            ap.queueAction(null);
        }
    }

    /**
     * Get the status variable of an agent. A conveninace method which gets the
     * corresponding variable.
     * 
     * 
     * @return status see class Agent for status.
     */
    private Status getAgentStatus(Instance instance, String agentName) throws EngineException {
        String var_name = Constants.VAR_STATUS_AGENT_PREFIX + agentName;
        VarValue value = instance.getVarValue(var_name);
        return Status.fromOrdinal(value.getLongValue());
    }

    /**
     * Is synced on Instance.
     */
    Status getAgentStatusFromName_synced(InstanceCredType instanceCred, String agentName) throws EngineException {
        String var_name = Constants.VAR_STATUS_AGENT_PREFIX + agentName;
        VarValue status_value = getVarValue_synced(instanceCred, var_name, Caller.INTERNAL);
        long status = status_value.getLongValue();
        return Status.fromOrdinal(status);
    }

    /**
     * ? Perform an action on an instance? Or on an agent which then performs a
     * a change on the instance?
     * 
     */
    public ActionResponse action_synced(InstanceCredType instanceCred, ActionEnum action, String useraction) throws EngineException {
        InstanceRunableAction runable = new InstanceRunableAction();
        runable.action = action;
        runable.useraction = useraction;
        boolean write = true;
        runWithInstance(instanceCred, runable, write);
        return runable.response;
    }

    /** Belongs to #action, only used there. */
    private class InstanceRunableAction extends Engine.InstanceRunnable {

        ActionEnum action;

        String useraction;

        ActionResponse response;

        public void run() throws EngineException {
            response = action(em, instance, action, useraction);
        }
    }

    private ActionResponse action(EntityManager em, Instance instance, ActionEnum action, String useraction) throws EngineException {
        ActionResponse response = new ActionResponse();
        long instanceID = instance.getInstanceID();
        Service service = instance.getService();
        Set<String> agentNames = service.getAgentNames();
        label_action_switch: switch(action) {
            case DESTROY:
            case RESET:
                for (String agentName : agentNames) {
                    try {
                        Status agent_status = getAgentStatus(instance, agentName);
                        if (agent_status == Status.QUEUED || agent_status == Status.RUNNING) {
                            String message = "Agent " + agentName + " is in status " + agent_status + ". First kill or stop it.";
                            response.setAccepted(false);
                            response.setMessage(message);
                            break label_action_switch;
                        }
                    } catch (ConfigException ce) {
                        if (config.isStackTrace()) {
                            log.error("Could not get agent status when" + " deleting instance, deleting anyway", ce);
                        } else {
                            log.error("Could not get agent status when" + " deleting instance, deleting anyway: " + Util.getMessageChain(ce));
                        }
                    }
                }
                if (action == ActionEnum.DESTROY) {
                    deleteInstance(instance, em);
                } else {
                    for (String agentName : agentNames) {
                        String agentID = AgentProxy.createAgentID(instanceID, agentName);
                        setAgentStatus(em, instance, agentID, Status.NEW);
                        for (String taskName : service.getTaskNames()) {
                            setTaskStatus(em, agentID, instance, taskName, Status.NEW);
                        }
                    }
                }
                response.setAccepted(true);
                break;
            case STOP:
            case KILL:
                for (String agentName : agentNames) {
                    String agentID = AgentProxy.createAgentID(instanceID, agentName);
                    Status status = getAgentStatus(instance, agentName);
                    if (agentProxies.containsKey(agentID)) {
                        if (!(status == Status.QUEUED || status == Status.RUNNING)) {
                            throw new InternalException("Invalid agent status, agent not running, status =" + status);
                        }
                        AgentProxy agentProxy = agentProxies.get(agentID);
                        String agent_command;
                        if (action == ActionEnum.STOP) agent_command = Agent.STOP; else if (action == ActionEnum.KILL) agent_command = Agent.KILL; else throw new RuntimeException("Bug!");
                        agentProxy.queueAction(agent_command);
                        log.debug("Agent gets a " + agent_command + " (Engine x0p8jf2) !");
                    } else {
                        if (status == Status.QUEUED || status == Status.RUNNING) {
                            Status new_status = (action == ActionEnum.KILL) ? Status.FAILED : Status.TERMINATED;
                            setAgentStatus(em, instance, agentID, new_status);
                            log.warn("Corrected inconsistent agent status: " + "status was " + status + " but no proxy there!");
                        }
                    }
                }
                response.setAccepted(true);
                break;
            case USER:
                String taskName = service.getTaskName(useraction);
                response = queueTask(em, taskName, service, instance);
                break;
            default:
                throw new EngineException("Action " + action + " not implemented");
        }
        return response;
    }

    /**
     * Submit a task to the agent (queues it there) and start the agent if it is
     * not running. Needs to be run in a synced (on instance) context.
     * 
     * @param taskName
     *            the name of the task to queue
     * @return whether actions is accepted, or whether not and why
     */
    private ActionResponse queueTask(EntityManager em, String taskName, Service service, Instance instance) throws EngineException {
        String agentName = service.getAgentName(taskName);
        Agent agent = service.getAgent(agentName);
        InstanceCredType instanceCred = instance.getInstanceCred();
        long instanceID = instance.getInstanceID();
        String agentID = AgentProxy.createAgentID(instanceID, agentName);
        {
            Status task_status_old = getTaskStatus(instance, taskName);
            switch(task_status_old) {
                case NEW:
                    break;
                case QUEUED:
                case RUNNING:
                    ActionResponse response = new ActionResponse();
                    response.setAccepted(false);
                    String s_str = task_status_old.toString();
                    String message = "the task " + taskName + " in agent " + agentName + " is not in status NEW, instead it " + s_str + ", please reset first to NEW!";
                    response.setMessage(message);
                    return response;
                case TERMINATED:
                case FAILED:
                case FINISHED:
                    break;
                default:
                    throw new RuntimeException("Uninplemented case in switch");
            }
        }
        String command = Agent.TASK + taskName;
        AgentProxy agentProxy = null;
        if (agentProxies.containsKey(agentID)) {
            if (true) {
                Status status = getAgentStatus(instance, agentName);
                if (!(status == Status.RUNNING || status == Status.QUEUED)) throw new InternalException("Inconsitent agent status (" + status + "), but proxy exists!");
            }
            agentProxy = agentProxies.get(agentID);
            setTaskStatus(em, agentID, instance, taskName, Status.QUEUED);
            agentProxy.queueAction(command);
        } else {
            {
                Status status_old = getAgentStatus(instance, agentName);
                switch(status_old) {
                    case NEW:
                        break;
                    case QUEUED:
                    case RUNNING:
                        throw new InternalException("Inconsitent agent status (" + status_old + "), but proxy does not exists!");
                    case TERMINATED:
                    case FAILED:
                    case FINISHED:
                        break;
                    default:
                        throw new InternalException("Uninplemented case in switch");
                }
            }
            try {
                String instanceKey = instance.getKey();
                URL agentURL = null;
                if (isServer()) {
                    String servleta = "servleta/executable";
                    agentURL = new URL(getAgentContextURL() + "/" + servleta + "/" + agentID + "/" + instanceKey);
                    log.debug("agentURL=" + agentURL);
                }
                agentProxy = agent.createAgentProxy(this, agentID, instanceCred, agentURL, instance);
                if (false) throw new RuntimeException("Bug hier, muss methode mit instance sein!!!");
                setAgentStatus(em, instance, agentID, Status.QUEUED);
                agentProxies.put(agentID, agentProxy);
                agentProxy.queueAction(command);
                setTaskStatus(em, agentID, instance, taskName, Status.QUEUED);
                try {
                    agentProxy.start();
                } catch (RuntimeException ex) {
                    setAgentStatus(em, instance, agentID, Status.FAILED);
                    setTaskStatus(em, agentID, instance, taskName, Status.FAILED);
                    throw ex;
                } catch (IOException ex) {
                    setAgentStatus(em, instance, agentID, Status.FAILED);
                    setTaskStatus(em, agentID, instance, taskName, Status.FAILED);
                    throw ex;
                } catch (EngineException ex) {
                    setAgentStatus(em, instance, agentID, Status.FAILED);
                    setTaskStatus(em, agentID, instance, taskName, Status.FAILED);
                    throw ex;
                }
            } catch (EngineException ex) {
                throw new EngineException("Could not start agent " + agentName, ex);
            } catch (IOException ex) {
                throw new InternalException("Could not start agent " + agentName, ex);
            }
        }
        ActionResponse response = new ActionResponse();
        response.setAccepted(true);
        return response;
    }

    /**
     * To be called by agent: pull for an action and wait for at most
     * timeout_request seconds until there is one.
     * 
     * Comment: agent is not persisted right now. Actions get lost when server
     * restarts.
     * 
     * @param timeout_request
     *            seconds to wait
     * @param key
     *            agents key (is the instanceKey)
     * @return the action to perform, or null if there is none.
     */
    public String pullaction(String agentID, String key, double timeout_request) throws EngineException {
        String action_response = null;
        long instanceID = AgentProxy.extractInstanceID(agentID);
        InstanceCredType instanceCred = new InstanceCredType();
        instanceCred.setId(instanceID);
        instanceCred.setKey(key);
        double timeout_engine = getTimeout();
        double timeout_seconds = (timeout_request < timeout_engine) ? timeout_request : timeout_engine;
        boolean agent_exists = agentProxies.containsKey(agentID);
        if (!agent_exists) {
            throw new EngineException("Agent with ID=" + agentID + " not known!");
        }
        AgentProxy agentProxy = agentProxies.get(agentID);
        if (!agentProxy.getKey().equals(key)) {
            throw new EngineException("Key for agent with ID=" + agentID + " is invalid!");
        }
        try {
            action_response = agentProxy.getWaitAction(timeout_seconds);
            if (action_response != null && action_response.startsWith(Agent.TASK)) {
                String taskName = action_response.substring(Agent.TASK.length());
                setTaskStatus(agentID, instanceCred, taskName, Status.RUNNING);
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException("wait for action was interrupted!");
        }
        return action_response;
    }

    /**
     * Internally called by getNotifications() only. Get a Notification which
     * contains all variables which are visible to the client. This is used in
     * case the instance is new to the client.
     * 
     * It is synced on the instance
     */
    private Notification getAllVarsNotification_synced(long instanceID) throws ClientException, ConfigException {
        Notification notification = new Notification();
        notification.setEvent(NotificationEventEnum.VARIABLES_CHANGE);
        notification.setInstanceID(instanceID);
        EntityManager em = createEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            Instance instance = em.find(Instance.class, instanceID);
            if (instance == null) {
                throw new ClientException(instanceID, "invalid instanceID=" + instanceID);
            }
            instance.setEngine(this);
            Map<String, VarAttributes> attributesMap = instance.getAttributesMap();
            tx.commit();
            Set<String> varNames = (Set<String>) attributesMap.keySet();
            for (String name : varNames) {
                VarAttributes va = attributesMap.get(name);
                if (va.getAccess() != AccessEnum.NONE) {
                    VarValue value = instance.getVarValue(name);
                    notification.getValue().add(value.asVariableValue());
                }
            }
        } catch (RuntimeException re) {
            throw new RuntimeException("Failed during getAllVarsNotification", re);
        } finally {
            if (tx != null && tx.isActive()) tx.rollback();
            em.close();
        }
        return notification;
    }

    /**
     * To be called by the client. Register for notifications and wait until
     * someone stores some, then return them.
     * 
     * @param clientID
     * @param instanceCreds
     *            credentials for the instances
     * @param timeout_request
     *            how long the client want to wait at maximum
     * @return the notifications
     * @throws Exception
     */
    public List<Notification> getNotifications(String clientID, List<InstanceCredType> instanceCreds, double timeout_request, String user) throws ClientException, ConfigException {
        double timeout_engine_seconds = getTimeout();
        double timeout_seconds = (timeout_request < timeout_engine_seconds) ? timeout_request : timeout_engine_seconds;
        long timeout_millis = new Double(1000 * timeout_seconds).longValue();
        List<Long> instanceIDs = new Vector<Long>(instanceCreds.size());
        for (InstanceCredType instanceCred : instanceCreds) {
            Instance instance = getInstance(instanceCred);
            if (instance == null) {
                continue;
            }
            long instanceID = instanceCred.getId();
            if (!instance.getKey().equals(instanceCred.getKey())) {
                throw new ClientException(instanceID, "Illegal key for instance with ID=" + instanceID);
            }
            instanceIDs.add(instanceID);
        }
        boolean old_connection = notificationHolders.containsKey(clientID);
        Set<Long> newInstances = new HashSet<Long>();
        NotificationHolder holder;
        List<Notification> notifications = new LinkedList<Notification>();
        synchronized (notificationLock) {
            if (old_connection) {
                holder = notificationHolders.get(clientID);
                Set<Long> oldInstances = new HashSet<Long>(holder.instanceIDs);
                holder.instanceIDs.clear();
                for (long instanceID : instanceIDs) {
                    holder.instanceIDs.add(instanceID);
                    if (!oldInstances.contains(instanceID)) {
                        newInstances.add(instanceID);
                    } else {
                    }
                }
                holder.accessTime.setTime(new Date().getTime());
            } else {
                double holder_deadtime = Constants.notificationHolderCleanupTimeSeconds;
                double delay_seconds = holder_deadtime;
                long delay = new Double(1000 * delay_seconds).longValue();
                HolderCleanupTask task = new HolderCleanupTask(delay, clientID);
                holder = new NotificationHolder(user, clientID, task);
                long period = delay;
                notificationHolderCleanupTimer.schedule(task, delay, period);
                for (long instanceID : instanceIDs) {
                    holder.instanceIDs.add(instanceID);
                    newInstances.add(instanceID);
                }
                notificationHolders.put(clientID, holder);
            }
        }
        if (!old_connection) {
            Notification n = new Notification();
            n.setEvent(NotificationEventEnum.SERVER_NEW_CONNECTION);
            notifications.add(n);
        }
        for (long instanceID : newInstances) {
            Notification notification = getAllVarsNotification_synced(instanceID);
            notifications.add(notification);
        }
        holder.interruptAndWaitAndFill(notifications, timeout_seconds);
        holder.accessTime.setTime(new Date().getTime());
        return notifications;
    }

    /** @return null if the AgentProxy does not exist. */
    public AgentProxy getAgentProxy(String agentID) {
        return agentProxies.get(agentID);
    }

    public int xyz(InstanceCredType instanceCred, int arg1) throws EngineException {
        InstanceRunableXyz runable = new InstanceRunableXyz();
        runable.arg1 = 13;
        boolean write = true;
        runWithInstance(instanceCred, runable, write);
        return runable.return1;
    }

    private class InstanceRunableXyz extends Engine.InstanceRunnable {

        int arg1;

        int return1;

        public void run() throws EngineException {
            return1 = arg1 + 1;
        }
    }

    /** instance, em and tx are injected before run ist called. */
    private abstract class InstanceRunnable {

        Instance instance;

        EntityManager em;

        abstract void run() throws EngineException;
    }

    private void runWithInstance(InstanceCredType instanceCred, InstanceRunnable runable, boolean write) throws EngineException {
        long instanceID = instanceCred.getId();
        Lock lock = null;
        if (true) {
            instanceLocks.putIfAbsent(instanceID, new ReentrantReadWriteLock());
            ReentrantReadWriteLock rwlock = instanceLocks.get(instanceID);
            lock = write ? rwlock.writeLock() : rwlock.readLock();
        } else {
        }
        lock.lock();
        EntityManager em = createEM();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            Instance instance = em.find(Instance.class, instanceID);
            if (instance == null) {
                instanceLocks.remove(instanceID);
                throw new EngineException("invalid instanceID=" + instanceID);
            }
            instance.setEngine(this);
            if (!instance.getKey().equals(instanceCred.getKey())) {
                throw new EngineException("Illegal key for instance with ID=" + instanceID);
            }
            runable.instance = instance;
            runable.em = em;
            runable.run();
            if (tx.isActive()) tx.commit();
        } catch (RuntimeException re) {
            throw new RuntimeException("Failed during ??? on instanceID=" + instanceID, re);
        } finally {
            if (tx != null && tx.isActive()) tx.rollback();
            em.close();
            lock.unlock();
        }
    }
}
