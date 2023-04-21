package org.tynamo.security.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

/**
 * @see SecurityService#hasRole(String)
 */
public class HasAllRoles {

    @Inject
    private SecurityService securityService;

    @Parameter(required = true, defaultPrefix = "literal")
    private String roles;

    boolean beforeRenderBody() {
        return securityService.hasAllRoles(roles);
    }
}
