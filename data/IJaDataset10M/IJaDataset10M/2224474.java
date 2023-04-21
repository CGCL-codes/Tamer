package net.sourceforge.ecm.security;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;

/**
 * TODO abr forgot to document this class
 */
public class MyAuthentication implements Authentication {

    private boolean authenticated;

    private String principal;

    private String credentials;

    private String name;

    private GrantedAuthority[] authorities;

    public MyAuthentication(String principal, String credentials, GrantedAuthority[] authorities) {
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities;
        setAuthenticated(true);
    }

    public GrantedAuthority[] getAuthorities() {
        return authorities;
    }

    public Object getCredentials() {
        return credentials;
    }

    public Object getDetails() {
        return null;
    }

    public Object getPrincipal() {
        return principal;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }

    public String getName() {
        return name;
    }
}
