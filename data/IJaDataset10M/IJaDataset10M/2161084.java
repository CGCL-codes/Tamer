package biz.com.bosspolis.crm.common.command;

import com.eis.ds.core.commandpattern.Command;
import com.eis.ds.core.commandpattern.CommandException;
import com.eis.ds.core.log.DSLogger;
import com.eis.ds.core.log.LoggerFactory;

public class LogoutCommand extends Command {

    private static DSLogger logger = LoggerFactory.getInstance().getLogger(LogoutCommand.class.getName());

    public void preExecute() throws CommandException {
    }

    public void postExecute() throws CommandException {
    }

    public void execute() throws CommandException {
        logger.debug("Logout");
    }
}
