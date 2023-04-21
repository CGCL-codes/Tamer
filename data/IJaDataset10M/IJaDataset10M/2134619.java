package net.sourceforge.mipa.test;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Jianping Yu <jianp.yue@gmail.com>
 */
public interface InterfaceA extends Remote {

    public void A(int i) throws RemoteException;
}
