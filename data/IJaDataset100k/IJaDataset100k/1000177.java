package com.google.appengine.testing.cloudcover.harness.junit3;

import junit.framework.Test;
import java.util.HashSet;
import java.util.Set;

/**
 * A {@link junit.framework.TestListener} that wipes all data written by the test when the
 * test finishes running.
 *
 * @author Max Ross <max.ross@gmail.com>
 */
public class TestNameCollector extends BaseTestListener {

    private final Set<String> testNames = new HashSet<String>();

    @Override
    public void startTest(Test test) {
        super.startTest(test);
        testNames.add(JUnit3TestHarness.getShortName(test));
    }

    public Set<String> getTestNames() {
        return testNames;
    }
}
