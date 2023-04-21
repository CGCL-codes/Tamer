package de.hbrs.inf.atarrabi.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.xml.bind.DataBindingException;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.contexts.ServletLifecycle;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.mail.MailSession;
import de.hbrs.inf.atarrabi.action.db.DbConnectorRead;
import de.hbrs.inf.atarrabi.action.db.DbConnectorReadCera2;
import de.hbrs.inf.atarrabi.action.lov.LovController;
import de.hbrs.inf.atarrabi.enums.AtarrabiConfigParam;
import de.hbrs.inf.atarrabi.enums.InitExceptionType;

/**
 * Atarrabi configuration component. Manages read and write access to externalized properties.
 * @author Florian Quadt
 */
@Name("atarrabiConfiguration")
@Scope(ScopeType.APPLICATION)
@Startup
public class AtarrabiConfiguration implements Serializable {

    private static final String FILE = "file:";

    private static final String NEWLINE = "\n";

    private static final String COLON_SPACE = ": ";

    private static final String JBOSS_SERVER_CONFIG_URL = "jboss.server.config.url";

    private static final String WEBLOGIC_HOME = "weblogic.home";

    private static final String WEBLOGIC_USERDIR = "user.dir";

    private static final long serialVersionUID = -7822701583079205661L;

    private static final String VERSION_FILE_NAME = "buildinfo.properties";

    private static final String HLINE = "----------------------------------------------------------------------------";

    private String usedPropsPathAsURL;

    private String usedPropsPath;

    private String usedVersionPropsPath;

    private String serverPropsPath;

    private String jndiPrefix = "";

    private ServerType serverType = ServerType.JBOSS;

    private Properties props;

    private Properties versionProps;

    private AuthenticationMode authenticationMode = AuthenticationMode.INTERNAL;

    private ServletContext servletContext = ServletLifecycle.getServletContext();

    @Logger
    private Log log;

    @In
    private LovController lovController;

    @In
    private StatusMessages statusMessages;

    @Out(scope = ScopeType.APPLICATION, required = true)
    private final List<InitException> initExceptions = new ArrayList<InitException>();

    /**
	 * Enum to distinguish between internal and external (LDAP) authentication.
	 */
    public enum AuthenticationMode {

        INTERNAL, LDAP
    }

    private enum ServerType {

        JBOSS, WEBLOGIC
    }

    /**
	 * Checks configuration for missing properties, valid mail configuration, and imported list of values.
	 */
    public void checkConfiguration() {
        this.initExceptions.clear();
        this.checkForMissingProperties();
        if (this.checkForeignDBAvailablility()) {
            if (lovController.lovsAreEmpty()) {
                this.initExceptions.add(new InitException(this.getResourceBundle().getString("system.alert.noImportedListOfValues")));
                log.warn("List of values are NOT present. Must be imported manually.");
            } else {
                log.info("List of values are present");
            }
        }
        log.debug("Count of init ex: " + initExceptions.size());
    }

    /**
	 * Checks whether the external database is available.
	 * @return true if external database is available, false else.
	 */
    public boolean checkForeignDBAvailablility() {
        log.debug("checking availability of foreign database..");
        DbConnectorRead dbread = new DbConnectorReadCera2();
        try {
            dbread.checkForeignDBAvailability();
            if (isAnyDBConnErrorInitException()) {
                this.checkConfiguration();
            }
            return true;
        } catch (IOException e) {
            log.error("CERA is not available!");
            statusMessages.add(Severity.ERROR, "Cant establish connection to cera database!");
            if (!isAnyDBConnErrorInitException()) {
                this.initExceptions.add(new InitException(InitExceptionType.DB_CONN_ERROR, "CERA is not available"));
            }
        }
        return false;
    }

    /**
	 * Checks whether the external database is available.
	 * @return true if external database is available, false else.
	 */
    public boolean checkForeignDBAvailablilityWithException() throws DataBindingException {
        log.debug("checking availability of foreign database with exception.");
        DbConnectorRead dbread = new DbConnectorReadCera2();
        try {
            dbread.checkForeignDBAvailability();
            if (isAnyDBConnErrorInitException()) {
                this.checkConfiguration();
            }
            return true;
        } catch (IOException e) {
            log.error("CERA is not available!");
            statusMessages.add(Severity.ERROR, "Entry hasn't been submitted to the CERA, but your data was saved. Please try again later.");
            throw new DataBindingException(null);
        }
    }

