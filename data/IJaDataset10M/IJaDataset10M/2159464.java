package com.powerhua.core.utils;

/**
 *
 * @author Brian.Xie 
 */
public class WebPathUtils {

    /**
     * 
     * @return
     */
    public static String getWebInfPath() {
        String webContentPath = getWebContentPath();
        return webContentPath + "/WEB-INF";
    }

    /**
     * 
     * @return
     */
    public static String getWebClassesPath() {
        String webContentPath = getWebContentPath();
        return webContentPath + "/WEB-INF/classes";
    }

    /**
     * 
     * @return
     */
    public static String getWebLibPath() {
        String webContentPath = getWebContentPath();
        return webContentPath + "/WEB-INF/lib";
    }

    /**
     * 
     * @return
     */
    public static String getWebContentPath() {
        String fullPath = PathUtils.getClassBasePath();
        String path = "";
        if (fullPath.indexOf(".jar!") > 0) {
            int i = fullPath.indexOf("/WEB-INF/lib/");
            path = fullPath.substring(0, i + 1);
        } else {
            int i = fullPath.indexOf("/WEB-INF/classes/");
            path = fullPath.substring(0, i + 1);
        }
        return path;
    }
}
