package ca.ucalgary.ebe.j3dperfunit;

import java.awt.Window;
import java.util.Vector;
import junit.framework.TestCase;
import ca.ucalgary.ebe.j3dperfunit.monitor.AWTMonitor;
import ca.ucalgary.ebe.j3dperfunit.monitor.J3DMonitor;
import ca.ucalgary.ebe.j3dperfunit.monitor.SceneGraphMonitor;
import ca.ucalgary.ebe.j3dperfunit.monitor.TestReporter;
import ca.ucalgary.ebe.j3dperfunit.util.ClassRunner;

/**
 * Part of this class is copied from "JFCTestCase" in JFCUnit whose source
 * code can be downloaded from http://jfcunit.sourceforge.net
 *  
 */
public class J3DPerfUnit {

    private Vector<String> winsToClose = new Vector<String>();

    /**
     * the deamon that manages AWTMonitor, SceneGraphMonitor, and TestThread
     */
    private static J3DMonitor monitor;

    /**
     * full name of a test file. For example: ca.ucalgary.wepa.tests.gui.LoadOrganTest
     */
    static String testFile = "";

    /**
     * Kick Start the Monitor to hopefully.
     * <p>
     * Get it started before any popups are displayed.
     */
    static {
        monitor = new J3DMonitor();
    }

    /**
     * Contructor
     *
     */
    public J3DPerfUnit() {
        super();
        SceneGraphMonitor.readyToCloseWins = false;
    }

    /**
     * 
     * @param testCaseName the name of a single test case
     * @return a new test case instance
     */
    public TestReceiver getTestReceiver(String testCaseName) {
        return new TestReceiver(testCaseName);
    }

    /**
     * This method MUST be called before all the tests.
     * @param testClass a class in which all tests are defined.
     *  <p>
     *  For example: ca.ucalgary.wepa.tests.gui.LoadOrganTest
     */
    public void setUp(Object testClass) {
        testFile = testClass.getClass().getCanonicalName();
    }

    /**
     * This method MUST be called after all the tests.
     * 
     */
    public void tearDown() {
        TestReceiver lastTestCase = new TestReceiver("last test");
        lastTestCase.setLastCaseInTest(true);
        lastTestCase.setCurrentJ3DTest(this);
        getMonitor().regTestReceiver(lastTestCase);
        testFile = "Abnonymous";
    }

    /**
     * Start a Frame Rate Counter
     * 
     * @param frmTitle the title of the test frame
     * @param cavIdx the index of the test canvas on 
     * the test frame
     */
    public void startFPSCounter(String frmTitle, int cavIdx) {
        TestReceiver startCounter = getTestReceiver("");
        startCounter.setUI(frmTitle, cavIdx);
        startCounter.startFPSCounter();
    }

    public void stopFPSCounter() {
        TestReceiver stopCounter = getTestReceiver("");
        stopCounter.stopFPSCounter();
    }

    /**
     * 
     * @return the currently running J3DMonitor instance
     */
    static J3DMonitor getMonitor() {
        return monitor;
    }

    /**
     * 
     * @return a list containing windows to close 
     */
    public Vector<String> getWinsToClose() {
        return winsToClose;
    }

    /**
	 * set windows to close after a test class is done
	 * @param winsTitle window titles array
	 */
    public void addWinsToClose(String[] winsTitle) {
        if (winsTitle != null && winsTitle.length != 0) {
            for (int i = 0; i < winsTitle.length; i++) {
                winsToClose.add(winsTitle[i]);
            }
        }
    }
}
