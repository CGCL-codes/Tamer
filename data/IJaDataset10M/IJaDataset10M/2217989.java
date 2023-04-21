package server.MWChatServer.auth;

import java.io.InputStream;

/**
 * A class that loads resources from locations relative to the
 * CLASSPATH, or relative to the ServletContext if one is specified.
 *
 * $Id: ResourceLoader.java,v 1.1 2005/11/07 23:37:17 torren Exp $
 */
public class ResourceLoader {

    /**
     * All static class - don't instantiate
     */
    protected ResourceLoader() {
    }

    /**
     * Load a resource from the CLASSPATH.  relativePath should use forward-slashes
     * and omit the first slash.
     *
     * e.g., getResource("path/to/my/resource");
     *
     * @param relativePath
     */
    public static InputStream getResource(String relativePath) throws Exception {
        InputStream is = ClassLoader.getSystemResourceAsStream(relativePath);
        if (is == null) {
            throw new Exception("Could not locate resource, " + relativePath);
        }
        return is;
    }
}
