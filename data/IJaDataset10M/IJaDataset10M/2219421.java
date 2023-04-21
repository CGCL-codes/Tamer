package net.beeger.osmorc.run.managingbundle;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The bundle remote interface which provides methods for managing bundles.
 *
 * @author <a href="mailto:janthomae@janthomae.de">Jan Thom&auml;</a>
 * @version $Id$
 */
public interface ManagingBundle extends Remote {

    /**
   * Lists all installed bundles.
   *
   * @return the names of all installed bundles.
   * @throws java.rmi.RemoteException in case an error occurs
   */
    public String[] listInstalledBundles() throws RemoteException;
}
