package fi.hip.gb.net;

import java.rmi.RemoteException;

/**
 * Interface for asynchronous communication of GBAgent.
 * 
 * @author Juho Karppinen
 * @version $Id: PushService.java 568 2005-10-08 20:47:39Z jkarppin $
 */
public interface PushService {

    /**
     * Start or stop listening of status and results changes. 
     * 
     * @param jobID ID of the job to listen for
     * @param l listener for changes
     * @param enabled enable or disable notification messages
     * @throws RemoteException if could not set the listener
     */
    public void setChangeListener(Long jobID, ChangeListener l, Boolean enabled) throws RemoteException;
}
