package org.brainypdm.modules.customgraph.classdef;

import java.util.Properties;
import org.brainypdm.exceptions.BaseException;
import org.brainypdm.modules.commons.configuration.BrainyConfiguration;

/***
 * 
 * generic graph properties
 * 
 * @author <a href="mailto:nico@brainypdm.org">Nico Bagari</a>
 *
 */
public abstract class GraphProps {

    /**
	 * generic graph properties
	 */
    protected Properties properties;

    public Properties getGenericProperties() {
        return properties;
    }

    public void setGenericProperties(Properties genericProperties) {
        this.properties = genericProperties;
    }

    public void setGraphProperty(String key, String value) {
        if (properties != null) {
            properties = new Properties();
        }
        properties.setProperty(key, value);
    }

    public String getGraphProperty(String key) throws BaseException {
        String ret = null;
        if (properties != null) {
            ret = properties.getProperty(key);
        }
        if (ret == null) {
            BrainyConfiguration configuration = BrainyConfiguration.getInstance();
            ret = configuration.getString(key);
        }
        return ret;
    }

    public boolean getBooleanGraphProperty(String key) throws BaseException {
        String tmp = getGraphProperty(key);
        if (tmp != null) {
            return Boolean.valueOf(tmp);
        }
        return false;
    }

    public long getLongGraphProperty(String key) throws BaseException {
        String tmp = getGraphProperty(key);
        if (tmp != null) {
            return Long.valueOf(tmp);
        }
        return (long) 0;
    }
}
