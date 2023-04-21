package com.j2biz.compote.installer;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * @deprecated will be no more used.
 *  
 */
public class PluginInstallClassLoader extends URLClassLoader {

    private final ClassLoader parent;

    private URL[] urls;

    /**
     * @param urls
     * @param parent
     */
    public PluginInstallClassLoader(URL[] urls, ClassLoader parent) {
        super(urls);
        if (urls == null) {
            throw new NullPointerException("parameter:urls");
        }
        if (parent == null) {
            throw new NullPointerException("parameter:parent");
        }
        this.urls = urls;
        this.parent = parent;
    }

    protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (isSystemClass(name)) {
            try {
                return this.parent.loadClass(name);
            } catch (ClassNotFoundException x) {
                ;
            }
        }
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException x) {
            ;
        }
        return this.parent.loadClass(name);
    }

    /**
     * @param name
     * @return
     */
    private boolean isSystemClass(String name) {
        return (name.startsWith("java.") || name.startsWith("javax."));
    }

    public URL getResource(String name) {
        URL res = searchForResource(name);
        if (res != null) return res; else return super.getResource(name);
    }

    /**
     * @param name
     * @return
     */
    private URL searchForResource(String name) {
        for (int i = 0; i < urls.length; i++) {
            String eForm = urls[i].toExternalForm();
            if (eForm.endsWith(name)) return urls[i]; else if (eForm.substring(0, eForm.lastIndexOf(".")).endsWith(name)) {
                return urls[i];
            }
        }
        return null;
    }
}
