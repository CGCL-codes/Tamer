package org.xactor.test.ws.atomictx.mock.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A bean that enlists <code>MockXAResource</code>s and <code>MockSynchronization</code>s in a
 * transaction.
 * 
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 */
public interface MockResourceEnlister extends Remote {

    void enlistXAResource(int prepareVote) throws RemoteException;

    void enlistSynchronization() throws RemoteException;

    int transactionStatus() throws RemoteException;
}
