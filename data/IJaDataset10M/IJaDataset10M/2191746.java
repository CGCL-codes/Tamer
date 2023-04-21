package org.xactor.test.dtm.interfaces;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

/**
 * Remote interface of the FrontEnd stateful session bean.
 *
 * @author <a href="mailto:reverbel@ime.usp.br">Francisco Reverbel</a>
 * @version $Revision: 37406 $
 */
public interface FrontEnd extends EJBObject {

    public void setBalances(int value1, int value2, int value3) throws RemoteException;

    public int[] getBalances() throws RemoteException;

    public void addToBalances(int value) throws RemoteException;

    public void setRollbackOnly() throws RemoteException;

    public void tellFirstAccountToSetRollbackOnly() throws RemoteException;

    public void tellSecondAccountToSetRollbackOnly() throws RemoteException;

    public void tellThirdAccountToSetRollbackOnly() throws RemoteException;
}
