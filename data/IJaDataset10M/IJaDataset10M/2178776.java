package cf.e_commerce.input.controller;

import java.rmi.RemoteException;
import cf.e_commerce.base.input.bean.CFInputBean;
import cf.e_commerce.base.input.bean.input.CFInputItem;
import cf.e_commerce.batch.processor.CFBatchProcessor.CFNotifyConfiguration;
import com.uplexis.idealize.base.exceptions.IdealizeCoreException;
import com.uplexis.idealize.base.exceptions.IdealizeInputException;
import com.uplexis.idealize.base.exceptions.IdealizeUnavailableResourceException;
import com.uplexis.idealize.base.loggers.IdealizeLogger;
import com.uplexis.idealize.hotspots.controller.input.InputController;
import com.uplexis.idealize.hotspots.input.bean.BaseBean;

/**
 * Controller for input environment
 * 
 * @author Felipe Melo
 * 
 */
public class CFInputController extends InputController {

    private final String canonicalName = this.getClass().getCanonicalName();

    private IdealizeLogger logger = IdealizeLogger.getInstance();

    @Override
    public boolean insertItem(BaseBean item) {
        CFInputItem inputItem = (CFInputItem) item;
        try {
            this.dataManipulator.insertItem(inputItem);
        } catch (IdealizeInputException e) {
            this.logger.logError(this.canonicalName + ".insertItem: could not insert item: " + e.getMessage(), e);
            return false;
        } catch (IdealizeUnavailableResourceException e) {
            this.logger.logError(this.canonicalName + ".insertItem: could not insert item: " + e.getMessage(), e);
            return false;
        } catch (IdealizeCoreException e) {
            this.logger.logError(this.canonicalName + ".insertItem: could not insert item: " + e.getMessage(), e);
            return false;
        }
        if (!this.notityRemoteProcessor(CFNotifyConfiguration.OP_INSERT, item.getItemId(), false)) this.logger.logError(this.canonicalName + ".insertItem: could not notify item " + item.getItemId() + " insertion");
        return true;
    }

    @Override
    public boolean removeItem(BaseBean item) {
        CFInputItem inputItem = (CFInputItem) item;
        try {
            this.dataManipulator.removeItem(inputItem);
        } catch (IdealizeInputException e) {
            this.logger.logError(this.canonicalName + ".removeItem: could not remove item: " + e.getMessage(), e);
            return false;
        } catch (IdealizeUnavailableResourceException e) {
            this.logger.logError(this.canonicalName + ".removeItem: could not remove item: " + e.getMessage(), e);
            return false;
        } catch (IdealizeCoreException e) {
            this.logger.logError(this.canonicalName + ".removeItem: could not remove item: " + e.getMessage(), e);
            return false;
        }
        if (!this.notityRemoteProcessor(CFNotifyConfiguration.OP_REMOVE, item.getItemId(), false)) this.logger.logError(this.canonicalName + ".removeItem: could not notify item " + item.getItemId() + " removal");
        return true;
    }

    @Override
    public boolean updateItem(BaseBean item) {
        CFInputItem inputItem = (CFInputItem) item;
        try {
            this.dataManipulator.updateItem(inputItem);
        } catch (IdealizeInputException e) {
            this.logger.logError(this.canonicalName + ".updateItem: could not update item: " + e.getMessage(), e);
            return false;
        } catch (IdealizeUnavailableResourceException e) {
            this.logger.logError(this.canonicalName + ".updateItem: could not update item: " + e.getMessage(), e);
            return false;
        } catch (IdealizeCoreException e) {
            this.logger.logError(this.canonicalName + ".updateItem: could not update item: " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public boolean insertUser(BaseBean item) {
        CFInputBean inputItem = (CFInputBean) item;
        try {
            this.dataManipulator.insertUser(inputItem);
        } catch (IdealizeInputException e) {
            this.logger.logError(this.canonicalName + ".insertUser: could not insert user " + inputItem.getUserId() + ": " + e.getMessage(), e);
            return false;
        } catch (IdealizeUnavailableResourceException e) {
            this.logger.logError(this.canonicalName + ".insertUser: could not insert user " + inputItem.getUserId() + ": " + e.getMessage(), e);
            return false;
        } catch (IdealizeCoreException e) {
            this.logger.logError(this.canonicalName + ".insertUser: could not insert user " + inputItem.getUserId() + ": " + e.getMessage(), e);
            return false;
        }
        if (!this.notityRemoteProcessor(CFNotifyConfiguration.OP_INSERT, inputItem.getUserId(), true)) this.logger.logError(this.canonicalName + ".insertUser: could not notify user " + inputItem.getUserId() + " insertion");
        return true;
    }

    @Override
    public boolean removeUser(BaseBean item) {
        CFInputBean inputItem = (CFInputBean) item;
        try {
            this.dataManipulator.removeUser(inputItem);
        } catch (IdealizeInputException e) {
            this.logger.logError(this.canonicalName + ".insertUser: could not remove user " + inputItem.getUserId() + ": " + e.getMessage(), e);
            return false;
        } catch (IdealizeUnavailableResourceException e) {
            this.logger.logError(this.canonicalName + ".insertUser: could not remove user " + inputItem.getUserId() + ": " + e.getMessage(), e);
            return false;
        } catch (IdealizeCoreException e) {
            this.logger.logError(this.canonicalName + ".insertUser: could not remove user " + inputItem.getUserId() + ": " + e.getMessage(), e);
            return false;
        }
        if (!this.notityRemoteProcessor(CFNotifyConfiguration.OP_REMOVE, inputItem.getUserId(), true)) this.logger.logError(this.canonicalName + ".removeUser: could not notify user " + inputItem.getUserId() + " removal");
        return true;
    }

    @Override
    public boolean updateUser(BaseBean item) {
        CFInputBean inputItem = (CFInputBean) item;
        try {
            this.dataManipulator.updateUser(inputItem);
        } catch (IdealizeInputException e) {
            this.logger.logError(this.canonicalName + ".insertUser: could not update user " + inputItem.getUserId() + ": " + e.getMessage(), e);
            return false;
        } catch (IdealizeUnavailableResourceException e) {
            this.logger.logError(this.canonicalName + ".insertUser: could not update user " + inputItem.getUserId() + ": " + e.getMessage(), e);
            return false;
        } catch (IdealizeCoreException e) {
            this.logger.logError(this.canonicalName + ".insertUser: could not update user " + inputItem.getUserId() + ": " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
	 * Notifies the remote batch processor
	 * @paran operation char with operation being performed
	 * @param itemId int with item id to be notified
	 * @param isUser boolean saying if it is a user or an item being accessed
	 * @return true if could notify
	 */
    private final boolean notityRemoteProcessor(char operation, long itemId, boolean isUser) {
        CFNotifyConfiguration conf = new CFNotifyConfiguration();
        int[] itemIdArray = { (int) itemId };
        conf.setRemovedItemArray(itemIdArray);
        conf.setUser(isUser);
        conf.setOperation(operation);
        try {
            this.remoteBatchProcessor.notifyDatabaseChange(conf);
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
