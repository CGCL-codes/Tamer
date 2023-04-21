package eu.mpower.security.accesscontrol;

import java.util.*;
import javax.security.auth.*;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import javax.security.auth.spi.*;
import eu.mpower.security.dbaccesscode.*;
import eu.mpower.framework.security.UserPrincipal;
import eu.mpower.framework.security.RolePrincipal;
import eu.mpower.framework.security.PrimaryRolePrincipal;
import eu.mpower.framework.security.types.soap.*;

/**
 * This LoginModule authenticates users with a password, by verifying the 
 * provided user name and password with the values stored in the database.
 * If user successfully authenticates itself, a UserPrincipal with the 
 * user's user name is added to the Subject.
 *
 * This LoginModule recognizes the debug option.
 * If set to true in the login Configuration,
 * debug messages will be output to the output stream, System.out.
 *
 * @author Eleni Themistokleous
 * @version 4, 03 April 2008
 * 
 */
public class UserPassLoginModule implements LoginModule {

    private Subject subject;

    private CallbackHandler callbackHandler;

    private Map sharedState;

    private Map options;

    private boolean debug = false;

    private boolean succeeded = false;

    private boolean commitSucceeded = false;

    private String username;

    private char[] password;

    private UserPrincipal userPrincipal;

    private RolePrincipal rolePrincipal;

    private PrimaryRolePrincipal primaryRolePrincipal;

