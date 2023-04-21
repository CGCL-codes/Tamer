package com.unitt.commons.authentication;

import com.unitt.commons.security.Identity;

public class MockProvider implements AuthenticationProvider {

    public boolean accepts(AuthenticationToken aToken) {
        if (aToken instanceof MockToken) {
            return ((MockToken) aToken).isAllowingAccepts();
        }
        return false;
    }

    public Identity authenticate(AuthenticationToken aToken) throws BadCredentialsException {
        MockToken token = (MockToken) aToken;
        if (token.isAllowingAuthentication()) {
            return token.getIdentity();
        }
        throw new BadCredentialsException(token);
    }
}
