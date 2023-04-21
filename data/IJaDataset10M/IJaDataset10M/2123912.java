package eu.roelbouwman.webproject.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Util {

    public static Properties properties = new Properties();

    static {
        try {
            InputStream is = new FileInputStream("config/settings.properties");
            properties.load(is);
        } catch (IOException e) {
            properties = null;
            System.out.println("error: " + e.getMessage());
        }
    }

    public Util() {
        loadProperties();
    }

    private void loadProperties() {
    }
}
