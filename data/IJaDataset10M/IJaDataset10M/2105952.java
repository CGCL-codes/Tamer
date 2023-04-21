package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;

public interface IShipment extends org.openxava.model.IModel {

    public static final String PROPERTY_type = "type";

    int getType() throws RemoteException;

    public static final String PROPERTY_time = "time";

    java.sql.Timestamp getTime() throws RemoteException;

    void setTime(java.sql.Timestamp time) throws RemoteException;

    public static final String PROPERTY_description = "description";

    String getDescription() throws RemoteException;

    void setDescription(String description) throws RemoteException;

    public static final String PROPERTY_mode = "mode";

    int getMode() throws RemoteException;

    public static final String PROPERTY_number = "number";

    int getNumber() throws RemoteException;

    org.openxava.test.model.ICustomerContactPerson getCustomerContactPerson() throws RemoteException;

    void setCustomerContactPerson(org.openxava.test.model.ICustomerContactPerson newCustomerContactPerson) throws RemoteException;
}
