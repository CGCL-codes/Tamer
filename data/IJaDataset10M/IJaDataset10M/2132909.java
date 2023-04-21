package gunit.framework;

import java.io.*;
import java.util.*;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <code>TestFactory</code> provides methods to create <code>Test</code>
 */
public class TestFactory {

    public static Test createTest(Class[] testClasses) {
        TestSuite suite = new TestSuite();
        for (int i = 0; i < testClasses.length; i++) {
            suite.addTest(new TestSuite(testClasses[i]));
        }
        return suite;
    }

    public static Test createTest(String fileName) {
        try {
            return createTest(new FileInputStream(fileName));
        } catch (Exception ex) {
            return new TestSuite();
        }
    }

    public static Test createTest(InputStream stream) {
        if (!(stream instanceof BufferedInputStream)) {
            stream = new BufferedInputStream(stream);
        }
        List classes = new ArrayList();
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            while ((line = reader.readLine()) != null) {
                try {
                    classes.add(Class.forName(line.trim()));
                } catch (Exception ex) {
                    System.out.println("Unable to create " + line);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                stream.close();
            } catch (Exception ex) {
            }
        }
        return createTest((Class[]) classes.toArray(new Class[0]));
    }
}
