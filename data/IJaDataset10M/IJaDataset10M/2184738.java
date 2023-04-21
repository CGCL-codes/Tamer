package br.edu.uncisal.interceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class PermissionInterceptor implements Interceptor {

    private static final long serialVersionUID = -7918135431675185762L;

    public void init() {
    }

    public void destroy() {
    }

    public String intercept(ActionInvocation actionInvocation) throws Exception {
        Object estudante = ActionContext.getContext().getSession().get("estudante");
        if (estudante != null) {
            return actionInvocation.invoke();
        }
        return "init";
    }
}
