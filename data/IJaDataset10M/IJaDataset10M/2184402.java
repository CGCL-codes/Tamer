package net.sourceforge.mipa.application;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * predicate detection result callback interface.
 * 
 * @author Jianping Yu <jianp.yue@gmail.com>
 */
public interface ResultCallback extends Remote {

    /**
     * result callback interface.
     * 
     * @param value
     *            predicate detection result
     * @param predicateID
     * 			  predicate ID
     */
    public void callback(String predicateID, String value) throws RemoteException;
}
