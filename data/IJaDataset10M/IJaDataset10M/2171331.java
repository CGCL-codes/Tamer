package flex.messaging.security;

import java.security.Principal;
import java.util.List;
import flex.messaging.FlexComponent;
import flex.messaging.FlexContext;
import flex.messaging.config.ConfigMap;
import flex.messaging.config.ConfigurationException;
import flex.messaging.config.SecurityConstraint;
import flex.messaging.endpoints.Endpoint;
import flex.messaging.log.Log;
import flex.messaging.log.LogCategories;

/**
 * Much of this logic has been taken from the Flash Remoting Gateway.
 * <p>
 * Since each application server manages sessions, users and security
 * differently, a separate LoginCommand needs to be written for
 * each server.
 * </p>
 *
 * @author Peter Farland
 * @exclude
 */
public class LoginManager implements FlexComponent {

    /** Log category for LoginManager. */
    public static final String LOG_CATEGORY = LogCategories.SECURITY;

    private static final int INVALID_LOGIN = 10050;

    private static final int LOGIN_REQ = 10051;

    private static final int NO_LOGIN_COMMAND = 10053;

    private static final int CANNOT_REAUTH = 10054;

    private static final int ACCESS_DENIED = 10055;

    private static final int LOGIN_REQ_FOR_AUTH = 10056;

    private static final int NO_BASIC_SECURITY = 10057;

    private static final int PER_CLIENT_ANT_APPSERVER = 10065;

    private LoginCommand loginCommand;

    private boolean perClientAuthentication;

    private boolean started;

    /**
     * Creates a new <code>LoginManager</code> instance.
     */
    public LoginManager() {
        perClientAuthentication = false;
    }

    /**
     * Implements FlexComponents.initialize.
     * This is no-op for LoginManager as it does not have an id and all
     * its properties are directly settable.
     *
     * @param id The id of the component.
     * @param configMap The properties for configuring component.
     */
    public void initialize(String id, ConfigMap configMap) {
    }

    /**
     * Validates the LoginManager before it is started.
     */
    protected void validate() {
        if (perClientAuthentication && loginCommand instanceof AppServerLoginCommand) {
            ConfigurationException configException = new ConfigurationException();
            configException.setMessage(PER_CLIENT_ANT_APPSERVER);
            throw configException;
        }
    }

    /**
     * Implements FlexComponent.start.
     * Starts the <code>LoginManager</code>.
     */
    public void start() {
        if (!started) {
            validate();
            if (loginCommand != null) loginCommand.start(FlexContext.getServletConfig());
            started = true;
        }
    }

    /**
     * Implements FlexComponents.stop.
     * Stops the <code>LoginManager</code>.
     */
    public void stop() {
        if (started) {
            if (loginCommand != null) loginCommand.stop();
            started = false;
        }
    }

    /**
     * Returns whether per client authentication is enabled or not.
     *
     * @return <code>true</code> if per client authentication is enabled;
     * otherwise <code>false</code>.
     */
    public boolean isPerClientAuthentication() {
        return perClientAuthentication;
    }

    /**
     * Sets whether per client authentication is enabled or not.
     *
     * @param perClientAuthentication <code>true</code> if per client authentication
     * is enabled; otherwise <code>false</code>.
     */
    public void setPerClientAuthentication(boolean perClientAuthentication) {
        this.perClientAuthentication = perClientAuthentication;
    }

    /**
     * Implements FlexComponent.isStarted.
     * Returns whether the LoginManager is started or not.
     *
     * @return <code>true</code> if the LoginManager is started; otherwise <code>false</code>.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Returns the login command used.
     *
     * @return loginCommand The login command used.
     */
    public LoginCommand getLoginCommand() {
        return loginCommand;
    }

    /**
     * Sets the login command used.
     *
     * @param value The login command to set.
     */
    public void setLoginCommand(LoginCommand value) {
        if (loginCommand == value) return;
        loginCommand = value;
        if (started) loginCommand.start(FlexContext.getServletConfig());
    }

