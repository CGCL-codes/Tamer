package org.dcm4chex.archive.web.maverick.admin;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.management.ObjectName;
import org.apache.log4j.Logger;
import org.dcm4chex.archive.ejb.interfaces.UserManager;
import org.dcm4chex.archive.ejb.interfaces.UserManagerHome;
import org.dcm4chex.archive.util.EJBHomeFactory;
import org.dcm4chex.archive.web.maverick.admin.perm.FolderPermissions;
import org.jboss.mx.util.MBeanServerLocator;

/**
 * 
 * @author franz.willer@gwi-ag.com
 * @version $Revision: 11261 $ $Date: 2009-05-08 04:15:19 -0400 (Fri, 08 May 2009) $
 * @since 13.04.2006
 */
public class UserAdminDelegate {

    private String securityDomain = "java:/jaas/dcm4chee-web";

    protected static Logger log = Logger.getLogger(UserAdminDelegate.class);

    /**
     * Get list of users from application server. 
     * @throws Exception
     */
    public List queryUsers() throws Exception {
        List userList = new ArrayList();
        UserManager manager = lookupUserManager();
        Iterator iterUsers = manager.getUsers().iterator();
        String user;
        StringBuffer sbRoles;
        while (iterUsers.hasNext()) {
            user = (String) iterUsers.next();
            userList.add(new DCMUser(user, manager.getRolesOfUser(user)));
        }
        return userList;
    }

    public void addUser(String userID, String passwd, Collection roles) throws RemoteException, Exception {
        lookupUserManager().addUser(userID, passwd, roles);
    }

    public void updateUser(String userID, Collection roles) throws RemoteException, Exception {
        lookupUserManager().updateUser(userID, roles);
        clearAuthenticationCache();
    }

    public boolean changePasswordForUser(String user, String oldPasswd, String newPasswd) throws RemoteException, Exception {
        boolean b = lookupUserManager().changePasswordForUser(user, oldPasswd, newPasswd);
        clearAuthenticationCache();
        return b;
    }

    public void removeUser(String userID) throws RemoteException, Exception {
        lookupUserManager().removeUser(userID);
        clearAuthenticationCache();
    }

    public Collection getRolesOfUser(String userID) throws RemoteException, Exception {
        return lookupUserManager().getRolesOfUser(userID);
    }

    public FolderPermissions getFolderPermissions(String userID) {
        return null;
    }

    private void clearAuthenticationCache() {
        try {
            MBeanServerLocator.locate().invoke(new ObjectName("jboss.security:service=JaasSecurityManager"), "flushAuthenticationCache", new Object[] { securityDomain }, new String[] { String.class.getName() });
        } catch (Exception x) {
            log.error("Cant clear Authentication Cache! Reason:", x);
        }
    }

    /**
     * Returns the UserManager bean.
     * 
     * @return The UserManager bean.
     * @throws Exception
     */
    protected UserManager lookupUserManager() throws Exception {
        UserManagerHome home = (UserManagerHome) EJBHomeFactory.getFactory().lookup(UserManagerHome.class, UserManagerHome.JNDI_NAME);
        return home.create();
    }
}
