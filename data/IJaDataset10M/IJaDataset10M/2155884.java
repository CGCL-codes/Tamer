package uk.ac.ebi.intact.commons.util;

import sun.net.www.protocol.file.FileURLConnection;
import uk.ac.ebi.intact.commons.CommonsRuntimeException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @version $Revision: 9541 $ $Date: 2007-08-22 09:56:15 -0400 (Wed, 22 Aug 2007) $
 */
public final class ClassUtils {

    private ClassUtils() {
    }

    /**
     * Tries a Class.loadClass with the context class loader of the current thread first and
     * automatically falls back to the ClassUtils class loader (i.e. the loader of the
     * intact-commons.jar lib) if necessary.
     *
     * @param type fully qualified name of a non-primitive non-array class
     *
     * @return the corresponding Class
     *
     * @throws NullPointerException   if type is null
     * @throws ClassNotFoundException
     */
    public static Class classForName(String type) throws ClassNotFoundException {
        if (type == null) throw new NullPointerException("type");
        try {
            return Class.forName(type, false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException ignore) {
            return Class.forName(type, false, ClassUtils.class.getClassLoader());
        }
    }

    public static InputStream getResourceAsStream(String resource) {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        if (stream == null) {
            stream = ClassUtils.class.getClassLoader().getResourceAsStream(resource);
        }
        return stream;
    }

    /**
     * @param resource      Name of resource(s) to find in classpath
     * @param defaultObject The default object to use to determine the class loader (if none associated with current thread.)
     *
     * @return Iterator over URL Objects
     */
    public static Iterator<URL> getResources(String resource, Object defaultObject) throws IOException {
        Enumeration<URL> resources = getCurrentLoader(defaultObject).getResources(resource);
        List<URL> lst = new ArrayList<URL>();
        while (resources.hasMoreElements()) {
            lst.add(resources.nextElement());
        }
        return lst.iterator();
    }

    /**
     * Gets the ClassLoader associated with the current thread.  Returns the class loader associated with
     * the specified default object if no context loader is associated with the current thread.
     *
     * @param defaultObject The default object to use to determine the class loader (if none associated with current thread.)
     *
     * @return ClassLoader
     */
    public static ClassLoader getCurrentLoader(Object defaultObject) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = defaultObject.getClass().getClassLoader();
        }
        return loader;
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new CommonsRuntimeException(e);
        }
    }

    public static Collection<URL> searchResourcesInClasspath(String resourcePath) throws IOException {
        return searchResourcesInClasspath(Thread.currentThread().getContextClassLoader(), resourcePath, "");
    }

    public static Collection<URL> searchResourcesInClasspath(String prefix, String suffix) throws IOException {
        return searchResourcesInClasspath(Thread.currentThread().getContextClassLoader(), prefix, suffix);
    }

    public static Collection<URL> searchResourcesInClasspath(ClassLoader classLoader, String prefix, String suffix) throws IOException {
        Enumeration<URL>[] enumerations = new Enumeration[] { classLoader.getResources(prefix), classLoader.getResources(prefix + "MANIFEST.MF") };
        Set<URL> urls = new HashSet<URL>();
        for (Enumeration<URL> enumeration : enumerations) {
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                URLConnection urlConnection = url.openConnection();
                urlConnection.setUseCaches(false);
                urlConnection.setDefaultUseCaches(false);
                if (urlConnection instanceof JarURLConnection) {
                    JarFile jarFile = ((JarURLConnection) urlConnection).getJarFile();
                    if (jarFile != null) {
                        searchJar(classLoader, urls, jarFile, prefix, suffix);
                    } else {
                        searchDir(urls, new File(URLDecoder.decode(url.getFile(), "UTF-8")), suffix);
                    }
                } else if (urlConnection instanceof FileURLConnection) {
                    urls.add(url);
                } else {
                    throw new CommonsRuntimeException("Cannot handler URLConnection of type: " + urlConnection);
                }
            }
        }
        return urls;
    }

    protected static void searchJar(ClassLoader cl, Set<URL> resultUrls, JarFile file, String prefix, String suffix) throws IOException {
        Enumeration<JarEntry> entriesEnumeration = file.entries();
        JarEntry entry;
        String name;
        while (entriesEnumeration.hasMoreElements()) {
            try {
                entry = entriesEnumeration.nextElement();
            } catch (Throwable t) {
                continue;
            }
            name = entry.getName();
            if (name.startsWith(prefix) && name.endsWith(suffix)) {
                Enumeration<URL> resourcesEnumeration = cl.getResources(name);
                while (resourcesEnumeration.hasMoreElements()) {
                    resultUrls.add(resourcesEnumeration.nextElement());
                }
            }
        }
    }

    protected static void searchDir(Set<URL> resultUrls, File dir, String suffix) throws IOException {
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            String absolutePath;
            for (File file : files) {
                absolutePath = file.getAbsolutePath();
                if (file.isDirectory()) {
                    searchDir(resultUrls, file, suffix);
                } else if (absolutePath.endsWith(suffix)) {
                    resultUrls.add(file.toURL());
                }
            }
        }
    }
}
