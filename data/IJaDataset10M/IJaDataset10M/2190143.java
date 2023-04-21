package BU_Material;

import PR_Material.IUCM103Model;
import PR_Material.IUCM104Model;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/**
 * @author Studio
 * @version 1.0
 * @created 15-12-2009 13:15:18
 */
public interface IMaterialManager extends Remote, Serializable {

    /**
	 *
	 * @param name
	 * @param producer
	 * @param size
	 * @param liftRestrictions
	 * @param sort
	 * @param expiration
	 */
    public void displayAllMaterialInStore(long storeID, long ucm103modelID) throws RemoteException;

    public void newStoreAccess(long branchID, long materialID, int count, Date date, String reasonOfAccess) throws RemoteException;

    public void addUCM103Model(IUCM103Model ucm103stub) throws RemoteException;

    public void removeUCM103Model(long modelID) throws RemoteException;

    public void addUCM104Model(IUCM104Model ucm104stub) throws RemoteException;

    public void removeUCM104Model(long modelID) throws RemoteException;
}
