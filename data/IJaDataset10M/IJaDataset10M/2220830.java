package net.frontlinesms.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import net.frontlinesms.Utils;
import org.apache.log4j.Logger;

/**
 * Base properties wrapper class.
 * @author Alex
 */
class BasePropertySet {

    /** Logging object for this instance. */
    public static final Logger LOG = Utils.getLogger(BasePropertySet.class);

    /** Map from property key to value */
    private Map<String, String> properties;

    /**
	 * @param propertyKey The key for the property to get
	 * @return value from {@link #properties}
	 */
    String getProperty(String propertyKey) {
        return this.properties.get(propertyKey);
    }

    Map<String, String> getProperties() {
        return properties;
    }

    /**
	 * Set the properties.  This should be done exactly once.
	 * @param properties value for {@link #properties}
	 */
    protected void setProperties(Map<String, String> properties) {
        assert (this.properties == null) : "Properties already set.  Cannot be changed.";
        assert (properties != null) : "Cannot set properties to null.";
        this.properties = properties;
    }

    /**
	 * Load properties from an {@link InputStream}.
	 * @param inputStream The input stream to load the properties from.
	 * @return The loaded properties
	 * @throws IOException If there was a problem loading the properties from the supplied {@link InputStream}.
	 */
    static void load(Map<String, String> map, InputStream inputStream) throws IOException {
        if (inputStream == null) throw new NullPointerException("The supplied input stream was null.");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0 || line.charAt(0) == '#') {
                } else {
                    int splitChar = line.indexOf('=');
                    if (splitChar <= 0) {
                        LOG.warn("Bad line in properties file: '" + line + "'");
                    } else {
                        String key = line.substring(0, splitChar);
                        if (map.containsKey(key)) {
                            LOG.warn("Duplicate key in properties file: ''");
                        } else {
                            String value = line.substring(splitChar + 1);
                            map.put(key, value);
                        }
                    }
                }
            }
            LOG.trace("EXIT");
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException ex) {
                LOG.warn("Exception thrown while closing stream 'fis'.", ex);
            }
        }
    }

    static HashMap<String, String> load(InputStream inputStream) throws IOException {
        HashMap<String, String> map = new HashMap<String, String>();
        load(map, inputStream);
        return map;
    }
}
