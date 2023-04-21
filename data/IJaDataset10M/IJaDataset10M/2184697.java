package unibg.overencrypt.server.managers;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unibg.overencrypt.core.EditPermissionManager;

/**
 * Manages all operation of edit permissions.
 *
 * @author Flavio Giovarruscio & Riccardo Tribbia
 * @version 1.0
 */
public class ServerEditPermissionManager {

    /** Logger for this class. */
    private static Logger LOGGER = LoggerFactory.getLogger(ServerEditPermissionManager.class);

    /**
	 * Sets the edit permission manager.
	 *
	 * @param  users the users
	 * @param  userId the user id
	 * @param  aclSEL the SEL acl
	 * @param  aclBEL the BEL acl
	 * @return a string array containing the new SEL acl, the users to add and the user to delete
	 */
    public static String[] setEditPermManager(String users, String userId, String aclSEL, String aclBEL) {
        String returnValues[] = new String[3];
        String newSEL = "";
        String addUser = "";
        String delUser = "";
        try {
            SessionManager.saveSession(userId, new EditPermissionManager());
            SessionManager.getEditPermissionManager(userId).setEditPermissionManager(users, userId, aclSEL, aclBEL);
            SessionManager.getEditPermissionManager(userId).prepareEditPermission();
            newSEL = SessionManager.getEditPermissionManager(userId).getNewACL();
            addUser = SessionManager.getEditPermissionManager(userId).getAddedUsers();
            delUser = SessionManager.getEditPermissionManager(userId).getRemovedUsers();
        } catch (SQLException sqlException) {
            LOGGER.error(sqlException.getMessage());
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
        }
        returnValues[0] = newSEL;
        returnValues[1] = addUser;
        returnValues[2] = delUser;
        return returnValues;
    }

    /**
	 * Gets the token info.
	 *
	 * @return a string array containing the token id and a boolean that verify if next token will be the last
	 */
    public static String[] getTokenInfo(String user) {
        String returnValues[] = new String[2];
        String idToken = "";
        try {
            idToken = SessionManager.getEditPermissionManager(user).getTokenID();
        } catch (Exception e) {
            LOGGER.error("Error while retrieving token ID for editing permissions", e);
        }
        boolean hasMoreTokens = SessionManager.getEditPermissionManager(user).hasMoreToken();
        returnValues[0] = idToken;
        returnValues[1] = String.valueOf(hasMoreTokens);
        return returnValues;
    }

    /**
	 * Creates the JSON token HBEL.
	 *
	 * @param user the logged user
	 * @return the token HBEL in JSON format
	 */
    public static String createJsonForTokenHBEL(String user) {
        String tokenHBEL = "";
        try {
            tokenHBEL = SessionManager.getEditPermissionManager(user).createJson();
        } catch (Exception exception) {
            LOGGER.debug(exception.getMessage());
        }
        return tokenHBEL;
    }

    /**
	 * Save token in database.
	 * 
	 * @param user the logged user
	 * @param token the token to save
	 */
    public static void putToken(String user, String token) {
        try {
            SessionManager.getEditPermissionManager(user).putTokensInDBWPES(token);
        } catch (Exception exception) {
            LOGGER.debug(exception.getMessage());
        }
    }
}
