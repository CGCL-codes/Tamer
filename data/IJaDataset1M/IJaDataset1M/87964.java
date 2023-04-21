package org.exteca.search;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exteca.utils.PropertyManager;
import org.exteca.utils.Resources;

/**
 *  Class containing static Properties methods which read properties from a 
 *  properties file and store the properties in the system.
 *
 *  @author Neetu Jain
 */
public final class PropertyConfigurator {

    /** The Logger */
    Log log = LogFactory.getLog(PropertyConfigurator.class);

    /**
     *  Sets the properties from the specified file into the System
     *  throws FileNotFoundException, IOException
     */
    public void setProperties() throws FileNotFoundException, IOException {
        String propertiesFile = System.getProperty("exteca.search.properties");
        if (propertiesFile != null) {
            PropertyManager.setProperties(propertiesFile);
        } else log.warn("could not find a system property called exteca.search.properties");
    }

    /**
     *  Returns the index location
     *
     */
    public String getIndex() {
        String key = "lucene.index";
        String property = getProperty(key);
        if (property == null) {
            log.warn("the index location is null: org.exteca.search.lucene.index not found in properties file");
        } else {
            if (log.isDebugEnabled()) log.debug("index location: " + property);
        }
        return property;
    }

    /**
     *  Whether to create a new index
     *
     */
    public boolean getCreateIndex() {
        String key = "lucene.createIndex";
        String property = getProperty(key);
        boolean createIndex = true;
        if (property != null) createIndex = new Boolean(property).booleanValue();
        return createIndex;
    }

    /**
     *  Returns the specified property from the system. 
     *  The property key is prefixed by the package name of this Class.
     *
     *  @param key - the key to retrieve from the System.
     */
    public String getProperty(String key) {
        String prefix = Resources.findPackageName(PropertyConfigurator.class);
        return System.getProperty(prefix + key);
    }
}
