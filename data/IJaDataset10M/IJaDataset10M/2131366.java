package com.wideplay.beanframe.isolation;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * Created with IntelliJ IDEA.
 * On: 19/02/2007
 *
 * This is a classloader that invokes a specified bean or set of beans
 * in an isolated context.
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class UrlInverseClassLoader extends URLClassLoader {

    private ClassLoader parentClassLoader;

    public UrlInverseClassLoader(URL[] urls) {
        super(urls);
    }

    public UrlInverseClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        parentClassLoader = parent;
    }

    public UrlInverseClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
        parentClassLoader = parent;
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, true);
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class c = findLoadedClass(name);
        if (c == null) {
            try {
                c = findClass(name);
            } catch (NoClassDefFoundError ncdfe) {
                getParent().loadClass(name);
            } catch (ClassNotFoundException e) {
                getParent().loadClass(name);
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }
}
