package org.xactor;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that allows a recoverable object to drive the recovery process.
 *
 * @author <a href="mailto:reverbel@ime.usp.br">Francisco Reverbel</a>
 * @version $Revision: 37634 $ 
 */
public interface RecoveryCoordinator extends Remote {

    Status replayCompletion(Resource r) throws RemoteException, TransactionNotPreparedException;
}
