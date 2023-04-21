package org.gjt.universe;

import java.lang.ClassLoader;
import java.io.*;
import java.util.*;
import java.util.zip.*;

public class JarLoader extends ClassLoader {

    private String jarName = null;

    private Hashtable classes = null;

    public JarLoader(String jarName) {
        super();
        this.jarName = new String(jarName);
        this.classes = new Hashtable();
    }

    public Class loadClass(String className, boolean resolveIt) throws ClassNotFoundException {
        Class c;
        String beanName;
        beanName = className.replace('.', '/') + ".class";
        if (classes.containsKey(className)) {
            return (Class) classes.get(className);
        }
        try {
            byte classData[] = readClass(beanName, jarName);
            if (classData != null) {
                c = defineClass(className, classData, 0, classData.length);
            } else {
                ClassLoader loader = getClass().getClassLoader();
                if (loader != null) {
                    c = loader.loadClass(className);
                } else {
                    c = findSystemClass(className);
                }
            }
            classes.put(className, c);
        } catch (IOException ioe) {
            Log.warning("IOException while loading class \"" + className + "\". Class not loaded.");
            throw new ClassNotFoundException("(loadClass() IOException)");
        }
        if (resolveIt) {
            resolveClass((Class) classes.get(className));
        }
        return (Class) classes.get(className);
    }

    private byte[] readClass(String className, String jarName) throws ClassNotFoundException, IOException {
        byte theClass[] = null;
        int totalBytes = 0;
        int maxBytes;
        if (jarName == null) {
            return null;
        }
        try {
            ZipFile zipFile = new ZipFile(jarName);
            ZipEntry zipEntry = zipFile.getEntry(className);
            Log.debug("loading " + className + " from " + jarName + "..");
            if (zipEntry == null) {
                return null;
            }
            byte bytes[] = new byte[(int) zipEntry.getSize()];
            InputStream inputStream = zipFile.getInputStream(zipEntry);
            maxBytes = (int) zipEntry.getSize();
            while (totalBytes < maxBytes) {
                totalBytes += inputStream.read(bytes, totalBytes, (maxBytes - totalBytes));
            }
            inputStream.close();
            theClass = bytes;
        } catch (ZipException e) {
            throw new IOException("(readClass() ZipException)");
        } catch (IOException e) {
            throw new IOException("(readClass() IOException)");
        }
        return theClass;
    }
}
