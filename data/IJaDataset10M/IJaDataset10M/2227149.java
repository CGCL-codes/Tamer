package org.tynamo.security.components;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.tynamo.security.services.PageService;
import org.tynamo.security.services.SecurityService;

/**
 * If subject is not authenticated rendered link to login page,
 * else link to logout.
 *
 */
public class LoginLink {

    @Inject
    @Property
    private SecurityService securityService;

    @Inject
    private PageService pageService;

    public String onActionFromTynamoLoginLink() {
        removeSavedRequest();
        return pageService.getLoginPage();
    }

    public void onActionFromTynamoLogoutLink() {
        securityService.getSubject().logout();
    }

    private void removeSavedRequest() {
        Subject subject = securityService.getSubject();
        if (subject != null) {
            subject.getSession().removeAttribute(WebUtils.SAVED_REQUEST_KEY);
        }
    }
}
