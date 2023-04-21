package de.mogwai.smartstart;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class SmartStart {

    protected static void addJarsTo(File aBaseFile, List<URL> aURL) throws MalformedURLException {
        File theFiles[] = aBaseFile.listFiles(new FileFilter() {

            public boolean accept(File aPathName) {
                return aPathName.getName().toLowerCase().endsWith(".jar");
            }
        });
        for (File theFile : theFiles) {
            aURL.add(theFile.toURL());
        }
    }

    public static void main(String aArgs[]) throws MalformedURLException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (aArgs.length < 1) {
            System.out.println("Usage <MainClass> <Classdir1> <Clasdir2>...");
            System.exit(0);
        }
        File theBaseFile = new File(".");
        List<URL> theURLList = new ArrayList<URL>();
        String theMainClassName = aArgs[0];
        System.out.println("Mogwai SmartStart");
        System.out.println("=================================================");
        System.out.println("Base directory is " + theBaseFile);
        System.out.println("Main class is " + theMainClassName);
        if (aArgs.length > 1) {
            for (int i = 1; i < aArgs.length; i++) {
                File theLibFile = new File(theBaseFile, aArgs[i]);
                addJarsTo(theLibFile, theURLList);
                System.out.println("Adding jars in subdirectory " + aArgs[i]);
            }
        }
        System.out.println("Constructing new classloader");
        ClassLoader theOriginalClassloader = Thread.currentThread().getContextClassLoader();
        URLClassLoader theClassLoader = new URLClassLoader(theURLList.toArray(new URL[0]), theOriginalClassloader);
        Thread.currentThread().setContextClassLoader(theClassLoader);
        System.out.println("Invoking main class");
        Class theMainClass = Class.forName(theMainClassName, true, theClassLoader);
        Method theMethod = theMainClass.getMethod("main", new Class[] { aArgs.getClass() });
        theMethod.invoke(theMainClass, new Object[] { aArgs });
    }
}
