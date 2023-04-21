package com.bastet.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.ImageIcon;

/**
 *  
 */
public class MrUtil {

    private static Logger logger = Logger.getLogger("com.bastet.util.MrUtil");

    private static Map envMap = null;

    static {
        Properties sysProperties = System.getProperties();
        String home = sysProperties.getProperty("java.home");
        String version = sysProperties.getProperty("java.version");
        if ((home == null) || (version == null)) System.out.println("Error accessing System properties!"); else System.out.println("Java Home: " + home + " Version: " + version);
    }

    /**
   * Sleep for the specified number of milliseconds. Wrapper of system
   * method that discards the exception
   *
   * @param millis
   */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }

    public static String getUserHomeUnix() {
        String userHome = System.getProperty("user.home");
        userHome = userHome.replaceAll("\\\\", "/");
        return userHome;
    }

    /**
   * @param fileName -
   *            The filename to search classpath for
   * @return The description file as File type.
   */
    public static File getResourceAsFile(String fileName) {
        String resolvedFileName = getResourcePath(fileName);
        return new File(resolvedFileName);
    }

    /**
   * @param fileName -
   *            The filename to search classpath for.
   * @return The canonical file name
   */
    public static String getResourcePath(String fileName) {
        URL url = ClassLoader.getSystemResource(fileName);
        if (url != null) {
            try {
                return URLDecoder.decode(url.getFile(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.warning("Unsupported encoding");
                return null;
            }
        } else {
            return fileName;
        }
    }

    public static ImageIcon createImageIcon(String iconFileName) {
        return createImageIcon(iconFileName, null);
    }

    public static ImageIcon createImageIcon(String iconFileName, String description) {
        String iconPath = "icons/" + iconFileName;
        String resolvedFileName = getResourcePath(iconPath);
        if (resolvedFileName != null) {
            return new ImageIcon(resolvedFileName, description);
        } else {
            logger.warning("Couldn't find file: " + iconFileName);
            return null;
        }
    }

    public static Object createInstance(String className) {
        Class c = null;
        try {
            c = Class.forName(className);
            return c.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
   * 
   * This method reads and combines properties from all occurances of
   * propFileName found on the classpath.
   * 
   * @param propFileName -
   *            The name of the property file to read
   * @return The combined set of properties found in all files named
   *         propFileName.
   */
    public static Properties loadProperties(String propFileName) {
        Properties props = new Properties();
        String propPath = "properties/" + propFileName;
        try {
            Enumeration e = ClassLoader.getSystemResources(propPath);
            if (!e.hasMoreElements()) {
                logger.warning("Can't find property file " + propPath);
                logger.warning("Please create one and place in your classpath");
            } else {
                while (e.hasMoreElements()) {
                    URL url = (URL) e.nextElement();
                    System.out.println("Loading properties from " + url);
                    props.load(url.openStream());
                }
            }
        } catch (IOException e1) {
            logger.warning("Can't find property file " + propPath);
            logger.warning("Please create one and place in your classpath");
        }
        if (envMap != null) replaceEnvVariables(props);
        if (logger.isLoggable(Level.FINE)) props.list(System.out);
        return props;
    }

    /**
   * Support UNIX shell-like environment variables in property files.
   * This is useful for filename paths and other things, e.g.:
   *
   *  program.tmpFile=$HOME/tmp/tmpFile1
   *
   */
    private static void replaceEnvVariables(Properties props) {
        Enumeration e = props.propertyNames();
        while (e.hasMoreElements()) {
            String propName = (String) e.nextElement();
            String propVal = props.getProperty(propName);
            if (propVal.indexOf("${") >= 0) {
                System.out.println("propVal = " + propVal);
                int start = propVal.indexOf("${");
                int stop = propVal.indexOf("}", start);
                String target = propVal.substring(start + 2, stop);
                System.out.println("target = " + target);
                propVal = propVal.replaceAll("\\$\\{" + target + "\\}", (String) envMap.get(target));
                props.setProperty(propName, propVal);
                System.out.println("Transformed propVal = " + propVal);
            }
            if (propVal.indexOf("${") >= 0) {
                System.out.println("MrUtil.replaceEnvVariables: couldn'y find env var " + propVal);
            }
        }
    }

    /**
   *  Simple routine to dump a stack trace.  There may be a standard java
   *  way of doing this without using an exception, but I can't find it
   *  at the moment...
   */
    public static void printStackTrace() {
        IOException e = new IOException("StackTrace");
        e.printStackTrace();
    }
}
