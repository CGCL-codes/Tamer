package biz.com.bosspolis.crm.common.command;

import java.util.List;
import biz.com.bosspolis.crm.AppConstants;
import biz.com.bosspolis.crm.common.dao.CommonDao;
import com.eis.ds.core.commandpattern.Command;
import com.eis.ds.core.commandpattern.CommandException;
import com.eis.ds.core.log.DSLogger;
import com.eis.ds.core.log.LoggerFactory;
import com.eis.ds.core.persistence.DBException;

public class GetUsersByUserLevelCommand extends Command {

    private static DSLogger logger = LoggerFactory.getInstance().getLogger(GetUsersByUserLevelCommand.class.getName());

    public void preExecute() throws CommandException {
    }

    public void postExecute() throws CommandException {
    }

    public void execute() throws CommandException {
        logger.info("execute()->");
        String sLevel = (String) getDTOArea().getParamItem(AppConstants.KEY_USERLEVEL);
        CommonDao dao = new CommonDao();
        List uList = null;
        try {
            uList = dao.getUsersByUserLevel(getConnection(), sLevel);
            if (uList == null) {
                throw new CommandException("No users found");
            }
            logger.info("No of users: " + uList.size());
        } catch (DBException e) {
            logger.error(e, e);
            throw new CommandException(e);
        }
        getDTOArea().addParamItem(AppConstants.KEY_USERLIST, uList);
    }
}
