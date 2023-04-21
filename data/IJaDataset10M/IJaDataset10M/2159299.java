package com.fdv.template.web.interceptor;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Map;

public class AuthenticationAjaxInterceptor implements Interceptor {

    private static final long serialVersionUID = 1L;

    public static final String INVOCATED_ACTION = "action";

    private static Log log = LogFactory.getLog(AuthenticationAjaxInterceptor.class);

    public void destroy() {
    }

    public void init() {
    }

    @SuppressWarnings("unchecked")
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        log.debug("Entering authentication interceptor");
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String action = actionInvocation.getProxy().getActionName();
            Map session = actionInvocation.getInvocationContext().getSession();
            session.put(INVOCATED_ACTION, action);
            return "relogin";
        } else {
            String result = actionInvocation.invoke();
            return result;
        }
    }
}
