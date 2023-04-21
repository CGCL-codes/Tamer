package at.redcross.tacos.web.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

public class WebSecurityExpressionRoot extends SecurityExpressionRoot {

    public WebSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }
}
