package org.mndacs.kernel.commands;

import org.apache.log4j.Logger;
import org.mndacs.datatobjects.CommandSet;
import org.mndacs.kernel.interfaces.ExecuteCommandInterface;
import org.mndacs.kernel.Kernel;

/**
 *
 * @author Wagner
 */
public class Shutdown implements ExecuteCommandInterface {

    private static Logger logger = Logger.getLogger(Shutdown.class);

    public int getCommandCode() {
        return 4;
    }

    public boolean execute(CommandSet cmd) {
        if (cmd.getFlagCommand() && cmd.getCommandString().equals("Shutdown")) {
            logger.warn("[" + cmd.getHostID() + "] send shutdown");
            Kernel.getInstance().shutdown();
        }
        return false;
    }

    public CommandSet getAnswer() {
        return null;
    }
}
