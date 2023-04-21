package de.fokus.fraunhofer.motion.soba.business.interfaces;

/**
 * Home interface for Account.
 * @xdoclet-generated at ${TODAY}
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface AccountHome extends javax.ejb.EJBHome {

    public static final String COMP_NAME = "java:comp/env/ejb/Account";

    public static final String JNDI_NAME = "Account";

    public de.fokus.fraunhofer.motion.soba.business.interfaces.Account create(java.lang.Long accountnumber, java.lang.Integer pin) throws javax.ejb.CreateException, java.rmi.RemoteException;

    public de.fokus.fraunhofer.motion.soba.business.interfaces.Account findByPrimaryKey(de.fokus.fraunhofer.motion.soba.business.impl.AccountPK pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
