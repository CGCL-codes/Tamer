package org.simpleframework.http.load;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

/**
 * This enables the <code>LoaderManager</code> implementation to
 * be exported to the RMI runtime. This is used for convienience 
 * so that the implementation only needs to provide the methods.
 *
 * @author Niall Gallagher
 */
abstract class LoaderStub extends UnicastRemoteObject implements LoaderManager {

    /**
    * Constructor exports the <code>LoaderManager</code> object
    * to the RMI runtime on an anonymous port number.
    *
    * @exception RemoteException if this could not be exported
    */
    protected LoaderStub() throws RemoteException {
    }
}
