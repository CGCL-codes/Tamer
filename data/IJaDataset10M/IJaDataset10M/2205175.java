package org.apache.shale.tiger.view.faces;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Utility class with methods that support getting a recursive list of classes starting with a specific package name.
 * </p>
 */
public final class PackageInfo {

    /**
     * <p>
     * The <code>Log</code> instance we will be using.
     * </p>
     */
    private transient Log log = null;

    /**
     * the singleton for this class
     */
    private static final PackageInfo INSTANCE = new PackageInfo();

    /**
     * <p>
     * Get the singleton instance of this class.
     * </p>
     */
    public static final PackageInfo getInstance() {
        return INSTANCE;
    }

    /**
     * <p>
     * Return an array of all classes, visible to our application class loader, in the specified Java package.
     * </p>
     * 
     * @param classes
     *            List of matching classes being accumulated
     * @param pckgname
     *            Package name used to select matching classes
     * 
     * @throws ClassNotFoundException
     */
    public Class[] getClasses(final List<Class> classes, final String pckgname) throws ClassNotFoundException {
        Enumeration resources;
        ClassLoader cld;
        String path;
        try {
            path = pckgname.replace('.', '/');
            cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            resources = cld.getResources(path);
            if (resources == null || !resources.hasMoreElements()) {
                throw new ClassNotFoundException("No resource for " + path);
            }
        } catch (NullPointerException e) {
            throw (ClassNotFoundException) new ClassNotFoundException(pckgname + " (" + pckgname + ") does not appear to be a valid package", e);
        } catch (IOException e) {
            throw (ClassNotFoundException) new ClassNotFoundException(pckgname + " (" + pckgname + ") does not appear to be a valid package", e);
        }
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            URLConnection connection = null;
            try {
                connection = resource.openConnection();
            } catch (IOException e) {
                throw (ClassNotFoundException) new ClassNotFoundException(pckgname + " (" + pckgname + ") does not appear to be a valid package", e);
            }
            if (connection instanceof JarURLConnection) {
                JarURLConnection juc = (JarURLConnection) connection;
                JarFile jarFile = null;
                try {
                    jarFile = juc.getJarFile();
                } catch (IOException e) {
                    throw (ClassNotFoundException) new ClassNotFoundException(pckgname + " (" + pckgname + ") does not appear to be a valid package", e);
                }
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String entryName = jarEntry.getName();
                    if (!entryName.startsWith(path)) {
                        continue;
                    }
                    if (!entryName.toLowerCase().endsWith(".class")) {
                        continue;
                    }
                    String className = filenameToClassname(entryName);
                    loadClass(classes, cld, className);
                }
            } else {
                File file;
                try {
                    file = new File(connection.getURL().toURI());
                } catch (URISyntaxException e) {
                    log().warn("error loading directory " + connection, e);
                    continue;
                }
                listFilesRecursive(classes, file, cld, pckgname);
            }
        }
        if (classes.size() < 1) {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package");
        }
        Class[] resolvedClasses = new Class[classes.size()];
        classes.toArray(resolvedClasses);
        return resolvedClasses;
    }

    /**
     * <p>
     * Convert a filename to a classname.
     * </p>
     * 
     * @param entryName
     *            Filename to be converted
     */
    protected String filenameToClassname(String entryName) {
        return entryName.substring(0, entryName.length() - 6).replace('/', '.');
    }

    /**
     * <p>
     * Load the class <code>className</code> using the classloader <code>cld</code>, and add it to the list.
     * </p>
     * 
     * @param classes
     *            List of matching classes being accumulated
     * @param cld
     *            ClassLoader from which to load the specified class
     * @param className
     *            Name of the class to be loaded
     */
    protected void loadClass(List<Class> classes, ClassLoader cld, String className) {
        try {
            classes.add(cld.loadClass(className));
        } catch (NoClassDefFoundError e) {
            log().warn("error loading class " + className, e);
        } catch (ClassNotFoundException e) {
            log().warn("error loading class " + className, e);
        }
    }

    /**
     * <p>
     * Traverse a directory structure starting at <code>base</code>, adding matching files to the specified list.
     * </p>
     * 
     * @param classes
     *            List of matching classes being accumulated
     * @param base
     *            Base file from which to recurse
     * @param cld
     *            ClassLoader being searched for matching classes
     * @param pckgname
     *            Package name used to select matching classes
     */
    protected void listFilesRecursive(final List<Class> classes, final File base, final ClassLoader cld, final String pckgname) {
        base.listFiles(new FileFilter() {

            public boolean accept(File file) {
                if (file.isDirectory()) {
                    listFilesRecursive(classes, file, cld, pckgname + "." + file.getName());
                    return false;
                }
                if (!file.getName().toLowerCase().endsWith(".class")) {
                    return false;
                }
                String className = filenameToClassname(pckgname + "." + file.getName());
                loadClass(classes, cld, className);
                return false;
            }
        });
    }

    /**
     * <p>
     * Return the <code>Log</code> instance to be used for this class, instantiating a new one if necessary.
     * </p>
     */
    private Log log() {
        if (log == null) {
            log = LogFactory.getLog(PackageInfo.class);
        }
        return log;
    }
}
