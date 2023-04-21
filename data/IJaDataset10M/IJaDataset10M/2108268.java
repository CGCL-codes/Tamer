package org.signserver.admin.cli.defaultimpl.groupkeyservice;

import java.rmi.RemoteException;
import org.signserver.admin.cli.defaultimpl.AbstractAdminCommand;
import org.signserver.admin.cli.defaultimpl.AdminCommandHelper;
import org.signserver.cli.spi.IllegalCommandArgumentsException;
import org.signserver.common.InvalidWorkerIdException;
import org.signserver.common.WorkerStatus;
import org.signserver.groupkeyservice.common.GroupKeyServiceStatus;

/**
 * Command containing common help methods for group key service commands. 
 *
 * @version $Id: BaseGroupKeyServiceCommand.java 2093 2012-02-02 14:19:27Z netmackan $
 * @author Philip Vendil
 */
public abstract class BaseGroupKeyServiceCommand extends AbstractAdminCommand {

    protected AdminCommandHelper helper = new AdminCommandHelper();

    /**
     * Method checking if the given workerId exists and if its a
     * group key service.
     */
    protected void isWorkerGroupKeyService(int workerId) throws RemoteException, InvalidWorkerIdException, IllegalCommandArgumentsException {
        helper.checkThatWorkerIsProcessable(workerId);
        WorkerStatus status = helper.getWorkerSession().getStatus(workerId);
        if (!(status instanceof GroupKeyServiceStatus)) {
            throw new IllegalCommandArgumentsException("Error: given workerId doesn't seem to point to any existing group key service.");
        }
    }
}
