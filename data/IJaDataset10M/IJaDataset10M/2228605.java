package org.torweg.pulse.component.core.accesscontrol;

import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.torweg.pulse.accesscontrol.User;
import org.torweg.pulse.accesscontrol.User.Everybody;
import org.torweg.pulse.annotations.Action;
import org.torweg.pulse.annotations.AnyAction;
import org.torweg.pulse.annotations.Permission;
import org.torweg.pulse.annotations.Action.Security;
import org.torweg.pulse.bundle.Bundle;
import org.torweg.pulse.bundle.Controller;
import org.torweg.pulse.bundle.Result;
import org.torweg.pulse.component.core.accesscontrol.sso.ISingleSignOnTask;
import org.torweg.pulse.configuration.ConfigBean;
import org.torweg.pulse.configuration.Configurable;
import org.torweg.pulse.configuration.Configurable2;
import org.torweg.pulse.configuration.PoorMansCache;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.service.PulseException;
import org.torweg.pulse.service.event.RedirectEvent;
import org.torweg.pulse.service.event.RedirectSafelyEvent;
import org.torweg.pulse.service.request.Command;
import org.torweg.pulse.service.request.Parameter;
import org.torweg.pulse.service.request.ServiceRequest;
import org.torweg.pulse.service.request.ServiceSession;

/**
 * This <code>Controller</code> is responsible for the authentication of a
 * <code>User</code> and the login/logout-procedure.
 * 
 * @author Christian Schatt, Thomas Weber
 * @version $Revision: 1.1 $
 * 
 */
public class Authentication extends Controller implements Configurable {

    /**
	 * the logger.
	 */
    private static final Logger LOGGER = Logger.getLogger(Authentication.class);

    /**
	 * The configuration of this <code>Authentication</code>.
	 */
    private AuthenticationConfig configuration = null;

    /**
	 * Initializes the configuration of the <code>Authentication</code>.
	 * 
	 * @param config
	 *            the configuration of the <code>Authentication</code>
	 */
    public final void init(final ConfigBean config) {
        this.configuration = (AuthenticationConfig) config;
        setAlwaysRun(AlwaysRun.PRE);
    }

    /**
	 * Handles login and logout requests.
	 * 
	 * @param bundle
	 *            the <code>Bundle</code> the <code>Authentication</code>
	 *            belongs to
	 * @param request
	 *            the current <code>ServiceRequest</code>
	 * 
	 * @return a <code>AuthenticationResult</code> with the results of the
	 *         operation
	 */
    @AnyAction
    @Action(value = "authenticate", generate = true, security = Security.ALWAYS)
    @Permission(value = "authenticate")
    public final Result run(final Bundle bundle, final ServiceRequest request) {
        Command command = request.getCommand();
        ServiceSession session = request.getSession();
        Map<String, String> mappings = this.configuration.getParameterMappings();
        Parameter login = command.getParameter(mappings.get("login"));
        command.removeParameter(login);
        Parameter logout = command.getParameter(mappings.get("logout"));
        command.removeParameter(logout);
        Parameter username = command.getParameter(mappings.get("username"));
        command.removeParameter(username);
        Parameter password = command.getParameter(mappings.get("password"));
        command.removeParameter(password);
        AuthenticationResult result = new AuthenticationResult();
        if ((login != null) && this.login(request, bundle, result, username, password)) {
            handlePostLogin(request);
            return null;
        } else if (logout != null) {
            logout(bundle, request, session);
            handlePostLogout(request);
            return null;
        }
        if ((request.getCommand().getParameter("redirect") != null) && (request.getCommand().getParameter("redirect").getFirstValue() != null)) {
            result.setRedirectURI(request.getCommand().getParameter("redirect").getFirstValue());
        }
        result.setLoginStatus(session.getAttribute(User.class.getCanonicalName()) != null);
        result.setParameterMappings(mappings);
        result.setLoginModes(this.configuration.getLoginModes());
        return result;
    }

    /**
	 * performs the required redirects after a successful login.
	 * 
	 * @param request
	 *            the request
	 */
    private void handlePostLogin(final ServiceRequest request) {
        String redirectURI = request.getHttpServletRequest().getHeader("Referer");
        if ((request.getCommand().getParameter("redirect") != null) && (request.getCommand().getParameter("redirect").getFirstValue() != null)) {
            redirectURI = request.getCommand().getParameter("redirect").getFirstValue();
        }
        if ((redirectURI != null) && (redirectURI.length() > 0) && (redirectURI.contains(request.getHttpServletRequest().getServerName()))) {
            request.getEventManager().addEvent(new RedirectEvent(redirectURI));
        } else {
            request.getEventManager().addEvent(new RedirectEvent(request.getCommand().createCopy(false).setAction("homepage").toCommandURL(request)));
        }
    }

    /**
	 * performs the required redirects after logout.
	 * 
	 * @param request
	 *            the current request
	 */
    private void handlePostLogout(final ServiceRequest request) {
        if (!this.configuration.isRedirectToHompageAction(request.getCommand().getAction())) {
            request.getEventManager().addEvent(new RedirectSafelyEvent());
        } else {
            request.getEventManager().addEvent(new RedirectEvent(request.getCommand().createCopy(false).setAction("homepage").setBundle("Core").toCommandURL(request)));
        }
    }

