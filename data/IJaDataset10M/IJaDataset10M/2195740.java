package org.appspy.client.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.appspy.client.common.CollectorFactory;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class FileConfigLoader implements ConfigLoader {

    public Properties loadConfig(String location) throws IOException {
        Properties properties = new Properties();
        InputStream is = new FileInputStream(getFile(location));
        properties.load(is);
        is.close();
        return properties;
    }

    public URL getURL(String location) throws IOException {
        return getFile(location).toURL();
    }

    protected File getFile(String location) {
        String configDir = CollectorFactory.getAppspyDir() + "/client/conf";
        return new File(configDir, location);
    }
}
