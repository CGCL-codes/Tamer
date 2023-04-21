package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;

public interface IFamily2 extends org.openxava.model.IModel {

    public static final String PROPERTY_description = "description";

    String getDescription() throws RemoteException;

    void setDescription(String description) throws RemoteException;

    public static final String PROPERTY_number = "number";

    int getNumber() throws RemoteException;
}
