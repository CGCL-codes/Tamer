package org.apache.axis2.classloader;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.net.URI;
import java.net.URLClassLoader;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;
import java.security.cert.Certificate;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * The JarFileClassLoader that loads classes and resources from a list of JarFiles.  This method is simmilar to URLClassLoader
 * except it properly closes JarFiles when the classloader is destroyed so that the file read lock will be released, and
 * the jar file can be modified and deleted.
 * <p>
 *
 * @version $Rev: 725315 $ $Date: 2008-12-10 10:45:38 -0500 (Wed, 10 Dec 2008) $
 */
public class JarFileClassLoader extends MultiParentClassLoader {

    private static final URL[] EMPTY_URLS = new URL[0];

    private final UrlResourceFinder resourceFinder = new UrlResourceFinder();

    private final AccessControlContext acc;

    /**
     * Creates a JarFileClassLoader that is a child of the system class loader.
     * @param urls a list of URLs from which classes and resources should be loaded
     */
    public JarFileClassLoader(URL[] urls) {
        super(EMPTY_URLS);
        this.acc = AccessController.getContext();
        addURLs(urls);
    }

    /**
     * Creates a JarFileClassLoader that is a child of the specified class loader.
     * @param urls a list of URLs from which classes and resources should be loaded
     * @param parent the parent of this class loader
     */
    public JarFileClassLoader(URL[] urls, ClassLoader parent) {
        super(EMPTY_URLS, parent);
        this.acc = AccessController.getContext();
        addURLs(urls);
    }

    public JarFileClassLoader(URL[] urls, ClassLoader parent, boolean inverseClassLoading, String[] hiddenClasses, String[] nonOverridableClasses) {
        super(EMPTY_URLS, parent, inverseClassLoading, hiddenClasses, nonOverridableClasses);
        this.acc = AccessController.getContext();
        addURLs(urls);
    }

    /**
     * Creates a named class loader as a child of the specified parents.
     * @param urls the urls from which this class loader will classes and resources
     * @param parents the parents of this class loader
     */
    public JarFileClassLoader(URL[] urls, ClassLoader[] parents) {
        super(EMPTY_URLS, parents);
        this.acc = AccessController.getContext();
        addURLs(urls);
    }

    public JarFileClassLoader(URL[] urls, ClassLoader[] parents, boolean inverseClassLoading, Collection hiddenClasses, Collection nonOverridableClasses) {
        super(EMPTY_URLS, parents, inverseClassLoading, hiddenClasses, nonOverridableClasses);
        this.acc = AccessController.getContext();
        addURLs(urls);
    }

    public JarFileClassLoader(URL[] urls, ClassLoader[] parents, boolean inverseClassLoading, String[] hiddenClasses, String[] nonOverridableClasses) {
        super(EMPTY_URLS, parents, inverseClassLoading, hiddenClasses, nonOverridableClasses);
        this.acc = AccessController.getContext();
        addURLs(urls);
    }

    public JarFileClassLoader(JarFileClassLoader source) {
        super(source);
        this.acc = AccessController.getContext();
        addURLs(source.getURLs());
    }

    public static ClassLoader copy(ClassLoader source) {
        if (source instanceof JarFileClassLoader) {
            return new JarFileClassLoader((JarFileClassLoader) source);
        } else if (source instanceof MultiParentClassLoader) {
            return new MultiParentClassLoader((MultiParentClassLoader) source);
        } else if (source instanceof URLClassLoader) {
            return new URLClassLoader(((URLClassLoader) source).getURLs(), source.getParent());
        } else {
            return new URLClassLoader(new URL[0], source);
        }
    }

    ClassLoader copy() {
        return JarFileClassLoader.copy(this);
    }

    /**
     * {@inheritDoc}
     */
    public URL[] getURLs() {
        return resourceFinder.getUrls();
    }

