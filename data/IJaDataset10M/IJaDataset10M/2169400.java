package de.plugmail;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
import de.plugmail.defaults.*;
import de.plugmail.plugins.*;
import de.plugmail.data.*;
import java.util.Properties;
import java.lang.reflect.*;

public class PlugMail {

    public PlugMail() {
        String pathSep = System.getProperty("file.separator");
        DefaultPropertyHandler propertyHandler;
        Properties configuration;
        int numberOfAccounts;
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("de" + pathSep + "plugmail" + pathSep + "log4j.properties"));
        Logger log = Logger.getLogger(this.getClass());
        propertyHandler = new DefaultPropertyHandler("de" + pathSep + "plugmail" + pathSep + "plugMail.properties");
        configuration = propertyHandler.getProperties();
        PluginLoader pluginLoader = new PluginLoader(configuration);
        try {
            pluginLoader.loadPlugin("loader");
        } catch (Exception e) {
            log.error("Could not load startUp plugin!", e);
        }
    }

    public static void main(String[] args) {
        PlugMail plugMail = new PlugMail();
    }
}
