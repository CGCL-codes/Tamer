package vqwiki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.naming.InitialContext;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import vqwiki.db.DatabaseConnection;
import vqwiki.servlets.WikiServlet;
import vqwiki.utils.Encryption;
import vqwiki.utils.JSPUtils;

/**
 * Provides the basic environment
 * This class is a bit confusing at the moment (Nov 2002) - it is in the process of
 * being changed from one system of property defaulting and retrieval to another.
 *
 * To add a new property:
 *
 * - Define constants for property name and default value below
 * - Set the default in the constructor below
 * - Set the property in AdministrationServlet's doPost method, look for the "properties" section
 * - Provide a form element for the property in admin.jsp
 * - Specify literal strings by using properties in ApplicationResources*.properties
 */
public class Environment {

    public static final String WIKI_VERSION = "2.8-beta";

    public static final String PROP_FILE = "vqwiki.properties";

    private Properties currentProperties;

    private static Environment instance;

    public static final String PROPERTY_HOME_DIR = "homeDir";

    public static final String PROPERTY_UPLOAD_DIR = "uploadDir";

    public static final String PROPERTY_INDEX_REFRESH_INTERVAL = "indexRefreshInterval";

    public static final String PROPERTY_ALLOW_HTML = "allowHTML";

    public static final String PROPERTY_PERSISTENCE_TYPE = "persistenceType";

    public static final String PROPERTY_VERSIONING_ON = "versioningOn";

    public static final String PROPERTY_EDIT_TIME_OUT = "editTimeOut";

    public static final String PROPERTY_ALLOW_BACK_TICK = "allowBackTick";

    public static final String PROPERTY_DRIVER = "driver";

    public static final String PROPERTY_URL = "url";

    public static final String PROPERTY_USERNAME = "username";

    public static final String PROPERTY_PASSWORD = "password";

    public static final String PROPERTY_FORCE_USERNAME = "force-username";

    public static final String DEFAULT_LOGO_IMAGE_NAME = "/images/logo.jpg";

    public static final boolean DEFAULT_ATTACHMENTS_TO_DATABASE = true;

    public static final String PROPERTY_ATTACHMENT_TYPE = "attachment-type";

    public static final String PROPERTY_ATTACHMENT_TIMESTAMP = "attachment-timestamp";

    public static final String PROPERTY_ALLOW_TEMPLATES = "allow-templates";

    public static final String PROPERTY_USE_PREVIEW = "use-preview";

    public static final String PROPERTY_NEW_LINE_BREAKS = "new-line-breaks";

    public static final String PROPERTY_SMTP_HOST = "smtp-host";

    public static final String PROPERTY_REPLY_ADDRESS = "reply-address";

    public static final String PROPERTY_RECENT_CHANGES_DAYS = "recent-changes-days";

    public static final String PROPERTY_MAXIMUM_BACKLINKS = "maximum-backlinks";

    public static final String PROPERTY_MAX_FILE_SIZE = "max-file-size";

    public static final String PROPERTY_TEMP_DIR = "tmp-dir";

    public static final String PROPERTY_SMTP_USERNAME = "smtp-username";

    public static final String PROPERTY_SMTP_PASSWORD = "smtp-password";

    public static final String PROPERTY_PARSER = "parser";

    public static final String PROPERTY_FORMAT_LEXER = "format-lexer";

    public static final String PROPERTY_LINK_LEXER = "link-lexer";

    public static final String PROPERTY_LAYOUT_LEXER = "layout-lexer";

    public static final String PROPERTY_COOKIE_EXPIRE = "cookie-expire";

    public static final String PROPERTY_CONVERT_TABS = "convert-tabs";

    public static final String PROPERTY_BASE_CONTEXT = "base-context";

    public static final String PROPERTY_ADMIN_PASSWORD = "adminPassword";

    public static final String PROPERTY_FIRST_USE = "firstUse";

    public static final String PROPERTY_CONVERT_ENTITIES = "convert-entities";

    public static final String PROPERTY_DEFAULT_TOPIC = "default-topic";

    public static final String PROPERTY_DATABASE_TYPE = "database-type";

    public static final String PROPERTY_ALLOW_VWIKI_LIST = "allow-vqwiki-list";

    public static final String PROPERTY_DBCP_MAX_ACTIVE = "dbcp-max-active";

    public static final String PROPERTY_DBCP_MAX_IDLE = "dbcp-max-idle";

    public static final String PROPERTY_DBCP_TEST_ON_BORROW = "dbcp-test-on-borrow";

    public static final String PROPERTY_DBCP_TEST_ON_RETURN = "dbcp-test-on-return";

    public static final String PROPERTY_DBCP_TEST_WHILE_IDLE = "dbcp-test-while-idle";

    public static final String PROPERTY_DBCP_MIN_EVICTABLE_IDLE_TIME = "dbcp-min-evictable-idle-time";

    public static final String PROPERTY_DBCP_TIME_BETWEEN_EVICTION_RUNS = "dbcp-time-between-eviction-runs";

    public static final String PROPERTY_DBCP_NUM_TESTS_PER_EVICTION_RUN = "dbcp-num-tests-per-eviction-run";

    public static final String PROPERTY_DBCP_WHEN_EXHAUSTED_ACTION = "dbcp-when-exhausted-action";

    public static final String PROPERTY_DBCP_VALIDATION_QUERY = "dbcp-validation-query";

    public static final String PROPERTY_DBCP_REMOVE_ABANDONED = "dbcp-remove-abandoned";

    public static final String PROPERTY_DBCP_REMOVE_ABANDONED_TIMEOUT = "dbcp-remove-abandoned-timeout";

