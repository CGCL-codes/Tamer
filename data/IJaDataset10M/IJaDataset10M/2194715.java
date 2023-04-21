package org.viewaframework.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.jar.JarEntry;

/**
 * This class tells the resource bundle loader how to handle it. Whether if it has to be loaded in
 * the cache or not. By default a resource bundle with this control is checked every time is called.
 * If the resource has changed then it will be loaded again, if it has not then it will be loaded 
 * from the cache.
 * 
 * @author Mario Garcia
 *
 */
public class LastModifiedControl extends ResourceBundle.Control implements ChildResourceBundleAware {

    private static final long CHECK_EVERY_TIME = 0;

    private ResourceBundle parent;

    private long previousModification;

    private long timeToLive;

    /**
	 * 
	 */
    public LastModifiedControl() {
        this.previousModification = System.currentTimeMillis();
        this.timeToLive = CHECK_EVERY_TIME;
    }

    public ResourceBundle getParent() {
        return this.parent;
    }

    /**
	 * @return
	 */
    public long getPreviousModification() {
        return previousModification;
    }

    /**
	 * @return
	 */
    public long getTimeToLive() {
        return timeToLive;
    }

    @Override
    public long getTimeToLive(String baseName, Locale locale) {
        return this.timeToLive;
    }

    @Override
    public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime) {
        String resource = this.toResourceName(baseName, format).replaceAll("java\\.properties", "properties");
        URL url = loader.getResource(resource);
        boolean result = false;
        try {
            if (url != null) {
                long lastModified = 0;
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    connection.setUseCaches(false);
                    if (connection instanceof JarURLConnection) {
                        JarEntry ent = ((JarURLConnection) connection).getJarEntry();
                        if (ent != null) {
                            lastModified = ent.getTime();
                            if (lastModified == -1) {
                                lastModified = 0;
                            }
                        }
                    } else {
                        lastModified = connection.getLastModified();
                    }
                }
                result = lastModified >= loadTime;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("all")
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        String bundleName = toBundleName(baseName, locale);
        ResourceBundle bundle = null;
        if (format.equals("java.class")) {
            try {
                Class<? extends ResourceBundle> bundleClass = (Class<? extends ResourceBundle>) loader.loadClass(bundleName);
                if (ResourceBundle.class.isAssignableFrom(bundleClass)) {
                    bundle = bundleClass.newInstance();
                } else {
                    throw new ClassCastException(bundleClass.getName() + " cannot be cast to ResourceBundle");
                }
            } catch (ClassNotFoundException e) {
            }
        } else if (format.equals("java.properties")) {
            final String resourceName = toResourceName(bundleName, "properties");
            final ClassLoader classLoader = loader;
            final boolean reloadFlag = reload;
            InputStream stream = null;
            try {
                stream = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {

                    public InputStream run() throws IOException {
                        InputStream is = null;
                        if (reloadFlag) {
                            URL url = classLoader.getResource(resourceName);
                            if (url != null) {
                                URLConnection connection = url.openConnection();
                                if (connection != null) {
                                    connection.setUseCaches(false);
                                    is = connection.getInputStream();
                                }
                            }
                        } else {
                            is = classLoader.getResourceAsStream(resourceName);
                        }
                        return is;
                    }
                });
            } catch (PrivilegedActionException e) {
                throw (IOException) e.getException();
            }
            if (stream != null) {
                try {
                    bundle = new ChildResourceBundle(stream, this.getParent());
                } finally {
                    stream.close();
                }
            }
        } else {
            throw new IllegalArgumentException("unknown format: " + format);
        }
        return bundle;
    }

    public void setParent(ResourceBundle parent) {
        this.parent = parent;
    }

    /**
	 * @param previousModification
	 */
    public void setPreviousModification(long previousModification) {
        this.previousModification = previousModification;
    }

    /**
	 * @param timeToLive
	 */
    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }
}
