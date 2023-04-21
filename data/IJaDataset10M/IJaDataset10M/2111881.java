package Managers;

import javax.servlet.http.HttpSession;
import Beans.UserBean;
import Beans.PermissionsBean;
import Managers.Utilities.ResponseMessagesContainer;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import Managers.Utilities.User;
import Utilities.ConfigurationReader;
import Utilities.Constants;
import Utilities.ParameterNames;
import Utilities.Interfaces.Controllable;
import Utilities.Interfaces.SelfValidating;

/**
 * Manager for user sessions. <br><br>
 * A user session is considered valid only if it has been registered with this manager.
 *
 * @see SessionsManager#loginUser(int, javax.servlet.http.HttpSession) User Login
 *
 * @author Angel Sanadinov
 */
public class SessionsManager implements SelfValidating, Controllable {

    private Map<Integer, User> users;

    private DatabaseManager database;

    private LogsManager logsManager;

    private VBoxConnectionsManager connectionsManager;

    private int maxResponseMessages;

    /**
     * Constructs the manager using the supplied arguments. <br><br>
     *
     * A synchronized <code>HashMap</code> is used to store data about each user
     * that has logged in and the initial capacity and load factor can be set to aid
     * the scalability of the application. Refer to the <code>HashMap</code>
     * documentation for more information.
     *
     * @param database reference to the database manager
     * @param logger reference to the logs manager
     * @param conManager reference to the VirtualBox connections manager
     * @param configuration configuration parameters for the manager
     */
    public SessionsManager(DatabaseManager database, LogsManager logger, VBoxConnectionsManager conManager, ConfigurationReader configuration) {
        this.database = database;
        this.logsManager = logger;
        this.connectionsManager = conManager;
        int mapInitialCapacity = 0;
        float mapLoadFactor = 0.75f;
        this.maxResponseMessages = 1;
        try {
            mapInitialCapacity = Integer.parseInt(configuration.getParameter(ConfigurationReader.PARAM_SESSIONS_USERS_MAP_INITIAL_CAPACITY));
            mapLoadFactor = Float.parseFloat(configuration.getParameter(ConfigurationReader.PARAM_SESSIONS_USERS_MAP_LOAD_FACTOR));
            maxResponseMessages = Integer.parseInt(configuration.getParameter(ConfigurationReader.PARAM_MAX_RESPONSE_MESSAGES));
        } catch (NumberFormatException e) {
            logsManager.logSystemEvent(System.currentTimeMillis(), Constants.LOG_SEVERITY_WARNING, "Sessions Manager: Failed to parse initialization parameter(s). " + "Values: parameter(C:current/D:default); " + "initialCapacity(C:" + mapInitialCapacity + "/D:0); " + "loadFactor(C:" + mapLoadFactor + "/D:0.75); " + "maxResponseMessages(C:" + maxResponseMessages + "/D:1);");
        }
        this.users = Collections.synchronizedMap(new HashMap<Integer, User>(mapInitialCapacity, mapLoadFactor));
        this.logsManager.logSystemEvent(System.currentTimeMillis(), Constants.LOG_SEVERITY_INFORMATION, "Sessions Manager started...");
    }

    /**
     * Verifies that the user bound to the supplied session object is registered
     * with the sessions manager.
     *
     * @param userSession the user session
     *
     * @return <code>true</code> if the user in the supplied session matches a
     *         user registered with the manager.
     */
    public boolean verifyUserSession(HttpSession userSession) {
        boolean result = false;
        if (userSession != null) {
            User userFromSession = (User) userSession.getAttribute(ParameterNames.SESSION_USER_OBJECT);
            if (userFromSession != null && userFromSession.isValid()) {
                User userFromMap = users.get(userFromSession.getUserData().getUserId());
                if (userFromMap != null && userFromMap.getUserData().getUserId() == userFromSession.getUserData().getUserId()) result = true; else ;
            } else ;
            return result;
        } else return false;
    }

    /**
     * Logs in a user into the system. <br><br>
     * <b>Note:</b> <i>Authentication should be performed before this method is
     * called!</i>
     *
     * @param userId the id of the user to be logged in
     * @param userSession the server session associated with that user
     * @param userRemoteAddress the IP address of the user to be logged in
     *
     * @return <code>true</code> if the user was successfully logged in or
     *         <code>false</code> otherwise
     *
     * @see SessionsManager Sessions Manager
     */
    public boolean loginUser(int userId, HttpSession userSession, String userRemoteAddress) {
        User newUser = new User(database.getUser(userId), userSession, this, database.getAllPermissionsForUser(userId));
        ResponseMessagesContainer messagesContainer = new ResponseMessagesContainer(maxResponseMessages);
        if (newUser.isValid() && newUser.getUserData().getAccountLevel() > Constants.DB_USER_ACCOUNT_LEVEL_DISABLED && newUser.getUserData().getUserId() == userId) {
            users.put(userId, newUser);
            userSession.setAttribute(ParameterNames.SESSION_USER_OBJECT, newUser);
            userSession.setAttribute(ParameterNames.SESSION_REQUEST_MESSAGES_OBJECT, messagesContainer);
            logsManager.log(System.currentTimeMillis(), Constants.LOG_SEVERITY_USER_LOGIN, "User logged in from " + userRemoteAddress, userId);
            logsManager.logSystemEvent(System.currentTimeMillis(), Constants.LOG_SEVERITY_DEBUG, "User logged in (" + userId + "," + userRemoteAddress + ")");
            database.updateUserLastLogin(userId, userRemoteAddress);
            return true;
        } else return false;
    }

