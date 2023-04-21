package beantools;

import java.io.File;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

class ClassFilter {

    private final String include;

    private final String exclude;

    private final Class[] annotations;

    ClassFilter(String include, String exclude, Class[] annotations) {
        this.include = include;
        this.exclude = exclude;
        this.annotations = annotations;
    }

    public Class<?> visitEntry(String relativePath, File file) throws ClassNotFoundException {
        if (!relativePath.endsWith(".class")) return null;
        if (relativePath.contains("$")) return null;
        String className = fileNameToClassName(relativePath);
        if (!className.matches(include)) return null;
        if (className.matches(exclude)) return null;
        System.out.print(".");
        return processClass(className, relativePath);
    }

    private Class<?> processClass(String className, String entryName) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        if (clazz == null) return null;
        if (hasOneOfAnnotations(clazz, annotations)) return clazz;
        for (Method method : clazz.getMethods()) {
            if (hasOneOfAnnotations(method, annotations)) return clazz;
        }
        return null;
    }

    private String fileNameToClassName(String entryName) {
        return entryName.replaceAll("/|\\\\", ".").replace(".class", "");
    }

    public boolean hasOneOfAnnotations(AnnotatedElement klazz, Class[] annotations) {
        for (Class a : annotations) {
            if (klazz.isAnnotationPresent(a)) return true;
        }
        return false;
    }
}
