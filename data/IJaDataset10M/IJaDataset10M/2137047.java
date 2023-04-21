package org.amlfilter.service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.amlfilter.dao.DAOUserInterface;
import org.amlfilter.model.User;
import org.amlfilter.service.GenericService;
import org.amlfilter.service.AMLFException;
import org.amlfilter.util.Base64Util;
import org.amlfilter.util.CryptographicUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;

/**
 * Provides utilities to manage the user lifecycle
 *
 * @author Harish Seshadri
 * @version $Id: UserService.java,v 1.5 2007/07/03 05:51:48 hseshadr Exp $
 */
public class UserService extends GenericService implements UserServiceInterface, InitializingBean {

    public static final String PROFILE_USER = "SSSUser";

    public static final String USER_DOES_NOT_EXIST = "The favor does not exist";

    public static final String USER_OBJ_NULL = "The user object cannot be null";

    public static final String HTTP_SESSION_NULL = "The http session cannot be null";

    public static final String HTTP_SERVLET_REQUEST_NULL = "The http servlet request cannot be null";

    private DAOUserInterface mDAOUser;

    /**
     * Get the DAO  config entry
     * @return The DAO  config entry
     */
    public DAOUserInterface getDAOUser() {
        return mDAOUser;
    }

    /**
     * Set the DAO  config entry
     * @param pDAOUser The DAO  config entry
     */
    public void setDAOUser(DAOUserInterface pDAOUser) {
        mDAOUser = pDAOUser;
    }

