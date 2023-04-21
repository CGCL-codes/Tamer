package com.cubusmail.server.util;

import javax.servlet.http.HttpServletRequest;
import com.cubusmail.common.model.Preferences;
import com.cubusmail.server.mail.SessionManager;

/**
 * ServletUtil
 * 
 * @author Juergen Schlierf
 */
public abstract class ServletUtil {

    public static final String getDefaultLocale(HttpServletRequest request) {
        if (SessionManager.isLoggedIn()) {
            Preferences prefs = SessionManager.get().getPreferences();
            return prefs.getLanguage();
        } else {
            return request.getLocale().toString();
        }
    }

    public static final String getCSS() {
        if (SessionManager.isLoggedIn()) {
            Preferences prefs = SessionManager.get().getPreferences();
            return prefs.getTheme();
        } else {
            return "";
        }
    }
}
