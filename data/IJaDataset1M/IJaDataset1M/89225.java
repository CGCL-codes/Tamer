package saeed.beans;

import javax.servlet.http.*;
import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import saeed.*;

public interface UserManager extends EJBObject {

    public String login(String name, String pass, String session) throws RemoteException;

    public void logout(HttpSession session) throws RemoteException;

    public String getUserFullName(String uid) throws RemoteException;

    public HashMap getUsersList() throws RemoteException;

    public UidList getUsersList(String gid) throws RemoteException;

    public String getGroupFullName(String gid) throws RemoteException;

    public HashMap getGroupsList() throws RemoteException;

    public String getFullName(String id) throws RemoteException;

    public String getMail(String id) throws RemoteException;

    public UidList getInGroups(String uid) throws RemoteException;

    public boolean isUser(String uid) throws RemoteException;

    public boolean isGroup(String gid) throws RemoteException;

    public int getMaxDoc() throws RemoteException;

    public int getMaxElem(Integer did) throws RemoteException;

    public void setUserParams(String uid, String fname, String email, UidList grps, String pwd) throws RemoteException;

    public void createUser(String uid, String fname, String email, UidList grps, String pwd) throws RemoteException;

    public void deleteUser(String uid) throws RemoteException;

    public void setGroupParams(String gid, String fname) throws RemoteException;

    public void createGroup(String gid, String fname) throws RemoteException;

    public void deleteGroup(String gid) throws RemoteException;
}
