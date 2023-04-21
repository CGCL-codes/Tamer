package com.jacum.cms.wsimpl;

import com.jacum.cms.security.SecurityService;
import com.jacum.cms.security.PrincipalDetails;

/**
 * @author timur
 */
public class FakeSecurityService implements SecurityService {

    public String loginWithUsernameAndPassword(String username, String password) {
        return "fake token";
    }

    public boolean isSessionValid(String sessionId) {
        return true;
    }

    public void initializeContext(String securitySessionId) {
    }

    public boolean isAuthorized(String functionId, Object item) {
        return true;
    }

    public void clearContext() {
    }

    public void logout(String sessionId) {
    }

    public PrincipalDetails getPrincipal() {
        return PrincipalDetails.getAnonymous();
    }

    public String getSecuritySessionId() {
        return "fake session id";
    }
}
