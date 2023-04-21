package test.junit.task;

import jstress.task.Result;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TaskTest extends TestCase {

    protected SimpleTask task;

    protected String testName = new String("test.junit.task.SimpleTask");

    public static TestSuite suite() {
        TestSuite suite = new TestSuite(TaskTest.class);
        return suite;
    }

    public TaskTest(String s) {
        super(s);
    }

    protected void setUp() {
        task = new SimpleTask();
    }

    protected void tearDown() {
        task = null;
    }

    public void testGetName() {
        assertEquals(testName, task.getName());
    }

    public void testRun() {
        try {
            task.initTask();
            task.run(null);
        } catch (Exception e) {
            fail("Exception caught: shouldn't be here " + e);
        }
        Result result = task.getResult();
        assertTrue(result.getElapsedTime() >= 0);
    }
}
