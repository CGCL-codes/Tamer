package org.htu.tinyspaces.api;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.htu.tinyspaces.error.TinyException;

/**
 * TinySpace.java
 * @author rich.midwinter
 * 
 * This interface represents the space which
 * entries are written to and read from.
 *
 */
public interface TinySpace extends Remote, Serializable {

    public void write(final Entry o) throws RemoteException, TinyException;

    public Entry read(final Entry o, final Long timeout) throws RemoteException, TinyException;

    public Entry readIfExists(final Entry o) throws RemoteException, TinyException;

    public Entry take(final Entry o, final Long timeout) throws RemoteException, TinyException;

    public Entry takeIfExists(final Entry o) throws RemoteException, TinyException;
}
