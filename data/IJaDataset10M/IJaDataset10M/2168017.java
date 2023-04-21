package de.mpiwg.vspace.generation.control.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import de.mpiwg.vspace.extension.ExceptionHandlingService;
import de.mpiwg.vspace.filehandler.services.FileHandler;
import de.mpiwg.vspace.generation.control.Activator;

public class PropertyHandler {

    private static PropertyHandler instance = null;

    private Properties properties;

    private PropertyHandler() {
        properties = new Properties();
        File file = FileHandler.getAbsoluteFileFromProject(Activator.PLUGIN_ID, "generation.properties");
        if (file == null) return;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            ExceptionHandlingService.INSTANCE.handleException(e);
            return;
        }
        try {
            properties.load(fis);
        } catch (IOException e) {
            ExceptionHandlingService.INSTANCE.handleException(e);
        }
    }

    public static PropertyHandler getInstance() {
        if (instance == null) instance = new PropertyHandler();
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}
