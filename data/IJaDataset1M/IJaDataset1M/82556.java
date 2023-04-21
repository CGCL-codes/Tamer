package Managers.Utilities;

import Beans.UserBean;
import Beans.PermissionsBean;
import Managers.SessionsManager;
import Utilities.Constants;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import Utilities.Interfaces.SelfValidating;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * A class used to store user data when dealing with sessions. <br><br>
 *
 * The sessions manager creates a new <code>User</code> object for each user sessions
 * and updates it whenever appropriate.
 *
 * <b>Note:</b> <i>A <code>User</code> is shared between the backend and the frontend.</i>
 *
 * @see SessionsManager
 *
 * @author Angel Sanadinov
 */
public class User implements HttpSessionBindingListener, SelfValidating {

    private SessionsManager manager;

    private UserBean userData;

    private HashMap<Integer, PermissionsBean> permissions;

    private HttpSession userSession;

    private int currentServerId;

    /**
     * Constructs the user object with the specified data.
     *
     * @param userData the data associated with the user
     * @param session the session associated with the user
     * @param manager the sessions manager that created the object
     * @param listOfPermissions a list of the permissions the user has
     */
    public User(UserBean userData, HttpSession session, SessionsManager manager, ArrayList<PermissionsBean> listOfPermissions) {
        if (userData.isValid()) {
            this.userData = userData;
            this.userSession = session;
            this.manager = manager;
            this.currentServerId = Constants.INVALID_SERVER_ID;
            permissions = new HashMap<Integer, PermissionsBean>(listOfPermissions.size());
            for (PermissionsBean currentPermissions : listOfPermissions) {
                if (currentPermissions.isValid() && currentPermissions.getUserId() == this.userData.getUserId()) {
                    permissions.put(currentPermissions.getServerId(), currentPermissions);
                } else ;
            }
        } else ;
    }

    /**
     * Updates a single permissions object. <br><br>
     *
     * <b>Note:</b> <i>Permissions for the server specified in the supplied object
     *              must already exist in the permissions list for the user.</i>
     *
     * @param newPermissions
     */
    public void updatePermissions(PermissionsBean newPermissions) {
        if (newPermissions != null && newPermissions.isValid() && newPermissions.getUserId() == this.userData.getUserId() && this.permissions.containsKey(newPermissions.getServerId())) {
            this.permissions.put(newPermissions.getServerId(), newPermissions);
        } else ;
    }

    /**
     * Removes the permissions for the specified server.
     *
     * @param serverId the id of the server
     */
    public void removePermissions(int serverId) {
        this.permissions.remove(serverId);
    }

    /**
     * Updates the data associated with the user.
     *
     * @param userData the new user data
     */
    public void updateUserData(UserBean userData) {
        if (userData != null && userData.isValid() && this.userData.getUserId() == userData.getUserId()) this.userData = userData; else ;
    }

    /**
     * Checks the validity of the object. <br><br>
     *
     * A <code>User</code> object is considered valid only if the reference to
     * its parent manager is available and if any data associated with the user is
     * also present and valid.
     *
     * @return
     */
    @Override
    public boolean isValid() {
        if (manager != null && userData != null && userData.isValid() && permissions != null && userSession != null) return true; else return false;
    }

    /**
     * Retrieves all permissions that the user has.
     *
     * @return an array with all available permissions
     */
    public PermissionsBean[] getAllPermissions() {
        PermissionsBean[] result = new PermissionsBean[permissions.size()];
        int i = 0;
        for (PermissionsBean currentPermissions : permissions.values()) {
            result[i] = currentPermissions;
            i++;
        }
        return result;
    }

    /**
     * Retrieves the permissions that the user has on the specified server.
     *
     * @param serverId the id of the server
     * @return the permissions for the server or <code>null</code> if no permissions
     *         are available
     */
    public PermissionsBean getPermissions(int serverId) {
        PermissionsBean result = null;
        if (serverId > 0) result = permissions.get(serverId); else ;
        return result;
    }

    /**
     * Retrieves the data associated with the user.
     *
     * @return the user data
     */
    public UserBean getUserData() {
        return userData;
    }

    /**
     * Retrieves the session to which this object is bound.
     *
     * @return the session
     */
    public HttpSession getUserSession() {
        return userSession;
    }

    /**
     * Compares the session id of the current user session and the specified session id.
     *
     * @param sessionId the session id which is to be checked
     * @return <code>true</code> if the IDs match or <code>false</code> otherwise
     */
    public boolean compareSessionId(String sessionId) {
        return this.userSession.getId().equals(sessionId);
    }

    /**
     * Retrieves the ID of the server that is currently being accessed.
     *
     * @return the server id
     *
     * @see Constants#INVALID_SERVER_ID
     */
    public int getCurrentServerId() {
        return currentServerId;
    }

    /**
     * Sets the ID of the server that is currently being accessed.
     *
     * @param currentServerId the new server ID
     */
    public void setCurrentServerId(int currentServerId) {
        this.currentServerId = currentServerId;
    }

    /**
     * This method is called when a new <code>User</code> object is bound to a sessions.
     * <br><br>
     *
     * <b>Note:</b> <i>There is no need to call this method!</i>
     *
     * @param event the event that identifies the session
     *
     * @see HttpSessionBindingListener
     */
    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        this.userSession = event.getSession();
    }

    /**
     * This method is called when a <code>User</code> object is unbound from a session.
     * <br><br>
     *
     * <b>Note:</b> <i>There is no need to call this method!</i>
     *
     * @param event the event that identifies the session
     *
     * @see HttpSessionBindingListener
     */
    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        manager.unregisterUser(this);
        manager = null;
        userData = null;
        permissions.clear();
        permissions = null;
    }
}
