package org.or5e.core;

import java.util.Map;
import org.or5e.core.security.v1.User;

/**
 * Unique Application Session will be created once the user Logged in on Desktop or WebApplication
 * 
 * @author Ponraj Suthanthiramani
 * @version 0.1b
 */
public class ApplicationSession {

    private User currentUser = null;

    private Map<String, Object> userSettings = null;

    private ApplicationContext context = null;

    public final User getCurrentUser() {
        return currentUser;
    }

    public final void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public final Map<String, Object> getUserSettings() {
        return userSettings;
    }

    public final void setUserSettings(Map<String, Object> userSettings) {
        this.userSettings = userSettings;
    }

    public final ApplicationContext getContext() {
        return context;
    }

    public final void setContext(ApplicationContext context) {
        this.context = context;
    }
}
