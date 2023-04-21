package com.vlee.ejb.customer;

import java.sql.Timestamp;
import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.math.*;

public interface StringTemplate extends EJBObject {

    public StringTemplateObject getObject() throws RemoteException;

    public void setObject(StringTemplateObject valObj) throws RemoteException;

    public Long getPkid() throws RemoteException;
}
