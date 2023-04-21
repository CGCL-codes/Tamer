package com.kwoksys.biz.auth;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.exception.UsernameNotFoundException;
import com.kwoksys.biz.admin.user.AccountLockoutValidator;
import com.kwoksys.biz.auth.dao.AuthDao;
import com.kwoksys.framework.exception.DatabaseException;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.session.CookieManager;
import com.kwoksys.framework.connection.LDAP;
import com.kwoksys.framework.system.Access;
import com.kwoksys.framework.system.RequestContext;
import com.kwoksys.framework.util.StringUtils;
import com.kwoksys.framework.configs.ConfigManager;
import com.kwoksys.framework.configs.LogConfigManager;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * AuthService
 */
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = Logger.getLogger(AuthServiceImpl.class.getName());

    private static final AuthServiceImpl INSTANCE = new AuthServiceImpl();

    private AuthServiceImpl() {
    }

    public static AuthServiceImpl getInstance() {
        return INSTANCE;
    }

    public Set<Integer> getAccessGroupPerms(Integer groupId) throws DatabaseException {
        AuthDao authDao = new AuthDao();
        return authDao.getAccessGroupPerms(groupId);
    }

    public Set<Integer> getAccessUserPerms(Integer userId) throws DatabaseException {
        AuthDao authDao = new AuthDao();
        return authDao.getAccessUserPerms(userId);
    }

    public Map<Integer, Set> getAccessPermPages() throws DatabaseException {
        AuthDao authDao = new AuthDao();
        return authDao.getAccessPermPages();
    }

    public Map getAccessPages() throws DatabaseException {
        AuthDao authDao = new AuthDao();
        return authDao.getAccessPages();
    }

    public ActionMessages updateUserLogoutSession(Integer userId) throws DatabaseException {
        AuthDao authDao = new AuthDao();
        return authDao.updateUserLogoutSession(userId);
    }

    public boolean isValidUserSession(Integer userId, String sessionTokenCookie) throws DatabaseException {
        if (sessionTokenCookie.isEmpty()) {
            return false;
        }
        AuthDao authDao = new AuthDao();
        return authDao.isValidUserSession(userId, sessionTokenCookie);
    }

    public ActionMessages authenticateUser(RequestContext requestContext, AccessUser user) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        if (user.getUsername().isEmpty()) {
            errors.add("emptyUsername", new ActionMessage("auth.login.error.emptyUsername"));
        } else if (user.getRequestedPassword().isEmpty()) {
            errors.add("emptyPassword", new ActionMessage("auth.login.error.emptyPassword"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        try {
            AuthDao authDao = new AuthDao();
            errors = authDao.isValidUsername(user);
        } catch (UsernameNotFoundException e) {
            errors.add("noUsernameFound", new ActionMessage("auth.login.error.wrongCredential"));
            logger.warning(LogConfigManager.DB_AUTH_PREFIX + " Login failed for user: " + user.getUsername() + ". No such username.");
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        Date sysdate = requestContext.getSysdate();
        int accountLockout = -1;
        int accountLockoutLimit = ConfigManager.admin.getAccountLockoutThreshold();
        long accountLockoutMs = ConfigManager.admin.getAccountLockoutDurationMs();
        boolean validateAcctLockout = AccountLockoutValidator.validateAccountLockout();
        if (user.isAccountDisabled()) {
            errors.add("accountDisabled", new ActionMessage("auth.login.error.accountDisabled"));
            logger.warning(LogConfigManager.DB_AUTH_PREFIX + " Login failed for user: " + user.getUsername() + ". Account disabled.");
        } else if (user.isGuessUser()) {
            errors.add("noGuestUser", new ActionMessage("auth.login.error.noGuestUser"));
        } else if (validateAcctLockout && user.getInvalidLogonCount() == accountLockout && user.getInvalidLogonDate().getTime() > (sysdate.getTime() - accountLockoutMs)) {
            errors.add("login", new ActionMessage("auth.login.error.accountLocked"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        boolean isValidLogin = false;
        String authType = ConfigManager.auth.getAuthMethod();
        if (authType.equals(Access.AUTH_APP)) {
            isValidLogin = appAuthenticate(user);
            if (!isValidLogin) {
                logger.warning(LogConfigManager.DB_AUTH_PREFIX + " Login failed for user: " + user.getUsername() + ". Wrong password.");
            }
        } else if (authType.equals(Access.AUTH_LDAP)) {
            isValidLogin = ldapAuthenticate(user);
        } else if (authType.equals(Access.AUTH_MIXED)) {
            isValidLogin = (appAuthenticate(user) || ldapAuthenticate(user));
        }
        if (isValidLogin) {
            String randomChars = AuthUtils.generateRandomChars(13);
            AuthDao authDao = new AuthDao();
            authDao.updateUserLogonSession(user, randomChars);
        } else if (!validateAcctLockout) {
            errors.add("login", new ActionMessage("auth.login.error.wrongCredential"));
        } else {
            int invalidLoginCount = user.getInvalidLogonCount();
            if (invalidLoginCount == accountLockout) {
                invalidLoginCount = 1;
            } else {
                invalidLoginCount++;
            }
            if (invalidLoginCount >= accountLockoutLimit) {
                invalidLoginCount = accountLockout;
                errors.add("login", new ActionMessage("auth.login.error.accountLocked"));
            } else {
                errors.add("login", new ActionMessage("auth.login.error.wrongCredentialLimit", ConfigManager.admin.getAccountLockoutDurationMinutes(), (accountLockoutLimit - invalidLoginCount)));
            }
            AuthDao authDao = new AuthDao();
            authDao.updateUserInvalidLogon(user, invalidLoginCount);
        }
        return errors;
    }

    private boolean appAuthenticate(AccessUser accessUser) {
        return AuthUtils.hashPassword(accessUser.getRequestedPassword()).equals(accessUser.getHashedPassword());
    }

    private boolean ldapAuthenticate(AccessUser accessUser) {
        LDAP ldap = new LDAP();
        ldap.setUrl(ConfigManager.auth.getLdapUrlScheme() + ConfigManager.auth.getAuthLdapUrl());
        ldap.setUsername(accessUser.getUsername());
        ldap.setSecurityPrincipal(ConfigManager.auth.getAuthLdapSecurityPrincipal());
        ldap.setPassword(accessUser.getRequestedPassword());
        return ldap.authenticate().isEmpty();
    }

    /**
     * Test LDAP connection
     * @param user
     * @return
     * @throws DatabaseException
     */
    public ActionMessages testLdapConnection(String urlScheme, String url, String username, String password, String securityPrincipal) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        if (url.isEmpty()) {
            errors.add("emptyUrl", new ActionMessage("admin.config.auth.ldap.error.emptyUrl"));
        }
        if (username.isEmpty()) {
            errors.add("emptyUsername", new ActionMessage("admin.config.auth.ldap.error.emptyUsername"));
        } else if (password.isEmpty()) {
            errors.add("emptyPassword", new ActionMessage("admin.config.auth.ldap.error.emptyPassword"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        AccessUser appUser = new AccessUser();
        appUser.setUsername(username);
        appUser.setHashedPassword(password);
        try {
            AuthDao authDao = new AuthDao();
            errors = authDao.isValidUsername(appUser);
        } catch (UsernameNotFoundException e) {
            errors.add("invalidUsername", new ActionMessage("admin.config.auth.ldap.error.invalidUsername"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        if (appUser.isAccountDisabled()) {
            errors.add("accountDisabled", new ActionMessage("admin.config.auth.ldap.error.accountDisabled"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        LDAP ldap = new LDAP();
        ldap.setUrl(urlScheme + url);
        ldap.setUsername(username);
        ldap.setSecurityPrincipal(securityPrincipal);
        ldap.setPassword(password);
        return ldap.authenticate();
    }

    public boolean isValidBasicAuthentication(RequestContext requestContext, AccessUser user) throws Exception {
        String authHeaderRequest = requestContext.getRequest().getHeader("authorization");
        if (authHeaderRequest == null) {
            return false;
        }
        String authHeader = StringUtils.decodeBase64(authHeaderRequest.replace("Basic ", ""));
        String[] credential = authHeader.split(":");
        if (credential.length != 2) {
            return false;
        }
        user.setUsername(credential[0]);
        user.setRequestedPassword(credential[1]);
        ActionMessages errors = authenticateUser(requestContext, user);
        return errors.isEmpty();
    }

    public void initializeUserSession(HttpServletRequest request, HttpServletResponse response, AccessUser user) {
        HttpSession session = request.getSession();
        Locale locale = (Locale) session.getAttribute(SessionManager.SESSION_LOCALE);
        String theme = (String) session.getAttribute(SessionManager.SESSION_THEME);
        session.invalidate();
        session = request.getSession();
        session.setMaxInactiveInterval(ConfigManager.auth.getSessionTimeoutSeconds());
        if (locale != null) {
            session.setAttribute(SessionManager.SESSION_INIT, true);
            session.setAttribute(SessionManager.SESSION_LOCALE, locale);
        }
        if (theme != null) {
            session.setAttribute(SessionManager.SESSION_THEME, theme);
        }
        CookieManager.setUserId(response, String.valueOf(user.getId()));
        CookieManager.setSessionToken(response, user.getSessionToken());
    }
}
