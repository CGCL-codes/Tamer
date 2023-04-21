package gridrm.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Utility class for loading application properties/configuration. Contains application
 * specific default directory paths, which may be overridden by setting appropriate
 * system properties: config_path, log_path.<p>
 * 
 * Example. Default path may be overridden by setting system property config_path that points to 
 * configuration directory:<br>
 * <tt>java ... -Dconfig_path=./my_config ...</tt>
 */
public class Config {

    /**
	 * System property key referencing to application configuration path.
	 */
    public static final String PROP_SYS_CONFIG_PATH = "config_path";

    /**
	 * System property key referencing to log path.
	 */
    public static final String PROP_SYS_LOG_PATH = "log_path";

    /**
	 * Default application configuration path.
	 */
    public static final String DEFAULT_CONFIG_PATH = "./config";

    /**
	 * Default path for logs.
	 */
    public static final String DEFAULT_LOG_PATH = "./log";

    /**
	 * Returns application configuration path.
	 * @return Path string.
	 */
    public static String getConfigPath() {
        return System.getProperty(PROP_SYS_CONFIG_PATH, DEFAULT_CONFIG_PATH);
    }

    /**
	 * Returns application log path.
	 * @return Path string.
	 */
    public static String getLogPath() {
        return System.getProperty(PROP_SYS_LOG_PATH, DEFAULT_LOG_PATH);
    }

    /**
	 * Loads properties file from configuration directory.
	 * @param fileName Filename to be loaded.
	 * @return Properties class.
	 * @throws IOException 
	 */
    public static Properties getProperties(String fileName) throws IOException {
        String filePath = getConfigPath() + "/" + fileName;
        FileInputStream fInput = new FileInputStream(filePath);
        Properties prop = new Properties();
        prop.load(fInput);
        fInput.close();
        return prop;
    }

    /**
	 * Converts properties to arguments string, e.g. "PROP1=VAL1 PROP2=VAL2"
	 * @param prop
	 * @return
	 */
    public static String propertiesToArgs(Properties prop) {
        Enumeration iter = prop.propertyNames();
        StringBuffer sb = new StringBuffer();
        while (iter.hasMoreElements()) {
            String key = iter.nextElement().toString();
            sb.append(key);
            sb.append("=");
            sb.append(prop.getProperty(key));
            sb.append(" ");
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
