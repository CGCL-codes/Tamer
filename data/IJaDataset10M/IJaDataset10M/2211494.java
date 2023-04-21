package com.vlee.ejb.inventory;

import java.sql.Timestamp;
import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.math.*;

public interface StockRequisition extends EJBObject {

    public StockRequisitionObject getObject() throws RemoteException;

    public void setObject(StockRequisitionObject valObj) throws RemoteException;

    public Long getPkid() throws RemoteException;
}
