package com.mjs_svc.possibility.util;

/**
 *
 * @author Matthew Scott
 * @version $Id: Version.java 34 2010-04-16 22:44:14Z matthew.joseph.scott $
 */
public class Version {

    private static final String version = "Dev Snapshot";

    private static final int build = Integer.parseInt("$Rev: 34 $".replaceAll("[\\$\\s]", "").replace("Rev:", ""));

    public static int getBuild() {
        return build;
    }

    public static String getVersion() {
        return version;
    }
}
