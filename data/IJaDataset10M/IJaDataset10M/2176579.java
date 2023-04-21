package gate.util;

import gate.Gate;
import gate.creole.AbstractResource;
import java.beans.Introspector;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * GATE's class loader, which allows loading of classes over the net. A list of
 * URLs is searched, which should point at .jar files or to directories
 * containing class file hierarchies. The loader is also used for creating JAPE
 * RHS action classes.
 */
public class GateClassLoader extends URLClassLoader {

    protected static final Logger log = Logger.getLogger(GateClassLoader.class);

    /** Debug flag */
    private static final boolean DEBUG = false;

    /** Default construction - use an empty URL list. */
    public GateClassLoader(String name) {
        super(new URL[0]);
        this.id = name;
    }

    /** Chaining constructor. */
    public GateClassLoader(String name, ClassLoader parent) {
        super(new URL[0], parent);
        this.id = name;
    }

    /** Default construction with URLs list. */
    public GateClassLoader(String name, URL[] urls) {
        super(urls);
        this.id = name;
    }

    /** Chaining constructor with URLs list. */
    public GateClassLoader(String name, URL[] urls, ClassLoader parent) {
        super(urls, parent);
        this.id = name;
    }

    private String id = null;

    public String getID() {
        return id;
    }

    public String toString() {
        return "Classloader ID: " + id;
    }

    /**
   * Appends the specified URL to the list of URLs to search for classes and
   * resources.
   */
    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    @Override
    public URL getResource(String name) {
        URL result = null;
        result = super.getResource(name);
        if (result != null) return result;
        if (getParent() == null) {
            result = Gate.getClassLoader().findResource(name);
            if (result != null) return result;
        }
        Set<GateClassLoader> children;
        synchronized (childClassLoaders) {
            children = new LinkedHashSet<GateClassLoader>(childClassLoaders.values());
        }
        for (GateClassLoader cl : children) {
            result = cl.getResource(name);
            if (result != null) return result;
        }
        return null;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false, false);
    }

    /**
   * Delegate loading to the super class (loadClass has protected access there).
   */
    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return loadClass(name, resolve, false);
    }

    /**
   * Delegate loading to the super class (loadClass has protected access there).
   */
    public Class<?> loadClass(String name, boolean resolve, boolean localOnly) throws ClassNotFoundException {
        if (!this.equals(Gate.getClassLoader())) {
            try {
                Class<?> found = Gate.getClassLoader().getParent().loadClass(name);
                URL url = findResource(name.replace('.', '/') + ".class");
                if (url != null) log.warn(name + " is available via both the system classpath and a plugin; the plugin classes will be ignored");
                return found;
            } catch (ClassNotFoundException e) {
            }
        }
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
        }
        if (!localOnly) {
            if (getParent() == null) {
                try {
                    return Gate.getClassLoader().loadClass(name, resolve);
                } catch (ClassNotFoundException e) {
                }
            }
            Set<GateClassLoader> children;
            synchronized (childClassLoaders) {
                children = new LinkedHashSet<GateClassLoader>(childClassLoaders.values());
            }
            for (GateClassLoader cl : children) {
                try {
                    return cl.loadClass(name, resolve, true);
                } catch (ClassNotFoundException e) {
                }
            }
        }
        throw new ClassNotFoundException(name);
    }

    /**
   * Forward a call to super.defineClass, which is protected and final in super.
   * This is used by JAPE and the Jdk compiler class.
   */
    public Class<?> defineGateClass(String name, byte[] bytes, int offset, int len) {
        return super.defineClass(name, bytes, offset, len);
    }

    /**
   * Forward a call to super.resolveClass, which is protected and final in
   * super. This is used by JAPE and the Jdk compiler class
   */
    public void resolveGateClass(Class<?> c) {
        super.resolveClass(c);
    }

    /**
   * Given a fully qualified class name, this method returns the instance of
   * Class if it is already loaded using the ClassLoader or it returns null.
   */
    public Class<?> findExistingClass(String name) {
        return findLoadedClass(name);
    }

    Map<String, GateClassLoader> childClassLoaders = new LinkedHashMap<String, GateClassLoader>();

    /**
   * Returns a classloader that can, at some point in the future, be forgotton
   * which allows the class definitions to be garbage collected.
   * 
   * @param id
   *          the id of the classloader to return
   * @return either an existing classloader with the given id or a new
   *         classloader
   */
    public synchronized GateClassLoader getDisposableClassLoader(String id) {
        GateClassLoader gcl = childClassLoaders.get(id);
        if (gcl == null) {
            gcl = new GateClassLoader(id, new URL[0], null);
            childClassLoaders.put(id, gcl);
        }
        return gcl;
    }

    /**
   * Causes the specified classloader to be forgotten, making it and all the
   * class definitions loaded by it available for garbage collection
   * 
   * @param id
   *          the id of the classloader to forget
   */
    public void forgetClassLoader(String id) {
        Introspector.flushCaches();
        AbstractResource.flushBeanInfoCache();
        childClassLoaders.remove(id);
    }

    /**
   * Causes the specified classloader to be forgotten, making it and all the
   * class definitions loaded by it available for garbage collection
   * 
   * @param classloader
   *          the classloader to forget
   */
    public void forgetClassLoader(GateClassLoader classloader) {
        if (classloader != null) forgetClassLoader(classloader.getID());
    }
}
