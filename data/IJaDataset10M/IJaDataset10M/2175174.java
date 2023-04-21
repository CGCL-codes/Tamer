package biz.com.bosspolis.crm.product.command;

import persistence.com.bosspolis.crm.product.Product;
import biz.com.bosspolis.crm.AppConstants;
import biz.com.bosspolis.crm.product.dao.CommonDao;
import com.eis.ds.core.commandpattern.Command;
import com.eis.ds.core.commandpattern.CommandException;
import com.eis.ds.core.log.DSLogger;
import com.eis.ds.core.log.LoggerFactory;
import com.eis.ds.core.persistence.DBException;

public class UpdateProductCommand extends Command {

    private static DSLogger logger = LoggerFactory.getInstance().getLogger(UpdateProductCommand.class.getName());

    public void preExecute() throws CommandException {
    }

    public void postExecute() throws CommandException {
    }

    public void execute() throws CommandException {
        Product m = (Product) getDTOArea().getParamItem(AppConstants.KEY_PRODUCT);
        if (m == null) throw new CommandException("NULL product Object.");
        logger.debug("execute()->product: " + m.toString());
        CommonDao dao = new CommonDao();
        try {
            dao.updateProduct(getConnection(), m);
        } catch (DBException e) {
            logger.error(e);
            throw new CommandException(e);
        }
    }
}
