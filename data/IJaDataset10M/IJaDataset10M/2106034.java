package org.jproxy.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

class FakeURLClassLoader extends URLClassLoader {

    private final List<Class<?>> classList;

    FakeURLClassLoader(ClassLoader parent) {
        super(new URL[] {}, parent);
        classList = new ArrayList<Class<?>>();
    }

    final List<Class<?>> getClassList() {
        return classList;
    }

    final void addURL(URL url, String className) {
        super.addURL(url);
        try {
            Class<?> clazz = loadClass(className);
            classList.add(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
