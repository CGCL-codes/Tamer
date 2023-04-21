package org.jtools.classloader.adapter;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.jtools.classloader.ClAction;
import org.jtools.classloader.ClContext;
import org.jtools.classloader.construct.ClCreateModifyContext;

/**
 * A ClAdapter for a java.net.URLClassLoader
 */
public class URLClassLoaderAdapter extends SimpleClassLoaderAdapter {

    /**
     * Appends a classpath to an existing classloader instance.
     *
     * @param task
     *            the calling ClassloaderBase-task.
     * @param classloader
     *            the classloader instance to append the path to.
     * @return The ClassLoader instance or null if an error occured.
     */
    @Override
    public boolean appendClasspath(ClCreateModifyContext task, ClassLoader classloader) {
        URLClassLoader ucl = (URLClassLoader) classloader;
        String loaderId = task.getLoaderName();
        Method meth;
        try {
            meth = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            meth.setAccessible(true);
        } catch (SecurityException e1) {
            task.handleError("unable to setAccessible(true) for method addURL", e1);
            return false;
        } catch (NoSuchMethodException e1) {
            task.handleError("method addURL not found", e1);
            return false;
        }
        Set<String> localEntries = new HashSet<String>();
        String[] list = task.getClasspathURLs();
        for (int i = 0; i < list.length; i++) {
            try {
                URL url = task.getURLUtil().createURL(list[i]);
                String sUrl = url.toString();
                if (localEntries.add(sUrl) && task.handleClasspathEntry(ucl, sUrl)) {
                    meth.invoke(ucl, new Object[] { url });
                    task.handleDebug("URLClassLoader " + loaderId + ": adding path " + url);
                }
            } catch (MalformedURLException e) {
                task.handleError("createURL(\"" + list[i] + "\")", e);
            } catch (Exception e) {
                task.handleError("unable to invoke URLClassLoader.addURL(url)", e);
                return false;
            }
        }
        return true;
    }

    /**
     * returns the actual classpath of a classloader instance.
     *
     * @param task
     *            the calling ClassloaderBase-task.
     * @param classloader
     *            the classloader instance to get the path from.
     * @param defaultToFile
     *            if true, returned url-elements with file protocol should trim
     *            the leading 'file:/' prefix.
     * @return the path or null if an error occured
     */
    @Override
    public String[] getClasspath(ClContext task, ClassLoader classloader, boolean defaultToFile) {
        URL[] urls = ((URLClassLoader) classloader).getURLs();
        String[] result = new String[urls.length];
        for (int i = 0; i < urls.length; i++) {
            if (defaultToFile && ("file".equals(urls[i].getProtocol()))) {
                result[i] = task.getURLUtil().createFile(urls[i].toString()).toString();
            } else {
                result[i] = urls[i].toString();
            }
        }
        return result;
    }

    /**
     * Checks whether the adapter supports an action.
     *
     * @param action
     *            the action to check.
     * @return true, if action is supported.
     */
    @Override
    public boolean isSupported(ClAction action) {
        return true;
    }

    /**
     * creates a new ClassLoader instance.
     *
     * @param task
     *            the calling classloader task.
     * @return the newly created ClassLoader or null if an error occurs.
     */
    @Override
    protected ClassLoader newClassLoader(ClCreateModifyContext task) {
        ClassLoader parent = task.getParentLoader();
        String loaderId = task.getLoaderName();
        String[] scp = task.getClasspathURLs();
        ArrayList<URL> ucp = new ArrayList<URL>(scp.length);
        Set<String> localEntries = new HashSet<String>();
        for (int i = 0; i < scp.length; i++) {
            try {
                URL url = task.getURLUtil().createURL(scp[i]);
                String sUrl = url.toString();
                if (localEntries.add(sUrl) && task.handleClasspathEntry(parent, sUrl)) {
                    ucp.add(url);
                }
            } catch (Exception e) {
                task.handleError("createURL(\"" + scp[i] + "\")", e);
            }
        }
        URL[] urls = ucp.toArray(new URL[ucp.size()]);
        URLClassLoader cl = new URLClassLoader(urls, parent);
        task.handleDebug("URLClassLoader " + loaderId + " created.");
        for (int i = 0; i < urls.length; i++) {
            task.handleDebug("URLClassLoader " + loaderId + ": adding path " + urls[i]);
        }
        if (parent != null) {
            task.handleDebug("URLClassLoader " + loaderId + ": setting parent loader " + parent);
        }
        return cl;
    }
}
