package com.vlee.ejb.customer;

import java.sql.Timestamp;
import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.math.*;

public interface DeliveryTripSOLink extends EJBObject {

    public DeliveryTripSOLinkObject getObject() throws RemoteException;

    public void setObject(DeliveryTripSOLinkObject valObj) throws RemoteException;

    public Long getPkid() throws RemoteException;

    public void setDeliveryStatus(String buf) throws RemoteException;
}
