package test.junit.task;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TaskRunnerTest extends TestCase {

    public static TestSuite suite() {
        TestSuite suite = new TestSuite(TaskRunnerTest.class);
        return suite;
    }

    public TaskRunnerTest(String s) {
        super(s);
    }

    public void testDoRun() {
    }

    public void testElapsedTime() {
    }
}
