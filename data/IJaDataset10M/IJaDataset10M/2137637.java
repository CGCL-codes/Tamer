package com.aragost.araspect;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author aragost
 */
public class Properties {

    protected static ResourceBundle bundle = ResourceBundle.getBundle("araspect");

    public static String getVersion() {
        return bundle.getString("project.version");
    }

    public static String getLongName() {
        return bundle.getString("project.longname");
    }

    public static String getBuildId() {
        try {
            return bundle.getString("build.id");
        } catch (MissingResourceException e) {
            return null;
        }
    }

    public static String getBuildTimestamp() {
        try {
            return bundle.getString("build.timestamp");
        } catch (MissingResourceException e) {
            return null;
        }
    }

    public static String getFullVersion() {
        String buildTimestamp = getBuildTimestamp();
        String version = getVersion();
        if (buildTimestamp == null) {
            return version + " (dev)";
        } else {
            return version + " (build: " + buildTimestamp + ")";
        }
    }

    public static boolean isDemo() {
        try {
            return bundle.getString("demo").toLowerCase().equals("true");
        } catch (MissingResourceException e) {
            return false;
        }
    }
}