    /**
     * {@inheritDoc}
     */
    public void addURL(final URL url) {
        AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                resourceFinder.addUrl(url);
                return null;
            }
        }, acc);
    }

    /**
     * Adds an array of urls to the end of this class loader.
     * @param urls the URLs to add
     */
    protected void addURLs(final URL[] urls) {
        AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                resourceFinder.addUrls(urls);
                return null;
            }
        }, acc);
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() {
        resourceFinder.destroy();
        super.destroy();
    }

    /**
     * {@inheritDoc}
     */
    public URL findResource(final String resourceName) {
        return (URL) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                return resourceFinder.findResource(resourceName);
            }
        }, acc);
    }

    /**
     * {@inheritDoc}
     */
    protected Enumeration<URL> internalfindResources(final String name) throws IOException {
        return AccessController.doPrivileged(new PrivilegedAction<Enumeration<URL>>() {

            public Enumeration<URL> run() {
                return resourceFinder.findResources(name);
            }
        }, acc);
    }

    /**
     * {@inheritDoc}
     */
    protected String findLibrary(String libraryName) {
        int pathEnd = libraryName.lastIndexOf('/');
        if (pathEnd == libraryName.length() - 1) {
            throw new IllegalArgumentException("libraryName ends with a '/' character: " + libraryName);
        }
        final String resourceName;
        if (pathEnd < 0) {
            resourceName = System.mapLibraryName(libraryName);
        } else {
            String path = libraryName.substring(0, pathEnd + 1);
            String file = libraryName.substring(pathEnd + 1);
            resourceName = path + System.mapLibraryName(file);
        }
        ResourceHandle resourceHandle = (ResourceHandle) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                return resourceFinder.getResource(resourceName);
            }
        }, acc);
        if (resourceHandle == null) {
            return null;
        }
        URL url = resourceHandle.getUrl();
        if (!"file".equals(url.getProtocol())) {
            return null;
        }
        return new File(URI.create(url.toString())).getPath();
    }

    /**
     * {@inheritDoc}
     */
    protected Class findClass(final String className) throws ClassNotFoundException {
        try {
            return (Class) AccessController.doPrivileged(new PrivilegedExceptionAction() {

                public Object run() throws ClassNotFoundException {
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        String packageName;
                        int packageEnd = className.lastIndexOf('.');
                        if (packageEnd >= 0) {
                            packageName = className.substring(0, packageEnd);
                            securityManager.checkPackageDefinition(packageName);
                        }
                    }
                    String resourceName = className.replace('.', '/') + ".class";
                    ResourceHandle resourceHandle = resourceFinder.getResource(resourceName);
                    if (resourceHandle == null) {
                        throw new ClassNotFoundException(className);
                    }
                    byte[] bytes;
                    Manifest manifest;
                    try {
                        bytes = resourceHandle.getBytes();
                        manifest = resourceHandle.getManifest();
                    } catch (IOException e) {
                        throw new ClassNotFoundException(className, e);
                    }
                    Certificate[] certificates = resourceHandle.getCertificates();
                    URL codeSourceUrl = resourceHandle.getCodeSourceUrl();
                    definePackage(className, codeSourceUrl, manifest);
                    CodeSource codeSource = new CodeSource(codeSourceUrl, certificates);
                    return defineClass(className, bytes, 0, bytes.length, codeSource);
                }
            }, acc);
        } catch (PrivilegedActionException e) {
            throw (ClassNotFoundException) e.getException();
        }
    }

    private void definePackage(String className, URL jarUrl, Manifest manifest) {
        int packageEnd = className.lastIndexOf('.');
        if (packageEnd < 0) {
            return;
        }
        String packageName = className.substring(0, packageEnd);
        String packagePath = packageName.replace('.', '/') + "/";
        Attributes packageAttributes = null;
        Attributes mainAttributes = null;
        if (manifest != null) {
            packageAttributes = manifest.getAttributes(packagePath);
            mainAttributes = manifest.getMainAttributes();
        }
        Package pkg = getPackage(packageName);
        if (pkg != null) {
            if (pkg.isSealed()) {
                if (!pkg.isSealed(jarUrl)) {
                    throw new SecurityException("Package was already sealed with another URL: package=" + packageName + ", url=" + jarUrl);
                }
            } else {
                if (isSealed(packageAttributes, mainAttributes)) {
                    throw new SecurityException("Package was already been loaded and not sealed: package=" + packageName + ", url=" + jarUrl);
                }
            }
        } else {
            String specTitle = getAttribute(Attributes.Name.SPECIFICATION_TITLE, packageAttributes, mainAttributes);
            String specVendor = getAttribute(Attributes.Name.SPECIFICATION_VENDOR, packageAttributes, mainAttributes);
            String specVersion = getAttribute(Attributes.Name.SPECIFICATION_VERSION, packageAttributes, mainAttributes);
            String implTitle = getAttribute(Attributes.Name.IMPLEMENTATION_TITLE, packageAttributes, mainAttributes);
            String implVendor = getAttribute(Attributes.Name.IMPLEMENTATION_VENDOR, packageAttributes, mainAttributes);
            String implVersion = getAttribute(Attributes.Name.IMPLEMENTATION_VERSION, packageAttributes, mainAttributes);
            URL sealBase = null;
            if (isSealed(packageAttributes, mainAttributes)) {
                sealBase = jarUrl;
            }
            definePackage(packageName, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
        }
    }

    private String getAttribute(Attributes.Name name, Attributes packageAttributes, Attributes mainAttributes) {
        if (packageAttributes != null) {
            String value = packageAttributes.getValue(name);
            if (value != null) {
                return value;
            }
        }
        if (mainAttributes != null) {
            return mainAttributes.getValue(name);
        }
        return null;
    }

    private boolean isSealed(Attributes packageAttributes, Attributes mainAttributes) {
        String sealed = getAttribute(Attributes.Name.SEALED, packageAttributes, mainAttributes);
        return sealed != null && "true".equalsIgnoreCase(sealed);
    }
}
