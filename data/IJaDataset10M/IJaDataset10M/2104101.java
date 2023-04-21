package com.endlessloopsoftware.egonet.interfaces;

/**
 * Home interface for QuestionEJB.
 */
public interface QuestionEJBRemoteHome extends javax.ejb.EJBHome {

    public static final String COMP_NAME = "java:comp/env/ejb/QuestionEJB";

    public static final String JNDI_NAME = "QuestionEJBHome";

    public com.endlessloopsoftware.egonet.interfaces.QuestionEJBRemote create(com.endlessloopsoftware.egonet.util.QuestionDataValue data) throws javax.ejb.CreateException, java.rmi.RemoteException;

    public com.endlessloopsoftware.egonet.interfaces.QuestionEJBRemote findByPrimaryKey(com.endlessloopsoftware.egonet.interfaces.QuestionEJBPK pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
