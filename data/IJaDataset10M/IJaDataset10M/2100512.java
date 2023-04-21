package com.cube42.util.testing;

import java.io.File;
import junit.framework.TestCase;

/**
 * Standard unit test framework for cube42
 *
 * @author Matt Paulin
 * @version $Id: Cube42UnitTestCase.java,v 1.6 2003/03/12 00:27:49 zer0wing Exp $
 */
public class Cube42UnitTestCase extends TestCase {

    /**
     * Directory to store all testing files in
     */
    public static final String TEST_DIRECTORY_ROOT = "testFiles";

    /**
     * Constructs the Cube42TestCase
     *
     * @param   testname   The testname in the unit test
     */
    public Cube42UnitTestCase(String testname) {
        super(testname);
    }

    /**
     * Returns the directory where the unit test should be storing its
     * extra files
     *
     * @param   testClass       The test class running the test
     * @param   deleteOnExit    If true it deletes the directory when the
     *                          test is done
     * @return  Directory where the unit test should store its extra files
     */
    public static File getTestDirectory(Cube42UnitTestCase testClass, boolean deleteOnExit) {
        String name = testClass.getClass().getName();
        name = name.substring(name.lastIndexOf(".") + 1, name.length());
        File mainTestDir = new File(getPath(), TEST_DIRECTORY_ROOT);
        File testDir = new File(mainTestDir, name);
        testDir.mkdirs();
        if (deleteOnExit) {
            testDir.deleteOnExit();
        }
        return testDir;
    }

    /**
     * Utility method for getting the classpath so the entry files created
     * will be put in the correct spot
     */
    public static final String getPath() {
        return ClassLoader.getSystemResource(".").getFile().toString();
    }

    /**
     * Asserts that two classes are not equal
     *
     * @param   object1 The first object
     * @param   object2 The second object
     */
    public void assertNotEquals(Object object1, Object object2) {
        assertTrue(!object1.equals(object2));
    }

    /**
     * Asserts that two doubles are equal
     *
     * @param   d1  The expected double
     * @param   d2  The provided double
     * @param   precision   an integer representing the number of decimal
     *                      places in the precision of the check
     */
    public void assertEquals(double d1, double d2, int precision) {
        for (int i = 0; i < precision; i++) {
            d1 = d1 * 10;
            d2 = d2 * 10;
        }
        int id1 = (int) d1;
        int id2 = (int) d2;
        if (id1 != id2) {
            fail("The double " + d1 + " does not match the expected value " + d2 + " to the " + precision + " decimal place");
        }
    }
}
