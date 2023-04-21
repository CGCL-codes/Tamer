package org.riverock.dbrevision.offline;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Sergei Maslyukov
 *         Date: 14.12.2006
 *         Time: 17:24:53
 *         <p/>
 *         $Id$
 */
public class PropertiesProvider {

    private static boolean isServletEnv = false;

    private static String applicationPath = null;

    private static String configPath = null;

    private static Map<String, String> parameters = new HashMap<String, String>();

    public static String getConfigPath() {
        return configPath;
    }

    public static void setConfigPath(String configPath) {
        PropertiesProvider.configPath = configPath;
    }

    public static String getApplicationPath() {
        return applicationPath;
    }

    public static void setApplicationPath(String applicationPath) {
        PropertiesProvider.applicationPath = applicationPath;
    }

    public static boolean getIsServletEnv() {
        return isServletEnv;
    }

    public static void setIsServletEnv(boolean servletEnv) {
        isServletEnv = servletEnv;
    }

    public static Map<String, String> getParameters() {
        return parameters;
    }

    public static void setParameters(Map<String, String> parameters) {
        PropertiesProvider.parameters = parameters;
    }

    public static String getParameter(String key) {
        if (key == null) {
            return null;
        }
        return PropertiesProvider.parameters.get(key);
    }
}
