package net.sf.traser.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class to locate a resource.
 * @author karnokd, 2007.11.27.
 * @version $Revision 1.0$
 */
public final class ResourceLocator {

    /**
     * Private constructor.
     */
    private ResourceLocator() {
        throw new AssertionError("Utility class!");
    }

    /**
     * Try to locate a resouce by name and return it as an
     * inputstream. 
     * The search sequence is:
     * <ol>
     * <li>ResourceLocator.class.getResource()</li>
     * <li>Thread.currentThread().getContextClassLoader().getResource()</li>
     * <li>ClassLoader.getSystemResource()</li>
     * <li>File()</li>
     * <li>a File starting from the classpath's root directory of the ResourceLocator class</li>
     * </ol>
     * @param resource the resource name
     * @return the inputstream to the resource or null if not found.
     */
    public static URL find(String resource) {
        URL in = null;
        in = ResourceLocator.class.getResource(resource);
        if (in != null) {
            return in;
        }
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl != null) {
            in = cl.getResource(resource);
            if (in != null) {
                return in;
            }
        }
        in = ClassLoader.getSystemResource(resource);
        if (in != null) {
            return in;
        }
        File localFile = new File(resource);
        if (localFile.exists()) {
            try {
                return localFile.toURI().toURL();
            } catch (MalformedURLException ex) {
            }
        }
        String classRootPath = "";
        String myClassName = ResourceLocator.class.getSimpleName();
        URL currentURL = ResourceLocator.class.getResource(myClassName + ".class");
        String urlPath = null;
        if (currentURL != null) {
            urlPath = currentURL.getPath();
            if (urlPath.startsWith("/")) {
                urlPath = urlPath.substring(1);
            }
            String className = ResourceLocator.class.getName().replace('.', '/');
            int idx = className.lastIndexOf('/');
            if (idx >= 0) {
                className = className.substring(0, idx);
            }
            idx = urlPath.lastIndexOf(className);
            if (idx >= 0) {
                classRootPath = urlPath.substring(0, idx);
            } else {
                classRootPath = urlPath;
            }
            if (classRootPath.endsWith("/")) {
                classRootPath = classRootPath.substring(0, classRootPath.length() - 1);
            }
            localFile = new File(classRootPath + '/' + resource);
            if (localFile.exists()) {
                try {
                    return localFile.toURI().toURL();
                } catch (MalformedURLException ex) {
                }
            }
        }
        return null;
    }

    /**
     * Locates  a resource. The search sequence is:
     * <ol>
     * <li>ResourceLocator.class.getResource()</li>
     * <li>Thread.currentThread().getContextClassLoader().getResource()</li>
     * <li>ClassLoader.getSystemResource()</li>
     * <li>File()</li>
     * </ol>
     * @param resource the resource name
     * @return the inputstream to the resource or null if not found.
     */
    public static InputStream getResource(String resource) {
        InputStream in = null;
        in = ResourceLocator.class.getResourceAsStream(resource);
        if (in != null) {
            return in;
        }
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl != null) {
            in = cl.getResourceAsStream(resource);
            if (in != null) {
                return in;
            }
        }
        in = ClassLoader.getSystemResourceAsStream(resource);
        if (in != null) {
            return in;
        }
        File localFile = new File(resource);
        if (localFile.exists()) {
            try {
                return new FileInputStream(localFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ResourceLocator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     * Find a resource and return an InputStream to it if found.
     * @param resource the resource name to find
     * @return the InputStream or null if the resource was not found
     */
    public static InputStream findAsStream(String resource) {
        try {
            URL url = find(resource);
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException ex) {
            Logger.getLogger(ResourceLocator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