    /**
	 * Actually tries to login a user.
	 * 
	 * @param request
	 *            the current request
	 * @param bundle
	 *            the bundle
	 * @param result
	 *            the <code>AuthenticationResult</code> to record login failures
	 * @param username
	 *            the name of the <code>User</code> to be authenticated
	 * @param password
	 *            the password of the <code>User</code> to be authenticated
	 * @return <code>true</code>, if the login was successful. Otherwise
	 *         <code>false</code>.
	 */
    private boolean login(final ServiceRequest request, final Bundle bundle, final AuthenticationResult result, final Parameter username, final Parameter password) {
        if ((username != null) && (username.getFirstValue() != null) && (password != null) && (password.getFirstValue() != null)) {
            User user = null;
            Session sess = null;
            Transaction trans = null;
            try {
                sess = Lifecycle.getHibernateDataSource().createNewSession();
                trans = sess.beginTransaction();
                user = (User) sess.createQuery("from User as u where u.name = ?").setString(0, username.getFirstValue()).uniqueResult();
                trans.commit();
            } catch (Exception exception) {
                if (trans != null) {
                    trans.rollback();
                }
                throw new PulseException(exception);
            } finally {
                sess.close();
            }
            return postProcessLoadedUser(request, bundle, result, password, user);
        } else {
            result.setError(1, configuration.getErrorCodes().get(1));
            return false;
        }
    }

    /**
	 * Actually logs out the current user.
	 * 
	 * @param bundle
	 *            the bundle
	 * @param request
	 *            the request
	 * @param session
	 *            the service session
	 */
    private void logout(final Bundle bundle, final ServiceRequest request, final ServiceSession session) {
        User user = request.getUser();
        session.removeAttribute(User.class.getCanonicalName());
        request.refreshUser();
        if (this.configuration.isSingleSignOn()) {
            singleSignOnLogout(user, request, bundle);
        }
    }

    /**
	 * checks, if the loaded <code>User</code> is not <code>null</code> and
	 * active and if the password is correct.
	 * 
	 * @param request
	 *            the service session
	 * @param bundle
	 *            the bundle
	 * @param result
	 *            the result
	 * @param password
	 *            the password
	 * @param user
	 *            the user
	 * @return <code>true</code>, if the <code>User</code> has been successfully
	 *         authenticated. Otherwise <code>false</code>.
	 */
    private boolean postProcessLoadedUser(final ServiceRequest request, final Bundle bundle, final AuthenticationResult result, final Parameter password, final User user) {
        if ((user != null) && (!user.isExpunged())) {
            if (user.isActive()) {
                if (user.checkPassword(password.getFirstValue())) {
                    user.setLastLoginTime();
                    Session sess = null;
                    Transaction trans = null;
                    try {
                        sess = Lifecycle.getHibernateDataSource().createNewSession();
                        trans = sess.beginTransaction();
                        sess.saveOrUpdate(user);
                        trans.commit();
                        if (this.configuration.isSingleSignOn()) {
                            singleSignOnLogin(user, request, bundle, password);
                        }
                    } catch (Exception exception) {
                        if ((trans != null) && (trans.isActive())) {
                            try {
                                trans.rollback();
                            } catch (Exception e) {
                                LOGGER.warn("Error rolling back: " + e.getLocalizedMessage(), e);
                            }
                        }
                        throw new PulseException(exception);
                    } finally {
                        sess.close();
                    }
                    request.getSession().setAttribute(User.class.getCanonicalName(), user.getId());
                    return true;
                } else {
                    result.setError(4, configuration.getErrorCodes().get(4));
                    return false;
                }
            } else {
                result.setError(3, configuration.getErrorCodes().get(3));
                return false;
            }
        } else {
            result.setError(2, configuration.getErrorCodes().get(2));
            return false;
        }
    }

    /**
	 * processes the <code>ISingleSignOnTask</code>s during login.
	 * 
	 * @param request
	 *            the request
	 * @param user
	 *            the user
	 * @param bundle
	 *            the bundle
	 * @param password
	 *            the password, or <code>null</code> on sign out
	 */
    private void singleSignOnLogin(final User user, final ServiceRequest request, final Bundle bundle, final Parameter password) {
        for (Class<ISingleSignOnTask> task : this.configuration.getSingleSignOnTasks()) {
            ISingleSignOnTask taskInstance = prepareTask(task, bundle);
            if (taskInstance != null) {
                taskInstance.signOn(user, password.getFirstValue(), request);
            }
        }
    }

    /**
	 * processes the <code>ISingleSignOnTask</code>s during log out.
	 * 
	 * @param user
	 *            the user
	 * @param request
	 *            the request
	 * @param bundle
	 *            the bundle
	 */
    private void singleSignOnLogout(final User user, final ServiceRequest request, final Bundle bundle) {
        if (user instanceof Everybody) {
            return;
        }
        for (Class<ISingleSignOnTask> task : this.configuration.getSingleSignOnTasks()) {
            ISingleSignOnTask taskInstance = prepareTask(task, bundle);
            if (taskInstance != null) {
                taskInstance.signOff(user, request);
            }
        }
    }

    /**
	 * prepares a <code>ISingleSignOnTask</code> for execution.
	 * 
	 * @param task
	 *            the task
	 * @param bundle
	 *            the bundle
	 * @return the configured instance
	 */
    private ISingleSignOnTask prepareTask(final Class<ISingleSignOnTask> task, final Bundle bundle) {
        try {
            ISingleSignOnTask taskInstance = task.newInstance();
            if (taskInstance instanceof Configurable2) {
                ((Configurable2) taskInstance).initialize(PoorMansCache.getBundleConfiguration(task, bundle));
            }
            return taskInstance;
        } catch (InstantiationException e) {
            LOGGER.error("Error processing task '" + task.getCanonicalName() + "':" + e.getLocalizedMessage());
            return null;
        } catch (IllegalAccessException e) {
            LOGGER.error("Error processing task '" + task.getCanonicalName() + "':" + e.getLocalizedMessage());
            return null;
        }
    }
}
