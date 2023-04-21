package eviva.structure;

import junit.framework.TestCase;

/**
 * Tests Mission Control functionality
 */
public class MissionControlTest extends TestCase {

    MissionControl mission = new MissionControl();

    MissionControl.Mission m0;

    public MissionControlTest(String sTestName) {
        super(sTestName);
    }

    protected void setUp() throws Exception {
        super.setUp();
        m0 = mission.set(0, "Set up infrastructure");
    }

    protected void tearDown() throws Exception {
        mission.complete(m0);
    }

    /**
	 * @see MissionControl#getMissionLog()
	 */
    public void testGetMissionLog() {
        MissionControl.Mission m4 = mission.set(4, "Test mission log");
        MissionControl planB = new MissionControl();
        String realResult = planB.getMissionLog();
        String expectedResult = "";
        assertEquals("Empty initial log", expectedResult, realResult);
        mission.complete(m4);
    }

    /**
	 * @see MissionControl#set(Integer,String)
	 */
    public void testSet() {
        MissionControl.Mission m4 = mission.set(4, "Test set");
        MissionControl planB = new MissionControl();
        planB.set(3, "B");
        planB.set(6, "C");
        String realResult = planB.getMissionLog();
        String expectedResult = "   B\n      C\n";
        assertEquals("Log after double mission set", expectedResult, realResult);
        mission.complete(m4);
    }

    /**
	 * @see MissionControl#configureIndentation(Character)
	 */
    public void testConfigureIndentation() {
        MissionControl.Mission m4 = mission.set(4, "Test configuration");
        MissionControl planB = new MissionControl();
        planB.set(3, "B");
        planB.configureIndentation('x');
        String realResult = planB.getMissionLog();
        String expectedResult = "xxxB\n";
        assertEquals("Log with new indentation during mission", expectedResult, realResult);
        mission.complete(m4);
    }

    /**
	 * @see MissionControl#complete(Integer,String)
	 */
    public void testComplete() {
        MissionControl.Mission m4 = mission.set(4, "Test completing mission");
        MissionControl planB = new MissionControl();
        MissionControl.Mission mb = planB.set(3, "B");
        planB.complete(mb);
        String realResult = planB.getMissionLog();
        String expectedResult = "";
        assertEquals("Cleaned log after mission complete", expectedResult, realResult);
        mission.complete(m4);
    }

    /**
	 * @see MissionControl#abort(Integer,String)
	 */
    public void testAbort() {
        MissionControl.Mission m4 = mission.set(4, "Test aborted mission");
        MissionControl planB = new MissionControl();
        MissionControl.Mission mb = planB.set(3, "B");
        planB.abort(mb, "Error");
        String realResult = planB.getMissionLog();
        String expectedResult = "   B\n   B ***ABORTED\n";
        assertEquals("Log after mission abort", expectedResult, realResult);
        mission.complete(m4);
    }
}
