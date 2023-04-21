package edu.princeton.wordnet.wnscope;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import bsys.database.DBType;

/**
 * Deployer
 * 
 * @author <a href="mailto:bbou@ac-toulouse.fr">Bernard Bou</a>
 */
public class Deployer {

    private static final Logger LOG = Logger.getLogger(Deployer.class);

    private static String thePropertyFile = System.getProperty("user.home") + File.separator + ".wnpersistence";

    /**
	 * Normalize (purge unneeded keys and set default if missing)
	 * 
	 * @param thatDataMap
	 *            input map
	 * @return purget map
	 */
    public static Map<Object, Object> normalize(final Map<Object, Object> thatDataMap) {
        final String[] theseKeys = new String[] { DeployerKeys.HIBERNATE_DRIVER_CLASS_KEY, DeployerKeys.HIBERNATE_DIALECT_KEY, DeployerKeys.HIBERNATE_DRIVER_CLASS_KEY, DeployerKeys.HIBERNATE_URL_KEY, DeployerKeys.HIBERNATE_CATALOG_KEY, DeployerKeys.HIBERNATE_SCHEMA_KEY, DeployerKeys.HIBERNATE_USERNAME_KEY, DeployerKeys.HIBERNATE_PASSWORD_KEY, DeployerKeys.JDBC_DRIVER_JAR_KEY };
        final Set<String> thisKeySet = new HashSet<String>(Arrays.asList(theseKeys));
        final Map<Object, Object> thisDataMap = new HashMap<Object, Object>();
        for (final Entry<Object, Object> thisEntry : thatDataMap.entrySet()) if (thisKeySet.contains(thisEntry.getKey()) && thisEntry.getValue() != null && !"".equals(thisEntry.getValue())) {
            thisDataMap.put(thisEntry.getKey(), thisEntry.getValue());
        }
        return thisDataMap;
    }

    /**
	 * Build path
	 * 
	 * @param theseStrings
	 *            string components
	 * @return path
	 */
    public static String makeDirPath(final String... theseStrings) {
        final StringBuffer thisBuffer = new StringBuffer();
        for (final String thisString : theseStrings) {
            thisBuffer.append(thisString);
            thisBuffer.append(File.separator);
        }
        return thisBuffer.toString();
    }

    /**
	 * Save settings
	 * 
	 * @param thisMap
	 *            settings
	 * @throws Exception
	 */
    public static void save(final Map<Object, Object> thisMap) {
        final Properties theseProps = new Properties();
        theseProps.putAll(thisMap);
        for (final Entry<Object, Object> thisEntry : theseProps.entrySet()) {
            final Object thisValue = thisEntry.getValue();
            if (thisValue == null) {
                continue;
            }
            if (thisValue instanceof DBType) {
                theseProps.setProperty((String) thisEntry.getKey(), ((DBType) thisValue).name());
            }
            if (thisValue instanceof Boolean) {
                theseProps.setProperty((String) thisEntry.getKey(), thisEntry.getValue().toString());
            }
        }
        try {
            theseProps.store(new FileWriter(Deployer.thePropertyFile), null);
        } catch (final IOException e) {
            Deployer.LOG.warn("saving settings", e);
        }
    }

    /**
	 * Load settings
	 * 
	 * @param thisPath
	 *            path to settings file
	 * @throws Exception
	 */
    public static Properties load(final String thisPath) {
        final Properties theseProps = new Properties();
        try {
            theseProps.load(new FileReader(thisPath));
            for (final Entry<Object, Object> thisEntry : theseProps.entrySet()) {
                final Object thisValue = thisEntry.getValue();
                if (thisValue == null) {
                    continue;
                }
                try {
                    if (!"true".equals(thisValue) && !"false".equals(thisValue)) throw new IllegalArgumentException();
                    theseProps.put(thisEntry.getKey(), Boolean.valueOf((String) thisValue));
                } catch (final Throwable t) {
                }
                try {
                    theseProps.put(thisEntry.getKey(), DBType.valueOf((String) thisValue));
                } catch (final Throwable t) {
                }
            }
        } catch (final IOException e) {
            Deployer.LOG.warn("loading settings", e);
        }
        return theseProps;
    }

    /**
	 * Refresh logging from config
	 */
    public static void reconfigureLog() {
        final URL thisUrl = Deployer.class.getResource("/log4j.xml");
        DOMConfigurator.configure(thisUrl);
    }
}
