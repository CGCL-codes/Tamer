package org.apache.harmony.sql.tests.java.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TestHelper_ClassLoader extends ClassLoader {

    public TestHelper_ClassLoader() {
        super(null);
    }

    /**
     * Loads a class specified by its name
     * <p>
     * This classloader makes the assumption that any class it is asked to load
     * is in the current directory....
     */
    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        Class<?> theClass = null;
        if (!className.equals("org.apache.harmony.sql.tests.java.sql.TestHelper_DriverManager")) {
            return null;
        }
        String classNameAsFile = className.replace('.', '/') + ".class";
        String classPath = System.getProperty("java.class.path");
        String theSeparator = String.valueOf(File.pathSeparatorChar);
        String[] theClassPaths = classPath.split(theSeparator);
        for (int i = 0; (i < theClassPaths.length) && (theClass == null); i++) {
            if (theClassPaths[i].endsWith(".jar")) {
                theClass = loadClassFromJar(theClassPaths[i], className, classNameAsFile);
            } else {
                theClass = loadClassFromFile(theClassPaths[i], className, classNameAsFile);
            }
        }
        return theClass;
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        String[] disallowedClasses = { "org.apache.harmony.sql.tests.java.sql.TestHelper_Driver1", "org.apache.harmony.sql.tests.java.sql.TestHelper_Driver2", "org.apache.harmony.sql.tests.java.sql.TestHelper_Driver4", "org.apache.harmony.sql.tests.java.sql.TestHelper_Driver5" };
        Class<?> theClass;
        theClass = findLoadedClass(className);
        if (theClass != null) {
            return theClass;
        }
        theClass = this.findClass(className);
        if (theClass == null) {
            for (String element : disallowedClasses) {
                if (element.equals(className)) {
                    return null;
                }
            }
            theClass = Class.forName(className);
        }
        return theClass;
    }

    private Class<?> loadClassFromFile(String pathName, String className, String classNameAsFile) {
        Class<?> theClass = null;
        FileInputStream theInput = null;
        File theFile = null;
        try {
            theFile = new File(pathName, classNameAsFile);
            if (theFile.exists()) {
                int length = (int) theFile.length();
                theInput = new FileInputStream(theFile);
                byte[] theBytes = new byte[length + 100];
                int dataRead = 0;
                while (dataRead < length) {
                    int count = theInput.read(theBytes, dataRead, theBytes.length - dataRead);
                    if (count == -1) {
                        break;
                    }
                    dataRead += count;
                }
                if (dataRead > 0) {
                    theClass = this.defineClass(className, theBytes, 0, dataRead);
                    ClassLoader testClassLoader = theClass.getClassLoader();
                    if (testClassLoader != this) {
                        System.out.println("findClass - wrong classloader!!");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("findClass - exception reading class file.");
            e.printStackTrace();
        } finally {
            try {
                if (theInput != null) {
                    theInput.close();
                }
            } catch (Exception e) {
            }
        }
        return theClass;
    }

    private Class<?> loadClassFromJar(String jarfileName, String className, String classNameAsFile) {
        Class<?> theClass = null;
        JarFile theJar = null;
        try {
            theJar = new JarFile(jarfileName);
            JarEntry theEntry = theJar.getJarEntry(classNameAsFile);
            if (theEntry == null) {
                return theClass;
            }
            theEntry.getMethod();
            InputStream theStream = theJar.getInputStream(theEntry);
            long size = theEntry.getSize();
            if (size < 0) {
                size = 100000;
            }
            byte[] theBytes = new byte[(int) size + 100];
            int dataRead = 0;
            while (dataRead < size) {
                int count = theStream.read(theBytes, dataRead, theBytes.length - dataRead);
                if (count == -1) {
                    break;
                }
                dataRead += count;
            }
            if (dataRead > 0) {
                theClass = this.defineClass(className, theBytes, 0, dataRead);
                ClassLoader testClassLoader = theClass.getClassLoader();
                if (testClassLoader != this) {
                    System.out.println("findClass - wrong classloader!!");
                } else {
                    System.out.println("Testclassloader loaded class from jar: " + className);
                }
            }
        } catch (IOException ie) {
            System.out.println("TestHelper_ClassLoader: IOException opening Jar " + jarfileName);
        } catch (Exception e) {
            System.out.println("TestHelper_ClassLoader: Exception loading class from Jar ");
        } catch (ClassFormatError ce) {
            System.out.println("TestHelper_ClassLoader: ClassFormatException loading class from Jar ");
        } finally {
            try {
                if (theJar != null) {
                    theJar.close();
                }
            } catch (Exception e) {
            }
        }
        return theClass;
    }
}
