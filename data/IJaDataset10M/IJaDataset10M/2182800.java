package com.centraview.email.emailmanage;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface EmailManageHome extends EJBHome {

    /**

     * Called by the client to create an EJB bean instance. It requires a matching pair in
     * the bean class, i.e. ejbCreate().
     * @throws javax.ejb.CreateException
     */
    public EmailManage create() throws CreateException, RemoteException;
}
