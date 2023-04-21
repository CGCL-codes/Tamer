package br.com.arsmachina.tapestrysecurity.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.base.AbstractConditional;
import org.apache.tapestry5.ioc.annotations.Inject;
import br.com.arsmachina.authentication.service.UserService;

/**
 * Component tha render its tag (if such) and body only if the current user has a given permission.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@SupportsInformalParameters
public class IfHasPermission extends AbstractConditional {

    @Inject
    private UserService userService;

    /**
	 * The name of the permission.
	 */
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String permission;

    /**
	 * If <code>true</code>, inverts the test.
	 */
    @Parameter(value = "false", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private boolean negate;

    @Override
    protected boolean test() {
        String[] permissions = permission.split("[,\\s]+");
        return userService.hasPermissions(permissions) != negate;
    }
}
