package com.vlee.ejb.inventory;

import java.sql.Timestamp;
import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.math.*;

public interface BOMLink extends EJBObject {

    public BOMLinkObject getObject() throws RemoteException;

    public void setObject(BOMLinkObject valObj) throws RemoteException;

    public Integer getPkid() throws RemoteException;
}
