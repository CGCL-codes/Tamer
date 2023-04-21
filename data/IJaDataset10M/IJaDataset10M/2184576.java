package com.daffodilwoods.rmi.interfaces;

import java.rmi.Remote;
import com.daffodilwoods.database.resource.*;
import java.rmi.RemoteException;
import java.io.IOException;

public interface _RmiReader extends Remote {

    void close() throws IOException, RemoteException;

    char[] getChar(int int0, int int1) throws RemoteException;
}
