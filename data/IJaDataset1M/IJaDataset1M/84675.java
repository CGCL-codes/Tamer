package aaftt;

import static org.junit.Assert.*;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import ca.ucalgary.cpsc.ebe.fitClipse.exceptions.InvalidTimeException;
import ca.ucalgary.cpsc.ebe.fitClipse.exceptions.NegativeNumberException;
import ca.ucalgary.cpsc.ebe.fitClipse.exceptions.TestResultChangeException;
import ca.ucalgary.cpsc.ebe.fitClipse.testResults.TestResultFactory;
import ca.ucalgary.cpsc.ebe.fitClipse.tests.model.TestFactory;

public class TestResultTest {

    static TestResultCreator trCreator;

    TestResult testResult1;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        trCreator = new TestResultFactory();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        testResult1 = trCreator.newTestResult();
    }

    @After
    public void tearDown() throws Exception {
        testResult1 = null;
    }

    @org.junit.Test
    public void testCreateNewTestResult() {
        testResult1 = trCreator.newTestResult();
        assertNotNull("testResult is null, but should have an instance.", testResult1);
    }

    @org.junit.Test
    public void testSetTestOfTestResult() throws RefactoringException {
        TestResult testResult1 = trCreator.newTestResult();
        TestCreator testCreator = new TestFactory();
        Test test = testCreator.newTest();
        test.setName("test1");
        testResult1.setExecutedTest(test.getUniqueID());
        assertNotNull("returned test is null, but should be an instance of test.", testResult1.getExecutedTest());
    }

    @org.junit.Test
    public void testSetAndGetTestOfTestResult() throws RefactoringException {
        testResult1 = trCreator.newTestResult();
        TestCreator testCreator = new TestFactory();
        Test test = testCreator.newTest();
        test.setName("test1");
        testResult1.setExecutedTest(test.getUniqueID());
        assertEquals("The set test should be equal to the returned test.", "test1", testResult1.getExecutedTest());
    }

    @org.junit.Test
    public void testSetAndGetStartTime() throws InterruptedException, TestResultChangeException {
        Date startTime = GregorianCalendar.getInstance().getTime();
        testResult1.setStartTime(startTime);
        assertNotNull("the returned Date should not be null, but is null.", testResult1.getStartTime());
        Thread.sleep(1);
        assertEquals("the two dates should represent the exact same Date/time", startTime, testResult1.getStartTime());
    }

    @org.junit.Test
    public void testSetAndGetEndTime() throws InterruptedException, TestResultChangeException {
        Date endTime = GregorianCalendar.getInstance().getTime();
        testResult1.setEndTime(endTime);
        assertNotNull("the returned Date should not be null, but is null.", testResult1.getEndTime());
        Thread.sleep(1);
        assertEquals("the two dates should represent the exact same Date/time", endTime, testResult1.getEndTime());
    }

    @org.junit.Test
    public void testStartTimeAfterEndTime() throws InterruptedException, TestResultChangeException {
        Date endTime = GregorianCalendar.getInstance().getTime();
        testResult1.setEndTime(endTime);
        Date startTime = new Date(System.currentTimeMillis() + 100);
        try {
            testResult1.setStartTime(startTime);
            fail("Setting a start time, which represents a time later than the endTime should cause an exception.");
        } catch (InvalidTimeException ite) {
        }
    }

    @org.junit.Test
    public void testStartTimeEqulasEndTime() throws InterruptedException, TestResultChangeException {
        Date endTime = GregorianCalendar.getInstance().getTime();
        testResult1.setEndTime(endTime);
        try {
            testResult1.setStartTime(endTime);
        } catch (InvalidTimeException ite) {
            fail("Setting a start time, which represents a time equal to the endTime shouldn't cause an exception.");
        }
        assertEquals("", endTime, testResult1.getStartTime());
    }

    @org.junit.Test
    public void testStartTimeBeforeEndTime() throws InterruptedException, TestResultChangeException {
        Date endTime = GregorianCalendar.getInstance().getTime();
        testResult1.setEndTime(endTime);
        Date startTime = new Date(System.currentTimeMillis() - 100);
        try {
            testResult1.setStartTime(startTime);
        } catch (InvalidTimeException ite) {
            fail("Setting a start time, which represents a time earlier than the endTime shouldn't cause an exception.");
        }
        assertEquals("", startTime, testResult1.getStartTime());
    }

    @org.junit.Test
    public void testEndTimeBeforeStartTime() throws InterruptedException, TestResultChangeException {
        Date startTime = GregorianCalendar.getInstance().getTime();
        testResult1.setStartTime(startTime);
        Date endTime = new Date(System.currentTimeMillis() - 100);
        try {
            testResult1.setEndTime(endTime);
            fail("Setting an end time, which represents a time later than the startTime should cause an exception.");
        } catch (InvalidTimeException ite) {
        }
    }

    @org.junit.Test
    public void testEndTimeEqulasStartTime() throws InterruptedException, TestResultChangeException {
        Date startTime = GregorianCalendar.getInstance().getTime();
        testResult1.setStartTime(startTime);
        try {
            testResult1.setEndTime(startTime);
        } catch (InvalidTimeException ite) {
            fail("Setting a end time, which represents a time equal to the startTime shouldn't cause an exception.");
        }
        assertEquals("", startTime, testResult1.getEndTime());
    }

    @org.junit.Test
    public void testEndTimeAfterStartTime() throws InterruptedException, TestResultChangeException {
        Date startTime = GregorianCalendar.getInstance().getTime();
        testResult1.setStartTime(startTime);
        Date endTime = new Date(System.currentTimeMillis() + 100);
        try {
            testResult1.setEndTime(endTime);
        } catch (InvalidTimeException ite) {
            fail("Setting a end time, which represents a time earlier than the startTime shouldn't cause an exception.");
        }
        assertEquals("", endTime, testResult1.getEndTime());
    }

    @org.junit.Test
    public void testGetExecutionTime() throws TestResultChangeException {
        assertEquals("As no start and end time is set, -1 should be returned.", -1, testResult1.getExecutionTime());
        testResult1.setEndTime(new Date(System.currentTimeMillis()));
        assertEquals("As only the end time is set, -1 should be returned.", -1, testResult1.getExecutionTime());
        testResult1 = trCreator.newTestResult();
        testResult1.setStartTime(new Date(System.currentTimeMillis()));
        assertEquals("As only the start time is set, -1 should be returned.", -1, testResult1.getExecutionTime());
        testResult1.setStartTime(new Date(5000));
        testResult1.setEndTime(new Date(5000));
        assertEquals("As start and end Time are equal, 0 should be returned.", 0, testResult1.getExecutionTime());
        testResult1.setStartTime(new Date(5000));
        testResult1.setEndTime(new Date(6000));
        assertEquals("The difference should be 1000.", 1000, testResult1.getExecutionTime());
    }

    @org.junit.Test
    public void testSetAndGetTestOutput() {
        assertNull("testOutput should be null", testResult1.getTestOutput());
        testResult1.setTestOutput("Test Output.");
        assertNotNull("Output should not be null.", testResult1.getTestOutput());
        assertEquals("Output shoud be \"Test Output.\".", "Test Output.", testResult1.getTestOutput());
    }

    @org.junit.Test
    public void testSetAndGetNumWrong() throws TestResultChangeException {
        assertEquals("As nothing set, returned value should be 0", 0, testResult1.getNumWrong());
        testResult1.setNumWrong(5);
        assertEquals("Returned value should be 5", 5, testResult1.getNumWrong());
        try {
            testResult1.setNumWrong(-1);
            fail("Should through exception, as negative values are not wanted.");
        } catch (NegativeNumberException nne) {
        }
    }

    @org.junit.Test
    public void testSetAndGetNumRight() throws TestResultChangeException {
        assertEquals("As nothing set, returned value should be 0", 0, testResult1.getNumRight());
        testResult1.setNumRight(5);
        assertEquals("Returned value should be 5", 5, testResult1.getNumRight());
        try {
            testResult1.setNumRight(-1);
            fail("Should through exception, as negative values are not wanted.");
        } catch (NegativeNumberException nne) {
        }
    }

    @org.junit.Test
    public void testSetAndGetNumIgnored() throws TestResultChangeException {
        assertEquals("As nothing set, returned value should be 0", 0, testResult1.getNumIgnored());
        testResult1.setNumIgnored(5);
        assertEquals("Returned value should be 5", 5, testResult1.getNumIgnored());
        try {
            testResult1.setNumIgnored(-1);
            fail("Should through exception, as negative values are not wanted.");
        } catch (NegativeNumberException nne) {
        }
    }

    @org.junit.Test
    public void testSetAndGetNumExceptions() throws TestResultChangeException {
        assertEquals("As nothing set, returned value should be 0", 0, testResult1.getNumExceptions());
        testResult1.setNumExceptions(5);
        assertEquals("Returned value should be 5", 5, testResult1.getNumExceptions());
        try {
            testResult1.setNumExceptions(-1);
            fail("Should throw exception, as negative values are not wanted.");
        } catch (NegativeNumberException nne) {
        } catch (TestResultChangeException trce) {
            fail("Should not be reached, as NegativeNumberException was expacted.");
        }
    }
}
