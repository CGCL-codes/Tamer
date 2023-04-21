package com.objectcode.time4u.server.web.wicket;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.objectcode.time4u.server.auth.ITime4UUserDetails;

public class Time4UAuthenticatedWebSession extends AuthenticatedWebSession {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(Time4UAuthenticatedWebSession.class);

    public Time4UAuthenticatedWebSession(final AuthenticatedWebApplication application, Request request) {
        super(application, request);
    }

    @Override
    public boolean authenticate(final String username, final String password) {
        String u = username == null ? "" : username;
        String p = password == null ? "" : password;
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(u, p);
        try {
            AuthenticationManager authenticationManager = ((Time4UApplication) getApplication()).getAuthenticationManager();
            Authentication authResult = authenticationManager.authenticate(authRequest);
            setAuthentication(authResult);
            log.info("Login by user '" + username + "'.");
            return true;
        } catch (BadCredentialsException e) {
            log.info("Failed login by user '" + username + "'.");
            setAuthentication(null);
            return false;
        } catch (AuthenticationException e) {
            log.error("Could not authenticate a user", e);
            setAuthentication(null);
            throw e;
        } catch (RuntimeException e) {
            log.error("Unexpected exception while authenticating a user", e);
            setAuthentication(null);
            throw e;
        }
    }

    @Override
    public void signOut() {
        super.signOut();
        ITime4UUserDetails user = getUser();
        if (user != null) {
            log.info("Logout by user '" + user.getUsername() + "'.");
        }
        setAuthentication(null);
        invalidate();
    }

    public ITime4UUserDetails getUser() {
        ITime4UUserDetails user = null;
        if (isSignedIn()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            user = (ITime4UUserDetails) authentication.getPrincipal();
        }
        return user;
    }

    @Override
    public Roles getRoles() {
        if (isSignedIn()) {
            Roles roles = new Roles();
            GrantedAuthority[] authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            for (int i = 0; i < authorities.length; i++) {
                GrantedAuthority authority = authorities[i];
                roles.add(authority.getAuthority());
            }
            return roles;
        }
        return null;
    }

    public static Time4UAuthenticatedWebSession get() {
        return (Time4UAuthenticatedWebSession) Session.get();
    }

    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
