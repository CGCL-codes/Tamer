package edu.mit.wi.omnigene.service.security.rbac.profile;

import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

public interface UserProfileHome extends EJBHome {

    public UserProfile create(HashMap nv) throws RemoteException, CreateException;

    public UserProfile findByPrimaryKey(String name) throws RemoteException, FinderException;

    public Collection findByAttributeValue(HashMap hashMap) throws FinderException, RemoteException;
}