    public static final String PROPERTY_DBCP_LOG_ABANDONED = "dbcp-log-abandoned";

    public static final String PROPERTY_ATTACHMENT_INDEXING_ENABLED = "attachment-indexing";

    public static final String PROPERTY_EXTLINKS_INDEXING_ENABLED = "extlinks-indexing";

    public static final String PROPERTY_WIKI_SERVER_HOSTNAME = "wiki-server-hostname";

    public static final String PROPERTY_RECENT_CHANGES_REFRESH_INTERVAL = "recentChangesRefreshInterval";

    public static final String PROPERTY_FILE_ENCODING = "file-encoding";

    public static final String PROPERTY_SEPARATE_WIKI_TITLE_WORDS = "separate-wiki-title-words";

    public static final String PROPERTY_SUPPRESS_NOTIFY_WITHIN_SAME_DAY = "supress-notify-within-same-day";

    public static final String PROPERTY_USERGROUP_TYPE = "usergroup-type";

    public static final String PROPERTY_USERGROUP_FACTORY = "usergroupFactory";

    public static final String PROPERTY_USERGROUP_URL = "usergroupUrl";

    public static final String PROPERTY_USERGROUP_USERNAME = "usergroupUsername";

    public static final String PROPERTY_USERGROUP_PASSWORD = "usergroupPassword";

    public static final String PROPERTY_USERGROUP_BASIC_SEARCH = "usergroupBasicSearch";

    public static final String PROPERTY_USERGROUP_SEARCH_RESTRICTIONS = "usergroupSearchRestrictions";

    public static final String PROPERTY_USERGROUP_USERID_FIELD = "usergroupUseridField";

    public static final String PROPERTY_USERGROUP_FULLNAME_FIELD = "usergroupFullnameField";

    public static final String PROPERTY_USERGROUP_MAIL_FIELD = "usergroupMailField";

    public static final String PROPERTY_USERGROUP_DETAILVIEW = "usergroupDetailView";

    public static final String PROPERTY_FORCE_ENCODING = "force-encoding";

    public static final String PROPERTY_TOC_MAKE = "toc-make";

    public static final String PROPERTY_TOC_MINIMUM_HEADERS = "toc-minimum-headers";

    public static final String PROPERTY_FRANZ_NEWTOPIC_STYLE = "franz-newtopic-style";

    public static final String PROPERTY_EXTERNALLINK_NEWWINDOW = "externalLinkInNewWindow";

    public static final String PROPERTY_ENCODE_PASSWORDS = "encode-passwords";

    public static final String PROPERTY_USE_NEW_PARSER = "new-parser";

    public static final String PROPERTY_ERROR_ADMIN_MESSAGE = "error.admin.message";

    public static final String PROPERTY_CAPTCHA_VOICE_VALUE = "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory";

    public static final String PROPERTY_CAPTCHA_VOICE = "freetts.voices";

    private static final HashMap DEFAULTS = new HashMap();

    private static final Logger logger = Logger.getLogger(Environment.class);

    /** The actual local path that is the base of the context, i.e. where the wiki is installed */
    private String realPath;

    private String logoImageName;

    private boolean attachmentsToDatabase;

    private static List wikinameIgnore;

