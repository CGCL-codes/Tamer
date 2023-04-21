package scheduler.gui.test;

import junit.framework.*;
import scheduler.gui.*;
import scheduler.domain.*;

/**
 * Test class for ScheduleFrameTest.
 *
 * @author  piku
 * @version
 */
public class ScheduleFrameTest extends TestCase {

    /** The <code>ScheduleFrame</code> to be tested. */
    private ScheduleFrame testFrame;

    /** Creates new ScheduleFrameTest with the specified name
   *
   * @param name Test case name
   */
    public ScheduleFrameTest(String name) {
        super(name);
    }

    /** Sets up the test fixture
   *
   * Called before every test case.
   */
    protected void setUp() {
        Schedule sched = new Schedule(2001);
        sched.addDoctor(new Doctor("Dokter Vlimmen", "vlim"));
        sched.addDoctor(new Doctor("Dokter Snuggles", "snug"));
        sched.addDoctor(new Doctor("Dokter Watson", "wats"));
        testFrame = new ScheduleFrame(sched);
    }

    /** Tears down the test fixture.
   * Called after every test case method.
   */
    protected void tearDown() {
        testFrame = null;
    }

    /** Test the user interface */
    public void testUi() {
        testFrame.show();
    }

    /**
   * Assembles and returns a test suite for all the test methods
   * of this test case.
   *
   * @returns a non-null test suite.
   */
    public static Test suite() {
        TestSuite suite = new TestSuite(ScheduleFrameTest.class);
        return suite;
    }

    /**
   * Run the test case.
   */
    public static void main(String args[]) {
        String[] testCaseName = { ScheduleFrameTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }
}