    /**
	 * Get atarrabi build date.
	 * @return build date string
	 */
    public String getAtarrabiBuildDate() {
        return versionProps.getProperty("build.date");
    }

    /**
	 * Get atarrabi build number.
	 * @return build number string
	 */
    public String getAtarrabiBuildNumber() {
        return versionProps.getProperty("build.num");
    }

    /**
	 * Get atarrabi build SVN revision.
	 * @return SVN revision string
	 */
    public String getAtarrabiBuildSvnRevision() {
        return versionProps.getProperty("build.svn.lastrevision");
    }

    /**
	 * Get atarrabi build version (including the "Alpha|Beta|Final" suffix).
	 * @return build version string (e.g. "2.0 Beta", "2.0 Final")
	 */
    public String getAtarrabiBuildVersion() {
        return versionProps.getProperty("build.version");
    }

    /**
	 * Get atarrabi build version number (no "Alpha|Beta|Final" suffix).
	 * @return build version number (e.g. "2.0")
	 */
    public String getAtarrabiBuildVersionNumber() {
        Pattern p = Pattern.compile("^\\s*(\\d+(\\.\\d+)*).*");
        Matcher m = p.matcher(this.getAtarrabiBuildVersion());
        if (m.matches()) {
            return m.group(1);
        } else {
            return this.getAtarrabiBuildVersion();
        }
    }

    public AuthenticationMode getAuthenticationMode() {
        return authenticationMode;
    }

    /**
	 * Delivers true if the specified param results into "true".
	 * Ignores cases.
	 * If the mandatory flag for configutation parameter is set, the caller can count on
	 * a value. If set to false, the caller needs to check if the returned value is null.
	 * @param param the AtarrabiConfigParams enumeration
	 * @return false if value of property is not "true" by ignoring case.
	 */
    public boolean getBoolean(AtarrabiConfigParam param) {
        return this.getBoolean(param.getKeyName());
    }

