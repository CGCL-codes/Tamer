package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;

public interface IProduct3 extends org.openxava.model.IModel {

    public static final String PROPERTY_comments = "comments";

    java.lang.String getComments() throws RemoteException;

    void setComments(java.lang.String comments) throws RemoteException;

    public static final String PROPERTY_description = "description";

    String getDescription() throws RemoteException;

    void setDescription(String description) throws RemoteException;

    public static final String PROPERTY_number = "number";

    long getNumber() throws RemoteException;

    org.openxava.test.model.SubfamilyInfo getSubfamily1() throws RemoteException;

    void setSubfamily1(org.openxava.test.model.SubfamilyInfo newSubfamily1) throws RemoteException;

    org.openxava.test.model.IFamily getFamily() throws RemoteException;

    void setFamily(org.openxava.test.model.IFamily newFamily) throws RemoteException;

    org.openxava.test.model.SubfamilyInfo getSubfamily2() throws RemoteException;

    void setSubfamily2(org.openxava.test.model.SubfamilyInfo newSubfamily2) throws RemoteException;
}
