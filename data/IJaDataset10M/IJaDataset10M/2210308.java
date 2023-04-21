package org.apache.jdo.impl.enhancer.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Searches resources within a path.
 */
public class PathResourceLocator extends ResourceLocatorBase implements ResourceLocator {

    /**
     * The class loader for loading jdo resources.
     */
    private final URLClassLoader classLoader;

    /**
     * Returns a classloader initialized on the path provided to constructor.
     */
    public URLClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Creates an instance.
     */
    public PathResourceLocator(PrintWriter out, boolean verbose, String path) throws IOException {
        super(out, verbose);
        affirm(path != null);
        final List urls = new ArrayList();
        for (Enumeration e = new StringTokenizer(path, File.pathSeparator); e.hasMoreElements(); ) {
            final String s = (String) e.nextElement();
            final File file = new File(s).getCanonicalFile();
            final URL url = file.toURL();
            final String canonicalName = url.toString();
            affirm(canonicalName != null);
            if (!file.canRead()) {
                final String msg = getI18N("enhancer.cannot_read_resource", file.toString());
                throw new IOException(msg);
            }
            final String l = s.toLowerCase();
            if (!(file.isDirectory() || (file.isFile() && (l.endsWith(".jar") || l.endsWith(".zip"))))) {
                final String msg = getI18N("enhancer.illegal_path_element", file.toString());
                throw new IOException(msg);
            }
            urls.add(url);
            printMessage(getI18N("enhancer.using_path_element", canonicalName));
        }
        final URL[] urlArray = (URL[]) urls.toArray(new URL[urls.size()]);
        classLoader = new URLClassLoader(urlArray, null);
        affirm(classLoader != null);
    }

    /**
     * Finds a resource with a given name.
     */
    public InputStream getInputStreamForResource(String resourceName) {
        affirm(resourceName != null);
        final URL url = classLoader.findResource(resourceName);
        if (url == null) {
            printMessage(getI18N("enhancer.not_found_resource", resourceName));
            return null;
        }
        final InputStream stream;
        try {
            stream = url.openStream();
        } catch (IOException ex) {
            final String msg = getI18N("enhancer.io_error_while_reading_resource", url.toString(), ex.getMessage());
            throw new RuntimeException(msg);
        }
        affirm(stream != null);
        printMessage(getI18N("enhancer.found_resource", resourceName));
        return stream;
    }
}
