package eu.popeye.middleware.dataSharing.centralizedDataManagement.dataSharingServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import eu.popeye.middleware.dataSharing.dataSharingExceptions.*;

public interface DataSharingServer extends Remote {

    public boolean createSharedSpace(String name, int allowedSharedSpace) throws RemoteException, SharedSpaceAlreadyExistException;

    public boolean joinSharedSpace(String name, int allowedSharedSpace) throws SharedSpaceDoesNotExistException, RemoteException;

    public boolean leaveSharedSpace(String name) throws RemoteException, SharedSpaceDoesNotExistException;

    public boolean quitSharedSpace(String name) throws RemoteException;

    public void stop() throws RemoteException;
}
