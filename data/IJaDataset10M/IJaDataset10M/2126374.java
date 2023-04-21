package net;

import java.rmi.RemoteException;

/**
 * 
 * 
 * @author renat
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>��������: NIC</p>
 */
public interface RemoteMethod {

    Object operation(String operation, Object... params) throws RemoteException;
}
