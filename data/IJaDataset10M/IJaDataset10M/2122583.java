package com.vlee.ejb.customer;

import java.sql.Timestamp;
import java.util.*;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.math.*;
import com.vlee.util.*;

public interface StringTemplateHome extends EJBHome {

    public StringTemplate create(StringTemplateObject gsObj) throws RemoteException, CreateException;

    public StringTemplate findByPrimaryKey(Long pkid) throws FinderException, RemoteException;

    public Collection getObjects(QueryObject query) throws RemoteException;

    public List selectDistinctDescription(String stCategory) throws RemoteException;
}
