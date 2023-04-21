package org.callbackparams.junit3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import junit.framework.TestResult;

/**
 * @author Henrik Kaipe
 */
public class TestNamesForCallbackTestCase extends CallbackTestCase {

    private static ThreadLocal testNames = new ThreadLocal();

    private String currentTestName;

    public Collection getCallbackRecords() {
        return Arrays.asList(new Object[][] { { "FIRST" }, {}, { "THIRD", "WITH", "EXTRA", "STUFF" } });
    }

    public void setUp() {
        currentTestName = getName();
    }

    public void testCallbackTest(Comparator cmp) throws Exception {
        assertTrue("Setup should have set current test-name", currentTestName.startsWith("testCallbackTest$"));
        throw new Exception();
    }

    protected void tearDown() {
        ((Collection) testNames.get()).add(currentTestName);
    }

    public static void main(final String[] args) {
        testNames.set(new TreeSet());
        try {
            TestResult result = TestRunner.runTestCase(TestNamesForCallbackTestCase.class);
            assertEquals("Number of tests run", 3, result.runCount());
            assertEquals("Number of errors", 3, result.errorCount());
            final TreeSet expectedMethodNames = new TreeSet();
            final ArrayList recordList = new ArrayList(4);
            for (Iterator iRecords = new TestNamesForCallbackTestCase().getCallbackRecords().iterator(); iRecords.hasNext(); ) {
                final Object[] record = (Object[]) iRecords.next();
                recordList.addAll(Arrays.asList(record));
                expectedMethodNames.add("testCallbackTest" + recordList);
                recordList.clear();
            }
        } finally {
            testNames.set(null);
        }
    }
}
