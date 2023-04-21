package org.jaqlib;

import junit.framework.Test;
import junit.framework.TestSuite;

public class IterableQBClassTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.jaqlib");
        suite.addTestSuite(IterableQBClassTest.class);
        suite.addTestSuite(CustomConditonClassTest.class);
        suite.addTestSuite(ReflectiveConditionClassTest.class);
        return suite;
    }
}
