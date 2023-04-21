package com.jameba.jhdc.thirdparty.struts.interceptor;

import java.util.Map;
import com.jameba.jhdc.user.IJhdcUser;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class AuthenticationInterceptor implements Interceptor {

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation actionInvocation) throws Exception {
        Map session = actionInvocation.getInvocationContext().getSession();
        IJhdcUser user = (IJhdcUser) session.get(IJhdcUser.USER_KEY);
        boolean isAuthenticated = (null != user);
        if (!isAuthenticated) {
            return Action.LOGIN;
        } else {
            return actionInvocation.invoke();
        }
    }
}
