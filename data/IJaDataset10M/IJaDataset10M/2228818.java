package org.openxava.escuela.modelo;

/**
 * Home interface for Profesor.
 */
public interface ProfesorHome extends javax.ejb.EJBHome {

    public static final String COMP_NAME = "java:comp/env/ejb/Profesor";

    public static final String JNDI_NAME = "@subcontext@/ejb/org.openxava.escuela.modelo/Profesor";

    public org.openxava.escuela.modelo.ProfesorRemote create(java.util.Map values) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.escuela.modelo.ProfesorRemote create(org.openxava.escuela.modelo.ProfesorData data) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.escuela.modelo.ProfesorRemote create(org.openxava.escuela.modelo.ProfesorValue value) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.escuela.modelo.ProfesorRemote findByPrimaryKey(org.openxava.escuela.modelo.ProfesorKey pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
