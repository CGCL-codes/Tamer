package be.vds.jtbdive.client.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassPathManipulator {

    public static void main(String[] args) {
        List<String> files = getResourcesMatching(".properties");
        for (String string : files) {
            System.out.println(string);
        }
    }

    public static List<String> getResourcesMatching(String suffix) {
        String[] jars = System.getProperty("java.class.path").split(String.valueOf(File.pathSeparatorChar));
        List<String> f = new ArrayList<String>();
        for (String jarUrl : jars) {
            try {
                if (jarUrl.endsWith(".jar")) {
                    f.addAll(getResourcesInJarMatching(new JarFile(jarUrl), suffix));
                } else {
                    f.addAll(getResourcesInDirectoryMatching(new File(jarUrl), suffix));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    private static Collection<String> getResourcesInDirectoryMatching(File directory, String suffix) {
        List<String> classes = new ArrayList<String>();
        if (directory.listFiles() == null) return classes;
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                classes.addAll(getResourcesInDirectoryMatching(file, suffix));
            } else if (file.getAbsolutePath().endsWith(suffix)) {
                classes.add(file.getAbsolutePath());
            }
        }
        return classes;
    }

    private static Collection<String> getResourcesInJarMatching(JarFile jarFile, String suffix) {
        List<String> f = new ArrayList<String>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) entries.nextElement();
            if (jarEntry.getName().endsWith(suffix)) {
                f.add(jarEntry.getName());
            }
        }
        return f;
    }

    private static List<Object> getClassPathObject() {
        String[] jars = System.getProperty("java.class.path").split(String.valueOf(File.pathSeparatorChar));
        List<Object> f = new ArrayList<Object>();
        for (String jarUrl : jars) {
            try {
                if (jarUrl.endsWith(".jar")) {
                    f.add(new JarFile(jarUrl));
                } else {
                    f.add(new File(jarUrl));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    public static List<Class> getAllClasses() {
        List<Class> classes = new ArrayList<Class>();
        List<Object> obj = getClassPathObject();
        for (Object object : obj) {
            if (object instanceof File && ((File) object).isDirectory()) {
                File directory = ((File) object);
                classes.addAll(getClassesFromDirectory(directory, directory));
            } else if (object instanceof JarFile) {
                JarFile directory = ((JarFile) object);
                classes.addAll(getClassesFromJarFile(directory));
            }
        }
        return classes;
    }

    private static Collection<? extends Class> getClassesFromJarFile(JarFile jarFile) {
        List<Class> classes = new ArrayList<Class>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) entries.nextElement();
            if (jarEntry.getName().endsWith(".class")) {
                String className = jarEntry.getName().replaceAll(".class", "").replace(File.separatorChar, '.');
                try {
                    Class c = Class.forName(className);
                    classes.add(c);
                } catch (NoClassDefFoundError e) {
                    System.err.println(e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.err.println(e.getMessage());
                } catch (UnsatisfiedLinkError e) {
                    System.err.println(e.getMessage());
                } catch (IllegalAccessError e) {
                    System.err.println(e.getMessage());
                } catch (Error e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        return classes;
    }

    private static List<Class> getClassesFromDirectory(File directory, File root) {
        List<Class> classes = new ArrayList<Class>();
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                classes.addAll(getClassesFromDirectory(file, root));
            } else if (file.getAbsolutePath().endsWith(".class")) {
                try {
                    String className = file.getAbsolutePath().replaceAll(root.getAbsolutePath() + File.separatorChar, "").replaceAll(".class", "").replace(File.separatorChar, '.');
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }
}
