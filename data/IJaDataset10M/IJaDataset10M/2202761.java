package com.vlee.ejb.customer;

import java.sql.Timestamp;
import java.util.*;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.math.*;
import com.vlee.util.*;
import com.vlee.bean.reports.*;

public interface CustQuotationItemHome extends EJBHome {

    public CustQuotationItem create(CustQuotationItemObject gsObj) throws RemoteException, CreateException;

    public CustQuotationItem findByPrimaryKey(Long pkid) throws FinderException, RemoteException;

    public Collection getObjects(QueryObject query) throws RemoteException;
}
