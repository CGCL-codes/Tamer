package ru.adv.mozart.framework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import ru.adv.cache.CacheManagerImpl;
import ru.adv.db.app.request.ResourceLimits;
import ru.adv.io.UnknownIOSourceException;
import ru.adv.logger.TLogger;
import ru.adv.mozart.Defaults;
import ru.adv.util.BadBooleanException;
import ru.adv.util.ErrorCodeException;
import ru.adv.util.Files;
import ru.adv.util.IPAddress;
import ru.adv.util.InputOutput;
import ru.adv.util.InputOutputPrefix;
import ru.adv.util.InputOutputPrefixMap;
import ru.adv.util.InputOutputType;
import ru.adv.util.InvalidIPAddressException;
import ru.adv.util.Path;
import ru.adv.util.StringParser;
import ru.adv.util.Strings;
import ru.adv.util.Time;
import ru.adv.xml.transformer.Transformer;
import ru.adv.xml.transformer.XSLTFile;

/**
 * User: vic Date: 02.08.2004 Time: 16:33:16 Parser for mozart.conf
 */
public class MozartConfig implements Defaults {

    public static final String MOZART_CONFIG_PATH = "/opt/mozart/config";

    public static final String MOZART_CONFIG_PROP = "mozart.config";

    private static final String HTDOCS_SUFFIX = "/htdocs";

    private static final String HOSTROOT_PREFIX_NAME = "hostroot";

    private static final String APPROOT_PREFIX_NAME = "approot";

    private static final String KEY_PREFIX_HOSTROOT = "prefix." + APPROOT_PREFIX_NAME;

    private static final String MAX_EXPIRES = "max.expires";

    private static final String HOUR = "1h";

    private Properties localProps;

    private String smtpServer;

    private Path appRootDir;

    private Path hostRootDir;

    private String configPath;

    private List<IPAddress> debbugingIPs;

    private Set<String> appNames;

    private HashMap<String, Map<String, String>> appProperties = new HashMap<String, Map<String, String>>();

    private DependenceChecker dependencies;

    private Exception _errorXslException;

    private Transformer _errorTransformer = null;

    private Properties _cacheProperties = null;

    private Properties globalProps;

    private ResourceLimits _resourceLimits;

    private int _proxyPort;

    private long maxExpires;

    private static final String FOP_CONFIG_FILE_PROP = "fop.config.file";

    private static final String DEFAULT_FOP_CONFIG = "/opt/mozart/fop/fop.config";

    private String statisticURI;

    private static final String STATISTIC_URI = "statistic.uri";

    private static final String ADMIN_MOZART_STATISTIC = "/admin/query-stat";

    private TLogger logger = new TLogger(MozartConfig.class);

    public MozartConfig() {
        globalProps = new Properties();
        localProps = new Properties();
    }

    public synchronized void init(String configPath, String hostRootDir, String appRootDir, String defaultSMTPServer) throws IOException, ErrorCodeException {
        Assert.notNull(configPath);
        Assert.notNull(hostRootDir);
        Assert.notNull(appRootDir);
        this.configPath = configPath;
        this.dependencies = new DependenceChecker("MOZART_CONFIG", null);
        this.globalProps = loadGlobalConfig();
        this.localProps = loadLocalConfig();
        this.smtpServer = localProps.getProperty(SMTPSERVER_PROP, defaultSMTPServer);
        this.hostRootDir = new Path(hostRootDir);
        this.appRootDir = new Path(fixRootDir(appRootDir));
        this.debbugingIPs = Collections.unmodifiableList(createDebbugingIPs());
        String maxExpires = localProps.getProperty(MAX_EXPIRES);
        if (null == maxExpires) {
            maxExpires = HOUR;
        }
        this.maxExpires = Time.parse(maxExpires);
        this.statisticURI = localProps.getProperty(STATISTIC_URI);
        if (this.statisticURI == null) {
            this.statisticURI = ADMIN_MOZART_STATISTIC;
        }
        setHostrootPrefixToLocalProps();
        InputOutputPrefixMap set = createPrefixes(localProps, this.hostRootDir, this.appRootDir, new Path(appRootDir), new Path(appRootDir + "/" + TEMPLATES_ROOT));
        loadErrorXsl(localProps.getProperty(ERRORXSL_PROP, ERRORXSL), set);
        loadApplicationConfigs();
        this._cacheProperties = null;
        this._resourceLimits = new ResourceLimits(StringParser.toLong(localProps.getProperty("soft.execution.time.limit"), 0), StringParser.toLong(localProps.getProperty("hard.execution.time.limit"), 0), StringParser.toLong(localProps.getProperty("soft.request.size.limit"), 0), StringParser.toLong(localProps.getProperty("hard.request.size.limit"), 0), StringParser.toInt(localProps.getProperty(LIMIT_CONNECTION_PROP), LIMIT_CONNECTION_DEFAULT));
        this._proxyPort = StringParser.toInt(localProps.getProperty(PROXY_PORT), PROXY_PORT_DEFAULT);
    }

