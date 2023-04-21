package org.tanso.fountain.util;

import java.net.URL;
import java.net.URLClassLoader;
import org.tanso.fountain.util.rmi.PlatformRMI;

/**
 * This is a classloader which can reload class. The parent delegating model is
 * disposed. The properties of the object whose class is reloaded should be
 * maintained http://galaxystar.javaeye.com/blog/136427
 * 
 * @author ken.wu mail ken.wug@gmail.com 2007-9-28
 */
public class HotSwapClassLoader extends URLClassLoader implements IFountainClassLoader {

    public HotSwapClassLoader() {
        super(new URL[0]);
    }

    public HotSwapClassLoader(URL[] urls) {
        super(urls);
    }

    public HotSwapClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public synchronized Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }

    public synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (null != super.findLoadedClass(name)) {
            return reload(name, resolve);
        } else {
            return super.loadClass(name, resolve);
        }
    }

    public synchronized Class<?> reload(String name, boolean resolve) throws ClassNotFoundException {
        return new HotSwapClassLoader(super.getURLs(), super.getParent()).loadClass(name, resolve);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
        PlatformRMI.addRmiURL(url);
    }
}
