package org.exist.restlet.auth;

import java.util.logging.Level;
import org.exist.restlet.XMLDBResource;
import org.exist.security.Subject;
import org.exist.storage.BrokerPool;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;
import org.restlet.routing.Filter;

/**
 *
 * @author alex
 */
public class UserFilter extends Filter {

    SessionManager sessionManager;

    UserManager userManager;

    String realmName;

    String cookieName;

    String cookiePath;

    boolean isDebugLog;

    public UserFilter() {
    }

    public UserFilter(Context context) {
        super(context);
    }

    public void start() throws Exception {
        isDebugLog = "true".equals(getContext().getParameters().getFirstValue(UserFilter.class.getName() + ".debug"));
        cookieName = getContext().getParameters().getFirstValue(XMLDBResource.COOKIE_NAME);
        cookiePath = getContext().getParameters().getFirstValue(XMLDBResource.COOKIE_PATH_NAME);
        if (cookiePath == null) {
            cookiePath = "/";
        }
        if (cookieName != null && sessionManager == null) {
            sessionManager = new SessionManager(getContext());
            getContext().getAttributes().put(XMLDBResource.SESSION_MANAGER_NAME, sessionManager);
        }
        realmName = getContext().getParameters().getFirstValue(XMLDBResource.REALM_NAME);
        if (realmName != null) {
            getLogger().info("Using realm " + realmName);
        }
        String userManagerClassName = getContext().getParameters().getFirstValue(XMLDBResource.USER_MANAGER_CLASS_NAME);
        if (userManagerClassName != null) {
            getLogger().info("Instantiating user manager " + userManagerClassName);
            try {
                Class<? extends UserManager> userManagerClass = (Class<? extends UserManager>) this.getClass().getClassLoader().loadClass(userManagerClassName);
                try {
                    userManager = userManagerClass.getConstructor(Context.class).newInstance(getContext());
                } catch (NoSuchMethodException ex) {
                }
                if (userManager == null) {
                    try {
                        userManager = userManagerClass.getConstructor().newInstance();
                    } catch (NoSuchMethodException ex) {
                        getLogger().severe("There is no constructor available for verifier class " + userManagerClassName);
                    }
                }
            } catch (Exception ex) {
                getLogger().log(Level.SEVERE, "Error loading verifier class: " + userManagerClassName, ex);
            }
        }
        if (userManager == null) {
            String dbName = getContext().getParameters().getFirstValue(XMLDBResource.DBNAME_NAME);
            if (dbName != null) {
                getLogger().info("Instantiating DBUserVerifier for " + dbName);
                userManager = new DBUserVerifier(getContext(), BrokerPool.getInstance(dbName).getSecurityManager());
            }
        }
        if (userManager != null) {
            getContext().getAttributes().put(XMLDBResource.USER_MANAGER_NAME, userManager);
        }
        super.start();
    }

    protected int beforeHandle(Request request, Response response) {
        Cookie cookie = request.getCookies().getFirst(cookieName);
        if (cookie != null) {
            Subject user = sessionManager.getUser(cookie.getValue());
            if (user != null) {
                if (isDebugLog) {
                    getLogger().info(cookieName + "=" + cookie.getValue() + " is valid, user=" + user.getName());
                }
                request.getAttributes().put(XMLDBResource.USER_NAME, user);
                request.getAttributes().put(XMLDBResource.SESSION_NAME, cookie.getValue());
                request.getAttributes().put(XMLDBResource.NEW_USER_NAME, Boolean.FALSE);
            } else if (isDebugLog) {
                getLogger().info(cookieName + "=" + cookie.getValue() + " is invalid.");
            }
        }
        if (userManager != null) {
            request.getAttributes().put(XMLDBResource.USER_MANAGER_NAME, userManager);
        }
        if (sessionManager != null) {
            request.getAttributes().put(XMLDBResource.SESSION_MANAGER_NAME, sessionManager);
        }
        return Filter.CONTINUE;
    }

    protected void afterHandle(Request request, Response response) {
        if (cookieName != null) {
            Boolean isNew = (Boolean) request.getAttributes().get(XMLDBResource.NEW_USER_NAME);
            if (isNew != null && isNew) {
                String sessionId = (String) request.getAttributes().get(XMLDBResource.SESSION_NAME);
                if (sessionId == null) {
                    Subject user = (Subject) request.getAttributes().get(XMLDBResource.USER_NAME);
                    sessionId = sessionManager.newSession(user);
                    if (isDebugLog) {
                        getLogger().info("Setting session new cookie " + cookieName + "=" + sessionId + " for " + user.getName());
                    }
                }
                CookieSetting cookie = new CookieSetting(cookieName, sessionId);
                cookie.setPath(cookiePath == null ? "/" : cookiePath);
                response.getCookieSettings().add(cookie);
            }
        }
    }
}
