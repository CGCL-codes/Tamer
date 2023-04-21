package org.jplate.util.resourcemgr;

import java.io.InputStream;

/**
 *
 * This class satisfies the ResourceMgrIfc interface and adheres to the
 * Singleton pattern.
 *
 */
public final class ResourceMgr implements ResourceMgrIfc {

    /**
     *
     * Denote resource name is null message.
     *
     */
    private static final String RESOURCE_NAME_IS_NULL_MSG = "resourceName == null";

    /**
     *
     * This class manages our singleton.
     *
     */
    private static final class ResourceMgrSingleton {

        static final ResourceMgrIfc _singleton = new ResourceMgr();
    }

    /**
     *
     * Default constructor not allowed.
     *
     */
    private ResourceMgr() {
    }

    /**
     *
     * This method will return our singleton.
     *
     * @return our singleton.
     *
     */
    public static ResourceMgrIfc getSingleton() {
        return ResourceMgrSingleton._singleton;
    }

    /**
     *
     * This method will return a stream for resourceName.
     *
     * @param resourceName is the name of the resource desired.
     *
     * @return the stream for resource or null if one is not found.
     *
     * @throws NullPointerException if resourceName is null.
     *
     */
    public InputStream getResource(final String resourceName) {
        if (resourceName == null) {
            throw new NullPointerException(RESOURCE_NAME_IS_NULL_MSG);
        }
        final String resourceHome = RESOURCE_HOME;
        final StringBuffer sb = new StringBuffer(resourceHome.length() + resourceName.length());
        sb.append(resourceHome).append(resourceName);
        return getClass().getResourceAsStream(sb.toString());
    }
}
