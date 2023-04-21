package com.monkygames.sc2bob.io;

import java.io.*;

/**
 * Utility methods for handling resources such as files.
 * Note since java web start is being used, files must be opened with a different method then files on the local file system.
 * @version 1.0
 */
public class ResourceUtils {

    /**
     * Returns the path to the user's home dir with the .massiveline directory.
     * Note, if the dir doesn't exist, then one is created.
     * This directory is used for storing config files and game data files.
     * @return the path to the home directory for this game.
     **/
    public static String getHomeDir() {
        String home = System.getProperty("user.home") + File.separator + ".sc2bob";
        File dir = new File(home);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return home;
    }

    /**
     * Gets the path starting with the home dir and concats that to the passed in argument.
     * @param path the relative path.
     * @return the path starting from the user's home directory.
     **/
    public static String getHomePath(String path) {
        return getHomeDir() + File.separator + path;
    }

    /**
     * Gets the resource file as URL with the base dir being the user's home.
     * Note, this is not JWS safe, meaning this should only be used
     * for files that are stored on the user's disk (outside of the jws sandbox).
     * @param path the path to the resource.
     * @return a URL that returns the path and null if can't create the url.
     **/
    public static java.net.URL getURLFromHome(String path) {
        java.net.URL url = null;
        path = getHomePath(path);
        try {
            File file = new File(path);
            url = file.toURI().toURL();
        } catch (Exception e) {
        }
        return url;
    }

    /**
     * Gets the resource file with the base dir being the user's home.
     * Note, this is not JWS safe, meaning this should only be used
     * for files that are stored on the user's disk (outside of the jws sandbox).
     * @param path the path to the resource.
     * @return a URL that returns the path and null if can't create the url.
     **/
    public static File getFileFromHome(String path) {
        File file = null;
        path = getHomeDir() + File.separator + path;
        try {
            file = new File(path);
        } catch (Exception e) {
        }
        return file;
    }

    /**
     * Gets the resource for doing JWS file access from JAR files.
     * @param path the path to the resource.
     * @return a URL that returns the path (could be inside a jar).
     **/
    public static java.net.URL getResource(String path) {
        return Thread.currentThread().getContextClassLoader().getResource(path);
    }
}
