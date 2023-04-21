package com.centraview.file;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Called by the client to create an object of File. It requires a matching pair in
 * the bean class, i.e. ejbCreate().
 */
public interface CvFileHome extends EJBHome {

    /**
	 * create method
	 *
	 * @return     CvFile
	 * @exception   RemoteException  
	 * @exception   CreateException  
	 */
    public CvFile create() throws RemoteException, CreateException;
}
