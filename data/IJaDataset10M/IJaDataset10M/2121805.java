package util;

import java.util.Properties;
import java.util.StringTokenizer;
import java.io.*;
import java.awt.*;

/**
 * @author Jesper Nordenberg
 * @version $Revision: 1.1 $ $Date: 2002/05/08 23:37:03 $
 */
public class DataPropertiesRoot extends DataProperties {

    private Properties properties = new Properties();

    public DataPropertiesRoot() {
    }

    public void read(InputStream is) throws IOException {
        properties.load(is);
    }

    public void write(OutputStream os, String header) throws IOException {
        properties.store(os, header);
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public void set(String key, String defaultValue) {
        properties.setProperty(key, defaultValue);
    }

    public boolean contains(String key) {
        return properties.containsKey(key);
    }
}
