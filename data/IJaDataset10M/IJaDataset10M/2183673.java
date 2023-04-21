package com.enterprisedt.net.ftp;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *  Aggregates the version information
 *
 *  @author      Bruce Blackshaw
 *  @version     $Revision: 1.3 $
 */
public class VersionDetails {

    /**
     * Major version (substituted by ant)
     */
    private static final String majorVersion = "2";

    /**
     * Middle version (substituted by ant)
     */
    private static final String middleVersion = "0";

    /**
     * Middle version (substituted by ant)
     */
    private static final String minorVersion = "3";

    /**
     * Full version
     */
    private static int[] version;

    /**
     * Full version string
     */
    private static String versionString;

    /**
     * Timestamp of build
     */
    private static final String buildTimestamp = "6-Jun-2008 14:04:32 EST";

    /**
     * Work out the version array
     */
    static {
        try {
            version = new int[3];
            version[0] = Integer.parseInt(majorVersion);
            version[1] = Integer.parseInt(middleVersion);
            version[2] = Integer.parseInt(minorVersion);
            versionString = version[0] + "." + version[1] + "." + version[2];
        } catch (NumberFormatException ex) {
            System.err.println("Failed to calculate version: " + ex.getMessage());
            versionString = "Unknown";
        }
    }

    /**
     * Get the product version
     * 
     * @return int array of {major,middle,minor} version numbers 
     */
    public static final int[] getVersion() {
        return version;
    }

    /**
     * Get the product version string
     * 
     * @return version as string 
     */
    public static final String getVersionString() {
        return versionString;
    }

    /**
     * Get the build timestamp
     * 
     * @return d-MMM-yyyy HH:mm:ss z build timestamp 
     */
    public static final String getBuildTimestamp() {
        return buildTimestamp;
    }

    /**
     * Report on useful things for debugging purposes
     * 
     * @param obj     instance to report on
     * @return string
     */
    public static final String report(Object obj) {
        StringWriter result = new StringWriter();
        PrintWriter report = new PrintWriter(result, true);
        report.print("Class: ");
        report.println(obj.getClass().getName());
        report.print("Version: ");
        report.println(versionString);
        report.print("Build timestamp: ");
        report.println(buildTimestamp);
        try {
            report.print("Java version: ");
            report.println(System.getProperty("java.version"));
            report.print("CLASSPATH: ");
            report.println(System.getProperty("java.class.path"));
            report.print("OS name: ");
            report.println(System.getProperty("os.name"));
            report.print("OS arch: ");
            report.println(System.getProperty("os.arch"));
            report.print("OS version: ");
            report.println(System.getProperty("os.version"));
        } catch (SecurityException ex) {
            report.println("Could not obtain system properties");
        }
        return result.toString();
    }
}
