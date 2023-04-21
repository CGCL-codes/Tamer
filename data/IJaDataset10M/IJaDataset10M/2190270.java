package org.dyndns.doujindb.dat.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**  
* RMIOutputStream.java - RMI OutputStream.
* @author  nozomu
* @version 1.0
*/
public interface RMIOutputStream extends Remote {

    public void close() throws RemoteException;

    public void flush() throws RemoteException;

    public void write(byte[] b, int off, int len) throws RemoteException;

    public void write(byte[] b) throws RemoteException;

    public void write(int b) throws RemoteException;
}
