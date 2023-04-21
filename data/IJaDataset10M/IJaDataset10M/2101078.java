package org.wings.resource;

import java.io.IOException;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.plaf.ResourceDefaults;
import org.wings.util.PropertyUtils;

/**
 * For accessing static resources
 *
 * @author <a href="mailto:ole@freiheit.com">Ole Langbehn</a>
 */
public class ResourceManager {

    private static final transient Log log = LogFactory.getLog(ResourceManager.class);

    private static final String PROPERTIES_FILENAME = "org/wings/resource/resource.properties";

    public static Object getObject(String key, Class clazz) {
        return RESOURCES.get(key, clazz);
    }

    private static Properties properties;

    static {
        try {
            properties = PropertyUtils.loadProperties(PROPERTIES_FILENAME);
        } catch (IOException e) {
            log.fatal("Cannot open resource properties file at location " + PROPERTIES_FILENAME);
            properties = null;
            e.printStackTrace();
        }
    }

    public static final ResourceDefaults RESOURCES = new ResourceDefaults(null, properties);
}
