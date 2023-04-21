package com.Freshyboy.util;

/**
 *
 * @author Rilsikane
 */
public class PropertyUtil {

    public String readProperty(String name, String key) {
        String result = "";
        String filename = "config/" + name + ".properties";
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config");
        result = bundle.getString(key);
        return result;
    }
}
