package org.signserver.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Properties;
import javax.ejb.EJBException;
import org.apache.log4j.Logger;
import org.signserver.common.GlobalConfiguration;

/**
 * This is a  class containing global static configurations.
 * This is an old class that shouldn't be used directly but only
 * through the global configuration session bean.
 * 
 * FIXME: Move closer to GlobalConfigurationSessionBean and maybe shouldn't be public.
 *
 * @version $Id: GlobalConfigurationFileParser.java 1875 2011-09-29 14:50:48Z netmackan $
 */
public class GlobalConfigurationFileParser implements Serializable {

    private static final long serialVersionUID = -5866386252932621648L;

    /** Log4j instance for actual implementation class */
    public transient Logger log;

    public static final String SIGNERPROPERTY_BASE = "signer";

    public Properties properties = new Properties();

    private static GlobalConfigurationFileParser instance = null;

    private GlobalConfigurationFileParser(Properties testproperties) {
        log = Logger.getLogger(this.getClass());
        if (testproperties != null) {
            properties = testproperties;
        }
    }

    /**
     * Method returning the global configuration instance.
     * 
     * This is the one used in production
     * 
     * @return the GlobalConfigurationFileParser instance.
     */
    public static GlobalConfigurationFileParser getInstance() {
        if (instance == null) {
            instance = new GlobalConfigurationFileParser(null);
        }
        return instance;
    }

    /**
     * Method returning the global configuration instance used in testscripts
     * SHOULD ONLY BE USED FOR TESTING
     * 
     * @param testproperties contains the test properties.
     * 
     * @return the GlobalConfigurationFileParser instance.
     */
    public static GlobalConfigurationFileParser getInstance(Properties testproperties) {
        instance = new GlobalConfigurationFileParser(testproperties);
        return instance;
    }

    /**
     * Method that reloads the configuration from the property file.
     *
     */
    public void reloadConfiguration() {
        String propsfile = "/signserver_server.properties";
        InputStream is = this.getClass().getResourceAsStream(propsfile);
        try {
            WorkerFactory.getInstance().flush();
            if (is != null) {
                properties.load(is);
                is.close();
            }
        } catch (IOException e) {
            throw new EJBException(e);
        }
    }

    /**
     * Method returning the properties in the static
     * global configuration.
     * 
     * This method supports the old version of properties
     * and converts them to global scoped variables.
     */
    public Properties getStaticGlobalConfiguration() {
        Properties retval = new Properties();
        Iterator<?> iter = properties.keySet().iterator();
        while (iter.hasNext()) {
            String orgKey = (String) iter.next();
            String key = orgKey;
            if (key.startsWith("signer")) {
                key = key.replaceFirst("signer", "WORKER");
            }
            String tmpkey = key.toUpperCase();
            if (tmpkey.startsWith(GlobalConfiguration.SCOPE_GLOBAL) || tmpkey.startsWith(GlobalConfiguration.SCOPE_NODE)) {
                retval.setProperty(tmpkey, properties.getProperty(key));
            } else {
                retval.setProperty(GlobalConfiguration.SCOPE_GLOBAL + tmpkey, properties.getProperty(orgKey));
            }
        }
        return retval;
    }
}
