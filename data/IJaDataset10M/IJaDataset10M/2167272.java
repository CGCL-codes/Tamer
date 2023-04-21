package com.ek.mitapp.ui.action;

import java.io.*;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import com.ek.mitapp.MitigationAppException;
import com.ek.mitapp.system.Configuration;

/**
 * Opens the Synchro application.
 * <br>
 * Id: $Id: $
 *
 * @author dirwin
 */
public class OpenSynchroAction extends AbstractJExcelAction {

    /**
	 * Define the root logger.
	 */
    private static Logger logger = Logger.getLogger(OpenSynchroAction.class.getName());

    /**
	 * Define action parameters.
	 */
    private static final String NAME_STORE = "Click to open the Synchro application";

    private static final String ACTION_COMMAND_KEY_STORE = "open-synchro-command";

    private static final String SHORT_DESCRIPTION_STORE = "Click to open the Synchro application";

    private static final String LONG_DESCRIPTION_STORE = "Click to open the Synchro application";

    private static final int MNEMONIC_KEY_STORE = 'O';

    /**
	 * Default constructor.
	 * 
	 * @param configuration
	 */
    public OpenSynchroAction(Configuration configuration) {
        super(configuration);
    }

    /**
	 * 
	 * @param configuration
	 * @param iconOnly
	 */
    public OpenSynchroAction(Configuration configuration, boolean iconOnly) {
        super(configuration, iconOnly);
    }

    /**
	 * @see com.ek.mitapp.ui.action.AbstractJExcelAction#initialize()
	 */
    @Override
    protected final void initialize() {
        if (!iconOnly) {
            putValue(Action.NAME, NAME_STORE);
            putValue(Action.ACTION_COMMAND_KEY, ACTION_COMMAND_KEY_STORE);
            putValue(Action.SHORT_DESCRIPTION, SHORT_DESCRIPTION_STORE);
            putValue(Action.LONG_DESCRIPTION, LONG_DESCRIPTION_STORE);
            putValue(Action.MNEMONIC_KEY, new Integer(MNEMONIC_KEY_STORE));
        }
    }

    /**
	 * @see com.ek.mitapp.ui.action.AbstractJExcelAction#runImpl()
	 */
    @Override
    protected void runImpl() {
        try {
            String execString = configuration.getSynchroAppPath().getPath();
            logger.debug("Executing: " + execString);
            Runtime.getRuntime().exec(execString);
        } catch (IOException ioe) {
            logger.error("Error opening Synchro application: " + ioe.getMessage());
            JOptionPane.showMessageDialog(null, "<html><b>Error attempting to open the Synchro application!</b><br>Please verify that the path to the Synchro executable is correct.</html>", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