    /**
     * Logs out a user by invalidating the associated session.
     *
     * @param userId the id of the user that is logging out
     * @param userSession the session of the user
     *
     * @return <code>true</code> if the user was successfully logged out
     */
    public boolean logoutUser(int userId, HttpSession userSession) {
        User userLoggingOut = users.get(userId);
        if (userLoggingOut != null && userLoggingOut.compareSessionId(userSession.getId())) {
            userSession.invalidate();
            return true;
        } else return false;
    }

    /**
     * Removes the user object from the map. <br><br>
     *
     * <b>Note:</b> <i><code>logoutUser()</code> should be used for regular user logouts!</i>
     *
     * @param user the user object to be removed from the map
     * @see SessionsManager#logoutUser(int, javax.servlet.http.HttpSession) User Logout
     */
    public void unregisterUser(User user) {
        if (user.isValid() && users != null && users.containsValue(user)) {
            users.remove(user.getUserData().getUserId());
            connectionsManager.unregisterConnectionUser(user.getUserData().getUserId());
        } else ;
    }

    /**
     * Updates the data that is stored in the specified user's session. <br><br>
     *
     * The data for the specified user is retrieved from the database and the user
     * session is updated with it.
     * <br><br>
     * <b>Note:</b> <i>This method should only be called when the user data was
     *                 modified and an update from the database is necessary.</i>
     *
     * @param userId the id of the user that needs to be updated
     */
    public void updateUserData(int userId) {
        User user = users.get(userId);
        if (user != null) {
            user.updateUserData(database.getUser(userId));
            if (user.getUserData().getAccountLevel() <= Constants.DB_USER_ACCOUNT_LEVEL_DISABLED) logoutUser(userId, user.getUserSession()); else ;
            logsManager.logSystemEvent(System.currentTimeMillis(), Constants.LOG_SEVERITY_DEBUG, "Data was updated for user: " + userId);
        } else ;
    }

    /**
     * Updates the permissions of the specified user for the specified server. <br><br>
     *
     * The permissions data for the specified user and server is retrieved from
     * the database and the user session is updated with it.
     * <br><br>
     * <b>Note:</b> <i>This method should only be called when the permissions were
     *                 modified and an update from the database is necessary.</i>
     * @param userId the id of the user
     * @param serverId the server associated with the permissions
     */
    public void updateUserPermissions(int userId, int serverId) {
        User user = users.get(userId);
        if (user != null) {
            PermissionsBean permissions = database.getPermissions(userId, serverId);
            if (permissions != null && permissions.isValid()) user.updatePermissions(permissions); else user.removePermissions(serverId);
            logsManager.logSystemEvent(System.currentTimeMillis(), Constants.LOG_SEVERITY_DEBUG, "Permissions were updated for user: " + userId);
        } else ;
    }

    /**
     * Retrieves the permissions for the specified user on the specified server.
     *
     * @param userId the id of the user
     * @param serverId the id of the server
     *
     * @return the requested permissions or <code>null</code> if they could not
     *         be retrieved
     */
    public PermissionsBean getPermissionsForUserOnServer(int userId, int serverId) {
        PermissionsBean result = null;
        User user = users.get(userId);
        if (user != null) result = user.getPermissions(serverId); else ;
        return result;
    }

    /**
     * Retrieves user data based on the id of the user.
     *
     * @param userId the id of the user
     *
     * @return the requested user data or <code>null</code> if it could not be retrieved
     */
    public UserBean getUserData(int userId) {
        UserBean result = null;
        User user = users.get(userId);
        if (user != null) result = user.getUserData(); else ;
        return result;
    }

    /**
     * Checks the validity of the manager. <br><br>
     * The manager is valid only if the reference to the required managers are available
     * and the users map is initialized.
     *
     * @return <code>true</code> if the manager is valid
     */
    @Override
    public boolean isValid() {
        if (users != null && database != null && connectionsManager != null && logsManager != null) return true; else return false;
    }

    /**
     * Stops the manager and removes any associated resource references.
     */
    @Override
    public void stop() {
        logsManager.logSystemEvent(System.currentTimeMillis(), Constants.LOG_SEVERITY_INFORMATION, "Sessions Manager stopping...");
        users.clear();
        users = null;
        database = null;
        logsManager = null;
        connectionsManager = null;
    }
}
