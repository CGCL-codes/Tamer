package com.linux.sampad.components;

/**
 * some handy constants that can be used throughout the program
 */
public class Constants {

    /**
     * the name of the OS as given by the Java system property "os.name"
     */
    public static final String osname = System.getProperty("os.name");

    /**
     * true if the program is running on OS X
     */
    public static final boolean isOSX = osname.equalsIgnoreCase("Mac OS X");

    /**
     * true if the program is running on Linux
     */
    public static final boolean isLinux = osname.equalsIgnoreCase("Linux");

    /**
     * true if the program is running on Solaris
     */
    public static final boolean isSolaris = osname.equalsIgnoreCase("SunOS");

    /**
     * true if the program is running on Windows Vista
     */
    public static final boolean isVista = osname.equalsIgnoreCase("Windows Vista");

    /**
     * true if the program is running on Windows
     */
    public static final boolean isWindows = !(isOSX || isLinux || isSolaris);
}
