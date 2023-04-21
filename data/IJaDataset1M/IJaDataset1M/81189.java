package ru.ipo.dces.utils;

import java.util.List;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: I. Posov
 * Date: 11.05.2009
 * Time: 17:42:41
 */
class PluginClassLoader extends ClassLoader {

    private HashMap<String, Class<?>> classes = new HashMap<String, Class<?>>();

    private List<ClassLoader> classLoaders;

    public PluginClassLoader(List<ClassLoader> classLoaders) {
        this.classLoaders = classLoaders;
    }

    private Class<?> loadClassFromClassLoaders(String className) {
        for (ClassLoader classLoader : classLoaders) try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }

    public Class loadClass(String className) throws ClassNotFoundException {
        return (loadClass(className, true));
    }

    public synchronized Class loadClass(String className, boolean resolveIt) throws ClassNotFoundException {
        Class result = classes.get(className);
        if (result != null) return result;
        try {
            result = super.findSystemClass(className);
            return result;
        } catch (ClassNotFoundException ignored) {
        }
        result = loadClassFromClassLoaders(className);
        if (result == null) throw new ClassNotFoundException("class not found: " + className);
        if (resolveIt) resolveClass(result);
        classes.put(className, result);
        return result;
    }
}
