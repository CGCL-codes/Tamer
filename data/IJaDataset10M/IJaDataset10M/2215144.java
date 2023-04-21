package org.tynamo.security.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

/**
 * @see SecurityService#isLacksPermission(String)
 */
public class LacksPermission {

    @Inject
    private SecurityService securityService;

    @Parameter(required = true, defaultPrefix = "literal")
    private String permission;

    boolean beforeRenderBody() {
        return securityService.isLacksPermission(permission);
    }
}
