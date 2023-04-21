package com.ibm.wala.dila.tests.data.unittestcases;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class RepeatedTests1 extends TestCase {

    private int _count;

    public static void main(String[] args) {
        TestRunner.run(RepeatedTests1.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new RepeatedTests1("testCallSame", 1));
        suite.addTest(new RepeatedTests1("testCallSame", 2));
        suite.addTest(new RepeatedTests1("testCallSame", 3));
        return suite;
    }

    public RepeatedTests1(String test, int count) {
        super(test);
        _count = count;
    }

    public void testCallSame() throws Exception {
        assertTrue(_count > 0);
    }
}
