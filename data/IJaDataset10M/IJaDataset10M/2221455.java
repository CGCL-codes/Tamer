package org.openejb.test;

/**
 * @author <a href="mailto:david.blevins@visi.com">David Blevins</a>
 */
public class Main {

    public static void main(String args[]) {
        try {
            org.openejb.util.ClasspathUtils.addJarsToPath("lib");
            org.openejb.util.ClasspathUtils.addJarsToPath("dist");
        } catch (Exception e) {
            e.printStackTrace();
        }
        TestRunner.main(args);
    }
}
