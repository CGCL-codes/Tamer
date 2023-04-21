package biz.com.bosspolis.crm.sau.command;

import biz.com.bosspolis.crm.common.command.UpdateUserCommand;
import biz.com.bosspolis.crm.sau.dao.CommonDao;
import com.eis.ds.core.commandpattern.Command;
import com.eis.ds.core.commandpattern.CommandException;
import com.eis.ds.core.log.DSLogger;
import com.eis.ds.core.log.LoggerFactory;
import com.eis.ds.core.persistence.DBException;

public class UpdateUserLevelHKCommand extends Command {

    private static DSLogger logger = LoggerFactory.getInstance().getLogger(UpdateUserCommand.class.getName());

    public void preExecute() throws CommandException {
    }

    public void postExecute() throws CommandException {
    }

    public void execute() throws CommandException {
        CommonDao dao = new CommonDao();
        try {
            dao.updateUserLevel(getConnection());
            dao.expireRequest(getConnection());
        } catch (DBException e) {
            logger.error(e);
            throw new CommandException(e);
        }
    }
}
