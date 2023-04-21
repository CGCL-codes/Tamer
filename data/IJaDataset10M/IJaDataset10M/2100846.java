package org.hsqldb.lib;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.hsqldb.persist.Logger;

/**
 * An abstraction over the standard java.lang.Class.getResource[AsStream]() that
 * allows a superceding class loader to be injected at the top of the precedence
 * chain, for instance when the runtime must perform embedded resource loading
 * through a custom or non-standard protocol.
 *
 * @author boucherb@users
 * @version 1.8.1.3
 * @since 1.8.1.3
 */
public class ResourceStreamProvider {

    private static ClassLoader loader;

    private static HashSet forbiddenProtocols = new HashSet();

    static {
        forbiddenProtocols.add("file");
    }

    private ResourceStreamProvider() {
    }

    public static synchronized void setLoader(ClassLoader loader) {
        ResourceStreamProvider.loader = loader;
    }

    public static synchronized ClassLoader getLoader() {
        return ResourceStreamProvider.loader;
    }

    public static Set forbiddenProtocols() {
        return forbiddenProtocols;
    }

    public static boolean exists(String resource) {
        ClassLoader loader = ResourceStreamProvider.getLoader();
        URL url = null;
        if (loader == null) {
            url = Logger.class.getResource(resource);
        } else {
            url = loader.getResource(resource);
            if (url == null) {
                url = Logger.class.getResource(resource);
            }
        }
        return url != null && !forbiddenProtocols().contains(url.getProtocol());
    }

    public static InputStream getResourceAsStream(String resource) throws IOException {
        ClassLoader loader = ResourceStreamProvider.getLoader();
        URL url = null;
        if (loader == null) {
            url = Logger.class.getResource(resource);
        } else {
            url = loader.getResource(resource);
            if (url == null) {
                url = Logger.class.getResource(resource);
            }
        }
        if (url == null) {
            throw new IOException("Missing resource: " + resource);
        }
        String protocol = url.getProtocol();
        if (forbiddenProtocols.contains(protocol)) {
            throw new IOException("Wrong protocol [" + protocol + "] for resource : " + resource);
        }
        return url.openStream();
    }
}
