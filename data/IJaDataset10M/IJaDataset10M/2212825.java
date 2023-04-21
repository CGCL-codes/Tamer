package org.torweg.pulse.component.core.viewport.interims;

import java.util.Locale;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.torweg.pulse.accesscontrol.User;
import org.torweg.pulse.accesscontrol.UserAttribute;
import org.torweg.pulse.bundle.Bundle;
import org.torweg.pulse.bundle.Controller;
import org.torweg.pulse.component.core.viewport.result.IResultError;
import org.torweg.pulse.component.core.viewport.result.ViewportResult;
import org.torweg.pulse.service.request.ServiceRequest;
import org.torweg.pulse.util.requestbean.ContextAwareRequestBean;

/**
 * Interim solution for locale checks based on <tt>conf/user-attributes.xml</tt>
 * .
 * 
 * @author Daniel Dietz
 * @version $Revision: 1.2 $
 * 
 */
public final class LocaleRightsCheckUtility {

    /**
	 * the logger.
	 */
    private static final Logger LOGGER = Logger.getLogger(LocaleRightsCheckUtility.class);

    /**
	 * Private constructor.
	 */
    private LocaleRightsCheckUtility() {
        super();
    }

    /**
	 * Checks the rights of a given user against a given locale according to the
	 * ".Edit.locales" the user has assigned for the given bundle.
	 * 
	 * @param controller
	 *            the {@code Controller}
	 * @param requestBean
	 *            a {@code ContextAwareRequestBean}
	 * @param locale
	 *            the {@code Locale} to check the {@code User}s rights for
	 * 
	 * @return a {@code ViewportResult} with errors if checks fail, {@code null}
	 *         otherwise
	 */
    public static ViewportResult checkUserAgainstLocale(final Controller controller, final ContextAwareRequestBean requestBean, final Locale locale) {
        User user = requestBean.getServiceRequest().getUser();
        if (user == null) {
            LOGGER.warn("noUserInUserContext  \n-> not executing command:" + requestBean.getServiceRequest().getCommand().toCommandURL(requestBean.getServiceRequest()));
            ViewportResult result = new ViewportResult(controller);
            result.addError(Error.NOT_LOGGED_IN_ERROR, "You are not logged-in or your session has already expired.");
            return result;
        }
        if (user.isSuperuser()) {
            return null;
        }
        if (!checkUserRightsAgainstLocale(requestBean.getBundle(), locale, user)) {
            ViewportResult result = new ViewportResult(controller);
            result.addError(Error.USER_HAS_NO_EDITRIGHTS_FOR_LOCALE_ERROR, "You do not have the required rights for the current locale.");
            return result;
        }
        return null;
    }

    /**
	 * Checks the rights of a given user against a given locale according to the
	 * ".Edit.locales" the user has assigned for the given bundle.
	 * <p>
	 * Returns a {@code JSONObject} error = { e: {String} error } if checks
	 * fail, null otherwise.
	 * </p>
	 * 
	 * @param bundle
	 *            the {@code Bundle} of the caller
	 * @param request
	 *            the {@code ServiceRequest} of the caller
	 * @param locale
	 *            the {@code Locale} to check the {@code User}s rights for
	 * @return a {@code JSONObject} error if checks fail, {@code null}
	 *         otherwise.
	 */
    public static JSONObject checkUserAgainstLocale(final Bundle bundle, final ServiceRequest request, final Locale locale) {
        User user = request.getUser();
        if (user == null) {
            JSONObject error = new JSONObject();
            LOGGER.warn("noUserInUserContext  \n-> not executing command:" + request.getCommand().toCommandURL(request));
            error.put("e", Error.NOT_LOGGED_IN_ERROR);
            error.put("description", "You are not logged-in or your session has already expired.");
            return error;
        }
        if (user.isSuperuser()) {
            return null;
        }
        if (!checkUserRightsAgainstLocale(bundle, locale, user)) {
            JSONObject error = new JSONObject();
            error.put("e", Error.USER_HAS_NO_EDITRIGHTS_FOR_LOCALE_ERROR);
            error.put("description", "You do not have the required rights for the current locale.");
            return error;
        }
        return null;
    }

    /**
	 * Performs the check against the attributes.
	 * 
	 * @param bundle
	 *            the current bundle
	 * @param locale
	 *            the locale to check against
	 * @param user
	 *            the current user
	 * @return {@code true} if a matching locale has been found, {@code false}
	 *         otherwise
	 */
    private static boolean checkUserRightsAgainstLocale(final Bundle bundle, final Locale locale, final User user) {
        for (UserAttribute userAttribute : user.getAttributes()) {
            if (userAttribute.getName().equals(bundle.getName() + ".Edit.locales")) {
                for (String value : userAttribute.getValues()) {
                    if (value.equalsIgnoreCase(locale.toString())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
	 * Provides general error-codes for the pulse website-administration.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision: 1.3 $
	 */
    public enum Error implements IResultError {

        /**
		 * States that there is no logged-in {@code User} in the current
		 * context.
		 */
        NOT_LOGGED_IN_ERROR, /**
		 * States that the {@code User} does not have the required edit-rights
		 * the {@code Locale} of the entity currently being edited.
		 */
        USER_HAS_NO_EDITRIGHTS_FOR_LOCALE_ERROR
    }
}
