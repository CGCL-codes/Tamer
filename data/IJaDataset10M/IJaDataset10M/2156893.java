package com.ericdaugherty.mail;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.*;

/**
 *
 * @author mfg8876
 */
public class JESTestClassLoader extends URLClassLoader {

    private static final String JAVASTR = "java.";

    private static final String JAVAXSTR = "javax.";

    private static final String SUNSTR = "sun.";

    private static final String COMSUNSTR = "com.sun.";

    private final Map<URL, Class> loadedStandAlone;

    private final Vector<URL> allowedJars;

    private final Map<String, Class> loadedFromJars;

    private final char separator = File.separatorChar;

    public JESTestClassLoader(URL[] urls) {
        super(urls, JESTestClassLoader.class.getClassLoader());
        this.loadedStandAlone = new HashMap();
        this.allowedJars = new Vector();
        this.loadedFromJars = new ConcurrentHashMap(100, 0.75f, 12);
        for (int i = 0; i < urls.length; i++) {
            if (urls[i].getFile().toLowerCase().endsWith("class")) {
                loadedStandAlone.put(urls[i], (Class) null);
            } else {
                allowedJars.add(urls[i]);
            }
        }
    }

    @Override
    public final Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, true);
    }

    @Override
    protected final synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class c = null;
        if (name.startsWith(JAVASTR) || name.startsWith(JAVAXSTR) || name.startsWith(SUNSTR) || name.startsWith(COMSUNSTR)) {
            c = findSystemClass(name);
        } else {
            c = findLoadedClass(name);
            if (c == null) {
                try {
                    c = findClass(name);
                } catch (Exception e) {
                    try {
                        c = findStandAloneClass(name);
                    } catch (IOException e1) {
                    }
                }
                if (c == null) {
                    if (getParent() != null) {
                        c = getParent().loadClass(name);
                    } else {
                        c = findSystemClass(name);
                    }
                }
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }

    private Class<?> findStandAloneClass(String name) throws IOException {
        String fileSystemName = name.replace('.', separator) + ".class";
        Iterator<URL> iter = loadedStandAlone.keySet().iterator();
        URL url = null;
        while (iter.hasNext()) {
            url = iter.next();
            if (url.getFile().endsWith(fileSystemName)) {
                if (loadedStandAlone.get(url) != null) return loadedStandAlone.get(url);
                break;
            }
            url = null;
        }
        if (url == null) return null;
        Class clazz = null;
        byte[] classBytes;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(url.getFile()));
        try {
            byte[] b = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len = 0;
            while ((len = is.read(b)) > 0) {
                out.write(b, 0, len);
            }
            out.close();
            classBytes = out.toByteArray();
            clazz = defineClass(name, classBytes, 0, classBytes.length);
            loadedStandAlone.put(url, clazz);
            return clazz;
        } finally {
            try {
                is.close();
            } catch (IOException ioe) {
            }
        }
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        Class result = loadedFromJars.get(name);
        if (result != null) return result;
        Iterator<URL> iter = allowedJars.iterator();
        String jarName;
        Class clazz = null;
        while (iter.hasNext()) {
            jarName = iter.next().getFile();
            try {
                clazz = loadJar(jarName.substring(jarName.indexOf('/'), jarName.indexOf('!')), name);
                if (clazz != null) return clazz;
            } catch (IOException ioe) {
                System.out.println("ioe " + ioe.getMessage());
            }
        }
        return super.findClass(name);
    }

    private Class loadJar(String jarFile, String className) throws IOException {
        FileInputStream fis = new FileInputStream(jarFile);
        try {
            return loadJar(fis, className);
        } finally {
            fis.close();
        }
    }

    private Class loadJar(InputStream jarStream, String className) throws IOException {
        JarInputStream jis = null;
        Class clazz = null;
        byte[] classBytes;
        String fileSystemClassName = className.replace('.', separator) + ".class";
        try {
            jis = new JarInputStream(new BufferedInputStream(jarStream));
            JarEntry jarEntry = null;
            while ((jarEntry = jis.getNextJarEntry()) != null) {
                if (jarEntry.isDirectory()) {
                    continue;
                }
                if (jarEntry.getName().equals(fileSystemClassName)) {
                    byte[] b = new byte[1024];
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    int len = 0;
                    while ((len = jis.read(b)) > 0) {
                        out.write(b, 0, len);
                    }
                    out.close();
                    classBytes = out.toByteArray();
                    clazz = defineClass(className, classBytes, 0, classBytes.length);
                    loadedFromJars.put(className, clazz);
                    return clazz;
                }
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        } finally {
            jis.close();
        }
        return null;
    }
}
