package org.openxava.test.model;

/**
 * Home interface for Issue.
 */
public interface IssueHome extends javax.ejb.EJBHome {

    public static final String COMP_NAME = "java:comp/env/ejb/Issue";

    public static final String JNDI_NAME = "@subcontext@/ejb/org.openxava.test.model/Issue";

    public org.openxava.test.model.IssueRemote create(java.util.Map values) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.model.IssueRemote create(org.openxava.test.model.IssueData data) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.model.IssueRemote create(org.openxava.test.model.IssueValue value) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.model.IssueRemote findById(java.lang.String id) throws javax.ejb.FinderException, java.rmi.RemoteException;

    public org.openxava.test.model.IssueRemote findByPrimaryKey(org.openxava.test.model.IssueKey pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
