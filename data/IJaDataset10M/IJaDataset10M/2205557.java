package biz.com.bosspolis.crm.product.command;

import persistence.com.bosspolis.crm.product.*;
import biz.com.bosspolis.crm.AppConstants;
import biz.com.bosspolis.crm.product.dao.CommonDao;
import com.eis.ds.core.commandpattern.Command;
import com.eis.ds.core.commandpattern.CommandException;
import com.eis.ds.core.log.DSLogger;
import com.eis.ds.core.log.LoggerFactory;
import com.eis.ds.core.persistence.DBException;

public class CreateCategoryTypeCommand extends Command {

    private static DSLogger logger = LoggerFactory.getInstance().getLogger(CreateCategoryTypeCommand.class.getName());

    public void preExecute() throws CommandException {
    }

    public void postExecute() throws CommandException {
    }

    public void execute() throws CommandException {
        CategoryType p = (CategoryType) getDTOArea().getParamItem(AppConstants.KEY_CATEGORYTYPE);
        if (p != null) logger.debug("execute()->CategoryType: " + p.toString()); else {
            logger.debug("execute()->NULL category type.");
            return;
        }
        CommonDao dao = new CommonDao();
        try {
            dao.addCategoryType(getConnection(), p);
        } catch (DBException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        getDTOArea().addParamItem(AppConstants.KEY_CATEGORYTYPE, p);
    }
}
