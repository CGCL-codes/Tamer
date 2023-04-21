package org.architecture.common.util;

import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 프로퍼티 유틸
 * 
 * @author leesangboo
 * 
 */
public class PropertiesUtils {

    private static final Log log = LogFactory.getLog(PropertiesUtils.class);

    private Properties properties = new Properties();

    /**
     * 
     * @param filePath
     */
    public PropertiesUtils(String filePath) {
        initiallize(filePath);
    }

    /**
     * 
     * @param filePath
     */
    public void initiallize(String filePath) {
        InputStream in = null;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream("/" + filePath);
            if (in != null) {
                properties.load(in);
            } else {
                throw new Exception("Properties is null.");
            }
        } catch (Exception e) {
            log.error("Ensure the [" + filePath + "] peroperties file is readable and in your classpath.", e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
    }

    /**
	 * 
	 * @param key
	 * @return
	 */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
	 * @return Returns the properties.
	 */
    public Properties getProperties() {
        return properties;
    }

    /**
	 * 
	 */
    public String toString() {
        return properties.toString();
    }
}
