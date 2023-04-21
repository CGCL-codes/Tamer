package org.or5e.web.interceptor;

import org.or5e.core.BaseObject;
import org.or5e.web.core.AAAManagement;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class LoginInterceptor extends BaseObject implements Interceptor {

    private static final long serialVersionUID = -7139341799108623756L;

    private AAAManagement aaaManagement = null;

    public final AAAManagement getAaaManagement() {
        return aaaManagement;
    }

    public final void setAaaManagement(AAAManagement aaaManagement) {
        this.aaaManagement = aaaManagement;
    }

    @Override
    public void destroy() {
        System.out.println("Destroying Login Interceptor...");
    }

    @Override
    public void init() {
        System.out.println("Initializing Login Interceptor...");
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        System.out.println("Validating the User..." + this.aaaManagement);
        return invocation.invoke();
    }

    @Override
    public String getName() {
        return "LoginInterceptor";
    }
}