    /**
	 * Returns an Integer representation of the config parameter.
	 * @param param parameter to return the Integer representation for.
	 * @return Integer representation of config parameter if present, null else.
	 */
    public Integer getInteger(AtarrabiConfigParam param) {
        if (param == null) {
            return null;
        }
        try {
            String valueString = this.getString(param);
            if (valueString != null) {
                return Integer.parseInt(this.getString(param));
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            log.warn("Could not convert param #0 into an integer value. Please check your configuration!", param);
            return null;
        }
    }

    public String getJndiPrefix() {
        return jndiPrefix;
    }

    /**
	 * Converts a comma-separated string list to a java util list.
	 * @param key String containing the comma-separated list
	 * @return a Java list containing the comma-separated values from the string (trimmed)
	 */
    public List<String> getList(String key) {
        List<String> resultList = new ArrayList<String>();
        String value = props.getProperty(key);
        for (String s : value.split(",")) {
            resultList.add(s.trim());
        }
        return resultList;
    }

    /**
	 * Delivers all application properties as a map.
	 * @return a TreeMap (map ordered by keys) of all application properties.
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, String> getProperties() {
        return new TreeMap<String, String>((Map) props);
    }

    /**
	 * Delivers the value string of this config parameters.
	 * If the mandatory flag for configutation parameter is set, the caller can count on
	 * a value. If set to false, the caller needs to check if the returned value is null.
	 * @param param the AtarrabiConfigParams enumeration
	 * @return a value for this key if it exist.
	 */
    public String getString(AtarrabiConfigParam param) {
        if (this.getStringValue(param) == null) {
            return null;
        }
        return this.getStringValue(param).trim();
    }

    /**
	 * Delivers the value string of a property. Replacing each placeholder (template) with syntax {numberOfOccurrence}
	 * like {0} {1} {2} ... with the provided params. If no curly bracket was found return the original value string
	 * @param param
	 *            The property key
	 * @param params
	 *            the parameters for replacing the templates
	 * @return the value string of the property key
	 */
    public String getString(AtarrabiConfigParam param, String... params) {
        String value = this.getString(param);
        int i = 0;
        while (StringUtils.contains(value, '{') && StringUtils.contains(value, '}')) {
            value = StringUtils.replaceOnce(value, "{" + i + "}", params[i]);
            i++;
        }
        return value;
    }

    /**
	 * Method to retrieve a configuration parameter directly with its real name (property key).
	 * This method should <b>only</b> be used in XHTML for convenience.
	 * @param key - the real name of the property
	 * @return the value if key is found
	 */
    public String getStringForXHTML(String key) {
        return this.getStringValue(this.findConfigParamFromString(key));
    }

    /**
	 * Method to retrieve a configuration parameter directly with its real name (property key).
	 * This method should <b>only</b> be used in XHTML for convenience.
	 * @param key - the real name of the property
	 * @param defaultValue - the default value. Will be returned if no value can be found
	 * @return the value if key is found
	 */
    public String getStringForXHTML(String key, String defaultValue) {
        String value = this.getStringValue(this.findConfigParamFromString(key));
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    /**
	 * Sets authentication mode and reads properties from internal or external properties file. First the used
	 * application server is detected. After that it looks for file <code>atarrabi-?.?.properties</code> in a server
	 * dependent directory. If the file cannot be found it falls back to an internal properties file.
	 */
    @Create
    public void initAtarrabiConfiguration() {
        versionProps = new Properties();
        usedVersionPropsPath = "/" + VERSION_FILE_NAME;
        log.debug("Read version from property file... #0", usedVersionPropsPath);
        try {
            versionProps.load(this.getClass().getResourceAsStream(usedVersionPropsPath));
        } catch (IOException e) {
            log.error("Error while reading version file #0", usedVersionPropsPath, e);
        }
        serverType = this.getServerType();
        props = new Properties();
        if (serverType != null) {
            switch(serverType) {
                case JBOSS:
                    this.setJBossSettings();
                    break;
                case WEBLOGIC:
                    this.setWeblogicSettings();
                    break;
                default:
                    log.debug("Could detect server type '" + serverType + "' but no handler is provided.");
            }
        }
        log.debug("Try to read properties at: #0", serverPropsPath);
        try {
            if (serverPropsPath != null && new File(serverPropsPath).exists()) {
                setUsedPropsPath(FILE + serverPropsPath);
                log.debug("Read properties file #0", usedPropsPath);
                props.load(new URL(usedPropsPathAsURL).openStream());
                log.debug("Reading properties file was successful.");
            } else {
                log.debug("Did not find properties file. Stop startup. Please create a properties file at #0", serverPropsPath);
                throw new IllegalStateException("Did not find properties file at '" + serverPropsPath + "'. Atarrabi startup has been stopped.");
            }
        } catch (IOException e) {
            log.error("Error while reading property file #0", usedPropsPath, e);
        }
        log.info(printConfigInfo());
        this.checkConfiguration();
    }

    /**
	 * Returns true if an init exception of type DB_CONN_ERROR exist.
	 * 
	 * @return true if an init exception of type DB_CONN_ERROR exist.
	 */
    public boolean isAnyDBConnErrorInitException() {
        for (InitException aInitException : this.initExceptions) {
            if (aInitException.getExceptionType() == InitExceptionType.DB_CONN_ERROR) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Prints all system <i>environment variables</i> (like JAVA_HOME, CLASSPATH, DISPLAY etc.) to standard output.
	 */
    public void printSystemEnvironmentVariables() {
        Map<String, String> env = new TreeMap<String, String>(System.getenv());
        StringBuilder sb = new StringBuilder();
        sb.append(NEWLINE + NEWLINE);
        sb.append("System environment variables:" + NEWLINE);
        sb.append("-----------------------------------------------" + NEWLINE);
        for (Entry<String, String> entry : env.entrySet()) {
            sb.append(entry.getKey() + COLON_SPACE + entry.getValue() + NEWLINE);
        }
        sb.append(NEWLINE);
        log.info(sb);
    }

    /**
	 * Prints all system <i>properties</i> (like java.home, java.vendor, file.separator etc.) to standard output.
	 */
    public void printSystemProperties() {
        Map<Object, Object> sysProps = new TreeMap<Object, Object>(System.getProperties());
        StringBuilder sb = new StringBuilder();
        sb.append(NEWLINE + NEWLINE);
        sb.append("System properties:" + NEWLINE);
        sb.append("-----------------------------------------------" + NEWLINE);
        for (Entry<Object, Object> entry : sysProps.entrySet()) {
            sb.append(entry.getKey() + COLON_SPACE + entry.getValue() + NEWLINE);
        }
        sb.append(NEWLINE);
        log.info(sb);
    }

    /**
	 * Refreshs all List of values.
	 */
    public void importAllLovs() {
        try {
            lovController.refreshAllLovs();
        } catch (IOException e) {
            log.error("Could not import all LOVs", e);
            statusMessages.add(Severity.ERROR, "Import of LOV failed.");
            return;
        }
        statusMessages.add(Severity.INFO, "All lists of values have been imported.");
        log.info("All list of values have been imported.");
        this.checkConfiguration();
    }

    /**
	 * Refreshes properties from file.
	 * @return message about success for console output
	 */
    public String refreshPropertiesFromFile() {
        try {
            log.debug("Refresh properties from #0", usedPropsPath);
            props.load(new URL(usedPropsPathAsURL).openStream());
            return "Refresh of external properties file successful.";
        } catch (IOException e) {
            log.error("Error while refreshing properties from file #0", usedPropsPath, e);
        }
        return "Refresh failed";
    }

    /**
	 * Saves properties from memory to properties file in configuration paths.
	 * @param properties
	 *            The properties to save.
	 * @return a String with the result of the save operation.
	 */
    public String saveProperties(Map<String, String> properties) {
        try {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                props.setProperty(entry.getKey(), entry.getValue());
            }
            props.store(new FileOutputStream(usedPropsPath), "");
            this.refreshPropertiesFromFile();
            return "Properties saved.";
        } catch (IOException e) {
            log.error("Could not write properties to file #0", usedPropsPath, e);
            return "Error during saving properties.";
        }
    }

    public void setAuthenticationMode(AuthenticationMode authenticationMode) {
        this.authenticationMode = authenticationMode;
    }

    public void setJndiPrefix(String jndiPrefix) {
        this.jndiPrefix = jndiPrefix;
    }

    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
    }

    /**
	 * Tests whether required properties are missing or empty in properties file.
	 * If properties are missing application startup is stopped!
	 */
    private void checkForMissingProperties() {
        List<String> missingProperties = new ArrayList<String>();
        for (AtarrabiConfigParam p : AtarrabiConfigParam.values()) {
            if (p.isRequired() && (!props.containsKey(p.getKeyName()) || props.getProperty(p.getKeyName()).isEmpty())) {
                missingProperties.add(p.getKeyName());
            }
        }
        if (missingProperties.size() > 0) {
            String errorMsg = "There are " + missingProperties.size() + " required properties missing or empty: " + missingProperties.toString() + " Please add them to your properties file (" + usedPropsPath + ") or enter a value to start atarrabi.";
            log.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
    }

    /**
	 * Checks if mail configuration provides host and port.
	 */
    private void checkOfValidMailSession() {
        MailSession ms = this.getMailSession();
        String host = ms.getHost();
        Integer port = ms.getPort();
        String errorMsg = "";
        boolean valid = true;
        if (host == null || "".equals(host)) {
            errorMsg += " host";
            valid = false;
        }
        if (port == null) {
            errorMsg += " port";
            valid = false;
        }
        if (!valid) {
            this.initExceptions.add(new InitException(this.getResourceBundle().getString("system.error.exception.invalidMailSession")));
            log.error("Please check mail configuration for:#0", errorMsg);
        } else {
            log.info("Configuration for sending emails is valid.");
        }
    }

    /**
	 * Method find an enumeration type for given string or null if not found.
	 * @param key property key to lookup
	 * @return Atarrabi configuration parameter or null
	 */
    private AtarrabiConfigParam findConfigParamFromString(String key) {
        for (AtarrabiConfigParam p : AtarrabiConfigParam.values()) {
            if (key.equals(p.getKeyName())) {
                return p;
            }
        }
        return null;
    }

    /**
	 * Delivers true if the specified key results into "true".
	 * Ignores cases.
	 * @param key The property key
	 * @return false if value of property is not "true" by ignoring case.
	 */
    private boolean getBoolean(String key) {
        return Boolean.parseBoolean(getStringValue(this.findConfigParamFromString(key)));
    }

    /**
	 * Read connection string from existing db connection.
	 * @return username string
	 */
    private String getDBConnectionString() throws IOException {
        try {
            DataSource ds = (DataSource) InitialContext.doLookup(jndiPrefix + this.getString(AtarrabiConfigParam.CERA2_DATASOURCE_JNDI_NAME));
            return ds.getConnection().getMetaData().getURL();
        } catch (NamingException e) {
            log.error("Could not obtain datasource: #0", e.getMessage());
            throw new IOException(e);
        } catch (SQLException e) {
            log.error("A database exception occured.", e);
            throw new IOException(e);
        }
    }

    /**
	 * Read used username from existing db connection.
	 * @return username string
	 */
    private String getDBConnectionUsername() throws IOException {
        try {
            DataSource ds = (DataSource) InitialContext.doLookup(jndiPrefix + this.getString(AtarrabiConfigParam.CERA2_DATASOURCE_JNDI_NAME));
            return ds.getConnection().getMetaData().getUserName();
        } catch (NamingException e) {
            log.error("Could not obtain datasource: #0", e.getMessage());
            throw new IOException(e);
        } catch (SQLException e) {
            log.error("A database exception occured.", e);
            throw new IOException(e);
        }
    }

    /**
	 * Method returns the configured mail session in components.xml.
	 * @return configured instance of mail session
	 */
    private MailSession getMailSession() {
        Component comp = Component.forName("org.jboss.seam.mail.mailSession");
        return (MailSession) comp.newInstance();
    }

    /**
	 * Returns the atarrabi properties file name based on the version number set in 
	 * buildinfo.properties. (E.g. "atarrabi-2.1.properties", "atarrabi-3.0.properties")
	 * @return the filename of the property file
	 */
    private String getPropertyFileName() {
        return "atarrabi-" + this.getAtarrabiBuildVersionNumber() + ".properties";
    }

    /**
	 * Provides access to seam resource bundle. Allows to access to properties directly.
	 * @return the SeamResourceBundle instance
	 */
    private SeamResourceBundle getResourceBundle() {
        return (SeamResourceBundle) Component.getInstance("org.jboss.seam.core.resourceBundle");
    }

    /**
	 * Detects server type (JBoss, Weblogic).
	 * @return server type
	 */
    private ServerType getServerType() {
        ServerType st = null;
        if (System.getProperty(WEBLOGIC_HOME) != null) {
            st = ServerType.WEBLOGIC;
        } else {
            st = ServerType.JBOSS;
        }
        if (st != null) {
            log.debug("Detected application server: #0", st);
        } else {
            log.debug("Could not detect server type.");
        }
        return st;
    }

    /**
	 * Delivers the value string of a property.
	 * @param param Atarrabi config parameter
	 * @return the value string of the property key
	 */
    private String getStringValue(AtarrabiConfigParam param) {
        if (param == null) {
            return null;
        }
        if (param.isRequired() && !props.containsKey(param.getKeyName())) {
            log.error("Mandatory key '#0' in configuration not found. Please check atarrabi2.properties.", param.getKeyName());
            throw new IllegalStateException("Mandatory key " + param.getKeyName() + " in atarrabi configuration not found");
        }
        return props.getProperty(param.getKeyName());
    }

    /**
	 * Generates a box of important properties to show in the log file/console.
	 * @return Configuration box string
	 */
    private String printConfigInfo() {
        String dbConnString = "UNKNOWN";
        String dbUserString = "UNKNOWN";
        try {
            dbConnString = this.getDBConnectionString();
            dbUserString = this.getDBConnectionUsername();
        } catch (IOException e) {
            log.error("Could not determine username or connection string for database connection.");
            this.initExceptions.add(new InitException(this.getResourceBundle().getString("system.error.exception.noConnectionToForeignDb")));
        }
        StringBuilder sb = new StringBuilder();
        sb.append(NEWLINE);
        sb.append("----------------------- Atarrabi Configuration -- Start --------------------");
        sb.append(NEWLINE);
        sb.append(HLINE);
        sb.append(NEWLINE);
        sb.append(" Software version information");
        sb.append(NEWLINE);
        sb.append(HLINE);
        sb.append(NEWLINE);
        sb.append(" Atarrabi version           : " + this.getAtarrabiBuildVersion() + NEWLINE);
        sb.append(" Atarrabi build date        : " + this.getAtarrabiBuildDate() + NEWLINE);
        sb.append(" Atarrabi build number      : " + this.getAtarrabiBuildNumber() + NEWLINE);
        sb.append(" Atarrabi SVN revision      : " + this.getAtarrabiBuildSvnRevision() + NEWLINE);
        sb.append(HLINE);
        sb.append(NEWLINE);
        sb.append(" Server configuration information");
        sb.append(NEWLINE);
        sb.append(HLINE);
        sb.append(NEWLINE);
        sb.append(" Detected application server: " + serverType + NEWLINE);
        sb.append(" Home URL                   : " + this.getString(AtarrabiConfigParam.HOME_URL) + NEWLINE);
        sb.append(" Context path               : " + servletContext.getContextPath() + NEWLINE);
        sb.append(" Property file              : " + usedPropsPath + NEWLINE);
        sb.append(" Version property file      : " + usedVersionPropsPath + NEWLINE);
        sb.append(" JNDI prefix                : " + jndiPrefix + NEWLINE);
        sb.append(" Authentication mode        : " + authenticationMode + NEWLINE);
        sb.append(HLINE);
        sb.append(NEWLINE);
        sb.append("External Database connection information");
        sb.append(NEWLINE);
        sb.append(HLINE);
        sb.append(NEWLINE);
        sb.append(" Datasource JNDI name       : " + this.getString(AtarrabiConfigParam.CERA2_DATASOURCE_JNDI_NAME) + NEWLINE);
        sb.append(" DB connection string       : " + dbConnString + NEWLINE);
        sb.append(" DB username                : " + dbUserString + NEWLINE);
        sb.append(HLINE);
        sb.append(NEWLINE);
        sb.append("----------------------- Atarrabi Configuration -- End ----------------------");
        sb.append(NEWLINE);
        return sb.toString();
    }

    /**
	 * Set authentication mode and paths for JBoss.
	 */
    private void setJBossSettings() {
        authenticationMode = AuthenticationMode.INTERNAL;
        jndiPrefix = "java:";
        String path = "";
        if (System.getProperty(JBOSS_SERVER_CONFIG_URL) != null && !"".equals(JBOSS_SERVER_CONFIG_URL)) {
            path = System.getProperty(JBOSS_SERVER_CONFIG_URL).replace(FILE, "");
        } else {
            path = "/opt/jboss-as-4.2.3.GA/server/default/conf/";
        }
        log.debug("Using configuration path: '#0'", path);
        serverPropsPath = path + this.getPropertyFileName();
    }

    /**
	 * Sets the properties paths (conventional and URL)
	 * @param propsPathAsURL properties path as URL
	 */
    private void setUsedPropsPath(String propsPathAsURL) {
        this.usedPropsPathAsURL = propsPathAsURL;
        this.usedPropsPath = propsPathAsURL.replace(FILE, "");
    }

    /**
	 * Set authentication mode and paths for weblogic.
	 */
    private void setWeblogicSettings() {
        authenticationMode = AuthenticationMode.LDAP;
        String path = "";
        if (System.getProperty(WEBLOGIC_USERDIR) != null && !"".equals(System.getProperty(WEBLOGIC_USERDIR))) {
            path = System.getProperty(WEBLOGIC_USERDIR) + "/";
            serverPropsPath = path + this.getPropertyFileName();
        }
        log.debug("Using configuration path: '#0'", path);
    }
}
