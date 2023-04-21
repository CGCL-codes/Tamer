package com.j2biz.compote.velocity;

import java.util.List;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class MessageTool extends org.apache.velocity.tools.struts.MessageTool {

    public String get(String key, String bundle, Object[] args) {
        MessageResources res = getResources(bundle);
        if (res == null) {
            res = getDefaultMessageResources(request, application, bundle);
            if (res == null) return null;
        }
        if (args == null) {
            return res.getMessage(this.locale, key);
        } else {
            return res.getMessage(this.locale, key, args);
        }
    }

    public boolean exists(String key, String bundle) {
        MessageResources res = getResources(bundle);
        if (res == null) {
            res = getDefaultMessageResources(request, application, bundle);
            if (res == null) return false;
        }
        return res.isPresent(this.locale, key);
    }

    /**
     * Returns MessageResources from default struts module.
     * 
     * @param request
     * @param app
     * @param bundle
     * @return
     */
    public static MessageResources getDefaultMessageResources(HttpServletRequest request, ServletContext app, String bundle) {
        MessageResources resources = null;
        if (bundle == null) {
            bundle = Globals.MESSAGES_KEY;
        }
        resources = (MessageResources) request.getAttribute(bundle);
        if (resources == null) {
            resources = (MessageResources) app.getAttribute(bundle);
        }
        return resources;
    }

    /**
     * Looks up and returns the localized message for the specified key.
     * The user's locale is consulted to determine the language of the
     * message.
     *
     * @param key message key
     *
     * @return the localized message for the specified key or
     * <code>null</code> if no such message exists
     */
    public String get(String key) {
        return get(key, (Object[]) null);
    }

    /**
     * Looks up and returns the localized message for the specified key.
     * The user's locale is consulted to determine the language of the
     * message.
     *
     * @param key message key
     * @param bundle The bundle name to look for.
     *
     * @return the localized message for the specified key or
     * <code>null</code> if no such message exists
     * @since VelocityTools 1.1
     */
    public String get(String key, String bundle) {
        return get(key, bundle, (Object[]) null);
    }

    /**
     * Looks up and returns the localized message for the specified key.
     * Replacement parameters passed with <code>args</code> are
     * inserted into the message. The user's locale is consulted to
     * determine the language of the message.
     *
     * @param key message key
     * @param args replacement parameters for this message
     *
     * @return the localized message for the specified key or
     * <code>null</code> if no such message exists
     */
    public String get(String key, Object args[]) {
        return get(key, null, args);
    }

    /**
     * Same as {@link #get(String key, Object[] args)}, but takes a
     * <code>java.util.List</code> instead of an array. This is more
     * Velocity friendly.
     *
     * @param key message key
     * @param args replacement parameters for this message
     *
     * @return the localized message for the specified key or
     * <code>null</code> if no such message exists
     */
    public String get(String key, List args) {
        return get(key, args.toArray());
    }

    /**
     * Same as {@link #get(String key, Object[] args)}, but takes a
     * <code>java.util.List</code> instead of an array. This is more
     * Velocity friendly.
     *
     * @param key message key
     * @param bundle The bundle name to look for.
     * @param args replacement parameters for this message
     * @since VelocityTools 1.1
     * @return the localized message for the specified key or
     * <code>null</code> if no such message exists
     */
    public String get(String key, String bundle, List args) {
        return get(key, bundle, args.toArray());
    }

    /**
     * Checks if a message string for a specified message key exists
     * for the user's locale.
     *
     * @param key message key
     *
     * @return <code>true</code> if a message strings exists,
     * <code>false</code> otherwise
     */
    public boolean exists(String key) {
        return exists(key, null);
    }

    /**
     * Returns the user's locale. If a locale is not found, the default
     * locale is returned.
     * @deprecated This does not fit the purpose of MessageTool and will be
     *             removed in VelocityTools 1.2
     */
    public Locale getLocale() {
        return this.locale;
    }
}
