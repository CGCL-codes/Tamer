package org.carabiner.infinitest;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Exploratory test for JunitCore
 * @author bjrady
 *
 */
public class TestJunitCore {

    private List<Description> finishedList;

    private JUnitCore core;

    private ArrayList<Failure> failingList;

    @Before
    public void whenCoreHasListeners() {
        core = new JUnitCore();
        finishedList = new ArrayList<Description>();
        failingList = new ArrayList<Failure>();
        core.addListener(new RunListener() {

            public void testFinished(Description description) throws Exception {
                finishedList.add(description);
            }

            @Override
            public void testFailure(Failure failure) throws Exception {
                failingList.add(failure);
            }
        });
        StubTest.enable();
        core.run(new Class[] { StubTest.class });
    }

    @After
    public void cleanup() {
        core = null;
        finishedList = null;
        StubTest.disable();
    }

    @Test
    public void shouldRunTestsAndReportResults() throws Exception {
        Description desc = findDescription("shouldBeSucessful", finishedList);
        assertEquals("Display name", "shouldBeSucessful(org.carabiner.infinitest.TestJunitCore$StubTest)", desc.getDisplayName());
        assertTrue("Result should have no children", desc.getChildren().isEmpty());
        assertTrue("Test result, not suite result", desc.isTest());
        assertEquals("Test Count", 1, desc.testCount());
        desc = findDescription("shouldFailIfPropertyIsSet", finishedList);
        assertEquals("Display name", "shouldFailIfPropertyIsSet(org.carabiner.infinitest.TestJunitCore$StubTest)", desc.getDisplayName());
        assertTrue("Result should have no children", desc.getChildren().isEmpty());
        assertTrue("Test result, not suite result", desc.isTest());
        assertEquals("Test Count", 1, desc.testCount());
        assertEquals(2, finishedList.size());
    }

    private static Description findDescription(String methodName, List<Description> testList) {
        for (Description d : testList) {
            if (d.getDisplayName().startsWith(methodName + "(")) return d;
        }
        return null;
    }

    @Test
    public void shouldNotifyListenersAboutFailingTests() throws Exception {
        assertEquals(1, failingList.size());
        Failure failure = failingList.get(0);
        assertEquals("shouldFailIfPropertyIsSet(org.carabiner.infinitest.TestJunitCore$StubTest)", failure.getTestHeader());
        assertEquals("This test should fail", failure.getMessage());
    }

    public static class StubTest {

        @Test
        public void shouldBeSucessful() throws Exception {
        }

        @Test
        public void shouldFailIfPropertyIsSet() throws Exception {
            if (System.getProperty(StubTest.class.getName()) != null) fail("This test should fail");
        }

        public static void enable() {
            System.setProperty(StubTest.class.getName(), "");
        }

        public static void disable() {
            System.clearProperty(StubTest.class.getName());
        }
    }
}
