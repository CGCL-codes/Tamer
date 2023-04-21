package com.objecteffects.sample.web.stripes;

import javax.servlet.http.HttpServletRequest;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public final class LoginInterceptor implements Interceptor {

    @SuppressWarnings("unused")
    private final transient Log log = LogFactory.getLog(getClass());

    private final UserService userService = UserServiceFactory.getUserService();

    public Resolution intercept(final ExecutionContext context) throws Exception {
        final HttpServletRequest request = context.getActionBeanContext().getRequest();
        if (!this.userService.isUserLoggedIn()) return (new RedirectResolution(this.userService.createLoginURL(request.getRequestURI())));
        return (context.proceed());
    }
}
