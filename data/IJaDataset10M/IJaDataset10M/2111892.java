package org.jfree.base;

import java.util.Enumeration;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 02.11.2007, 18:42:27
 *
 * @author Thomas Morgner
 */
public class ClassPathDebugger {

    /**
   * Entry point.
   *
   * @param args  command line arguments.
   */
    public static void main(String[] args) {
        System.out.println("Listing the various classloaders:");
        System.out.println("Defined classloader source: " + ObjectUtilities.getClassLoaderSource());
        System.out.println("User classloader: " + ObjectUtilities.getClassLoader());
        System.out.println("Classloader for ObjectUtilities.class: " + ObjectUtilities.getClassLoader(ObjectUtilities.class));
        System.out.println("Classloader for String.class: " + ObjectUtilities.getClassLoader(String.class));
        System.out.println("Thread-Context Classloader: " + Thread.currentThread().getContextClassLoader());
        System.out.println("Defined System classloader: " + ClassLoader.getSystemClassLoader());
        System.out.println();
        try {
            System.out.println("Listing sources for '/jcommon.properties':");
            Enumeration resources = ObjectUtilities.getClassLoader(ObjectUtilities.class).getResources("jcommon.properties");
            while (resources.hasMoreElements()) {
                System.out.println(" " + resources.nextElement());
            }
            System.out.println();
            System.out.println("Listing sources for 'org/jfree/JCommonInfo.class':");
            resources = ObjectUtilities.getClassLoader(ObjectUtilities.class).getResources("org/jfree/JCommonInfo.class");
            while (resources.hasMoreElements()) {
                System.out.println(" " + resources.nextElement());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
