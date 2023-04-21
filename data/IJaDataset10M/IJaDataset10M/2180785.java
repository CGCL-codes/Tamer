package net.sf.joafip.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import net.sf.joafip.NotStorableClass;

/**
 * to find url of resource trying:<br>
 * <ul>
 * <li>Thread.currentThread().getContextClassLoader() if getContextClassLoader
 * method exist ( JDK 1.2 or later )
 * <li>using {@link ResourceFinder} class loader
 * <li>using {@link ClassLoader#getSystemResource(String)}
 * </ul>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public final class ResourceFinder extends AbstractClassLoaderFinder {

    private ResourceFinder() {
        super();
    }

    public static URL getResource(final String resourceName) throws MalformedURLException {
        ClassLoader classLoader = null;
        URL url = null;
        try {
            classLoader = getCurrentThreadContextClassLoader();
            if (classLoader != null) {
                url = classLoader.getResource(resourceName);
            }
        } catch (IllegalArgumentException exception) {
        } catch (IllegalAccessException exception) {
        } catch (InvocationTargetException exception) {
        }
        if (classLoader == null || url == null) {
            classLoader = getThisClassClassLoader();
            url = classLoader.getResource(resourceName);
        }
        if (classLoader == null || url == null) {
            url = ClassLoader.getSystemResource(resourceName);
        }
        if (url == null) {
            final URI uri = new File(resourceName).toURI();
            url = uri.toURL();
        }
        return url;
    }
}
