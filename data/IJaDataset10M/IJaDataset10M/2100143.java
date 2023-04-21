package org.pluginbuilder.autotestsuite.junit3.testplugin.internal;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StaticSuiteTestCase extends TestCase {

    public static Test suite() {
        TestSuite result = new TestSuite(SimpleTestCase.class);
        result.setName("PSS");
        return result;
    }
}