    /**
	 * Authenticate the user using basic authorization
     * @param pRequest The http request
     * @throws AMLFException
	 */
    public void authenticateUserByBasicAuth(HttpServletRequest pRequest) throws AMLFException {
        final String methodSignature = "void authenticateUserByBasicAuth(HttpServletRequest): ";
        if (null == pRequest) {
            throw new AMLFException(HTTP_SERVLET_REQUEST_NULL);
        }
        String authHeader = pRequest.getHeader("Authorization");
        logInfo(methodSignature + authHeader);
        String authVal = authHeader.substring(6);
        try {
            String userNameAndPassword = Base64Util.decodeAsString(authVal);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
	 * Login the user given the user and the session
     * @param pUser The user
     * @param pSession The http session
	 */
    public void loginUser(User pUser, HttpSession pSession) throws AMLFException {
        final String methodSignature = "void loginUser(User,HttpSession): ";
        if (null == pUser) {
            throw new AMLFException(USER_OBJ_NULL);
        }
        if (null == pSession) {
            throw new AMLFException(HTTP_SESSION_NULL);
        }
        pSession.setAttribute(PROFILE_USER, pUser);
    }

    /**
	 * Login the user given the request
     * @param pUser The user
     * @param pRequest The http request
	 */
    public void loginUser(User pUser, HttpServletRequest pRequest) throws AMLFException {
        final String methodSignature = "void loginUser(User,HttpServletRequest): ";
        if (null == pUser) {
            throw new AMLFException(USER_OBJ_NULL);
        }
        if (null == pRequest) {
            throw new AMLFException(HTTP_SERVLET_REQUEST_NULL);
        }
        loginUser(pUser, pRequest.getSession());
    }

    /**
	 * Logout the user
     * @param pUser The user
     * @param pRequest The http request
	 */
    public void logoutUser(User pUser, HttpSession pSession) throws AMLFException {
        final String methodSignature = "void logoutUser(User): ";
        if (null == pUser) {
            throw new AMLFException(USER_OBJ_NULL);
        }
        if (null == pSession) {
            throw new AMLFException(HTTP_SESSION_NULL);
        }
        pSession.setAttribute(PROFILE_USER, null);
    }

    /**
	 * Logout the user
     * @param pUser The user
     * @param pRequest The http request
	 */
    public void logoutUser(User pUser, HttpServletRequest pRequest) throws AMLFException {
        final String methodSignature = "void logoutUser(User): ";
        if (null == pUser) {
            throw new AMLFException(USER_OBJ_NULL);
        }
        logoutUser(pUser, pRequest.getSession());
    }

    /**
	 * Is the user logged in?
     * @param pUser The user
     * @param pSession The http session
	 */
    public boolean isUserLoggedIn(User pUser, HttpSession pSession) throws AMLFException {
        final String methodSignature = "void isUserLoggedIn(User,HttpSession): ";
        if (null == pUser) {
            throw new AMLFException(USER_OBJ_NULL);
        }
        if (!doesUserExist(pUser)) {
            return false;
        }
        if (null == pSession.getAttribute(PROFILE_USER)) {
            return false;
        }
        return true;
    }

    /**
	 * Is the user logged in?
     * @param pUser The user
     * @param pRequest The http request
	 */
    public boolean isUserLoggedIn(User pUser, HttpServletRequest pRequest) throws AMLFException {
        final String methodSignature = "void isUserLoggedIn(User,HttpServletRequest): ";
        if (null == pUser) {
            throw new AMLFException(USER_OBJ_NULL);
        }
        return isUserLoggedIn(pUser, pRequest.getSession());
    }

    /**
     * Does the user exist?
     * @param pUser The user
     * @return True if it exists, false otherwise
     */
    public boolean doesUserExist(User pUser) throws AMLFException {
        return getDAOUser().doesUserExist(pUser);
    }

    /**
	 * Get the user
     * @param pUserId The user id
	 * @return The user object
	 */
    public User getUser(Long pUserId) throws AMLFException {
        final String methodSignature = "User getUser(Long): ";
        if (null == pUserId) {
            throw new IllegalArgumentException("The favor id cannot be null");
        }
        User user = null;
        try {
            user = getDAOUser().getUser(pUserId);
        } catch (DataAccessException dae) {
            Map parametersMap = new HashMap();
            parametersMap.put("userId", pUserId);
            throw new AMLFException(dae.getMessage(), methodSignature, parametersMap);
        }
        if (null == user) {
            Map parametersMap = new HashMap();
            parametersMap.put("userId", pUserId);
            throw new AMLFException(USER_DOES_NOT_EXIST, methodSignature, parametersMap);
        }
        return user;
    }

    /**
     * Get the user by login
     * @param pUserName The user login
     * @return The user
     * @throws AMLFException
     */
    public User getUser(String pLogin) throws AMLFException {
        final String methodSignature = "User getUser(String): ";
        logInfo("Finding user by login: " + pLogin);
        if (null == pLogin) {
            return null;
        }
        return getDAOUser().getUser(pLogin);
    }

    /**
     * Get the user by login & password
     * @param pLogin The user login
     * @param pClearTextPassword The clear text password
     * @return The user
     * @throws AMLFException
     */
    public User getUser(String pLogin, String pClearTextPassword) throws AMLFException {
        final String methodSignature = "User getUser(String, String): ";
        if (null == pLogin) {
            return null;
        }
        logInfo("Finding user by login: " + pLogin + "; and clear text password: " + pClearTextPassword);
        String password = null;
        try {
            password = CryptographicUtils.generateMD5Hash(pClearTextPassword.trim());
        } catch (NoSuchAlgorithmException nsae) {
            throw new AMLFException(nsae.getMessage());
        }
        return getDAOUser().getUser(pLogin, password);
    }

    /**
     * Get all the users
     * @return A list with all the users
     * @throws AMLFException
     */
    public List getAllUsers() throws AMLFException {
        return getDAOUser().getAllUsers();
    }

    /**
     * Update the user
     * @param pUser The user
     */
    public void updateUser(User pUser) throws AMLFException {
        final String methodSignature = "void updateUser(User): ";
        String password = null;
        try {
            password = CryptographicUtils.generateMD5Hash(pUser.getPassword().trim());
        } catch (NoSuchAlgorithmException nsae) {
            throw new AMLFException(nsae.getMessage());
        }
        pUser.setPassword(password);
        logInfo(methodSignature + "Hashed password: " + password);
        getDAOUser().updateUser(pUser);
    }

    /**
     * Store the user only if it is unique
     * @param pUser The user
     */
    public void storeUser(User pUser) throws AMLFException {
        final String methodSignature = "void storeUser(User): ";
        if (getDAOUser().doesUserExist(pUser)) {
            throw new EntityAlreadyExistsException("The user with login " + pUser.getEmail() + " already exists");
        }
        String password = null;
        try {
            password = CryptographicUtils.generateMD5Hash(pUser.getPassword().trim());
        } catch (NoSuchAlgorithmException nsae) {
            throw new AMLFException(nsae.getMessage());
        }
        pUser.setPassword(password);
        logInfo(methodSignature + "Hashed password: " + password);
        getDAOUser().storeUser(pUser);
    }

    /**
     * Refresh the user
     * @param pUser The user
     */
    public void refreshUser(User pUser) throws AMLFException {
        getDAOUser().refreshUser(pUser);
    }

    /**
     * Delete the user
     * @param pUser The user
     */
    public void deleteUser(User pUser) throws AMLFException {
        getDAOUser().deleteUser(pUser);
    }

    /**
     * Show the user
     * @param pUser The user
     * @return The string representation of the user
     */
    public String showUser(User pUser) throws AMLFException {
        return pUser.toString();
    }
}
