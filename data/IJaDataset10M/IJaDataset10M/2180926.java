package com.vlee.ejb.user;

import java.util.*;
import java.rmi.RemoteException;
import javax.ejb.*;

public interface UserDetailsHome extends EJBHome {

    public UserDetails create(Integer usrid, Calendar bufDob, String bufSex, String bufEthnic, String bufICNo, String bufICType, Calendar bufLastVerified) throws RemoteException, CreateException;

    public UserDetails findByPrimaryKey(Integer urid) throws FinderException, RemoteException;

    public UserDetails findByPrimaryKeyId(Integer pkid) throws FinderException, RemoteException;

    public Collection findAllPrimaryKeyId() throws FinderException, RemoteException;

    public Collection findObjectsGiven(String fieldName, String strCriteria) throws FinderException, RemoteException;
}
