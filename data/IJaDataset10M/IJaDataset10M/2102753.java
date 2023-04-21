package br.ita.comp.ces22.quiz.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    private static Properties properties = null;

    private static final String propertiesFileName = "/resources/email.properties";

    private PropertiesLoader() {
    }

    public static String getField(String key) {
        if (properties == null) {
            loadProperties();
        }
        return (String) properties.getProperty(key);
    }

    private static void loadProperties() {
        if (properties == null) {
            properties = new Properties();
            InputStream in = PropertiesLoader.class.getResourceAsStream(propertiesFileName);
            try {
                properties.load(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
