package com.vlee.ejb.inventory;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import com.vlee.util.QueryObject;

public interface StockAdjustmentHome extends EJBHome {

    public StockAdjustment create(StockAdjustmentObject gsObj) throws RemoteException, CreateException;

    public StockAdjustment findByPrimaryKey(Long pkid) throws FinderException, RemoteException;

    public Collection getObjects(QueryObject query) throws RemoteException;
}
