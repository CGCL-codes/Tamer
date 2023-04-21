package com.ttdev.centralwire;

import java.util.Map;
import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

public class CentralWireSession extends AuthenticatedWebSession {

    private static final Logger logger = LoggerFactory.getLogger(CentralWireSession.class);

    private static final long serialVersionUID = 1L;

    @SpringBean
    private AuthenticationManager authMgr;

    @SpringBean
    private Map<String, String> roleMap;

    public CentralWireSession(Request request) {
        super(request);
        InjectorHolder.getInjector().inject(this);
    }

    @Override
    public boolean authenticate(String username, String password) {
        try {
            Authentication auth = authMgr.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    @Override
    public Roles getRoles() {
        CentralWireApp app = (CentralWireApp) getApplication();
        Roles mockedRoles = app.getMockedUserRoles();
        if (mockedRoles != null) {
            return mockedRoles;
        }
        Roles roles = new Roles();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority ga : auth.getAuthorities()) {
            String authString = mapRole(ga.getAuthority());
            if (authString != null) {
                roles.add(authString);
            }
        }
        return roles;
    }

    private String mapRole(String authString) {
        String ResultedRole = roleMap.get(authString.toLowerCase());
        if (ResultedRole != null) {
            ResultedRole = "ROLE_" + ResultedRole.toUpperCase();
            logger.info("Mapping role {} to {}", authString, ResultedRole);
        } else {
            logger.warn("Unable to map role {}", authString);
        }
        return ResultedRole;
    }
}
