package org.xaware.ide.runtime;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This class provides ClassLoaders used to load user-provided classes. Factory methods are defined to provide instances
 * which are aware of static directories and dynamic directories.
 * 
 * @author Tim Uttormark
 */
public class XAClassLoader extends URLClassLoader {

    /** Class level logger */
    private static final XAwareLogger logger = XAwareLogger.getXAwareLogger(XAClassLoader.class.getName());

    /** Count of classLoaders created, used to give them unique names */
    private static int classLoaderCount = 0;

    /** Unique name of each object instance, to aid in debugging */
    private String classLoaderName;

    /**
     * Constructor made private to enforce factory pattern below.
     */
    private XAClassLoader(final String name, final URL[] urls, final ClassLoader parentCL) {
        super(urls, parentCL);
        this.classLoaderName = name;
    }

    /**
     * Creates and returns a new ClassLoader instance which is able to load classes from the static class and jar
     * directories.
     * 
     * @return a new static XAClassLoader instance
     */
    public static XAClassLoader getStaticClassLoader(final ClassLoader parentCL) {
        final ClassPathUtils classPathUtils = new ClassPathUtils();
        final List<File> classDirs = classPathUtils.getStaticClassDirs();
        final URL[] classDirURLs = dirListToURLs(classDirs);
        final XAClassLoader staticClassLoader = new XAClassLoader("static_" + classLoaderCount++, classDirURLs, parentCL);
        staticClassLoader.addJarsFromDirList(classPathUtils.getStaticJarDirs());
        return staticClassLoader;
    }

    /**
     * Creates and returns a new ClassLoader instance which is able to load classes from the dynamic class and jar
     * directories.
     * 
     * @return a new dynamic XAClassLoader instance
     */
    public static XAClassLoader getDynamicClassLoader(final ClassLoader parentCL) {
        final ClassPathUtils classPathUtils = new ClassPathUtils();
        final List<File> classDirs = classPathUtils.getDynamicClassDirs();
        final URL[] classDirURLs = dirListToURLs(classDirs);
        final XAClassLoader dynamicClassLoader = new XAClassLoader("dynamic_" + classLoaderCount++, classDirURLs, parentCL);
        dynamicClassLoader.addJarsFromDirList(classPathUtils.getDynamicJarDirs());
        return dynamicClassLoader;
    }

    /**
     * Converts a List of directories into an array of URLs.
     * 
     * @param dirList
     *            the List of dirs to be converted
     * @return an array of URL refering to the dirs provided.
     */
    private static URL[] dirListToURLs(final List<File> dirList) {
        final URL[] urlArray = new URL[dirList.size()];
        for (int i = 0; i < urlArray.length; i++) {
            final File dir = dirList.get(i);
            final URL url = dirToURL(dir);
            urlArray[i] = url;
        }
        return urlArray;
    }

    /**
     * Converts one directory into a URL.
     * 
     * @param dir
     *            the dir to be converted
     * @return a URL refering to the dir provided.
     */
    private static URL dirToURL(final File dir) {
        URL dirURL = null;
        try {
            dirURL = dir.toURL();
        } catch (final MalformedURLException e) {
            final String errMsg = "Could not generate URL from directory " + dir.getAbsolutePath() + ", " + e.getMessage();
            logger.warning(errMsg);
            throw new IllegalArgumentException(errMsg);
        }
        return dirURL;
    }

    /**
     * Searches the list of directories provided for jar/zip files, and adds the URL for each jar/zip file found to the
     * list of URLs searched by this ClassLoader.
     * 
     * @param dirList
     *            a List of directory Files
     */
    private void addJarsFromDirList(final List<File> dirList) {
        for (File dir : dirList) {
            if (dir.exists()) {
                addJarsFromDir(dir);
            } else {
                logger.warning("Directory " + dir.getAbsolutePath() + " not found.");
            }
        }
    }

    /**
     * Searches a single directory provided for jar/zip files, and adds the URL for each jar/zip file found to the list
     * of URLs searched by this ClassLoader.
     * 
     * @param dir
     *            a directory File
     */
    private void addJarsFromDir(final File dir) {
        final List<File> jarList = ClassPathUtils.getAllJarsWithinDir(dir);
        Collections.sort(jarList);
        for (File jarFile : jarList) {
            try {
                addURL(jarFile.toURL());
            } catch (final MalformedURLException e) {
                final String errMsg = "Could not generate URL from File " + jarFile.getAbsolutePath() + ", " + e.getMessage();
                logger.severe(errMsg);
                throw new IllegalArgumentException(errMsg);
            }
        }
    }

    /**
     * toString() method for debugging/logging purposes.
     */
    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("My name: " + classLoaderName);
        buf.append("\nMy ID: " + getClass().getName() + "@" + Integer.toHexString(hashCode()));
        buf.append("\nMy URLs: " + Arrays.asList(super.getURLs()));
        buf.append("\nMy parent: " + super.getParent());
        return buf.toString();
    }
}
