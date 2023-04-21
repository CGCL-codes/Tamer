package org.bungeni.extutils;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.SystemParameterReader;

/**
 * Allows getting and setting of editor properties from the settings db's
 * general_editor_properties table
 * @author Ashok Hariharan
 */
public class BungeniEditorProperties {

    private static HashMap<String, String> propertiesMap = new HashMap<String, String>();

    private static Logger log = Logger.getLogger(BungeniEditorProperties.class.getName());

    public static final String ODF_URI_PREFIX = "uri:";

    public static String SAX_PARSER_DRIVER = "org.apache.xerces.parsers.SAXParser";

    /** Creates a new instance of BungeniEditorProperties */
    public BungeniEditorProperties() {
    }

    private static int PROPERTY_NAME_COLUMN = 0;

    private static int PROPERTY_VALUE_COLUMN = 1;

    public static void setEditorProperty(String propertyName, String propertyValue) {
        SystemParameterReader.getInstance().setParameter(propertyName, propertyValue);
        try {
            SystemParameterReader.getInstance().save();
        } catch (IOException ex) {
            log.error("Error while saving", ex);
        }
    }

    public static void setPropertyInMap(String propName, String propValue) {
        propertiesMap.put(propName, propValue);
    }

    /**
     * Short-hand helper for getEditorProperty
     * @param propertyName
     * @return
     */
    public static String get(String propertyName) {
        return getEditorProperty(propertyName);
    }

    public static String getEditorProperty(String propertyName) {
        String propertyValue = "";
        if (propertiesMap.containsKey(propertyName)) {
            return propertiesMap.get(propertyName);
        } else {
            propertyValue = SystemParameterReader.getInstance().getParameter(propertyName);
            propertyValue = propertyValue == null ? "" : propertyValue;
            propertiesMap.put(propertyName, propertyValue);
        }
        return propertyValue;
    }
}
