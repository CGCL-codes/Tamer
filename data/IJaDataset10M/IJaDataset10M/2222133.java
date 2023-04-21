package org.ztemplates.web.request.impl;

import org.ztemplates.actions.ZISecurityProvider;
import org.ztemplates.web.ZISecurityService;

public class ZSecurityServiceImpl implements ZISecurityService {

    private final ZISecurityProvider securityProvider;

    public ZSecurityServiceImpl(ZISecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

    public ZISecurityProvider getSecurityProvider() {
        return securityProvider;
    }

    public String getUserName() throws Exception {
        return securityProvider.getUserName();
    }

    public boolean isUserInRole(String role) throws Exception {
        return securityProvider.isUserInRole(role);
    }
}