    /**
     * Initialise the environment (put defaults in a table)
     */
    private Environment() {
        logger.debug("new Environment");
        DEFAULTS.put(PROPERTY_UPLOAD_DIR, "upload");
        DEFAULTS.put(PROPERTY_INDEX_REFRESH_INTERVAL, "1440");
        DEFAULTS.put(PROPERTY_ALLOW_HTML, "false");
        DEFAULTS.put(PROPERTY_PERSISTENCE_TYPE, "FILE");
        DEFAULTS.put(PROPERTY_VERSIONING_ON, "true");
        DEFAULTS.put(PROPERTY_EDIT_TIME_OUT, "10");
        DEFAULTS.put(PROPERTY_ALLOW_BACK_TICK, "true");
        DEFAULTS.put(PROPERTY_DRIVER, "org.gjt.mm.mysql.Driver");
        DEFAULTS.put(PROPERTY_URL, "jdbc:mysql://localhost/vqwiki");
        DEFAULTS.put(PROPERTY_USERNAME, "vqwiki");
        DEFAULTS.put(PROPERTY_PASSWORD, "vqwiki");
        DEFAULTS.put(PROPERTY_FORCE_USERNAME, "false");
        DEFAULTS.put(PROPERTY_MAX_FILE_SIZE, "2000000");
        DEFAULTS.put(PROPERTY_TEMP_DIR, "tmp");
        DEFAULTS.put(PROPERTY_SMTP_USERNAME, "");
        DEFAULTS.put(PROPERTY_SMTP_PASSWORD, "");
        DEFAULTS.put(PROPERTY_PARSER, "vqwiki.lex.DefaultWikiParser");
        DEFAULTS.put(PROPERTY_FORMAT_LEXER, "vqwiki.lex.FormatLex");
        DEFAULTS.put(PROPERTY_LINK_LEXER, "vqwiki.lex.LinkLex");
        DEFAULTS.put(PROPERTY_LAYOUT_LEXER, "vqwiki.lex.LayoutLex");
        DEFAULTS.put(PROPERTY_RECENT_CHANGES_DAYS, "5");
        DEFAULTS.put(PROPERTY_MAXIMUM_BACKLINKS, "20");
        DEFAULTS.put(PROPERTY_REPLY_ADDRESS, "vqwiki-admin@localhost");
        DEFAULTS.put(PROPERTY_SMTP_HOST, "");
        DEFAULTS.put(PROPERTY_NEW_LINE_BREAKS, "1");
        DEFAULTS.put(PROPERTY_ALLOW_TEMPLATES, "true");
        DEFAULTS.put(PROPERTY_USE_PREVIEW, "false");
        DEFAULTS.put(PROPERTY_ATTACHMENT_TYPE, "inline");
        DEFAULTS.put(PROPERTY_ATTACHMENT_TIMESTAMP, "true");
        DEFAULTS.put(PROPERTY_COOKIE_EXPIRE, "31104000");
        DEFAULTS.put(PROPERTY_CONVERT_TABS, "true");
        DEFAULTS.put(PROPERTY_FIRST_USE, "true");
        DEFAULTS.put(PROPERTY_CONVERT_ENTITIES, "false");
        DEFAULTS.put(PROPERTY_DEFAULT_TOPIC, "StartingPoints");
        DEFAULTS.put(PROPERTY_DATABASE_TYPE, "mysql");
        DEFAULTS.put(PROPERTY_ALLOW_VWIKI_LIST, "true");
        DEFAULTS.put(PROPERTY_DBCP_MAX_ACTIVE, "10");
        DEFAULTS.put(PROPERTY_DBCP_MAX_IDLE, "3");
        DEFAULTS.put(PROPERTY_DBCP_TEST_ON_BORROW, "true");
        DEFAULTS.put(PROPERTY_DBCP_TEST_ON_RETURN, "true");
        DEFAULTS.put(PROPERTY_DBCP_TEST_WHILE_IDLE, "true");
        DEFAULTS.put(PROPERTY_DBCP_MIN_EVICTABLE_IDLE_TIME, "600");
        DEFAULTS.put(PROPERTY_DBCP_TIME_BETWEEN_EVICTION_RUNS, "120");
        DEFAULTS.put(PROPERTY_DBCP_NUM_TESTS_PER_EVICTION_RUN, "5");
        DEFAULTS.put(PROPERTY_DBCP_WHEN_EXHAUSTED_ACTION, String.valueOf(GenericObjectPool.WHEN_EXHAUSTED_GROW));
        DEFAULTS.put(PROPERTY_DBCP_VALIDATION_QUERY, "SELECT 1");
        DEFAULTS.put(PROPERTY_DBCP_REMOVE_ABANDONED, "true");
        DEFAULTS.put(PROPERTY_DBCP_REMOVE_ABANDONED_TIMEOUT, "120");
        DEFAULTS.put(PROPERTY_DBCP_LOG_ABANDONED, "true");
        DEFAULTS.put(PROPERTY_WIKI_SERVER_HOSTNAME, "");
        DEFAULTS.put(PROPERTY_FILE_ENCODING, "utf-8");
        DEFAULTS.put(PROPERTY_ATTACHMENT_INDEXING_ENABLED, "false");
        DEFAULTS.put(PROPERTY_EXTLINKS_INDEXING_ENABLED, "false");
        DEFAULTS.put(PROPERTY_RECENT_CHANGES_REFRESH_INTERVAL, "1");
        DEFAULTS.put(PROPERTY_SEPARATE_WIKI_TITLE_WORDS, "false");
        DEFAULTS.put(PROPERTY_SUPPRESS_NOTIFY_WITHIN_SAME_DAY, "false");
        DEFAULTS.put(PROPERTY_USERGROUP_TYPE, "");
        DEFAULTS.put(PROPERTY_USERGROUP_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        DEFAULTS.put(PROPERTY_USERGROUP_URL, "ldap://localhost:389");
        DEFAULTS.put(PROPERTY_USERGROUP_USERNAME, "");
        DEFAULTS.put(PROPERTY_USERGROUP_PASSWORD, "");
        DEFAULTS.put(PROPERTY_USERGROUP_BASIC_SEARCH, "ou=users,dc=mycompany,dc=com");
        DEFAULTS.put(PROPERTY_USERGROUP_SEARCH_RESTRICTIONS, "objectClass=person");
        DEFAULTS.put(PROPERTY_USERGROUP_USERID_FIELD, "uid");
        DEFAULTS.put(PROPERTY_USERGROUP_MAIL_FIELD, "mail");
        DEFAULTS.put(PROPERTY_USERGROUP_FULLNAME_FIELD, "cn");
        DEFAULTS.put(PROPERTY_USERGROUP_DETAILVIEW, "@@cn@@</a><br/>@@title@@<br/>Telefon: @@telephoneNumber@@<br/>Mobil: @@mobile@@<br/>@@ou@@ / @@businessCategory@@<br/><a href=\"mailto:@@mail@@\">@@mail@@</a> <br/>");
        DEFAULTS.put(PROPERTY_FORCE_ENCODING, "ISO-8859-1");
        DEFAULTS.put(PROPERTY_TOC_MAKE, "false");
        DEFAULTS.put(PROPERTY_TOC_MINIMUM_HEADERS, "3");
        DEFAULTS.put(PROPERTY_FRANZ_NEWTOPIC_STYLE, "false");
        DEFAULTS.put(PROPERTY_EXTERNALLINK_NEWWINDOW, "false");
        DEFAULTS.put(PROPERTY_ENCODE_PASSWORDS, "false");
        DEFAULTS.put(PROPERTY_USE_NEW_PARSER, "false");
        DEFAULTS.put(PROPERTY_ERROR_ADMIN_MESSAGE, "");
        logger.info("Using properties file location: " + getPropertiesFileLocation());
        refresh();
        DEFAULTS.put(PROPERTY_CAPTCHA_VOICE, PROPERTY_CAPTCHA_VOICE_VALUE);
    }

    /**
     *
     */
    public static String relativeDirIfNecessary(String path) {
        if (path.length() <= 2) {
            return path;
        }
        if (!path.startsWith("/") && !(Character.isLetter(path.charAt(0)) && path.charAt(1) == ':')) {
            return new File(dir(), path).getAbsolutePath();
        }
        return path;
    }

    /**
     * The directory to place attachments in. This is either an absolute path if the admin setting for "upload directory"
     * starts with a "/" or a drive letter, or it is a relative path.
     * @param virtualWiki
     * @param name
     * @return
     */
    public File uploadPath(String virtualWiki, String name) {
        String dir = Environment.relativeDirIfNecessary(Environment.getInstance().getUploadDir());
        if (virtualWiki == null || "".equals(virtualWiki)) {
            virtualWiki = WikiBase.DEFAULT_VWIKI;
        }
        File baseDir = new File(dir, virtualWiki);
        baseDir.mkdirs();
        dir = baseDir.getAbsolutePath();
        File uploadedFile = new File(dir, name);
        return uploadedFile;
    }

    /**
     * Get a stream from a resource name using this classes class loader
     * @param resourceName
     * @return
     */
    public InputStream getResourceAsStream(String resourceName) {
        return Environment.class.getResourceAsStream(resourceName);
    }

    /**
     * Get default value for an admin property
     * @param settingName
     * @return
     */
    private String defaultValue(String settingName) {
        Object object = DEFAULTS.get(settingName);
        return (String) object;
    }

    /**
     * Return a location to store/retrieve the VQWiki properties file. This is in order of precedence:
     * <ol>
     *   <li>The path given by the context environment entry "propertiesFile"</li>
     *   <li>The path found by looking for the resource named "/vqwiki.properties" using the class loader
     * for this class. This will usually be located in WEB-INF/classes</li>
     *   <li>A file called vqwiki.properties in the process owner's home directory</li>
     * </ol>
     * @return properties file location
     */
    private String getPropertiesFileLocation() {
        String propertiesFilePath = null;
        try {
            propertiesFilePath = System.getProperty("vqwiki.propertiesFile");
        } catch (Exception e) {
            logger.debug("Cannot retrieve vqwiki.propertiesFile system property.");
        }
        if (propertiesFilePath == null || propertiesFilePath.trim().length() == 0) {
            try {
                InitialContext ictx = new InitialContext();
                propertiesFilePath = (String) ictx.lookup("java:comp/env/propertiesFile");
                logger.debug("properties file from context: " + propertiesFilePath);
            } catch (Exception e) {
                logger.debug("No entry for properties in context:\n" + e.toString());
            }
        }
        if (propertiesFilePath == null) {
            URL resource = Environment.class.getResource("/vqwiki.properties");
            logger.debug("properties file as resource: " + resource);
            if (resource != null) {
                String defaultencoding = null;
                try {
                    defaultencoding = System.getProperty("file.encoding");
                } catch (AccessControlException ae) {
                    logger.warn("This application server doesn't allow to access " + "file.encoding with System.getProperty. Set default " + "encoding for filename-URL to UTF-8");
                    defaultencoding = "UTF-8";
                }
                try {
                    propertiesFilePath = URLDecoder.decode(resource.getFile(), defaultencoding);
                } catch (UnsupportedEncodingException e) {
                    logger.error("The platform's default encoding is not supported in the JDK.", e);
                    try {
                        propertiesFilePath = URLDecoder.decode(resource.getFile(), "UTF-8");
                    } catch (UnsupportedEncodingException e1) {
                        logger.fatal("Even UTF-8 is not supported by this JDK!", e1);
                    }
                }
                logger.debug("properties file as file from resource: " + propertiesFilePath);
            }
        }
        if (propertiesFilePath == null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(System.getProperty("user.home"));
            buffer.append(System.getProperty("file.separator"));
            buffer.append("wiki/vqwiki.properties");
            propertiesFilePath = buffer.toString();
        }
        return propertiesFilePath;
    }

    /**
     *
     */
    private InputStream getPropertiesInputStream() throws Exception {
        String propertiesFile = getPropertiesFileLocation();
        if (propertiesFile == null) {
            return getResourceAsStream("/vqwiki.properties");
        }
        return new FileInputStream(propertiesFile);
    }

    /**
     *
     */
    public String getStringSetting(String settingName) {
        try {
            if (currentProperties == null) {
                currentProperties = new Properties();
                currentProperties.load(getPropertiesInputStream());
            }
        } catch (Exception e) {
            logger.error(e);
            return defaultValue(settingName);
        }
        String value = currentProperties.getProperty(settingName);
        if (value == null) {
            return defaultValue(settingName);
        }
        return value;
    }

    /**
     *
     */
    public int getIntSetting(String settingName) {
        String asString = getStringSetting(settingName);
        logger.debug("Setting as string:" + asString);
        return Integer.parseInt(asString);
    }

    /**
     *
     */
    public boolean getBooleanSetting(String settingName) {
        return Boolean.valueOf(getStringSetting(settingName)).booleanValue();
    }

    /**
     *
     */
    public void setSetting(String settingName, String value) {
        try {
            if (currentProperties == null) {
                currentProperties = new Properties();
                currentProperties.load(getPropertiesInputStream());
            }
        } catch (Exception e) {
            logger.error("Error setting " + settingName, e);
            return;
        }
        currentProperties.setProperty(settingName, value);
    }

    /**
     *
     */
    public void setSetting(String settingName, boolean value) {
        setSetting(settingName, String.valueOf(value));
    }

    /**
     *
     */
    public void setSetting(String settingName, int value) {
        setSetting(settingName, String.valueOf(value));
    }

    /**
     *
     */
    public boolean getForceUsername() {
        return getBooleanSetting(PROPERTY_FORCE_USERNAME);
    }

    /**
     * Re-read the properties file and sets the home directory. If no properties
     * file is found it will be the user's home directory + "/wiki"
     */
    public void refresh() {
        currentProperties = new Properties();
        File f = null;
        try {
            f = getPropsFile();
            logger.debug("Loading properties from " + f);
            currentProperties.load(new FileInputStream(f));
            if (currentProperties.getProperty(PROPERTY_HOME_DIR) == null) {
                currentProperties.setProperty(PROPERTY_HOME_DIR, System.getProperty("user.home") + System.getProperty("file.separator") + "wiki");
            }
            if (currentProperties.getProperty("attachmentsToDatabase") == null) {
                setSetting(PROPERTY_ALLOW_BACK_TICK, DEFAULT_ATTACHMENTS_TO_DATABASE);
            } else {
                setSetting(PROPERTY_ALLOW_BACK_TICK, Boolean.valueOf(currentProperties.getProperty("allowBackTick")).booleanValue());
            }
        } catch (Exception e) {
            logger.debug("Couldn't access properrties file, using static defaults: " + f);
            logger.debug("Handled exception: " + e);
            currentProperties = new Properties();
            currentProperties.setProperty(PROPERTY_HOME_DIR, System.getProperty("user.home") + System.getProperty("file.separator") + "wiki");
        }
        logger.debug("Properties: " + currentProperties);
        logoImageName = lookupLogoImageName();
        if (getPersistenceType() == WikiBase.DATABASE) {
            DatabaseConnection.setPoolInitialized(false);
        }
    }

    /**
     *
     */
    public void saveProperties() throws Exception {
        File f = getPropsFile();
        logger.debug("Writing to properties file: " + f);
        if (currentProperties == null) {
            currentProperties = new Properties();
        }
        OutputStream out = getPropertiesOutputStream();
        logger.debug(currentProperties);
        currentProperties.store(out, "VQWiki");
        out.close();
    }

    /**
     * Singleton reinforcement
     */
    public static Environment getInstance() {
        if (instance == null) {
            instance = new Environment();
        }
        return instance;
    }

    /**
     *
     */
    private File getPropsFile() throws Exception {
        return new File(getPropertiesFileLocation());
    }

    /**
     * Returns the base directory for the Wiki file-system
     */
    public String getHomeDir() {
        return getStringSetting(PROPERTY_HOME_DIR);
    }

    /**
     *
     */
    public void setHomeDir(String dir) {
        setSetting(PROPERTY_HOME_DIR, dir);
    }

    /**
     * Returns the directory for uploading topic attachments to
     */
    public String getUploadDir() {
        return getStringSetting(PROPERTY_UPLOAD_DIR);
    }

    /**
     *
     */
    public int getIndexRefreshInterval() {
        return getIntSetting(PROPERTY_INDEX_REFRESH_INTERVAL);
    }

    /**
     *
     */
    public void setIndexRefreshInterval(int interval) {
        setSetting(PROPERTY_INDEX_REFRESH_INTERVAL, interval);
    }

    /**
     *
     */
    public int getRecentChangesRefreshInterval() {
        return getIntSetting(PROPERTY_RECENT_CHANGES_REFRESH_INTERVAL);
    }

    /**
     *
     */
    public void setRecentChangesRefreshInterval(int interval) {
        setSetting(PROPERTY_RECENT_CHANGES_REFRESH_INTERVAL, interval);
    }

    /**
     *
     */
    public boolean getAllowHTML() {
        return getBooleanSetting(PROPERTY_ALLOW_HTML);
    }

    /**
     *
     */
    public void setAllowHTML(boolean allow) {
        setSetting(PROPERTY_ALLOW_HTML, allow);
    }

    /**
     *
     */
    public boolean isVersioningOn() {
        return getBooleanSetting(PROPERTY_VERSIONING_ON);
    }

    /**
     *
     */
    public void setVersioningOn(boolean on) {
        setSetting(PROPERTY_VERSIONING_ON, on);
    }

    /**
     *
     */
    public int getPersistenceType() {
        if (getStringSetting(PROPERTY_PERSISTENCE_TYPE).equals("DATABASE")) {
            return WikiBase.DATABASE;
        } else {
            return WikiBase.FILE;
        }
    }

    /**
     *
     */
    public void setPersistenceType(int persistenceType) {
        if (persistenceType == WikiBase.FILE) {
            setSetting(PROPERTY_PERSISTENCE_TYPE, "FILE");
        } else if (persistenceType == WikiBase.DATABASE) {
            setSetting(PROPERTY_PERSISTENCE_TYPE, "DATABASE");
        }
    }

    /**
     *
     */
    public int getEditTimeOut() {
        return getIntSetting(PROPERTY_EDIT_TIME_OUT);
    }

    /**
     *
     */
    public void setEditTimeOut(int timeout) {
        setSetting(PROPERTY_EDIT_TIME_OUT, timeout);
    }

    /**
     *
     */
    public void setUploadDir(String dir) {
        setSetting(PROPERTY_UPLOAD_DIR, dir);
    }

    /**
     *
     */
    public static String dir() {
        return getInstance().getHomeDir() + System.getProperty("file.separator");
    }

    /**
     *
     */
    public String getBaseContext() {
        return getStringSetting(PROPERTY_BASE_CONTEXT);
    }

    /**
     *
     */
    public void setBaseContext(String baseContext) {
        setSetting(PROPERTY_BASE_CONTEXT, baseContext);
    }

    /**
     *
     */
    public boolean isAllowBackTick() {
        return getBooleanSetting(PROPERTY_ALLOW_BACK_TICK);
    }

    /**
     *
     */
    public void setAllowBackTick(boolean allowBackTick) {
        setSetting(PROPERTY_ALLOW_BACK_TICK, allowBackTick);
    }

    /**
     *
     */
    public boolean isAttachmentsToDatabase() {
        return attachmentsToDatabase;
    }

    /**
     *
     */
    public void setAttachmentsToDatabase(boolean _attachmentsToDatabase) {
        this.attachmentsToDatabase = _attachmentsToDatabase;
    }

    /**
     *
     */
    public String getDriver() {
        return getStringSetting(PROPERTY_DRIVER);
    }

    /**
     *
     */
    public void setDriver(String driver) {
        setSetting(PROPERTY_DRIVER, driver);
    }

    /**
     *
     */
    public String getUrl() {
        return getStringSetting(PROPERTY_URL);
    }

    /**
     *
     */
    public void setUrl(String url) {
        setSetting(PROPERTY_URL, url);
    }

    /**
     *
     */
    public String getUserName() {
        return getStringSetting(PROPERTY_USERNAME);
    }

    /**
     *
     */
    public void setUserName(String userName) {
        setSetting(PROPERTY_USERNAME, userName);
    }

    /**
     *
     */
    public String getPassword() {
        if (getBooleanSetting(PROPERTY_ENCODE_PASSWORDS)) {
            return Encryption.decrypt(getStringSetting(PROPERTY_PASSWORD));
        }
        return getStringSetting(PROPERTY_PASSWORD);
    }

    /**
     *
     */
    public void setPassword(String password) throws Exception {
        if (getBooleanSetting(PROPERTY_ENCODE_PASSWORDS)) {
            password = Encryption.encrypt(password);
        }
        setSetting(PROPERTY_PASSWORD, password);
    }

    /**
     *
     */
    public boolean isForceUsername() {
        return getBooleanSetting(PROPERTY_FORCE_USERNAME);
    }

    /**
     *
     */
    public void setForceUsername(boolean forceUsername) {
        setSetting(PROPERTY_FORCE_USERNAME, forceUsername);
    }

    /**
     *
     */
    public String getLogoImageName() {
        return this.logoImageName;
    }

    /**
     *
     */
    public boolean isDefaultLogoImageName() {
        return DEFAULT_LOGO_IMAGE_NAME.equals(logoImageName);
    }

    /**
     *
     */
    public boolean isLogoImageAbsoluteUrl() {
        return lookupLogoImageName().indexOf(':') >= 0;
    }

    /**
     *
     */
    public boolean isEmailAvailable() {
        String smtpHost = this.getStringSetting(PROPERTY_SMTP_HOST);
        if (smtpHost != null) {
            return !"".equals(smtpHost);
        }
        return false;
    }

    /**
     *
     */
    public boolean isAllowVirtualWikiList() {
        return this.getBooleanSetting(PROPERTY_ALLOW_VWIKI_LIST);
    }

    /**
     *
     */
    public boolean isTemplatesAvailable() {
        return this.getBooleanSetting(PROPERTY_ALLOW_TEMPLATES);
    }

    /**
     *
     */
    public boolean isPreviewAvailable() {
        return this.getBooleanSetting(PROPERTY_USE_PREVIEW);
    }

    /**
     *
     */
    public boolean isConvertTabs() {
        return this.getBooleanSetting(PROPERTY_CONVERT_TABS);
    }

    /**
     *
     */
    public String getDefaultTopic() {
        return this.getStringSetting(PROPERTY_DEFAULT_TOPIC);
    }

    /**
     *
     */
    public String getDefaultTopicEncoded() {
        return JSPUtils.encodeURL(this.getStringSetting(PROPERTY_DEFAULT_TOPIC));
    }

    /**
     *
     */
    private String lookupLogoImageName() {
        String _logoImageName = null;
        try {
            InitialContext ictx = new InitialContext();
            _logoImageName = (String) ictx.lookup("java:comp/env/logoImageName");
            if (_logoImageName != null && !"".equals(logoImageName)) {
                return _logoImageName;
            }
        } catch (Exception e) {
            logger.debug("Exception retrieving logoImageName:\n" + e.toString());
        }
        return DEFAULT_LOGO_IMAGE_NAME;
    }

    /**
     *
     */
    private OutputStream getPropertiesOutputStream() throws Exception {
        String propertiesFile = getPropertiesFileLocation();
        return new FileOutputStream(propertiesFile, false);
    }

    /**
     *
     */
    public boolean isFirstUse() {
        if (getBooleanSetting(PROPERTY_FIRST_USE)) {
            logger.info("First use of VQWiki, creating admin password");
            try {
                setAdminPassword(generateNewAdminPassword());
                setSetting(PROPERTY_FIRST_USE, false);
                saveProperties();
            } catch (Exception e) {
                logger.error(e);
            }
            return true;
        }
        return false;
    }

    /**
     *
     */
    public String getAdminPassword() {
        if (getBooleanSetting(PROPERTY_ENCODE_PASSWORDS)) {
            return Encryption.decrypt(getStringSetting(PROPERTY_ADMIN_PASSWORD));
        }
        return getStringSetting(PROPERTY_ADMIN_PASSWORD);
    }

    /**
     *
     */
    public void setAdminPassword(String password) throws Exception {
        if (getBooleanSetting(PROPERTY_ENCODE_PASSWORDS)) {
            password = Encryption.encrypt(password);
        }
        setSetting(PROPERTY_ADMIN_PASSWORD, password);
    }

    /**
     *
     */
    public String generateNewAdminPassword() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 5; i++) {
            int n = (int) (Math.random() * 26 + 65);
            buffer.append((char) n);
        }
        String value = buffer.toString();
        return value;
    }

    /**
     *
     */
    public String getSmtpPassword() {
        if (getBooleanSetting(PROPERTY_ENCODE_PASSWORDS)) {
            return Encryption.decrypt(getStringSetting(PROPERTY_SMTP_PASSWORD));
        }
        return getStringSetting(PROPERTY_SMTP_PASSWORD);
    }

    /**
     *
     */
    public void setSmtpPassword(String smtpPassword) throws Exception {
        if (getBooleanSetting(PROPERTY_ENCODE_PASSWORDS)) {
            smtpPassword = Encryption.encrypt(smtpPassword);
        }
        setSetting(PROPERTY_SMTP_PASSWORD, smtpPassword);
    }

    /**
     *
     */
    public String getUserGroupPassword() {
        if (getBooleanSetting(PROPERTY_ENCODE_PASSWORDS)) {
            return Encryption.decrypt(getStringSetting(PROPERTY_USERGROUP_PASSWORD));
        }
        return getStringSetting(PROPERTY_USERGROUP_PASSWORD);
    }

    /**
     *
     */
    public void setUserGroupPassword(String userGroupPassword) throws Exception {
        if (getBooleanSetting(PROPERTY_ENCODE_PASSWORDS)) {
            userGroupPassword = Encryption.encrypt(userGroupPassword);
        }
        setSetting(PROPERTY_USERGROUP_PASSWORD, userGroupPassword);
    }

    /**
     *
     */
    public boolean isMySQL() {
        return ("mysql".equalsIgnoreCase(getDatabaseType()));
    }

    /**
     *
     */
    public boolean isOracle() {
        return ("oracle".equalsIgnoreCase(getDatabaseType()));
    }

    /**
     *
     */
    public String getDatabaseType() {
        return getStringSetting(PROPERTY_DATABASE_TYPE);
    }

    /**
     *
     */
    public String getFileEncoding() {
        return getStringSetting(PROPERTY_FILE_ENCODING);
    }

    /**
     *
     */
    public String getForceEncoding() {
        return getStringSetting(PROPERTY_FORCE_ENCODING);
    }

    /**
     * Property for making automatic TOC on the right side.
     * @return true if there shold be a table of content on every site, false otherwise.
     */
    public boolean isTocInsert() {
        return getBooleanSetting(PROPERTY_TOC_MAKE);
    }

    /**
     * Property for the minimum headlines to activate a TOC.
     * @return minimum headlines to activate TOC.
     */
    public int getTocMinimumHeaders() {
        return getIntSetting(PROPERTY_TOC_MINIMUM_HEADERS);
    }

    /**
     * Property for switching between Franz Newtopic style (a new topic has a question mark after the topic) or
     * CSS newtopic style (the whole topic will be displayed with the css-class "newtopic").
     * @return true if a new topic should have a clickable question mark after the word,
     *         false if a new topic word should be displayed with the css class "newtopic".
     */
    public boolean isFranzNewTopicStyle() {
        return getBooleanSetting(PROPERTY_FRANZ_NEWTOPIC_STYLE);
    }

    public boolean isOpenExtLinkInNewWindow() {
        return getBooleanSetting(PROPERTY_EXTERNALLINK_NEWWINDOW);
    }

    /**
     *
     */
    public boolean doIgnoreWikiname(String name) {
        if (wikinameIgnore == null) {
            wikinameIgnore = new ArrayList();
            InputStream in = getClass().getResourceAsStream("/wikiname.ignore");
            if (in == null) {
                logger.debug("No wikinames to ignore, wikiname.ignore does not exist");
                return false;
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    logger.debug("Adding " + line.toLowerCase() + " to ignore list");
                    wikinameIgnore.add(line.toLowerCase());
                }
                reader.close();
                in.close();
            } catch (IOException e) {
                logger.warn("Error reading wikiname.ignore", e);
            }
        }
        if (wikinameIgnore.isEmpty()) {
            return false;
        }
        boolean ignore = wikinameIgnore.contains(name.toLowerCase());
        if (ignore) {
            logger.debug("Do ignore " + name);
        }
        return ignore;
    }

    /**
     *
     */
    public boolean isAttachmentIndexingEnabled() {
        return getBooleanSetting(PROPERTY_ATTACHMENT_INDEXING_ENABLED);
    }

    /**
     *
     */
    public boolean isAttachmentTimestamp() {
        return this.getBooleanSetting(PROPERTY_ATTACHMENT_TIMESTAMP);
    }

    /**
     *
     */
    public boolean isExtLinksIndexingEnabled() {
        return getBooleanSetting(PROPERTY_EXTLINKS_INDEXING_ENABLED);
    }

    /**
     * Return true if titles are to be spaced between the humps in the CamelCaps, e.g "Camel Caps"
     *
     * @return true if separate title words is on
     */
    public boolean isSeparateWikiTitleWords() {
        return this.getBooleanSetting(PROPERTY_SEPARATE_WIKI_TITLE_WORDS);
    }

    /**
     * This is set by the main controller servlet on every request
     * @param realPath the actual local path that is the base of the context
     */
    public void setRealPath(String realPath) {
        logger.debug("real path: " + realPath);
        this.realPath = realPath;
    }

    /**
     * the actual local path that is the base of the context
     * @return path
     */
    public String getRealPath() {
        return realPath;
    }

    /**
     * @return Returns the usergroupType.
     */
    public int getUsergroupType() {
        String persistenceType = getStringSetting(PROPERTY_USERGROUP_TYPE);
        if (persistenceType.equals("LDAP")) {
            return WikiBase.LDAP;
        } else if (persistenceType.equals("DATABASE")) {
            return WikiBase.DATABASE;
        } else {
            return 0;
        }
    }

    /**
     * @param membershipType The usergroupType to set.
     */
    public void setUsergroupType(int membershipType) {
        String usergroupType;
        if (membershipType == WikiBase.LDAP) {
            usergroupType = "LDAP";
        } else if (membershipType == WikiBase.DATABASE) {
            usergroupType = "DATABASE";
        } else {
            usergroupType = "";
        }
        setSetting(PROPERTY_USERGROUP_TYPE, usergroupType);
    }

    /**
     *
     */
    public boolean getEncodePasswords() {
        return getBooleanSetting(PROPERTY_ENCODE_PASSWORDS);
    }

    /**
     *
     */
    public void setEncodePasswords(boolean encode) throws Exception {
        String adminPassword = this.getAdminPassword();
        String dbPassword = this.getPassword();
        String smtpPassword = this.getSmtpPassword();
        String userGroupPassword = this.getUserGroupPassword();
        setSetting(PROPERTY_ENCODE_PASSWORDS, encode);
        setAdminPassword(adminPassword);
        setPassword(dbPassword);
        setSmtpPassword(smtpPassword);
        setUserGroupPassword(userGroupPassword);
    }

    /**
     * Gets administration error message (e.g. who the user should call for problems).
     * @return the error message if any
     */
    public String getAdminErrorMessage() {
        return getStringSetting(PROPERTY_ERROR_ADMIN_MESSAGE);
    }

    /**
     * Sets administration error message (e.g. who the user should call for problems).
     * @param value the error message
     */
    public void setAdminErrorMessage(String value) {
        setSetting(PROPERTY_ERROR_ADMIN_MESSAGE, value);
    }

    /**
     * FIXME (PARSER_TEMP) - temporary property until conversion is complete
     */
    public boolean getUseNewParser() {
        return getBooleanSetting(PROPERTY_USE_NEW_PARSER);
    }

    /**
     * FIXME (PARSER_TEMP) - temporary property until conversion is complete
     */
    public void setUseNewParser(boolean encode) throws Exception {
        setSetting(PROPERTY_USE_NEW_PARSER, encode);
    }

    /**
     *
     */
    public String getActionAdmin() {
        return WikiServlet.ACTION_ADMIN;
    }

    /**
     *
     */
    public String getActionAllTopics() {
        return WikiServlet.ACTION_ALL_TOPICS;
    }

    /**
     *
     */
    public String getActionAppend() {
        return WikiServlet.ACTION_APPEND;
    }

    /**
     *
     */
    public String getActionAttach() {
        return WikiServlet.ACTION_ATTACH;
    }

    /**
     *
     */
    public String getActionCancel() {
        return WikiServlet.ACTION_CANCEL;
    }

    /**
     *
     */
    public String getActionDiff() {
        return WikiServlet.ACTION_DIFF;
    }

    /**
     *
     */
    public String getActionEdit() {
        return WikiServlet.ACTION_EDIT;
    }

    /**
     *
     */
    public String getActionEditUser() {
        return WikiServlet.ACTION_EDIT_USER;
    }

    /**
     *
     */
    public String getActionFirstUse() {
        return WikiServlet.ACTION_FIRST_USE;
    }

    /**
     *
     */
    public String getActionHistory() {
        return WikiServlet.ACTION_HISTORY;
    }

    /**
     *
     */
    public String getActionImport() {
        return WikiServlet.ACTION_IMPORT;
    }

    /**
     *
     */
    public String getActionLocklist() {
        return WikiServlet.ACTION_LOCKLIST;
    }

    /**
     *
     */
    public String getActionLogin() {
        return WikiServlet.ACTION_LOGIN;
    }

    /**
     *
     */
    public String getActionMember() {
        return WikiServlet.ACTION_MEMBER;
    }

    /**
     *
     */
    public String getActionMenuJump() {
        return WikiServlet.ACTION_MENU_JUMP;
    }

    /**
     *
     */
    public String getActionNotify() {
        return WikiServlet.ACTION_NOTIFY;
    }

    /**
     *
     */
    public String getActionOrphanedTopics() {
        return WikiServlet.ACTION_ORPHANED_TOPICS;
    }

    /**
     *
     */
    public String getActionPreview() {
        return WikiServlet.ACTION_PREVIEW;
    }

    /**
     *
     */
    public String getActionPrint() {
        return WikiServlet.ACTION_PRINT;
    }

    /**
     *
     */
    public String getActionRecentChanges() {
        return WikiServlet.ACTION_RECENT_CHANGES;
    }

    /**
     *
     */
    public String getActionRss() {
        return WikiServlet.ACTION_RSS;
    }

    /**
     *
     */
    public String getActionSave() {
        return WikiServlet.ACTION_SAVE;
    }

    /**
     *
     */
    public String getActionSaveTemplate() {
        return WikiServlet.ACTION_SAVE_TEMPLATE;
    }

    /**
     *
     */
    public String getActionSaveUser() {
        return WikiServlet.ACTION_SAVE_USER;
    }

    /**
     *
     */
    public String getActionSearch() {
        return WikiServlet.ACTION_SEARCH;
    }

    /**
     *
     */
    public String getActionSearchResults() {
        return WikiServlet.ACTION_SEARCH_RESULTS;
    }

    /**
     *
     */
    public String getActionTodoTopics() {
        return WikiServlet.ACTION_TODO_TOPICS;
    }

    /**
     *
     */
    public String getActionUnlock() {
        return WikiServlet.ACTION_UNLOCK;
    }
}
