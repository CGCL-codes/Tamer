package com.traxel.lumbermill;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import com.traxel.lumbermill.event.AllEventTests;
import com.traxel.lumbermill.log.AllLogTests;
import com.traxel.lumbermill.filter.AllFilterTests;

public class AllLumbermillTests {

    public static Test suite() {
        TestSuite suite;
        suite = new TestSuite(AllLumbermillTests.class.getName());
        suite.addTest(AllEventTests.suite());
        suite.addTest(AllLogTests.suite());
        suite.addTest(AllFilterTests.suite());
        return suite;
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
}
