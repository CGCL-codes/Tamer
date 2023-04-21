package org.jproxy.classloader;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassLoaderFinder {

    private final String DOT_CLASS = ".class";

    private final String DOT_JAR = ".jar";

    public List<Class<?>> findClasses(Class<? extends Annotation> annotation) {
        List<Class<?>> classes = findClassesInClasses(annotation);
        List<Class<?>> lib = findClassesInLib(annotation);
        classes.addAll(lib);
        return Collections.unmodifiableList(classes);
    }

    private List<Class<?>> findClassesInClasses(Class<? extends Annotation> annotation) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        FakeClassLoader fakeClassLoader = loadFakeClassLoader(getPath("classes"));
        for (Class<?> clazz : fakeClassLoader.getClassList()) if (clazz.getAnnotation(annotation) != null) classList.add(loadClassInActiveClassLoader(clazz));
        return classList;
    }

    private List<Class<?>> findClassesInLib(Class<? extends Annotation> annotation) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        for (File tmp : getPath("lib").listFiles()) {
            if (!tmp.getName().endsWith(DOT_JAR)) continue;
            FakeURLClassLoader fakeURLClassLoader = loadFakeURLClassLoader(tmp);
            for (Class<?> clazz : fakeURLClassLoader.getClassList()) if (clazz.getAnnotation(annotation) != null) classList.add(loadClassInActiveClassLoader(clazz));
        }
        return classList;
    }

    private Class<?> loadClassInActiveClassLoader(Class<?> clazz) {
        try {
            return Class.forName(clazz.getName(), true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private FakeClassLoader loadFakeClassLoader(File classPath) {
        FakeClassLoader fakeClassLoader = new FakeClassLoader(Thread.currentThread().getContextClassLoader());
        addClasses(fakeClassLoader, classPath, classPath.getPath().length() + 1);
        return fakeClassLoader;
    }

    private FakeURLClassLoader loadFakeURLClassLoader(File path) {
        String jarFile = new StringBuilder("jar:file://").append(path.getPath()).append("!").append(File.separator).toString();
        FakeURLClassLoader fakeUrlClassLoader = new FakeURLClassLoader(Thread.currentThread().getContextClassLoader());
        JarFile jar;
        try {
            jar = new JarFile(path);
        } catch (IOException e1) {
            return fakeUrlClassLoader;
        }
        Enumeration<JarEntry> entrys = jar.entries();
        JarEntry entry = null;
        while (entrys.hasMoreElements()) {
            entry = entrys.nextElement();
            String relativePath = entry.toString();
            if (entry.isDirectory() || !relativePath.endsWith(DOT_CLASS)) continue;
            try {
                URL classFile = new URL(new StringBuilder(jarFile).append(relativePath).toString());
                fakeUrlClassLoader.addURL(classFile, getClassName(relativePath));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return fakeUrlClassLoader;
    }

    private String getClassName(String clazz) {
        StringBuilder path = new StringBuilder(clazz);
        int pos = -1;
        while ((pos = path.indexOf(File.separator, pos)) > -1) path.replace(pos, pos + 1, ".");
        pos = path.length();
        return path.delete(pos - DOT_CLASS.length(), pos).toString();
    }

    private File getPath(String folder) {
        StringBuilder path = new StringBuilder(System.getProperty("jproxy.classloader.path"));
        return new File(path.append(folder).toString());
    }

    private void addClasses(ClassLoader classLoader, File path, int beginRelativePath) {
        for (File tmp : path.listFiles()) {
            if (tmp.isDirectory()) addClasses(classLoader, tmp, beginRelativePath); else if (tmp.getName().endsWith(DOT_CLASS)) addClass(classLoader, tmp, beginRelativePath);
        }
    }

    private void addClass(ClassLoader classLoader, File classPath, int beginRelativePath) {
        String path = getClassName(new StringBuilder(classPath.getPath()).delete(0, beginRelativePath).toString());
        try {
            Class.forName(path.toString(), true, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
