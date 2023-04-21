package visad;

import java.rmi.*;

/**
   RemoteServer is the interface for serving RemoteDataReferences.
   A RemoteServerImpl should be bound to a URL via Naming.rebind,
   and accessed remotely via this RemoteServer interface.<P>
*/
public interface RemoteServer extends Remote {

    /** return the RemoteDataReference with index on this
      RemoteServer, or null */
    RemoteDataReference getDataReference(int index) throws RemoteException;

    /** return the RemoteDataReference with name on this
      RemoteServer, or null */
    RemoteDataReference getDataReference(String name) throws VisADException, RemoteException;

    /** return an array of all RemoteDataReferences on this
      RemoteServer, or null */
    RemoteDataReference[] getDataReferences() throws RemoteException;

    /** add a new RemoteDataReferenceImpl to server and extend array */
    void addDataReference(RemoteDataReferenceImpl ref) throws RemoteException;

    /** set array of all RemoteDataReferences on this RemoteServer */
    void setDataReferences(RemoteDataReferenceImpl[] rs) throws RemoteException;

    /** remove a RemoteDataReferenceImpl from server and shrink size of array */
    void removeDataReference(RemoteDataReferenceImpl ref) throws RemoteException;

    /** return array of all RemoteDisplays in this RemoteServer */
    RemoteDisplay[] getDisplays() throws RemoteException;

    /** get a RemoteDisplay by index */
    RemoteDisplay getDisplay(int index) throws RemoteException;

    /** get a RemoteDisplay by name */
    RemoteDisplay getDisplay(String name) throws VisADException, RemoteException;

    /** add a new RemoteDisplayImpl to server and extend array */
    void addDisplay(RemoteDisplayImpl rd) throws RemoteException;

    /** set all RemoteDisplayImpls to serve */
    void setDisplays(RemoteDisplayImpl[] rd) throws RemoteException;

    /** remove a RemoteDisplayImpl from server and shrink size of array */
    void removeDisplay(RemoteDisplayImpl rd) throws RemoteException;
}
