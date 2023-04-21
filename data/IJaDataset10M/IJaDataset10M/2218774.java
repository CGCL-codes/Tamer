package de.mpiwg.vspace.modules.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import de.mpiwg.vspace.extension.ExceptionHandlingService;
import de.mpiwg.vspace.modules.Activator;
import de.mpiwg.vspace.modules.Constants;

public class PropertyHandler {

    private static PropertyHandler instance = null;

    private Properties properties;

    private PropertyHandler() {
        properties = new Properties();
        URL propertiesUrl = FileLocator.find(Activator.getDefault().getBundle(), new Path(Constants.PROPERTIES_FILE), null);
        URL fileURL = null;
        try {
            fileURL = FileLocator.toFileURL(propertiesUrl);
        } catch (IOException e) {
            ExceptionHandlingService.INSTANCE.handleException(e);
        }
        File file = new File(fileURL.getPath());
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
        String prop = properties.getProperty(key);
        if (prop == null) prop = "";
        return prop;
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}
