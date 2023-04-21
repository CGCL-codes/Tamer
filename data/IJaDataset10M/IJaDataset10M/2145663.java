package es.caib.signatura.client.swing;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.*;

public class SignaturaClientProperties {

    private static final String CONFIGURATION_FILE = "signatura";

    private PropertyResourceBundle properties;

    public SignaturaClientProperties() throws Exception {
        try {
            properties = (PropertyResourceBundle) ResourceBundle.getBundle(CONFIGURATION_FILE);
        } catch (MissingResourceException e) {
            properties = (PropertyResourceBundle) ResourceBundle.getBundle(CONFIGURATION_FILE, new Locale("ca"));
        }
    }

    public String getProperty(String name) {
        String value;
        try {
            value = properties.getString(name);
        } catch (MissingResourceException me) {
            value = name;
        }
        return value;
    }
}
