package org.apache.hadoop.util;

import org.apache.hadoop.HadoopVersionAnnotation;

/**
 * This class finds the package info for Hadoop and the HadoopVersionAnnotation
 * information.
 */
public class VersionInfo {

    private static Package myPackage;

    private static HadoopVersionAnnotation version;

    static {
        myPackage = HadoopVersionAnnotation.class.getPackage();
        version = myPackage.getAnnotation(HadoopVersionAnnotation.class);
    }

    /**
   * Get the meta-data for the Hadoop package.
   * @return
   */
    static Package getPackage() {
        return myPackage;
    }

    /**
   * Get the Hadoop version.
   * @return the Hadoop version string, eg. "0.6.3-dev"
   */
    public static String getVersion() {
        return version != null ? version.version() : "Unknown";
    }

    /**
   * Get the subversion revision number for the root directory
   * @return the revision number, eg. "451451"
   */
    public static String getRevision() {
        return version != null ? version.revision() : "Unknown";
    }

    /**
   * The date that Hadoop was compiled.
   * @return the compilation date in unix date format
   */
    public static String getDate() {
        return version != null ? version.date() : "Unknown";
    }

    /**
   * The user that compiled Hadoop.
   * @return the username of the user
   */
    public static String getUser() {
        return version != null ? version.user() : "Unknown";
    }

    /**
   * Get the subversion URL for the root Hadoop directory.
   */
    public static String getUrl() {
        return version != null ? version.url() : "Unknown";
    }

    /**
   * Returns the buildVersion which includes version, 
   * revision, user and date. 
   */
    public static String getBuildVersion() {
        return VersionInfo.getVersion() + " from " + VersionInfo.getRevision() + " by " + VersionInfo.getUser() + " on " + VersionInfo.getDate();
    }

    public static void main(String[] args) {
        System.out.println("Hadoop " + getVersion());
        System.out.println("Subversion " + getUrl() + " -r " + getRevision());
        System.out.println("Compiled by " + getUser() + " on " + getDate());
    }
}
