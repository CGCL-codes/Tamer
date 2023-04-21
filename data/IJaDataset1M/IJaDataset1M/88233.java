package org.readyesb.ejb.service.interfaces;

/**
 * Home interface for CoreAcl.
 * @xdoclet-generated at ${TODAY}
 * @copyright ReadyESB.org
 * @author Leo-Fan.aq
 * @version ${version}
 */
public interface CoreAclHome extends javax.ejb.EJBHome {

    public static final String COMP_NAME = "java:comp/env/ejb/CoreAcl";

    public static final String JNDI_NAME = "ejb/CoreAcl";

    public org.readyesb.ejb.service.interfaces.CoreAcl create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
