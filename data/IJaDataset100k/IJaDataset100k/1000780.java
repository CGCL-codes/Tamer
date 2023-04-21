package velosurf.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import velosurf.model.Entity;
import velosurf.web.l10n.Localizer;

/**
 * Used to store contextual values relatives to the user (in a web context, there is one UserContext per http session).
 *
 * @author <a href="mailto:claude.brisson@gmail.com">Claude Brisson</a>
 */
public class UserContext {

    /**
     * key used to store the user context in the http session.
     */
    public static final String USER_CONTEXT_KEY = "velosurf.util.UserContext:session-key";

    /**
     * Constructor.
     */
    public UserContext() {
    }

    /**
     * Last error setter.
     * @param err error
     */
    public synchronized void setError(String err) {
        error = err;
    }

    /**
     * Last error getter.
     * @return last error message.
     */
    public String getError() {
        return error;
    }

    /**
     * Localizer setter.
     *
     * @param loc localizer
     */
    public synchronized void setLocalizer(Localizer loc) {
        localizer = loc;
    }

    /**
     * Locale setter.
     *
     * @param loc Locale
     */
    public synchronized void setLocale(Locale loc) {
        locale = loc;
    }

    /**
     * Localize a parameterized message.
     * @param str message to localize
     * @param params parameters that are meant to replace "{0}", "{1}", ... in the message
     * @return localized message
     */
    public String localize(String str, Object... params) {
        Logger.debug("@@@@ localize: str=<" + str + ">, params=" + Arrays.asList(params) + ", result=" + MessageFormat.format(str, params));
        if (localizer == null) {
            return MessageFormat.format(str, params);
        } else if (params.length == 0) {
            return localizer.get(str);
        } else {
            return localizer.get(str, params);
        }
    }

    /**
     * Clear validation errors.
     */
    public synchronized void clearValidationErrors() {
        validationErrors.clear();
    }

    /**
     * Add a validation error.
     * @param err validation error
     */
    public synchronized void addValidationError(String err) {
        validationErrors.add(err);
    }

    /**
     * Get all validation error messages.
     *
     * @return validation error messages
     */
    public synchronized List<String> getValidationErrors() {
        if (validationErrors.size() == 0) {
            return null;
        }
        List<String> ret = new ArrayList<String>(validationErrors);
        validationErrors.clear();
        return ret;
    }

    /**
     * generic getter.
     *
     */
    public Object get(String key) {
        if ("error".equalsIgnoreCase(key)) {
            return getError();
        } else if ("validationErrors".equalsIgnoreCase(key)) {
            return getValidationErrors();
        } else if ("locale".equalsIgnoreCase(key)) {
            return getLocale();
        }
        return null;
    }

    /**
     * Locale getter.
     * @return current locale
     */
    public Locale getLocale() {
        if (localizer != null) {
            return localizer.getLocale();
        } else if (locale != null) {
            return locale;
        } else {
            return Locale.getDefault();
        }
    }

    /**
     * Set the last inserted ID for an entity.
     * @param entity entity
     * @param id last inserted id
     */
    public synchronized void setLastInsertedID(Entity entity, long id) {
        lastInsertedIDs.put(entity, id);
    }

    /**
     * Get the last inserted ID for an entity.
     * @param entity entity
     * @return last inserted ID of -1
     */
    public long getLastInsertedID(Entity entity) {
        Long id = lastInsertedIDs.get(entity);
        if (id != null) {
            return id.longValue();
        } else {
            Logger.error("getLastInsertID called for entity '" + entity + "' which doesn't have any");
            return -1;
        }
    }

    /** last error message */
    private String error = "";

    /** list of validation error messages */
    private List<String> validationErrors = new ArrayList<String>();

    /** localizer */
    private Localizer localizer = null;

    /** locale */
    private Locale locale = null;

    /** map of last inserted IDs */
    private Map<Entity, Long> lastInsertedIDs = new HashMap<Entity, Long>();
}
