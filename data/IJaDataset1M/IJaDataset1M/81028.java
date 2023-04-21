package net.sourceforge.sandirc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * <p>
 *  Utility class to load some properties
 *  It will be usefull to read and save some configurations
 * </p>
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class PropertiesConfigurationLoader {

    private PropertiesConfigurationLoader() {
    }

    public static void save(File propertiesFile, Properties props) {
        try {
            OutputStream bos = new FileOutputStream(propertiesFile);
            props.store(bos, null);
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Properties loadURL(String propertiesFile) {
        Properties properties = new Properties();
        try {
            InputStream is = PropertiesConfigurationLoader.class.getResourceAsStream(propertiesFile);
            properties.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static Properties load(File propertiesFile) {
        Properties properties = new Properties();
        try {
            InputStream is = new FileInputStream(propertiesFile);
            properties.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
