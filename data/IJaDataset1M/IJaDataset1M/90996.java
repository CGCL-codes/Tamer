package net.techwatch.commons.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;

public class FileUtils {

    public static Properties loadFromClasspath(String filename) {
        InputStream inStream = Thread.currentThread().getClass().getClassLoader().getResourceAsStream(filename);
        Properties properties = new Properties();
        try {
            if (inStream != null) {
                properties.load(inStream);
                return properties;
            }
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            IOUtils.closeQuietly(inStream);
        }
    }
}
