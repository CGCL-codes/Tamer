package com.tutego.insel.tools;

import java.util.*;

public class MemClassLoader extends ClassLoader {

    private final Map<String, MemJavaFileObject> classFiles = new HashMap<String, MemJavaFileObject>();

    public MemClassLoader() {
        super(ClassLoader.getSystemClassLoader());
    }

    public void addClassFile(final MemJavaFileObject memJavaFileObject) {
        this.classFiles.put(memJavaFileObject.getClassName(), memJavaFileObject);
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        Class<?> parent = super.findClass(name);
        if (parent != null) {
            return parent;
        }
        final byte[] bytes = getClassAsBytes(name);
        return defineClass(name, bytes, 0, bytes.length);
    }

    public byte[] getClassAsBytes(final String name) {
        final MemJavaFileObject fileObject = this.classFiles.get(name);
        return fileObject == null ? null : fileObject.getClassBytes();
    }
}
