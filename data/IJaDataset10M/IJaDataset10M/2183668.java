package org.primordion.xholon.samples;

import org.primordion.xholon.base.IMessage;
import org.primordion.xholon.base.StateMachineEntity;
import org.primordion.xholon.base.XholonWithPorts;

/**
 * Test FSM. Test all finite state machine (FSM) features supported by Xholon.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.2 (Created on November 16, 2005)
 */
public class XhTestFsm extends XholonWithPorts implements CeTestFsm {

    public static final int P_PARTNER = 0;

    public static final int SIG1 = 100;

    public static final int SIG2 = 200;

    public static final int SIG3 = 300;

    public static final int TEST_SCENARIO_1 = 1;

    public static final int TEST_SCENARIO_2 = 2;

    public static final int TEST_SCENARIO_3 = 3;

    public static final int TEST_SCENARIO_4 = 4;

    public static final int TEST_SCENARIO_5 = 5;

    protected static int testScenario = TEST_SCENARIO_4;

    public int state = 0;

    public String roleName = null;

    protected static int x = 10;

    protected static int y = 5;

    /**
	 * Constructor.
	 */
    public XhTestFsm() {
    }

    /**
	 * Set which test scenario to run.
	 * @param testScen The test scenario.
	 */
    public static void setTestScenario(int testScen) {
        testScenario = testScen;
    }

    /**
	 * Get which test scenario is being run.
	 * @return The test scenario.
	 */
    public static int getTestScenario() {
        return testScenario;
    }

    public void initialize() {
        super.initialize();
        state = 0;
        roleName = null;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void act() {
        switch(xhc.getId()) {
            case TestFsmSystemCE:
                processMessageQ();
                break;
            case TestHarnessCE:
                if (state == 0) {
                    System.out.println("Harness ");
                    switch(testScenario) {
                        case TEST_SCENARIO_1:
                            x = 10;
                            y = 5;
                            port[P_PARTNER].sendMessage(SIG2, null, this);
                            port[P_PARTNER].sendMessage(SIG3, null, this);
                            port[P_PARTNER].sendMessage(SIG1, null, this);
                            break;
                        case TEST_SCENARIO_2:
                            x = 9;
                            y = 6;
                            port[P_PARTNER].sendMessage(SIG2, null, this);
                            port[P_PARTNER].sendMessage(SIG3, null, this);
                            port[P_PARTNER].sendMessage(SIG1, null, this);
                            break;
                        case TEST_SCENARIO_3:
                            x = 10;
                            y = 5;
                            port[P_PARTNER].sendMessage(SIG2, null, this);
                            port[P_PARTNER].sendMessage(SIG1, null, this);
                            port[P_PARTNER].sendMessage(SIG1, null, this);
                            port[P_PARTNER].sendMessage(SIG1, null, this);
                            break;
                        case TEST_SCENARIO_4:
                            x = 10;
                            y = 5;
                            port[P_PARTNER].sendMessage(SIG2, null, this);
                            port[P_PARTNER].sendMessage(SIG2, null, this);
                            break;
                        case TEST_SCENARIO_5:
                            break;
                        default:
                            System.out.println(testScenario + " is an invalid TestScenario.");
                            break;
                    }
                    state = 1;
                }
                break;
            default:
                break;
        }
        super.act();
    }

    public void processReceivedMessage(IMessage msg) {
        switch(xhc.getId()) {
            case FsmXholonCE:
                if (this.hasChildNodes()) {
                    ((StateMachineEntity) getFirstChild()).doStateMachine(msg);
                }
                break;
            default:
                System.out.println("XhTestFsm: message with no receiver " + msg);
                break;
        }
    }

    public void performActivity(int activityId, IMessage msg) {
        switch(activityId) {
            case 786621749:
                System.out.println("StateA1 trans SIG2");
                break;
            case 241571744:
                System.out.println("Choice else");
                break;
            case 599761723:
                System.out.println("StateA1 trans SIG1 x < 10");
                break;
            case 795181731:
                System.out.println("StateA1 trans SIG1 x >= 10");
                break;
            case 19439146:
                System.out.println("StateA3  trans SIG1");
                break;
            case 498341955:
                System.out.println("StateA3  trans SIG3");
                break;
            case 347291938:
                System.out.println("Choice y == 5");
                break;
            case 689161769:
                System.out.println("init transition");
                break;
            case 835681751:
                System.out.println("StateB  trans SIG3");
                break;
            case 33891125:
                System.out.println("StateB  trans SIG2");
                break;
            case 239421219:
                System.out.println("StateB  trans SIG1");
                break;
            case 2679713:
                System.out.println("StateA  from entry point to StateA1");
                break;
            case 873631956:
                System.out.println("StateA  from exit point to StateB");
                break;
            case 993691739:
                System.out.println("StateA1 entry");
                break;
            case 496291735:
                System.out.println("StateA2 entry");
                break;
            case 975991737:
                System.out.println("StateA3 entry");
                break;
            case 561641291:
                System.out.println("StateA3 entry     KSW");
                break;
            case 931141733:
                System.out.println("StateA1 exit");
                break;
            default:
                System.out.println("XhTestFsm: performActivity() unknown Activity " + activityId);
                break;
        }
    }

    public boolean performGuard(int activityId, IMessage msg) {
        switch(activityId) {
            case 694651742:
                return true;
            case 939661724:
                return x < 10;
            case 114621726:
                return x >= 10;
            case 491111937:
                return y == 5;
            default:
                System.out.println("XhTestFsm: performGuard() unknown Activity " + activityId);
                return false;
        }
    }

    public String toString() {
        String outStr = getName();
        if ((port != null) && (port.length > 0)) {
            outStr += " [";
            for (int i = 0; i < port.length; i++) {
                if (port[i] != null) {
                    outStr += " port:" + port[i].getName();
                }
            }
            outStr += "]";
        }
        outStr += " state=" + state;
        return outStr;
    }
}
