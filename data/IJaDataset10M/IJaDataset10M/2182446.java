package de.miethxml.toolkit.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 * 
 * 
 * 
 * 
 * 
 * 
 * @deprecated
 */
public class CustomClassLoader extends URLClassLoader {

    Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(this.getClass().getName());

    /**
	 * @param urls
	 * 
	 */
    private ClassLoader parent;

    private ArrayList nativeLibraryPath = new ArrayList();

    public CustomClassLoader() {
        super(new URL[] {});
        parent = this.getClass().getClassLoader();
    }

    public CustomClassLoader(ClassLoader parent) {
        this();
        this.parent = parent;
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }

    public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (name.startsWith("java.") || name.startsWith("javax.swing.")) {
            return parent.loadClass(name);
        }
        Class clazz = findLoadedClass(name);
        if (clazz == null) {
            try {
                clazz = super.findClass(name);
            } catch (Exception e) {
                return parent.loadClass(name);
            } catch (Error er) {
                return parent.loadClass(name);
            }
        }
        if (resolve) {
            resolveClass(clazz);
        }
        return clazz;
    }

    public void addJarLibrary(String lib) {
        File libfile = new File(lib);
        if (libfile.isFile()) {
            try {
                URL url = libfile.toURI().toURL();
                super.addURL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addJarDirectory(String dir) {
        File libDir = new File(dir);
        if (libDir.isDirectory()) {
            File[] files = libDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".jar")) {
                    try {
                        URL url = files[i].toURI().toURL();
                        super.addURL(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void addResourceDirectory(String dir) {
        File libDir = new File(dir);
        if (libDir.isDirectory()) {
            try {
                URL url = libDir.toURI().toURL();
                super.addURL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addResource(String resource) {
        File lib = new File(resource);
        if (lib.exists()) {
            try {
                URL url = lib.toURI().toURL();
                super.addURL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public String findLibrary(String name) {
        String libname = System.mapLibraryName(name);
        Iterator i = nativeLibraryPath.iterator();
        while (i.hasNext()) {
            String path = (String) i.next();
            File f = new File(path + File.separator + libname);
            if (f.exists() && f.isFile()) {
                return f.getAbsolutePath();
            }
        }
        return super.findLibrary(name);
    }

    public void addNativeLibraryPath(String path) {
        File p = new File(path);
        if (p.exists() && p.isDirectory()) {
            nativeLibraryPath.add(p.getAbsolutePath());
        }
    }

    public synchronized void addClassDirectory(String path) {
        File libDir = new File(path);
        if (libDir.isDirectory()) {
            try {
                URL url = libDir.toURI().toURL();
                super.addURL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * add single jar-file or directory with jarfiles or a classes-directory to
	 * classpath
	 * 
	 * @param path
	 */
    public synchronized void addPathElement(String path) {
        File f = new File(path);
        if (f.isFile()) {
            addJarLibrary(path);
            return;
        } else if (f.isDirectory()) {
            addClassDirectory(path);
            addJarDirectory(path);
        }
    }
}
