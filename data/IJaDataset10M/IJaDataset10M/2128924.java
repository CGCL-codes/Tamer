package com.wideplay.warp.security;

import com.google.inject.Key;
import com.google.inject.matcher.Matcher;
import org.acegisecurity.userdetails.UserDetailsService;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 17/10/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class SimpleConfig {

    private Key<? extends UserDetailsService> userDetailsService;

    private String successUrl;

    private String failUrl;

    private String logoutUrl;

    private Matcher<? super Class<?>> classMatcher = null;

    private Matcher<? super Method> methodMatcher = null;

    private String anonymousKey;

    private String denyUrl;

    private final List<AuthorizationEntry> authorizations = new ArrayList<AuthorizationEntry>();

    public Matcher<? super Class<?>> getClassMatcher() {
        return classMatcher;
    }

    public void setClassMatcher(Matcher<? super Class<?>> classMatcher) {
        this.classMatcher = classMatcher;
    }

    public String getFailUrl() {
        return failUrl;
    }

    public void setFailUrl(String failUrl) {
        this.failUrl = failUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public Matcher<? super Method> getMethodMatcher() {
        return methodMatcher;
    }

    public void setMethodMatcher(Matcher<? super Method> methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public Key<? extends UserDetailsService> getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(Key<? extends UserDetailsService> userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String getAnonymousKey() {
        return anonymousKey;
    }

    public void setAnonymousKey(String anonymousKey) {
        this.anonymousKey = anonymousKey;
    }

    public String getDenyUrl() {
        return denyUrl;
    }

    public void setDenyUrl(String denyUrl) {
        this.denyUrl = denyUrl;
    }

    public void addAuthorization(String antUrlPattern, String[] roles) {
        this.authorizations.add(new AuthorizationEntry(antUrlPattern, roles));
    }

    public List<AuthorizationEntry> getAuthorizations() {
        return authorizations;
    }

    public boolean shouldSecureMethods() {
        return null != classMatcher && null != methodMatcher;
    }

    static class AuthorizationEntry {

        private final String antUrlPattern;

        private final String[] roles;

        private AuthorizationEntry(String antUrlPattern, String[] roles) {
            this.antUrlPattern = antUrlPattern;
            this.roles = roles;
        }

        public String getAntUrlPattern() {
            return antUrlPattern;
        }

        public String[] getRoles() {
            return roles;
        }
    }
}
