package org.apache.tools.ant.taskdefs.optional.junit;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * validates that the suite() method works in classes that don't
 * implement Test.
 */
public class SuiteMethodTest {

    public static Test suite() {
        return new Nested("testMethod");
    }

    public static class Nested extends TestCase {

        public Nested(String name) {
            super(name);
        }

        public void testMethod() {
            assertTrue(true);
        }
    }
}
