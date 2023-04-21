package grobid.utilities;

import grobid.exceptions.GROBIDServiceException;
import grobid.exceptions.GrobidException;
import grobid.exceptions.GrobidPropertyException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class GrobidProperties {

    private static Log logger = LogFactory.getLog(GrobidProperties.class);

    public static final String PROP_RESOURCE_PATH = "grobid.resource_path";

    public static final String PROP_TMP_PATH = "grobid.temp_path";

    public static final String PROP_BIN_PATH = "grobid.bin_path";

    /**
     * name of property of path to where to find the configuration file for log4j
     */
    public static final String PROP_LOG4J_PATH = "grobid.log4props_path";

    /**
	 * name of property which determines, where to find sample files if grobid runs in test mode.
	 */
    public static final String PROP_SAMPLE_PATH = "grobid.sample_path";

    public static final String PROP_USE_LANG_ID = "grobid.use_language_id";

    public static final String PROP_CROSSREF_ID = "grobid.crossref_id";

    public static final String PROP_CROSSREF_PW = "grobid.crossref_pw";

    public static final String PROP_CROSSREF_HOST = "grobid.crossref_host";

    public static final String PROP_CROSSREF_PORT = "grobid.crossref_port";

    public static final String PROP_MYSQL_HOST = "grobid.mysql_host";

    public static final String PROP_MYSQL_PORT = "grobid.mysql_port";

    public static final String PROP_MYSQL_USERNAME = "grobid.mysql_username";

    public static final String PROP_MYSQL_PW = "grobid.mysql_passwd";

    public static final String PROP_MYSQL_DB_NAME = "grobid.mysql_db_name";

    public static final String PROP_PROXY_HOST = "grobid.proxy_host";

    public static final String PROP_PROXY_PORT = "grobid.proxy_port";

    public static final String PROP_NB_THREADS = "grobid.nb_threads";

    /**
	 * name of property which determines, if grobid runs in test mode.
	 */
    public static final String PROP_TEST_MODE = "grobid.testMode";

    /**
     * determines if properties like the firstnames, lastnames country codes and dictionaries are supposed to be read from $GROBID_HOME path or not (possible values (true|false) dafault is false)
     */
    public static final String PROP_RESOURCE_INHOME = "grobid.resources.inHome";

    public static final String ENV_GROBID_HOME = "GROBID_HOME";

    public static final String FILE_GROBID_PROPERTIES = "grobid.properties";

    public static final String FILE_GROBID_PROPERTIES_PRIVATE = "grobid_private.properties";

    public static final String FILE_ENDING_TEI_HEADER = ".header.tei.xml";

    public static final String FILE_ENDING_TEI_FULLTEXT = ".fulltext.tei.xml";

    /**
     * Detereming the path, where the grobid jobs can be stored.
     */
    public static final String PROP_SERVICE_JOBFOLDER_PATH = "grobid.service.job_path";

    public static final String[] PATH_PROPERTIES = { PROP_RESOURCE_PATH, PROP_TMP_PATH, PROP_BIN_PATH, PROP_LOG4J_PATH, PROP_SERVICE_JOBFOLDER_PATH, PROP_SAMPLE_PATH };

    public static final String[] NOT_NULL_PROPERTIES = { PROP_RESOURCE_PATH, PROP_BIN_PATH, PROP_LOG4J_PATH, PROP_SERVICE_JOBFOLDER_PATH, PROP_SAMPLE_PATH };

    public static final String[] PATHES_TO_CREATE = { PROP_TMP_PATH, PROP_SERVICE_JOBFOLDER_PATH };

    /**
     * Checks if the given properties contains non-empty and non-null values for the properties of list {@link GrobidProperties#NOT_NULL_PROPERTIES}.
     * @param props Properties to be checked. 
     */
    public static void checkProperties(Properties props) {
        if (props == null) throw new GrobidPropertyException("The given property object is null, therefore it does not contain any property which is necessary for running grobid.");
        for (String propname : NOT_NULL_PROPERTIES) {
            String prop = props.getProperty(propname);
            if ((prop == null) || (!prop.equals(""))) throw new GrobidPropertyException("The property '" + propname + "' is null or empty. This property is necessary for running grobid.");
        }
    }

    /**
     * Loads property file from home variable.
     */
    public static synchronized void init() {
        String homefolderName = System.getenv(GrobidProperties.ENV_GROBID_HOME);
        if ((homefolderName == null) || (homefolderName.equals(""))) {
            homefolderName = System.getProperty(GrobidProperties.ENV_GROBID_HOME);
            if ((homefolderName == null) || (homefolderName.equals(""))) {
                throw new GrobidException("Cannot init GrobidProperties, because environmant variable '" + GrobidProperties.ENV_GROBID_HOME + "' is not set.");
            }
        }
        init(homefolderName);
    }

    /**
     * Loads property file from home variable.
     */
    public static synchronized void init(String homefolderName) {
        System.setProperty(GrobidProperties.ENV_GROBID_HOME, homefolderName);
        File homeFolder = new File(homefolderName);
        if (!homeFolder.exists()) throw new GrobidException("Cannot init GrobidProperties, because the folder '" + homeFolder.getAbsolutePath() + "' to where the environmant variable '" + GrobidProperties.ENV_GROBID_HOME + "' points to does not exists.");
        File propFile = null;
        {
            propFile = new File(homeFolder.getAbsoluteFile() + "/" + FILE_GROBID_PROPERTIES_PRIVATE);
            if (!propFile.exists()) {
                propFile = new File(homeFolder.getAbsoluteFile() + "/" + FILE_GROBID_PROPERTIES);
                if (!propFile.exists()) throw new GrobidException("Cannot init GrobidProperties, because the property file '" + propFile.getAbsolutePath() + "' does not exists.");
            }
            logger.info("Using grobid properties from file '" + propFile.getAbsolutePath() + "'.");
        }
        copyProperties2SystemProperties(propFile);
    }

    /**
     * Copies all properties given in properties object to SystemProperties. When a property contains a path, this
     * method tries to make this path absolute, this is done by making the path absolute, relative to the current path 
	 * (path from where Grobid is started). If property-value contains {@link GrobidProperties#ENV_GROBID_HOME},
	 * this String will be replaced by the path given by {@link GrobidProperties#ENV_GROBID_HOME} and therefore is absolute
	 * and a subpath of {@link GrobidProperties#ENV_GROBID_HOME}.
	 * @param propertiesFile file containing properties, which are supposed to be copied
     */
    public static synchronized void copyProperties2SystemProperties(File propertiesFile) {
        Properties props = new Properties();
        try {
            if (!propertiesFile.exists()) throw new RuntimeException("Cannot load Grobid properties, because file '" + propertiesFile.getAbsolutePath() + "' does not exists. Please set environment variable '" + GrobidProperties.ENV_GROBID_HOME + "' and put the property file '" + propertiesFile.getAbsolutePath() + "' in it.");
            props.load(new FileInputStream(propertiesFile));
        } catch (Exception e) {
            throw new GrobidException("Cannot load properties for Grobid from file '" + propertiesFile.getAbsolutePath() + "'", e);
        }
        copyProperties2SystemProperties(props);
    }

    /**
     * Copies all properties given in properties object to SystemProperties. When a property contains a path, this
     * method tries to make this path absolute, this is done by making the path absolute, relative to the current path 
	 * (path from where Grobid is started). If property-value contains {@link GrobidProperties#ENV_GROBID_HOME},
	 * this String will be replaced by the path given by {@link GrobidProperties#ENV_GROBID_HOME} and therefore is absolute
	 * and a subpath of {@link GrobidProperties#ENV_GROBID_HOME}.
	 * @param properties properties, which are supposed to be copied
     */
    public static synchronized void copyProperties2SystemProperties(Properties properties) {
        {
            try {
                Enumeration<String> propNames = (Enumeration<String>) properties.propertyNames();
                while (propNames.hasMoreElements()) {
                    String propName = propNames.nextElement();
                    String propValue = properties.getProperty(propName);
                    System.setProperty(propName, propValue);
                    {
                        for (String pathProp : PATH_PROPERTIES) {
                            if (pathProp.equalsIgnoreCase(propName)) {
                                boolean createPath = false;
                                for (String path : PATHES_TO_CREATE) {
                                    if (path.equals(pathProp)) {
                                        createPath = true;
                                        break;
                                    }
                                }
                                makePropertyPathesAbsolute(createPath, propName);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new GROBIDServiceException("An exception occured while running Grobid.", e);
            }
        }
    }

    /**
	 * Searches for propertyName in SystemProperties and makes the path absolute, relative to the current path 
	 * (path from where Grobid is started), in case it is not. If property-value contains 
	 * {@link GrobidProperties#ENV_GROBID_HOME},
	 * this String will be replaced by the path given by {@link GrobidProperties#ENV_GROBID_HOME}.
	 * @param createPath if true, the directory will be created when it does not exists
	 * @param propertyName name of Systemproperty
	 */
    protected static synchronized void makePropertyPathesAbsolute(Boolean createPath, String propertyName) {
        File grobidHomePath = null;
        {
            String grobidHomePathName = null;
            grobidHomePathName = System.getProperty(GrobidProperties.ENV_GROBID_HOME);
            if (grobidHomePathName == null) {
                grobidHomePathName = System.getenv(GrobidProperties.ENV_GROBID_HOME);
                if (grobidHomePathName == null) {
                    throw new GrobidException("Cannot start GrobidService, because the environment varibale '" + GrobidProperties.ENV_GROBID_HOME + "' for home folder is not set.");
                }
            } else {
                grobidHomePath = new File(grobidHomePathName);
                if (!grobidHomePath.exists()) {
                    throw new GROBIDServiceException("Cannot start GrobidService, because the folder of " + "environment varibale '" + GrobidProperties.ENV_GROBID_HOME + "' does not exists: '" + grobidHomePath + "'.");
                }
            }
        }
        String propValue = System.getProperty(propertyName);
        if ((propValue == null) || (propValue.equals(""))) throw new GrobidException("Systemproperties does not contain property for property name'" + propertyName + "'.");
        propValue = propValue.replace("$" + GrobidProperties.ENV_GROBID_HOME, grobidHomePath.getAbsolutePath());
        File propTargetPath = new File(propValue);
        if (!propTargetPath.exists()) {
            if (createPath) {
                if (!propTargetPath.mkdirs()) {
                    throw new GROBIDServiceException("Cannot create path for property '" + propertyName + "' ('" + propTargetPath.getAbsolutePath() + "').");
                }
            } else {
                throw new GROBIDServiceException("Path for property '" + propertyName + "' ('" + propTargetPath.getAbsolutePath() + "') does not exists.");
            }
        }
        System.setProperty(propertyName, propTargetPath.getAbsolutePath());
    }
}
