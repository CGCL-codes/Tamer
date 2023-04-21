package jhomenet.ui.action;

import javax.swing.Action;
import org.apache.log4j.Logger;
import jhomenet.commons.GeneralApplicationContext;

/**
 * TODO: Class description.
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class PollHardwareAction extends AbstractProgressAction<AbstractProgressAction.VoidResult> {

    /**
	 * Define the root logger.
	 */
    private static Logger logger = Logger.getLogger(PollHardwareAction.class.getName());

    /**
	 * 
	 */
    private final GeneralApplicationContext serverContext;

    /**
	 * 
	 */
    private final String hardwareAddr;

    /**
	 * Define action parameters.
	 */
    private static final String NAME_STORE = "Poll";

    private static final String ACTION_COMMAND_KEY_STORE = "poll-hardware-command";

    private static final String SHORT_DESCRIPTION_STORE = "Poll hardware command";

    private static final String LONG_DESCRIPTION_STORE = "Poll hardware command";

    private static final int MNEMONIC_KEY_STORE = 'P';

    /**
	 * 
	 */
    public PollHardwareAction(String hardwareAddr, GeneralApplicationContext serverContext) {
        super();
        if (hardwareAddr == null) throw new IllegalArgumentException("Hardware address cannot be null!");
        if (serverContext == null) throw new IllegalArgumentException("Server context cannot be null!");
        this.hardwareAddr = hardwareAddr;
        this.serverContext = serverContext;
    }

    /**
	 * @see jhomenet.ui.action.AbstractProgressAction#initialize()
	 */
    @Override
    protected final void initialize() {
        putValue(Action.NAME, NAME_STORE);
        putValue(Action.ACTION_COMMAND_KEY, ACTION_COMMAND_KEY_STORE);
        putValue(Action.SHORT_DESCRIPTION, SHORT_DESCRIPTION_STORE);
        putValue(Action.LONG_DESCRIPTION, LONG_DESCRIPTION_STORE);
        putValue(Action.MNEMONIC_KEY, new Integer(MNEMONIC_KEY_STORE));
        this.setProgressText("Polling hardware...");
        this.setDisplayCancelButton(Boolean.TRUE);
    }

    /**
	 * @see jhomenet.ui.action.AbstractProgressAction#runImpl()
	 */
    @Override
    protected AbstractProgressAction.VoidResult runImpl() throws ActionException {
        try {
            serverContext.getHardwareManager().captureAndPersistSensorData(hardwareAddr);
        } catch (jhomenet.commons.hw.mngt.HardwareManagerException hme) {
            logger.error("Error while capturing hardware data: " + hme.getMessage(), hme);
            throw new ActionException(hme);
        }
        return AbstractProgressAction.VOID;
    }
}
