package com.actionbazaar.buslogic;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Security Interceptor
 */
public class SecurityInterceptor {

    @SuppressWarnings({ "EjbEnvironmentInspection" })
    @Resource
    private SessionContext sessionContext;

    @AroundInvoke
    public Object checkUserRole(InvocationContext context) throws Exception {
        if (!sessionContext.isCallerInRole("CSR")) {
            throw new SecurityException("No permission to cancel bid.");
        }
        return context.proceed();
    }
}