    private void setHostrootPrefixToLocalProps() {
        if (localProps.getProperty(KEY_PREFIX_HOSTROOT) == null) {
            localProps.setProperty(KEY_PREFIX_HOSTROOT, appRootDir.getRoot());
        }
    }

    private String fixRootDir(String rootDir) {
        String result = StringUtils.cleanPath(rootDir);
        if (result.endsWith(HTDOCS_SUFFIX)) {
            result = result.substring(0, result.length() - HTDOCS_SUFFIX.length());
        }
        return result;
    }

    public synchronized String getProperty(String propName) {
        return localProps.getProperty(propName);
    }

    public synchronized String getProperty(String propName, String defaultValue) {
        return localProps.getProperty(propName, defaultValue);
    }

    /**
     * return Properties conatins names with substring "cache"
     * 
     * @return
     */
    public synchronized Properties getCacheProperties() {
        if (_cacheProperties == null) {
            _cacheProperties = new Properties();
            for (Iterator i = localProps.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry property = (Map.Entry) i.next();
                if (property.getKey().toString().startsWith(CacheManagerImpl.CACHE_PROP_PREFIX)) {
                    _cacheProperties.put(property.getKey(), property.getValue());
                }
            }
        }
        return _cacheProperties;
    }

    public synchronized long getWatchConfigInterval() {
        long interval = RELOAD_INTERVAL_DEFAULT;
        String value = getProperty(RELOAD_INTERVAL_PROP, "");
        if (value.length() > 0) {
            try {
                interval = Integer.parseInt(value) * 1000;
            } catch (NumberFormatException e) {
            }
        }
        return interval;
    }

    /**
     * Список IP адресов для которых использовуется режим отладки
     * 
     * @return
     */
    public List<IPAddress> getDebbugingIPs() {
        return debbugingIPs;
    }

    /**
     * Список IP адресов для которых использовуется режим отладки
     * 
     * @return
     */
    private List<IPAddress> createDebbugingIPs() throws InvalidIPAddressException {
        List<IPAddress> debbugingIps = new LinkedList<IPAddress>();
        String ips = localProps.getProperty(DEBUGGING_IPS_PROP);
        if (ips != null) {
            StringTokenizer st = new StringTokenizer(ips, " ,");
            while (st.hasMoreTokens()) {
                debbugingIps.add(new IPAddress(st.nextToken()));
            }
        }
        return debbugingIps;
    }

    public boolean getSessionIdInURL() {
        try {
            return StringParser.toBoolean(getProperty(SESSION_IN_URL_PROP, "false"));
        } catch (BadBooleanException e) {
        }
        return false;
    }

    public Path getHostRoot() {
        return appRootDir;
    }

    public long getMaxExpires() {
        return maxExpires;
    }

    public String getStatisticURI() {
        return statisticURI;
    }

    protected synchronized boolean isChanged() {
        logger.info("Test changed mozart config. Includes = " + dependencies.getIncludes());
        return dependencies.isChanged();
    }

    protected Set getIncludes() {
        return dependencies.getIncludes();
    }

    public int getCacheDbType() {
        return 0;
    }

