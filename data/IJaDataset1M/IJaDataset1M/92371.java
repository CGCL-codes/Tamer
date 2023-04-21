package com.vlee.ejb.user;

import java.sql.Timestamp;
import java.util.*;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.math.*;
import com.vlee.util.*;

public interface GeneralEntityIndexHome extends EJBHome {

    public GeneralEntityIndex create(GeneralEntityIndexObject gsObj) throws RemoteException, CreateException;

    public GeneralEntityIndex findByPrimaryKey(Integer pkid) throws FinderException, RemoteException;

    public Collection getObjects(QueryObject query) throws RemoteException;
}
