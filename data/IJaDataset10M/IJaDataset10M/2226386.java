package com.technoetic.xplanner.security.module.jaas;

import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.module.LoginSupport;

/**
 * This is an unsupported JAAS login module adapter. It's provide as an
 * example of a LoginModule implementation.
 */
public class JaasLoginModuleAdapter implements LoginModule {

    protected transient Logger log = Logger.getLogger(getClass());

    private static final String USERID = "javax.security.auth.login.name";

    private static final String PASSWORD = "javax.security.auth.login.password";

    private Class principalClass;

    private Map options;

    private String name;

    private transient javax.security.auth.spi.LoginModule jaasLoginModule;

    private transient LoginSupport loginSupport;

    public JaasLoginModuleAdapter(LoginSupport support) {
        loginSupport = support;
    }

    public JaasLoginModuleAdapter(LoginSupport support, javax.security.auth.spi.LoginModule jaasLoginModule, Class principalClass, Map options) {
        this.options = options;
        this.principalClass = principalClass;
        this.jaasLoginModule = jaasLoginModule;
        this.loginSupport = support;
    }

    public void setOptions(Map options) {
        this.options = options;
    }

    public javax.security.auth.spi.LoginModule getJAASLoginModule() {
        return jaasLoginModule;
    }

    public Class getPrincipalClass() {
        return principalClass;
    }

    public Subject authenticate(String userId, String password) throws AuthenticationException {
        log.debug(ATTEMPTING_TO_AUTHENTICATE + this.getName() + " (" + userId + ")");
        Subject subject = loginSupport.createSubject();
        Map sharedState = new HashMap();
        sharedState.put(USERID, userId);
        sharedState.put(PASSWORD, password.toCharArray());
        jaasLoginModule.initialize(subject, new UserIdAndPasswordCallbackHandler(userId, password), sharedState, options);
        try {
            if (jaasLoginModule.login()) {
                jaasLoginModule.commit();
            } else {
                throw new AuthenticationException(MESSAGE_AUTHENTICATION_FAILED_KEY);
            }
        } catch (FailedLoginException e) {
            throw new AuthenticationException(MESSAGE_AUTHENTICATION_FAILED_KEY);
        } catch (LoginException e) {
            log.error("login error", e);
            throw new AuthenticationException(MESSAGE_SERVER_ERROR_KEY);
        }
        Set principals = subject.getPrincipals(getPrincipalClass());
        Iterator principalIterator = principals.iterator();
        if (principalIterator.hasNext()) {
            Principal jaasUserPrincipal = (Principal) principalIterator.next();
            loginSupport.populateSubjectPrincipalFromDatabase(subject, jaasUserPrincipal.getName());
        }
        log.debug(AUTHENTICATION_SUCCESFULL + this.getName());
        return subject;
    }

    public boolean isCapableOfChangingPasswords() {
        return false;
    }

    public void changePassword(String userId, String password) throws AuthenticationException {
        throw new UnsupportedOperationException("changePassword not supported");
    }

    public void logout(HttpServletRequest request) throws AuthenticationException {
        request.getSession().invalidate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLoginSupport(LoginSupport loginSupport) {
        this.loginSupport = loginSupport;
    }
}
