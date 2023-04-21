package org.ztemplates.web.spring;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ztemplates.actions.ZISecurityProvider;
import org.ztemplates.web.ZISecurityService;
import org.ztemplates.web.ZTemplates;

/**
 * Spring proxy for ZISecurityService
 * 
 * @author gerd
 * 
 */
@Component(ZISecurityService.SPRING_NAME)
@Scope("request")
public class ZSecurityServiceSpring implements ZISecurityService {

    private final ZISecurityService service;

    public ZSecurityServiceSpring() {
        this.service = ZTemplates.getSecurityService();
    }

    public ZISecurityProvider getSecurityProvider() {
        return service.getSecurityProvider();
    }

    public String getUserName() throws Exception {
        return service.getUserName();
    }

    public boolean isUserInRole(String role) throws Exception {
        return service.isUserInRole(role);
    }
}
