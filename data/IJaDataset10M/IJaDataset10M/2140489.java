package com.sitescape.team.asmodule.security.jaas;

import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import com.sitescape.team.asmodule.bridge.BridgeUtil;

public class SiteScapeLoginModule implements LoginModule {

    private static final String CLASS_NAME = "com.sitescape.team.security.jaas.SiteScapeLoginModule";

    private LoginModule loginModule;

    public SiteScapeLoginModule() {
        try {
            Class classObj = Class.forName(CLASS_NAME, true, BridgeUtil.getClassLoader());
            loginModule = (LoginModule) classObj.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean abort() throws LoginException {
        return loginModule.abort();
    }

    public boolean commit() throws LoginException {
        return loginModule.commit();
    }

    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        loginModule.initialize(subject, callbackHandler, sharedState, options);
    }

    public boolean login() throws LoginException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(BridgeUtil.getClassLoader());
            return loginModule.login();
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    public boolean logout() throws LoginException {
        return loginModule.logout();
    }
}
