package es.alvsanand.webpage.common;

import java.util.Properties;
import javax.faces.context.FacesContext;

public class AlvsanandProperties {

    /**
	 * The Logger for this class.
	 */
    private static final transient Logger logger = new Logger(AlvsanandProperties.class);

    private static Properties properties = null;

    private static final String WEB_INF_DIR = "/WEB-INF";

    public static final String CONFIG_FILE_NAME = "config.xml";

    private static final String DEFAULT_CONFIG_FILE = WEB_INF_DIR + "/" + CONFIG_FILE_NAME;

    public static String getProperty(String key) {
        if (properties == null && FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null) {
            try {
                properties = new Properties();
                properties.loadFromXML(FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(DEFAULT_CONFIG_FILE));
            } catch (Exception e) {
                logger.error("Error loading config properties", e);
            }
        }
        if (properties == null && key != null) return null;
        return properties.getProperty(key);
    }

    public static void setProperties(Properties properties) {
        AlvsanandProperties.properties = properties;
    }
}
