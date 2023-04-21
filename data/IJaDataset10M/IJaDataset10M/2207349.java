package org.torweg.pulse.accesscontrol.authentication;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jdom.Element;
import org.torweg.pulse.configuration.AbstractConfigBean;
import org.torweg.pulse.configuration.ConfigurationException;

/**
 * The configuration of the {@code Authentication}.
 * 
 * @author Christian Schatt
 * @version $Revision: 1380 $
 * 
 */
public class AuthenticationConfig extends AbstractConfigBean {

    /**
	 * The serialVersionUID of this {@code AuthenticationConfig}.
	 */
    private static final long serialVersionUID = -1086986185169763274L;

    /**
	 * The name-mappings for the httpParameters.
	 */
    private final Map<String, String> parameterMappings = new HashMap<String, String>();

    /**
	 * The login-modes provided by the {@code Authentication}.
	 */
    private final Set<String> loginModes = new HashSet<String>();

    /**
	 * The names of actions which will not redirect to last view on logout.
	 */
    private final Set<String> redirectToHompageActions = new HashSet<String>();

    /**
	 * The error-codes of the {@code Authentication}.
	 */
    private final Map<Integer, String> errorCodes = new HashMap<Integer, String>();

    /**
	 * the single sign on tasks.
	 */
    private final Set<Class<ISingleSignOnTask>> singleSignOnTasks = new HashSet<Class<ISingleSignOnTask>>();

    /**
	 * checks if the passed action-name is a redirect-to-homepage-action.
	 * 
	 * @param actionName
	 *            the name to check
	 * 
	 * @return true if the passed actionName is a redirect-to-homepage-action,
	 *         false otherwise
	 */
    public final boolean isRedirectToHompageAction(final String actionName) {
        return this.redirectToHompageActions.contains(actionName);
    }

    /**
	 * Initializes the configuration of the {@code Authentication}.
	 * 
	 * @param conf
	 *            The JDOM-{@code Element} containing the configuration of
	 *            the {@code Authentication}.
	 * 
	 * @see org.torweg.pulse.configuration.ConfigBean#init(org.jdom.Element)
	 */
    @SuppressWarnings("unchecked")
    public final void init(final Element conf) {
        List<Element> list = (List<Element>) conf.getChild("parameter-mappings").getChildren();
        for (Element mapping : list) {
            this.parameterMappings.put(mapping.getName(), mapping.getAttributeValue("name"));
        }
        list = (List<Element>) conf.getChild("login-modes").getChildren("login-mode");
        for (Element mode : list) {
            this.loginModes.add(mode.getAttributeValue("name"));
        }
        list = (List<Element>) conf.getChild("error-codes").getChildren("error-code");
        for (Element code : list) {
            this.errorCodes.put(Integer.valueOf(code.getAttributeValue("value")), code.getAttributeValue("description"));
        }
        list = (List<Element>) conf.getChild("redirect-to-homepage-on-logout").getChildren("action");
        for (Element action : list) {
            this.redirectToHompageActions.add(action.getAttributeValue("name"));
        }
        Element ssoEl = conf.getChild("single-sign-on");
        if (ssoEl != null) {
            list = (List<Element>) ssoEl.getChildren("task");
            for (Element t : list) {
                try {
                    Class<ISingleSignOnTask> clazz = (Class<ISingleSignOnTask>) Class.forName(t.getAttributeValue("class"));
                    this.singleSignOnTasks.add((Class<ISingleSignOnTask>) clazz);
                } catch (ClassNotFoundException e) {
                    throw new ConfigurationException(e.getLocalizedMessage(), e);
                } catch (ClassCastException e) {
                    throw new ConfigurationException(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    /**
	 * Returns the name-mappings for the httpParameters provided by the
	 * {@code Authentication}.
	 * 
	 * @return the name-mappings for the httpParameters provided by the
	 *         {@code Authentication}
	 */
    protected final Map<String, String> getParameterMappings() {
        return this.parameterMappings;
    }

    /**
	 * Returns the login-modes provided by the {@code Authentication}.
	 * 
	 * @return the login-modes provided by the {@code Authentication}
	 */
    protected final Set<String> getLoginModes() {
        return this.loginModes;
    }

    /**
	 * Returns the error-codes provided by the {@code Authentication}.
	 * 
	 * @return the error-codes provided by the {@code Authentication}
	 */
    protected final Map<Integer, String> getErrorCodes() {
        return this.errorCodes;
    }

    /**
	 * indicates whether single-sign-on is activated.
	 * 
	 * @return {@code true}, if the authentication is configured for
	 *         single-sign-on. Otherwise {@code false}.
	 */
    protected final boolean isSingleSignOn() {
        return this.singleSignOnTasks.isEmpty() ^ true;
    }

    /**
	 * returns an unmodifiable collection of all {@code ISingleSignOnTask}s
	 * configured.
	 * 
	 * @return an unmodifiable view of all {@code ISingleSignOnTask}s
	 */
    public final Collection<Class<ISingleSignOnTask>> getSingleSignOnTasks() {
        return Collections.unmodifiableCollection(this.singleSignOnTasks);
    }
}