    public String getQueryStatLogFile() {
        String result = System.getProperty(LOG_QUERY_STAT);
        if (result == null) {
            result = localProps.getProperty(LOG_QUERY_STAT, LOG_QUERY_STAT_DEFAULT);
        }
        return result;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    /**
     * имена существующих applications
     * 
     * @return
     */
    public synchronized Set<String> getAppNames() {
        return appNames;
    }

    private boolean castUseRemovedValue(String strValue) {
        try {
            return StringParser.toBoolean(strValue);
        } catch (BadBooleanException e) {
            try {
                return StringParser.toBoolean(USE_REMOVED_DEFAULT);
            } catch (BadBooleanException e1) {
            }
        }
        return false;
    }

    /**
     * Создает конфиг для виртуального хоста
     * 
     * @return
     */
    public MAppConfig getDefaultMAppConfig() throws UnknownIOSourceException {
        MAppConfig appConfig = new MAppConfig(true);
        appConfig.setRootPath(getHostRoot());
        appConfig.setDocPath(getHostRoot());
        appConfig.setTemplatesPath(new Path(appConfig.getDocPath() + "/" + TEMPLATES_ROOT));
        appConfig.setPrefixes(createPrefixes(localProps, this.hostRootDir, getHostRoot(), appConfig.getDocPath(), appConfig.getTemplatesPath()));
        if (appConfig.getPrefixes().containsKey(PREFIX_HTDOCS_NAME)) {
            logger.info("Set htdocs path to " + appConfig.getPrefixes().get(PREFIX_HTDOCS_NAME).getRoot());
            appConfig.setDocPath(appConfig.getPrefixes().get(PREFIX_HTDOCS_NAME).getRoot());
        }
        appConfig.setAliases(createAliases(getProperty(ALIASES_PROP), appConfig.getDocPath()));
        appConfig.setSiteTreeFile(getProperty(SITETREE_PROP, SITETREE_DEFAULT));
        appConfig.setDatabaseDescriptorFile(getProperty(DBDESC_PROP, DBDESC_DEFAULT));
        appConfig.setDefaultXsl(getProperty(DEFAULT_XSL_PROP, DEFAULT_XSL_DEFAULT));
        appConfig.setIndexFiles(createIndexFilesSet(getProperty(DEFAULT_INDEXES_PROP), DEFAULT_INDEXES));
        appConfig.setUseRemoved(castUseRemovedValue(getProperty(USE_REMOVED_PROP, USE_REMOVED_DEFAULT)));
        appConfig.setFopConfigFile(getProperty(FOP_CONFIG_FILE_PROP, DEFAULT_FOP_CONFIG));
        return appConfig;
    }

    /**
     * Создает конфиг {@link MAppConfig}
     * 
     * @param appName
     *            имя приложения
     * @return {@link MAppConfig} или null, если не найден appName
     */
    public MAppConfig getMAppConfig(String appName) throws MozartConfigException {
        Map<String, String> appProps = appProperties.get(appName);
        if (appProps != null) {
            MAppConfig mAppConfig = new MAppConfig(false);
            mAppConfig.setUrlPrefix(getRequiredPropValue(appName, appProps, APP_URL_PREFIX_PROP));
            mAppConfig.setRootPath(new Path(getRequiredPropValue(appName, appProps, APPROOT_PROP)));
            mAppConfig.setDocPath(new Path(mAppConfig.getRootPath().getRoot()));
            mAppConfig.setTemplatesPath(new Path(mAppConfig.getRootPath().getRoot() + "/" + TEMPLATES_ROOT));
            try {
                mAppConfig.setPrefixes(createPrefixes(appProps, this.hostRootDir, mAppConfig.getRootPath(), mAppConfig.getDocPath(), mAppConfig.getTemplatesPath()));
            } catch (UnknownIOSourceException e) {
                throw new MozartConfigException(e);
            }
            mAppConfig.setAliases(createAliases((String) appProps.get(ALIASES_PROP), mAppConfig.getDocPath()));
            mAppConfig.setSiteTreeFile(getRequiredPropValue(appName, appProps, SITETREE_PROP));
            mAppConfig.setDatabaseDescriptorFile(getPropValue(appProps, DBDESC_PROP, DBDESC_DEFAULT));
            mAppConfig.setDefaultXsl(getPropValue(appProps, DEFAULT_XSL_PROP, null));
            mAppConfig.setIndexFiles(createIndexFilesSet(getPropValue(appProps, DEFAULT_INDEXES_PROP, null), DEFAULT_INDEXES));
            mAppConfig.setUseRemoved(castUseRemovedValue(getPropValue(appProps, USE_REMOVED_PROP, USE_REMOVED_DEFAULT)));
            mAppConfig.setFopConfigFile(getPropValue(appProps, FOP_CONFIG_FILE_PROP, DEFAULT_FOP_CONFIG));
            return mAppConfig;
        }
        throw new MozartConfigException("Mozart application " + appName + " not found in config");
    }

    public String toString() {
        String str = "";
        str += "Main mozart config:\n" + localProps + "\n";
        if (null != appProperties) {
            for (Entry<String, Map<String, String>> app : appProperties.entrySet()) {
                str += "Application " + app.getKey() + ":\n";
                str += "  " + app.getValue().toString();
            }
        }
        return str;
    }

    public Exception getErrorXslException() {
        return _errorXslException;
    }

    public Transformer getErrorTransformer() {
        return _errorTransformer;
    }

    private void loadErrorXsl(String xsl, InputOutputPrefixMap set) {
        try {
            _errorTransformer = new XSLTFile(InputOutput.create(xsl, set));
        } catch (Exception e) {
            _errorTransformer = null;
            _errorXslException = e;
        }
    }

    private Properties loadGlobalConfig() throws IOException, UnknownIOSourceException {
        String path = System.getProperty(MOZART_CONFIG_PROP, MOZART_CONFIG_PATH);
        try {
            Properties result = loadAppConfig(path);
            return result;
        } catch (FileNotFoundException e) {
            logger.debug("Cannot locate global mozart config " + path);
            return new Properties();
        }
    }

    private Properties loadLocalConfig() throws IOException, UnknownIOSourceException {
        Properties result = new Properties();
        for (Map.Entry<Object, Object> entry : globalProps.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        Properties localProps = loadAppConfig(configPath);
        for (Map.Entry<Object, Object> entry : localProps.entrySet()) {
            result.setProperty((String) entry.getKey(), (String) entry.getValue());
        }
        return result;
    }

    private Properties loadAppConfig(String path) throws IOException, UnknownIOSourceException {
        logger.debug("Load properties from " + path);
        Properties result = new Properties();
        if (path != null) {
            InputStream input = new FileInputStream(path);
            result.load(input);
            input.close();
            for (Iterator i = result.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry entry = (Map.Entry) i.next();
                entry.setValue(((String) entry.getValue()).trim());
            }
            dependencies.addInclude(InputOutput.create(path).createInclude());
        }
        return result;
    }

    private void loadApplicationConfigs() throws IOException, UnknownIOSourceException {
        HashMap<String, Map<String, String>> appProperties = new HashMap<String, Map<String, String>>();
        HashMap<String, String> defaultProperties = new HashMap<String, String>();
        initAppProperties(appProperties, defaultProperties);
        readAppConfigFiles(appProperties);
        for (Entry<String, String> entry : defaultProperties.entrySet()) {
            fillAppFromMainConfig(appProperties, entry.getKey(), entry.getValue());
        }
        this.appNames = Collections.unmodifiableSet(appProperties.keySet());
        this.appProperties = appProperties;
    }

    private void fillAppFromMainConfig(HashMap appProperties, String defaultKey, String defaultVal) {
        for (Iterator ii = appProperties.entrySet().iterator(); ii.hasNext(); ) {
            Map.Entry appEntry = (Map.Entry) ii.next();
            HashMap appProps = (HashMap) appEntry.getValue();
            if (!appProps.containsKey(defaultKey)) {
                appProps.put(defaultKey, defaultVal);
            }
        }
    }

    private void readAppConfigFiles(HashMap appProperties) throws IOException, UnknownIOSourceException {
        for (Iterator apps = appProperties.entrySet().iterator(); apps.hasNext(); ) {
            Map.Entry app = (Map.Entry) apps.next();
            HashMap appProps = (HashMap) app.getValue();
            String confFile = (String) appProps.get("conf");
            Properties appConfProps = null;
            if (confFile != null) {
                appConfProps = loadAppConfig(confFile);
            }
            for (Iterator i = appConfProps.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry entry = (Map.Entry) i.next();
                if (!appProps.containsKey(entry.getKey())) {
                    appProps.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private void initAppProperties(HashMap appProperties, HashMap defaultProperties) {
        for (Iterator mainProperties = localProps.entrySet().iterator(); mainProperties.hasNext(); ) {
            Map.Entry mainProperty = (Map.Entry) mainProperties.next();
            String mainPropName = (String) mainProperty.getKey();
            String mainPropValue = (String) mainProperty.getValue();
            List items = Strings.split(mainPropName, ".", false);
            if (items.size() >= 3 && items.get(0).equals("app")) {
                String appName = (String) items.get(1);
                HashMap app = (HashMap) appProperties.get(appName);
                if (app == null) {
                    app = new HashMap();
                    appProperties.put(appName, app);
                }
                items.remove(0);
                items.remove(0);
                String appKey = Strings.join(".", items);
                app.put(appKey, mainPropValue);
            } else {
                defaultProperties.put(mainPropName, mainPropValue);
            }
        }
    }

    private static String getRequiredPropValue(String appName, Map<String, String> props, String propName) throws MozartConfigException {
        String value = props.get(propName);
        if (value == null) {
            throw new MozartConfigException("Not defined '" + propName + "' property for '" + appName + "; mozart application");
        }
        return value;
    }

    private static String getPropValue(Map<String, String> props, String propName, String defaultValue) {
        String value = props.get(propName);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    private Set<String> createIndexFilesSet(String propValue, Set<String> defaultIndexes) {
        return (propValue != null) ? Collections.unmodifiableSet(new HashSet<String>(Strings.split(propValue, " \t,"))) : defaultIndexes;
    }

    private static Map<String, String> createAliases(String aliasesValue, Path docPath) {
        Map<String, String> aliases = new HashMap<String, String>();
        if (aliasesValue != null) {
            StringTokenizer st = new StringTokenizer(aliasesValue, ",");
            while (st.hasMoreTokens()) {
                String pair = st.nextToken();
                StringTokenizer st1 = new StringTokenizer(pair, " \t");
                if (st1.hasMoreTokens()) {
                    String alias = st1.nextToken();
                    if (st1.hasMoreTokens()) {
                        String root = st1.nextToken();
                        if (!alias.endsWith("/")) {
                            alias += "/";
                        }
                        aliases.put(alias, root);
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        if (!aliases.containsKey("/")) {
            aliases.put("/", docPath.getRoot());
        }
        return aliases;
    }

    private static InputOutputPrefixMap createPrefixes(Map props, Path hostRoot, Path appRoot, Path docPath, Path templatePath) throws UnknownIOSourceException {
        InputOutputPrefixMap prefixes = new InputOutputPrefixMap();
        prefixes.add(new InputOutputPrefix(HOSTROOT_PREFIX_NAME, InputOutputType.FILE, hostRoot));
        prefixes.add(new InputOutputPrefix(APPROOT_PREFIX_NAME, InputOutputType.FILE, appRoot));
        prefixes.add(new InputOutputPrefix(PREFIX_HTDOCS_NAME, InputOutputType.FILE, docPath));
        prefixes.add(new InputOutputPrefix(PREFIX_SRC_NAME, InputOutputType.FILE, docPath));
        prefixes.add(new InputOutputPrefix(PREFIX_TEMPLATES_NAME, InputOutputType.FILE, templatePath));
        prefixes.add(new InputOutputPrefix("resource", InputOutputType.RESOURCE, new Path("/")));
        for (Iterator i = props.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            String propName = (String) entry.getKey();
            String propValue = (String) entry.getValue();
            List items = Strings.split(propName, ".");
            if (items.size() == 2 && items.get(0).equals(PREFIX_PREFIX_PROP)) {
                String prefixName = (String) items.get(1);
                String prefixPath = propValue;
                prefixes.add(new InputOutputPrefix(prefixName, InputOutputType.FILE, expandPrefixes(prefixPath, prefixes)));
            }
        }
        prefixes.add(new InputOutputPrefix("file", InputOutputType.FILE, appRoot));
        return prefixes;
    }

    private static Path expandPrefixes(final String path, InputOutputPrefixMap prefixes) throws UnknownIOSourceException {
        String result = path;
        if (!Files.isAbsolute(path)) {
            result = InputOutput.create(path, prefixes).getRealPath();
        }
        return new Path(result);
    }

    public ResourceLimits getResourceLimits() {
        return _resourceLimits;
    }

    public int getProxyPort() {
        return _proxyPort;
    }
}
