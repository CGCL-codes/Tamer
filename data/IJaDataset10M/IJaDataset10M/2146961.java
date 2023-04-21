package org.bcholmes.jmicro.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class PropertiesLoader {

    public static Properties loadProperties(Class<?> type, String fileName) {
        Properties properties = new Properties();
        InputStream input = type.getResourceAsStream(fileName);
        if (input != null) {
            try {
                properties.load(input);
            } catch (IOException e) {
            } finally {
                IOUtils.closeQuietly(input);
            }
        }
        return properties;
    }

    public static Properties loadProperties(Class<?> type, String prefix, Locale locale) {
        return loadProperties(type, getPropertiesFileName(prefix, locale));
    }

    private static String getPropertiesFileName(String prefix, Locale locale) {
        if (locale == Locale.ENGLISH || locale == null) {
            return prefix + ".properties";
        } else if (StringUtils.isBlank(locale.getCountry())) {
            return prefix + "_" + locale.getLanguage() + ".properties";
        } else {
            return prefix + "_" + locale.getLanguage() + "_" + locale.getCountry() + ".properties";
        }
    }
}
