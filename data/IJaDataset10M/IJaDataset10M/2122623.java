package org.openxava.test.model;

/**
 * Remote interface for Course.
 */
public interface CourseRemote extends org.openxava.ejbx.EJBReplicable, org.openxava.test.model.ICourse {

    public java.lang.String getDescription() throws java.rmi.RemoteException;

    public void setDescription(java.lang.String newDescription) throws java.rmi.RemoteException;

    public int getNumber() throws java.rmi.RemoteException;

    public int getYear() throws java.rmi.RemoteException;

    public org.openxava.test.model.CourseData getData() throws java.rmi.RemoteException;

    public void setData(org.openxava.test.model.CourseData data) throws java.rmi.RemoteException;

    public org.openxava.test.model.CourseValue getCourseValue() throws java.rmi.RemoteException;

    public void setCourseValue(org.openxava.test.model.CourseValue value) throws java.rmi.RemoteException;
}
