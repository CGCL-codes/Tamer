package com.sortedunderbelly.appengineunit.harness.junitx;

import com.sortedunderbelly.appengineunit.spi.TestRun;
import junit.framework.Test;
import junit.framework.TestSuite;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

/**
 * Base implementation of a {@link TestRun} for different versions of JUnit.
 *
 * @author Max Ross <max.ross@gmail.com>
 */
public class JUnitTestRun implements TestRun {

    private final TestSuite testSuite;

    private final Logger logger = Logger.getLogger(getClass().getName());

    public JUnitTestRun(TestSuite testSuite) {
        this.testSuite = testSuite;
    }

    public Iterable<String> getTestIds(long runId) {
        List<String> testIds = new ArrayList<String>();
        for (Test t : toList(testSuite.tests())) {
            try {
                Class<?> testClass = Class.forName(t.toString());
                if (testClass.isAnonymousClass() || testClass.isLocalClass()) {
                    logger.warning(runId + ": Cannot schedule test " + t.getClass().getName() + " for execution because it is an anonymous or local class.");
                } else {
                    testIds.add(testClass.getName());
                }
            } catch (ClassNotFoundException e) {
                logger.warning(runId + ": Cannot schedule instance of class " + t.getClass().getName() + "for execution because its String represenation, " + t.toString() + ", is not an available class.");
            }
        }
        return testIds;
    }

    private List<Test> toList(Enumeration e) {
        List<Test> tests = new ArrayList<Test>();
        while (e.hasMoreElements()) {
            tests.add((Test) e.nextElement());
        }
        return tests;
    }
}
