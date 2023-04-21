package rhul_sc.benchmarktest;

import javacard.framework.APDU;
import javacard.framework.ISOException;
import javacard.framework.JCSystem;

/**
 * This is the superclass of all test modules. It controls the immediate flow and offers 
 * methods and data shared between each module.
 * 
 * @author gruna
 * @version 0.3
 *
 */
public abstract class TestCase {

    /**
	 * Reference to the applet instance to access set/get and judge methods
	 * @see BenchmarkApplet
	 */
    protected BenchmarkApplet applet;

    /**
	 * Storing internal state, that is the protocol, application and life cycle
	 * state as well as the module identifier. 
	 */
    protected byte[] state;

    /**
     * The current security parameter length in byte and bit for 
     * asymmetric and symmetric computations
     */
    public static final short SECURITY_PARAMETER_SYMMETRIC = 80;

    public static final short SECURITY_PARAMETER_ASYMMETRIC_BYTE = 128;

    public static final short SECURITY_PARAMETER_ASYMMETRIC_BIT = 1024;

    /**
     * Index values for the state array
     */
    public static final byte STATEPOS_PROTOCOL = 0x00;

    public static final byte STATEPOS_LIFECYCLE = 0x01;

    public static final byte STATEPOS_RUNSTATE = 0x02;

    public static final byte STATEPOS_IDENTIFIER = 0x03;

    /**
	 * Storing the module identifier
	 */
    protected byte identifier;

    /**
	 * There are two life cycle states: prepare and run
	 */
    public static final byte LIFECYCLE_PREPARE = 0x00;

    public static final byte LIFECYCLE_RUN = 0x01;

    /**
	 * Command shared by each protocol. INITIALIZED requests
	 * the modules to return 0 if all persistent data was initialised and 1 otherwise
	 * In case all data is initialised and the lifecycle state is 
	 * PREPARE, a state transiation to RUN is initiated.
	 */
    public static final byte INITIALIZED = 0x7c;

    /**
	 * Command shared by each protocol. EXIT will voluntarily deselect the 
	 * module by itself. 
	 */
    public static final byte EXIT = 0x7e;

    /**
	 * The maximum length of data within one APDU
	 */
    public static final short LENGTH_APDU_SLICE = 0xff;

    /**
	 * Error thrown if current state does not allow given operation
	 */
    public static final short SW_STATE_NOT_ALLOW_OP = 0x6710;

    /**
     * Error thrown if calculation is detected to be incorrect
     */
    public static final short SW_CALCULATION_ERROR = 0x6711;

    /***
     * Error thrown if input is illegal
     */
    public static final short SW_WRONG_INPUT = 0x6712;

    /**
     * Error thrown if access request is illegal
     */
    public static final short SW_ACCESS_NOT_ALLOWED = 0x6713;

    /**
     * Constructor initializing transient state array, applet reference and 
     * identifier
     * @param applet
     * @param identifier
     */
    public TestCase(BenchmarkApplet applet, byte identifier) {
        this.applet = applet;
        this.state = JCSystem.makeTransientByteArray((short) 4, JCSystem.CLEAR_ON_RESET);
        this.identifier = identifier;
        this.state[STATEPOS_IDENTIFIER] = this.identifier;
    }

    /**
     * Method that each test module needs to implement and will be called for each command
     * while in PREPARE state.
     * 
     * @param apdu the request apdu
     */
    public abstract void prepare(APDU apdu);

    /**
     * Method that each test module needs to implement and will be called for each command
     * while in RUN state.
     * 
     * @param apdu the request apdu
     */
    public abstract void run(APDU apdu);

    /**
     * Called when module is selected
     */
    public abstract void select();

    /**
     * Called wehn modules is involuntarily deselected
     */
    public abstract void deselect();

    /**
     * Selects the stage conforming process method run() or prepare().
     * 
     * @param apdu the request apdu
     * @throws ISOException If life cycle state is in a wrong state.
     */
    public void process(APDU apdu) throws ISOException {
        switch(state[STATEPOS_LIFECYCLE]) {
            case LIFECYCLE_PREPARE:
                this.prepare(apdu);
                break;
            case LIFECYCLE_RUN:
                this.run(apdu);
                break;
            default:
                ISOException.throwIt(SW_ACCESS_NOT_ALLOWED);
        }
    }

    protected static void stateError(BenchmarkApplet applet) {
        applet.judge(false, SW_STATE_NOT_ALLOW_OP);
    }

    protected static void genericError(BenchmarkApplet applet, short code) {
        applet.judge(false, code);
    }

    protected static void calcError(BenchmarkApplet applet) {
        applet.judge(false, SW_CALCULATION_ERROR);
    }

    protected static void inputError(BenchmarkApplet applet) {
        applet.judge(false, SW_WRONG_INPUT);
    }

    protected static void accessError(BenchmarkApplet applet) {
        applet.judge(false, SW_ACCESS_NOT_ALLOWED);
    }
}