    /**
     * Initialize this LoginModule.
     *
     * @param subject the <code>Subject</code> to be authenticated. 
     * @param callbackHandler a <code>CallbackHandler</code> for communicating
     * with the end user (storing the provided user name and password). 
     * @param options options specified in the login <code>Configuration</code>
     * for this particular <code>LoginModule</code>.
     */
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<java.lang.String, ?> sharedState, Map<java.lang.String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
        debug = "true".equalsIgnoreCase((String) options.get("debug"));
    }

    /**
     * Authenticate the user by verifying the provided user name and password.
     * @return true in all cases since this <code>LoginModule</code>
     *		should not be ignored.
     *
     * @exception FailedLoginException if the authentication fails. <p>
     *
     * @exception LoginException if this <code>LoginModule</code>
     *		is unable to perform the authentication.
     */
    public boolean login() throws LoginException {
        if (callbackHandler == null) throw new LoginException("Error: no CallbackHandler available to" + "garner authentication information from the user");
        try {
            Callback[] callbacks = new Callback[2];
            callbacks[0] = new NameCallback("user name: ");
            callbacks[1] = new PasswordCallback("password: ", false);
            callbackHandler.handle(callbacks);
            username = ((NameCallback) callbacks[0]).getName();
            char[] tmpPassword = ((PasswordCallback) callbacks[1]).getPassword();
            if (tmpPassword == null) {
                tmpPassword = new char[0];
            }
            password = new char[tmpPassword.length];
            System.arraycopy(tmpPassword, 0, password, 0, tmpPassword.length);
            ((PasswordCallback) callbacks[1]).clearPassword();
        } catch (java.io.IOException ioe) {
            throw new LoginException(ioe.toString());
        } catch (UnsupportedCallbackException uce) {
            throw new LoginException("Error: " + uce.getCallback().toString() + " not available to garner authentication information " + "from the user");
        }
        if (debug) {
            System.out.println("\t\t[UserPassLoginModule] " + "user entered user name: " + username);
            System.out.print("\t\t[UserPassLoginModule] " + "user entered password: ");
            for (int i = 0; i < password.length; i++) System.out.print(password[i]);
            System.out.println();
        }
        boolean usernameCorrect = false;
        boolean passwordCorrect = false;
        eu.mpower.security.dbaccesscode.DBConnector con = new DBConnector();
        if ((con.AuthenticateUser(username, String.valueOf(password))) != null) {
            usernameCorrect = true;
            passwordCorrect = true;
            if (debug) System.out.println("\t\t[UserPassLoginModule] " + "authentication succeeded");
            succeeded = true;
            return true;
        } else {
            if (debug) System.out.println("\t\t[UserPassLoginModule] " + "authentication failed");
            succeeded = false;
            username = null;
            for (int i = 0; i < password.length; i++) password[i] = ' ';
            password = null;
            if (!usernameCorrect) {
                throw new FailedLoginException("User Name Incorrect");
            } else {
                throw new FailedLoginException("Password Incorrect");
            }
        }
    }

    /**
     * This method is called if the LoginContext's overall authentication succeeded
     * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
     * succeeded).
     *
     * If this LoginModule's own authentication attempt
     * succeeded (checked by retrieving the private state saved by the
     * <code>login</code> method), then this method associates a
     * <code>UserPrincipal</code> with the <code>Subject</code> located in the
     * <code>LoginModule</code>.  If this LoginModule's own
     * authentication attempted failed, then this method removes
     * any state that was originally saved.
     *
     * @exception LoginException if the commit fails.
     *
     * @return true if this LoginModule's own login and commit
     *		attempts succeeded, or false otherwise.
     */
    public boolean commit() throws LoginException {
        if (succeeded == false) {
            return false;
        } else {
            eu.mpower.security.dbaccesscode.DBConnector conn = new DBConnector();
            eu.mpower.framework.security.types.soap.UserInfo userinfo = (conn.getUserInfo(username)).getUserInfo();
            int useridentity = userinfo.getUserID();
            userPrincipal = new UserPrincipal(Integer.toString(useridentity));
            if (!subject.getPrincipals().contains(userPrincipal)) subject.getPrincipals().add(userPrincipal);
            primaryRolePrincipal = new PrimaryRolePrincipal(userinfo.getPrimaryRoleName());
            if (!subject.getPrincipals().contains(primaryRolePrincipal)) subject.getPrincipals().add(primaryRolePrincipal);
            int rolesno = (userinfo.getRoles()).size();
            List<eu.mpower.framework.security.types.soap.RoleInfo> roles = new ArrayList<RoleInfo>(rolesno);
            roles = userinfo.getRoles();
            for (int i = 0; i < roles.size(); i++) {
                rolePrincipal = new RolePrincipal(roles.get(i).getRoleName());
                if (!subject.getPrincipals().contains(rolePrincipal)) subject.getPrincipals().add(rolePrincipal);
            }
            if (debug) {
                System.out.println("\t\t[UserPassLoginModule] " + "added UserPrincipal and RolePrincipal to Subject");
            }
            username = null;
            for (int i = 0; i < password.length; i++) password[i] = ' ';
            password = null;
            commitSucceeded = true;
            return true;
        }
    }

    /**
     * This method is called if the LoginContext's overall authentication failed.
     * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
     * did not succeed).
     *
     * If this LoginModule's own authentication attempt
     * succeeded (checked by retrieving the private state saved by the
     * <code>login</code> and <code>commit</code> methods),
     * then this method cleans up any state that was originally saved.
     *
     * @exception LoginException if the abort fails.
     *
     * @return false if this LoginModule's own login and/or commit attempts
     *		failed, and true otherwise.
     */
    public boolean abort() throws LoginException {
        if (succeeded == false) {
            return false;
        } else if (succeeded == true && commitSucceeded == false) {
            succeeded = false;
            username = null;
            if (password != null) {
                for (int i = 0; i < password.length; i++) password[i] = ' ';
                password = null;
            }
            userPrincipal = null;
        } else {
            logout();
        }
        return true;
    }

    /**
     * Logout the user.
     *
     * <p> This method removes the <code>UserPrincipal</code>
     * that was added by the <code>commit</code> method.
     *
     * @exception LoginException if the logout fails.
     *
     * @return true in all cases since this <code>LoginModule</code>
     *          should not be ignored.
     */
    public boolean logout() throws LoginException {
        subject.getPrincipals().remove(userPrincipal);
        succeeded = false;
        succeeded = commitSucceeded;
        username = null;
        if (password != null) {
            for (int i = 0; i < password.length; i++) password[i] = ' ';
            password = null;
        }
        userPrincipal = null;
        return true;
    }
}
