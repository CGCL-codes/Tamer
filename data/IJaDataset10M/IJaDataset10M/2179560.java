package pl.otros.logview.loader;

import java.io.*;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Logger;

public class BaseLoader {

    public static final Logger LOGGER = Logger.getLogger(BaseLoader.class.getName());

    /**
   * Load classes (interface implementation) from directory.
   * 
   * @param <T>
   * @param dir
   *          dir with classes, zip or jar's
   * @param type
   *          interface to load
   * @return
   */
    public <T> Collection<T> load(File dir, Class<T> type) {
        Set<T> logImporters = new HashSet<T>();
        if (!dir.exists()) {
            return new ArrayList<T>();
        }
        File[] files = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".jar") || pathname.getName().endsWith(".zip")) {
                    return true;
                }
                return false;
            }
        });
        logImporters.addAll(loadFromDir(dir, type));
        for (int i = 0; i < files.length; i++) {
            logImporters.addAll(loadFromJar(files[i], type));
        }
        return logImporters;
    }

    public <T> Collection<T> loadFromDir(File file, Class<T> type) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            List<Class<T>> implementationClasses = getInterfaceImplementations(type, file);
            for (Class<?> class1 : implementationClasses) {
                try {
                    T classInstance = (T) class1.newInstance();
                    list.add(classInstance);
                } catch (Throwable e) {
                    LOGGER.severe(String.format("Error creating class %s from file %s: %s", class1.getName(), file, e.getMessage()));
                }
            }
        } catch (Throwable e) {
            LOGGER.severe(String.format("Error loading class type %s from file %s: %s", type, file, e.getMessage()));
        }
        return list;
    }

    public <T> Collection<T> loadFromJar(File file, Class<T> type) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            List<Class<T>> implementationClasses = getInterfaceImplementations(type, file);
            for (Class<?> class1 : implementationClasses) {
                try {
                    T am = (T) class1.newInstance();
                    list.add(am);
                } catch (Throwable e) {
                    LOGGER.severe(String.format("Error creating class %s from file %s: %s", class1.getName(), file, e.getMessage()));
                }
            }
        } catch (Throwable e) {
            LOGGER.severe(String.format("Error loading class type %s from file %s: %s", type, file, e.getMessage()));
        }
        return list;
    }

    public <T> List<Class<T>> getInterfaceImplementations(Class<T> interfaceClass, File f) throws FileNotFoundException, IOException, ClassNotFoundException {
        ArrayList<Class<T>> list = new ArrayList<Class<T>>();
        List<String> classes = null;
        if (f.isDirectory()) {
            classes = getClassesFromDir(f);
        } else {
            classes = getClassesFromJar(f);
        }
        URL url = f.toURI().toURL();
        ClassLoader cl = new URLClassLoader(new URL[] { url }, this.getClass().getClassLoader());
        for (String klazz : classes) {
            try {
                Class<?> c = cl.loadClass(klazz);
                if (interfaceClass.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
                    list.add((Class<T>) c);
                }
            } catch (Throwable t) {
                LOGGER.warning(String.format("Error checking if class %s from file %s is implementing %s: %s", klazz, f, interfaceClass.getName(), t.getMessage()));
            }
        }
        return list;
    }

    public List<String> getClassesFromDir(File dir) throws FileNotFoundException, IOException {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<File> fileList = new ArrayList<File>();
        findClassesFiles(dir, fileList);
        int length = dir.getAbsolutePath().length();
        for (File file : fileList) {
            String classPath = file.getAbsolutePath().substring(length + 1).replace(".class", "").replace(File.separatorChar, '.');
            list.add(classPath);
        }
        return list;
    }

    private void findClassesFiles(File dir, ArrayList<File> list) {
        File[] listFiles = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().endsWith("class");
            }
        });
        for (File file : listFiles) {
            if (file.isDirectory()) {
                findClassesFiles(file, list);
            } else {
                list.add(file);
            }
        }
    }

    public List<String> getClassesFromJar(File jarFile) throws FileNotFoundException, IOException {
        ArrayList<String> list = new ArrayList<String>();
        JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarFile));
        JarEntry jarEntry;
        while (true) {
            jarEntry = jarInputStream.getNextJarEntry();
            if (null == jarEntry) {
                break;
            }
            String s = jarEntry.getName();
            if (s.endsWith(".class")) {
                list.add(s.replace('/', '.').substring(0, s.length() - 6));
            }
        }
        return list;
    }
}
