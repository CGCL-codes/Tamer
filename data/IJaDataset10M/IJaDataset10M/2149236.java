package jhomenet.server.auth;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import org.apache.log4j.Logger;
import jhomenet.commons.JHomenetException;
import jhomenet.commons.auth.*;
import jhomenet.commons.event.*;
import jhomenet.commons.persistence.PersistenceException;

/**
 * This class is responsible for managing the application authentication.
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class DefaultAuthenticationManager implements AuthenticationManager {

    /**
	 * Define a logging mechanism.
	 */
    private static Logger logger = Logger.getLogger(DefaultAuthenticationManager.class.getName());

    /**
	 * 
	 */
    private static final Random RNG = new Random();

    /**
	 * List of authentication manager listeners.
	 */
    private final List<AuthenticationManagerListener> listeners = new CopyOnWriteArrayList<AuthenticationManagerListener>();

    /**
	 * Authentication facade that is used to communicate to the underlying
	 * database resources.
	 */
    private static final AuthPersistenceFacade authFacade = new DefaultAuthPersistenceFacade();

    /**
	 * A list of the currently logged in users.
	 */
    private final Map<String, Credential> loggedInUsers = new ConcurrentHashMap<String, Credential>();

    /**
	 * 
	 */
    private final ExecutorService pool;

    /**
	 * 
	 */
    private static final Integer poolSize = 2;

    /**
	 * Constructor.
	 */
    public DefaultAuthenticationManager() {
        super();
        pool = Executors.newFixedThreadPool(poolSize);
    }

    /**
	 * 
	 * @see jhomenet.commons.auth.AuthenticationManager#getCredentialList()
	 */
    public final List<User> getUserList(Credential authUser) {
        if (!this.isAuthenticated(authUser)) throw new JHomenetException("Un-authenticated users cannot add new users");
        try {
            return authFacade.findAllUsers();
        } catch (PersistenceException pe) {
            logger.error("Error while retrieving list of users: " + pe.getMessage(), pe);
            return new ArrayList<User>(0);
        }
    }

    /**
	 * @see jhomenet.commons.auth.AuthenticationManager#checkAdminUser()
	 */
    public final Boolean checkAdminUser() {
        try {
            if (authFacade.checkAdminUser() != null) return Boolean.TRUE; else return Boolean.FALSE;
        } catch (PersistenceException pe) {
            logger.error("Error while checking the admin user: " + pe.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
	 * @see jhomenet.commons.auth.AuthenticationManager#addUser(jhomenet.commons.auth.User)
	 */
    @Override
    public User addUser(User userToAdd, Credential authUser) throws JHomenetException {
        if (!this.isAuthenticated(authUser)) throw new JHomenetException("Un-authenticated users cannot add new users");
        if (!this.isAuthorized(authUser.getPrincipal().getName(), Roles.ADMIN)) throw new JHomenetException("Un-authorized users cannot add new users");
        try {
            userToAdd = authFacade.saveUser(userToAdd);
            this.fireUserAdded(userToAdd);
            return userToAdd;
        } catch (PersistenceException pe) {
            logger.error("Unable to add new user: " + pe.getMessage(), pe);
            throw new JHomenetException("Unable to add new user", pe);
        }
    }

    /**
	 * @see jhomenet.commons.auth.AuthenticationManager#deleteUser(jhomenet.commons.auth.User)
	 */
    @Override
    public User deleteUser(User userToDelete, Credential authUser) throws JHomenetException {
        if (!this.isAuthenticated(authUser)) throw new JHomenetException("Un-authenticated users cannot delete users");
        if (!this.isAuthorized(authUser.getPrincipal().getName(), Roles.ADMIN)) throw new JHomenetException("Un-authorized users cannot add new users");
        try {
            authFacade.deleteUser(userToDelete);
            this.fireUserDeleted(userToDelete);
            return userToDelete;
        } catch (PersistenceException pe) {
            logger.error("Unable to delete user: " + pe.getMessage(), pe);
            throw new JHomenetException("Unable to delete user", pe);
        }
    }

    /**
	 * @see jhomenet.commons.auth.AuthenticationManager#login(java.lang.String, java.lang.String)
	 */
    public final Credential login(String username, String passwordHash) {
        try {
            return this.authenticateUser(username, passwordHash).get();
        } catch (InterruptedException ie) {
            return null;
        } catch (ExecutionException ee) {
            return null;
        }
    }

    /**
	 * Get the list of currently logged in users.
	 * 
	 * @return
	 */
    public final List<Credential> getCurrentlyLoggedInUsers() {
        return new ArrayList<Credential>(loggedInUsers.values());
    }

    /**
	 * @see jhomenet.commons.auth.AuthenticationManager#logout(jhomenet.commons.auth.Credential)
	 */
    public Boolean logout(Credential userStatus) {
        if (logoutUser(userStatus.getPrincipal()) != null) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
	 * 
	 * @param username
	 * @param passwordHash
	 * @return
	 */
    private Future<Credential> authenticateUser(final String username, final String passwordHash) {
        logger.debug("Attempting to authentication user: " + username);
        return this.pool.submit(new AuthenticationCallable(username, passwordHash));
    }

    /**
	 * Add a user into the system. This method assumes that the passed username
	 * has already been authenticated. 
	 * 
	 * @param username
	 * @return
	 */
    private Credential loginUser(String username) {
        return loginUser(new User(username));
    }

    /**
	 * 
	 * @param user
	 * @return
	 */
    private Credential loginUser(User user) {
        if (loggedInUsers.containsKey(user.getName())) {
            loggedInUsers.remove(user.getName());
        }
        loggedInUsers.put(user.getName(), new Credential(user, RNG.nextLong()));
        EventLogger.addNewInfoEvent(DefaultAuthenticationManager.class.getName(), "User '" + user.getName() + "' logged in", EventCategory.SYSTEM);
        return loggedInUsers.get(user.getName());
    }

    /**
	 * Log a user out from the system.
	 * 
	 * @param principal
	 */
    private Credential logoutUser(Principal principal) {
        return logoutUser(principal.getName());
    }

    /**
	 * Log a user out from the system.
	 * 
	 * @param username
	 */
    private Credential logoutUser(String username) {
        return loggedInUsers.remove(username);
    }

    /**
	 * @see jhomenet.commons.auth.AuthenticationManager#isAuthenticated(jhomenet.commons.auth.Credential)
	 */
    public final Boolean isAuthenticated(Credential authUser) {
        if (authUser == null) return Boolean.FALSE;
        Credential us = this.loggedInUsers.get(authUser.getPrincipal().getName());
        if (us == null) return Boolean.FALSE;
        if (authUser.getLoginId().equals(us.getLoginId())) return Boolean.TRUE; else return Boolean.FALSE;
    }

    /**
	 * 
	 * @param user
	 * @param requiredRole
	 * @return
	 */
    private Boolean isAuthorized(String username, Roles requiredRole) {
        User dbUser = authFacade.getUserByUsername(username);
        if (dbUser != null) {
            if (requiredRole.isCompatible(dbUser.getRole())) return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
	 * @see jhomenet.commons.auth.AuthenticationManager#addAuthenticationManagerListener(jhomenet.commons.auth.AuthenticationManagerListener)
	 */
    @Override
    public void addAuthenticationManagerListener(AuthenticationManagerListener l) {
        this.listeners.add(l);
    }

    /**
	 * @see jhomenet.commons.auth.AuthenticationManager#removeAuthenticationManagerListener(jhomenet.commons.auth.AuthenticationManagerListener)
	 */
    @Override
    public void removeAuthenticationManagerListener(AuthenticationManagerListener l) {
        this.listeners.remove(l);
    }

    /**
	 * 
	 * @param c
	 */
    private void fireUserAdded(Principal p) {
        for (AuthenticationManagerListener l : listeners) {
            try {
                l.userAdded(new AuthenticationManagerEvent(p, this));
            } catch (RuntimeException e) {
                logger.error("Unexpected exception while notifying listeners of added user: " + e.getMessage(), e);
                listeners.remove(l);
            }
        }
    }

    /**
	 * 
	 * @param c
	 */
    private void fireUserDeleted(Principal p) {
        for (AuthenticationManagerListener l : listeners) {
            try {
                l.userDeleted(new AuthenticationManagerEvent(p, this));
            } catch (RuntimeException e) {
                logger.error("Unexpected exception while notifying listeners of deleted user: " + e.getMessage(), e);
                listeners.remove(l);
            }
        }
    }

    private class AuthenticationCallable implements Callable<Credential> {

        /**
		 * 
		 */
        private final String username, passwordHash;

        /**
		 * 
		 */
        public AuthenticationCallable(String username, String passwordHash) {
            super();
            this.username = username;
            this.passwordHash = passwordHash;
        }

        /**
		 * @see java.util.concurrent.Callable#call()
		 */
        @Override
        public Credential call() throws Exception {
            if (loggedInUsers.containsKey(username)) {
                logger.info("User '" + username + "' already logged into the system!");
                return loggedInUsers.get(username);
            }
            if (authFacade.checkCredentials(username, passwordHash)) {
                return loginUser(username);
            } else {
                return null;
            }
        }
    }
}
