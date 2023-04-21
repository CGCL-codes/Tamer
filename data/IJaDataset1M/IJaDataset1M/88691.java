package dk.pervasive.jcaf;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Implementations of this interface receives notification about changes to the
 * context of an Entity. This is the RMI Remote interface, to receive
 * notifications across VMs.
 * 
 * @see EntityListener
 * @see Entity
 * @see ContextService
 * 
 * @author <a href="mailto:bardram@daimi.au.dk">Jakob Bardram </a>
 *  
 */
public interface RemoteEntityListener extends Remote {

    /**
     * Notification about changes in an entity's context information.
     * 
     * @param event
     * @throws RemoteException
     */
    public void contextChanged(ContextEvent event) throws RemoteException;
}
