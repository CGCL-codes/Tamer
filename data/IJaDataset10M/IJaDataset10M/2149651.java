package de.fuh.xpairtise.tests.plugin.model;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PluginModelTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for de.fuh.xpairtise.tests.plugin.model");
        suite.addTestSuite(PrefsTest.class);
        return suite;
    }
}
