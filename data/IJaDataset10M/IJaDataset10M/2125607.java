package com.vlee.ejb.accounting;

import java.util.*;
import java.math.*;
import java.rmi.RemoteException;
import javax.ejb.*;
import java.sql.Timestamp;
import com.vlee.util.*;

public interface BankInSlipHome extends EJBHome {

    public BankInSlip create(BankInSlipObject newObj) throws RemoteException, CreateException;

    public BankInSlip findByPrimaryKey(Long pkid) throws FinderException, RemoteException;

    public Collection findAllObjects() throws FinderException, RemoteException;

    public Collection findObjectsGiven(String fieldName, String value) throws FinderException, RemoteException;

    public Collection getObjects(QueryObject query) throws RemoteException;
}
