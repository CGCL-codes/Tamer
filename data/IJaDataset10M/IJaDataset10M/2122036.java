package com.vlee.ejb.mrp;

import java.sql.Timestamp;
import java.util.*;
import java.io.*;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.math.*;
import com.vlee.util.*;

public interface MRNIndexHome extends EJBHome {

    public MRNIndex create(MRNIndexObject gsObj) throws RemoteException, CreateException;

    public MRNIndex findByPrimaryKey(Long pkid) throws FinderException, RemoteException;

    public Collection getObjects(QueryObject query) throws RemoteException;
}
