package org.pcomeziantou.volleylife.tools;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * URL content read from a class resource.
 * 
 * @author Emmanuel Puybaret
 */
public class ResourceURLContent extends URLContent {

    private static final long serialVersionUID = 1L;

    private boolean multiPartResource;

    /**
	 * Creates a content for <code>resourceName</code> relative to
	 * <code>resourceClass</code>.
	 * 
	 * @param resourceClass
	 *            the class relative to the resource name to load
	 * @param resourceName
	 *            the name of the resource
	 * @throws IllegalArgumentException
	 *             if the resource doesn't match a valid resource.
	 */
    public ResourceURLContent(Class<?> resourceClass, String resourceName) {
        this(resourceClass, resourceName, false);
    }

    /**
	 * Creates a content for <code>resourceName</code> relative to
	 * <code>resourceClass</code>.
	 * 
	 * @param resourceClass
	 *            the class relative to the resource name to load
	 * @param resourceName
	 *            the name of the resource
	 * @param multiPartResource
	 *            if <code>true</code> then the resource is a multi part
	 *            resource stored in a directory with other required resources
	 * @throws IllegalArgumentException
	 *             if the resource doesn't match a valid resource.
	 */
    public ResourceURLContent(Class<?> resourceClass, String resourceName, boolean multiPartResource) {
        super(getClassResource(resourceClass, resourceName));
        if (getURL() == null) {
            throw new IllegalArgumentException("Unknown resource " + resourceName);
        }
        this.multiPartResource = multiPartResource;
    }

    /**
	 * Creates a content for <code>resourceName</code> relative to
	 * <code>resourceClassLoader</code>.
	 * 
	 * @param resourceClassLoader
	 *            the class loader used to load the given resource name
	 * @param resourceName
	 *            the name of the resource
	 * @throws IllegalArgumentException
	 *             if the resource doesn't match a valid resource.
	 */
    public ResourceURLContent(ClassLoader resourceClassLoader, String resourceName) {
        super(resourceClassLoader.getResource(resourceName));
        if (getURL() == null) {
            throw new IllegalArgumentException("Unknown resource " + resourceName);
        }
    }

    private static final boolean isJava1dot5dot0_16 = System.getProperty("java.version").startsWith("1.5.0_16");

    /**
	 * Returns the URL of the given resource relative to
	 * <code>resourceClass</code>.
	 */
    private static URL getClassResource(Class<?> resourceClass, String resourceName) {
        URL defaultUrl = resourceClass.getResource(resourceName);
        if (isJava1dot5dot0_16 && defaultUrl != null && "jar".equalsIgnoreCase(defaultUrl.getProtocol())) {
            String defaultUrlExternalForm = defaultUrl.toExternalForm();
            if (defaultUrl.toExternalForm().indexOf("!/") == -1) {
                String fixedUrl = "jar:" + resourceClass.getProtectionDomain().getCodeSource().getLocation().toExternalForm() + "!/" + defaultUrl.getPath();
                if (!fixedUrl.equals(defaultUrlExternalForm)) {
                    try {
                        return new URL(fixedUrl);
                    } catch (MalformedURLException ex) {
                    }
                }
            }
        }
        return defaultUrl;
    }

    /**
	 * Creates a content for <code>resourceUrl</code>.
	 * 
	 * @param url
	 *            the URL of the resource
	 */
    public ResourceURLContent(URL url, boolean multiPartResource) {
        super(url);
        this.multiPartResource = multiPartResource;
    }

    /**
	 * Returns <code>true</code> if the resource is a multi part resource stored
	 * in a directory with other required resources.
	 */
    public boolean isMultiPartResource() {
        return this.multiPartResource;
    }
}