    /**
     * Perform login with username and credentials.
     *
     * @param username Username to use to login.
     * @param credentials Credentials to use to login.
     */
    public void login(String username, Object credentials) {
        if (getCurrentPrincipal() == null) {
            if (loginCommand != null) {
                if (username != null && credentials != null) {
                    Principal authenticated = loginCommand.doAuthentication(username, credentials);
                    if (authenticated == null) {
                        SecurityException se = new SecurityException();
                        se.setMessage(INVALID_LOGIN);
                        se.setCode(SecurityException.CLIENT_AUTHENTICATION_CODE);
                        throw se;
                    }
                    setCurrentPrincipal(authenticated);
                } else {
                    SecurityException se = new SecurityException();
                    se.setMessage(LOGIN_REQ);
                    se.setCode(SecurityException.CLIENT_AUTHENTICATION_CODE);
                    throw se;
                }
            } else {
                SecurityException se = new SecurityException();
                se.setMessage(NO_LOGIN_COMMAND);
                se.setCode(SecurityException.SERVER_AUTHENTICATION_CODE);
                throw se;
            }
        } else {
            String comparisonUsername;
            if (loginCommand instanceof LoginCommandExt) {
                comparisonUsername = ((LoginCommandExt) loginCommand).getPrincipalNameFromCredentials(username, credentials);
            } else {
                comparisonUsername = username;
            }
            if (comparisonUsername != null && !comparisonUsername.equals(getCurrentPrincipal().getName())) {
                SecurityException se = new SecurityException();
                se.setMessage(CANNOT_REAUTH);
                se.setCode(SecurityException.CLIENT_AUTHENTICATION_CODE);
                throw se;
            }
        }
    }

    /**
     * Perform logout.
     */
    public void logout() {
        if (loginCommand != null) {
            loginCommand.logout(getCurrentPrincipal());
            if (FlexContext.isPerClientAuthentication()) FlexContext.setUserPrincipal(null); else FlexContext.getFlexSession().invalidate();
        } else {
            FlexContext.getFlexSession().invalidate();
            SecurityException se = new SecurityException();
            se.setMessage(NO_LOGIN_COMMAND);
            se.setCode(SecurityException.SERVER_AUTHORIZATION_CODE);
            throw se;
        }
    }

    /**
     * Throws various <code>SecurityException</code>s depending on whether an authenticated user
     * is associated with the current session and if one is whether the passed constraint prohibits
     * this user from performing an operation.  If a valid user is found and passed the constraint,
     * no exceptions are thrown
     *
     * @param constraint Constraint against which the current user is authorized.
     */
    public void checkConstraint(SecurityConstraint constraint) {
        if (constraint == null) return;
        Principal currentPrincipal = getCurrentPrincipal();
        if (currentPrincipal != null) {
            List roles = constraint.getRoles();
            boolean authorized = roles == null || checkRoles(currentPrincipal, roles);
            if (!authorized) {
                SecurityException se = new SecurityException();
                se.setMessage(ACCESS_DENIED);
                se.setCode(SecurityException.CLIENT_AUTHORIZATION_CODE);
                throw se;
            }
        } else {
            if (!isCustomAuth(constraint)) {
                if (FlexContext.getHttpResponse() == null) {
                    Endpoint endpoint = FlexContext.getEndpoint();
                    String endpointId = (endpoint != null) ? endpoint.getId() : "unknown";
                    SecurityException se = new SecurityException();
                    se.setMessage(NO_BASIC_SECURITY, new Object[] { constraint.getId(), endpointId });
                    se.setCode(SecurityException.CLIENT_AUTHORIZATION_CODE);
                    throw se;
                }
                FlexContext.getHttpResponse().setStatus(401);
                FlexContext.getHttpResponse().addHeader("WWW-Authenticate", "Basic realm=\"default\"");
            }
            SecurityException se = new SecurityException();
            se.setMessage(LOGIN_REQ_FOR_AUTH);
            se.setCode(SecurityException.CLIENT_AUTHENTICATION_CODE);
            throw se;
        }
    }

    /**
     * Returns true if the passed in principal belongs to at least one of the
     * roles in the passed in list of roles.
     *
     * @param principal Principal to check against roles
     * @param roles list of roles
     * @return true if principal belongs to at least one of the roles in the list
     */
    public boolean checkRoles(Principal principal, List roles) {
        if (loginCommand == null) {
            if (Log.isWarn()) Log.getLogger(LOG_CATEGORY).warn("Login command is null. Please ensure that the login-command" + " tag has the correct server attribute value" + ", or use 'all' to use the login command regardless of the server.");
            return false;
        }
        return loginCommand.doAuthorization(principal, roles);
    }

    private Principal getCurrentPrincipal() {
        return FlexContext.getUserPrincipal();
    }

    private void setCurrentPrincipal(Principal p) {
        FlexContext.setUserPrincipal(p);
    }

    private boolean isCustomAuth(SecurityConstraint constraint) {
        return SecurityConstraint.CUSTOM_AUTH_METHOD.equals(constraint.getMethod());
    }
}
