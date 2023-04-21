package nl.kbna.dioscuri.module.cpu32;

import java.io.*;
import java.util.*;

public class ClassFileBuilder {

    private static final int CLASSES_PER_LOADER = 10;

    private static final InputStream realModeSkeleton, protectedModeSkeleton;

    private static CustomClassLoader currentClassLoader;

    static {
        realModeSkeleton = loadSkeletonClass(RealModeSkeletonBlock.class);
        protectedModeSkeleton = loadSkeletonClass(ProtectedModeSkeletonBlock.class);
        newClassLoader();
    }

    private static InputStream loadSkeletonClass(Class clz) {
        byte[] classBytes = null;
        String classRes = clz.getName().replace('.', '/') + ".class";
        try {
            ClassLoader cl = ClassFileBuilder.class.getClassLoader();
            if (cl == null) System.err.println("ClassFileBuilder failed to get the classloader.");
            InputStream in = cl.getResourceAsStream(classRes);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            while (true) {
                int ch = in.read();
                if (ch < 0) break;
                bout.write((byte) ch);
            }
            classBytes = bout.toByteArray();
        } catch (Exception e) {
            System.out.println("Exception reading in skeleton class " + clz + " into ClassFileBuilder: " + e);
        }
        return new ByteArrayInputStream(classBytes);
    }

    private ClassFileBuilder() {
    }

    public static ClassFile createNewRealModeSkeletonClass() {
        ClassFile cf = new ClassFile();
        try {
            realModeSkeleton.reset();
            DataInputStream dis = new DataInputStream(realModeSkeleton);
            cf.read(dis);
        } catch (IOException e) {
            System.out.println("read error: " + e);
        }
        return cf;
    }

    public static ClassFile createNewProtectedModeSkeletonClass() {
        ClassFile cf = new ClassFile();
        try {
            protectedModeSkeleton.reset();
            DataInputStream dis = new DataInputStream(protectedModeSkeleton);
            cf.read(dis);
        } catch (IOException e) {
            System.out.println("read error: " + e);
        }
        return cf;
    }

    public static CodeBlock instantiateClass(ClassFile cf) {
        cf.update();
        String className = cf.getClassName();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            cf.write(new DataOutputStream(bos));
        } catch (IOException e) {
            System.out.println("write error: " + e);
        } catch (Exception e) {
            System.out.println("cb ic error: " + e);
        }
        byte[] classBytes = bos.toByteArray();
        Class codeBlockClass = currentClassLoader.createClass(className, classBytes, 0, classBytes.length);
        CodeBlock compiledBlock = null;
        try {
            compiledBlock = (CodeBlock) codeBlockClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException("Could not instantiate class", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not instantiate class", e);
        }
        return compiledBlock;
    }

    private static void newClassLoader() {
        currentClassLoader = new CustomClassLoader();
    }

    static class CustomClassLoader extends ClassLoader {

        private Hashtable classes;

        private int classesCount;

        public CustomClassLoader() {
            super(ClassFileBuilder.class.getClassLoader());
            classes = new Hashtable();
        }

        public Class createClass(String name, byte[] b, int off, int len) {
            if (++classesCount == CLASSES_PER_LOADER) newClassLoader();
            Class newClass = defineClass(name, b, off, len);
            classes.put(name, newClass);
            return newClass;
        }

        public Class findClass(String name) throws ClassNotFoundException {
            Class myClass = (Class) classes.get(name);
            if (myClass != null) return myClass; else throw new ClassNotFoundException(name);
        }
    }
}
