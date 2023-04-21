package org.ztemplates.web.request.impl;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.ztemplates.actions.ZISecurityProvider;
import org.ztemplates.web.ZTemplates;

public class ZSecurityProviderImpl implements ZISecurityProvider {

    public boolean isUserInRole(String role) {
        HttpServletRequest request = ZTemplates.getServletService().getRequest();
        return request.isUserInRole(role);
    }

    public String getUserName() {
        HttpServletRequest request = ZTemplates.getServletService().getRequest();
        Principal p = request.getUserPrincipal();
        return p == null ? null : p.getName();
    }
}
