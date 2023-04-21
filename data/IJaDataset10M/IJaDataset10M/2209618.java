package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;

public interface IReport extends org.openxava.model.IModel {

    public static final String PROPERTY_oid = "oid";

    String getOid() throws RemoteException;

    public static final String PROPERTY_name = "name";

    String getName() throws RemoteException;

    void setName(String name) throws RemoteException;

    org.openxava.test.model.IRanges getRanges() throws RemoteException;

    void setRanges(org.openxava.test.model.IRanges newRanges) throws RemoteException;
}
