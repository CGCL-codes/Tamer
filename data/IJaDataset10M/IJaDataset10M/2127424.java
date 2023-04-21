package com.vlee.ejb.ecommerce;

import java.sql.Timestamp;
import java.util.*;
import java.io.*;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.math.*;
import com.vlee.util.*;

public interface EStoreHome extends EJBHome {

    public EStore findByPrimaryKey(Long pkid) throws FinderException, RemoteException;
}
