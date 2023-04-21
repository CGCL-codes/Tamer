package com.googlecode.janrain4j.springframework.security;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import com.googlecode.janrain4j.api.engage.response.UserDataResponse;

/**
 * {@link org.springframework.security.core.Authentication} implementation for 
 * Janrain authentication.
 * 
 * @author Marcel Overdijk
 * @since 1.1
 */
@SuppressWarnings("serial")
public class JanrainAuthenticationToken extends AbstractAuthenticationToken {

    private Object principal = null;

    private UserDataResponse userDataResponse = null;

    /**
     * Constructor used for an authentication request. The {@link
     * org.springframework.security.core.Authentication#isAuthenticated()} will
     * return <code>false</code>.
     *
     * @param userDataResponse The <code>UserDataResponse</code>.
     */
    public JanrainAuthenticationToken(UserDataResponse userDataResponse) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.principal = userDataResponse.getProfile().getIdentifier();
        this.userDataResponse = userDataResponse;
        setAuthenticated(false);
    }

    /**
     * Constructor used for an authentication response. The {@link
     * org.springframework.security.core.Authentication#isAuthenticated()} will
     * return <code>true</code>.
     *
     * @param principal The authenticated principal.
     * @param authorities The granted authorities.
     * @param userDataResponse The <code>UserDataResponse</code>.
     */
    public JanrainAuthenticationToken(Object principal, Collection<GrantedAuthority> authorities, UserDataResponse userDataResponse) {
        super(authorities);
        this.principal = principal;
        this.userDataResponse = userDataResponse;
        setAuthenticated(true);
    }

    /**
     * Returns the credentials.
     */
    public Object getCredentials() {
        return userDataResponse.getAccessCredentials();
    }

    /**
     * Returns the principal.
     */
    public Object getPrincipal() {
        return principal;
    }

    /**
     * Returns the user data Janrain Engage knows about the user signing into your website.
     */
    public UserDataResponse getUserDataResponse() {
        return userDataResponse;
    }
}
