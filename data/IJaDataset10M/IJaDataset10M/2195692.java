package com.siberhus.stars.security;

import java.lang.reflect.Method;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.aop.AnnotationsAuthorizingMethodInterceptor;

/**
 * 
 * Thanks Janne Jalkanen for your helpful information
 * http://www.ecyrd.com/ButtUgly/wiki/Main_blogentry_100910_1
 * 
 * 
 */
@Intercepts(LifecycleStage.HandlerResolution)
public class ShiroSecurityInterceptor extends AnnotationsAuthorizingMethodInterceptor implements Interceptor {

    public Resolution intercept(ExecutionContext ctx) throws Exception {
        Resolution resolution = ctx.proceed();
        MethodInvocation mi = new StripesMethodInvocation(ctx);
        assertAuthorized(mi);
        return resolution;
    }

    private static class StripesMethodInvocation implements MethodInvocation {

        private ExecutionContext executionContext;

        public StripesMethodInvocation(ExecutionContext executionContext) {
            this.executionContext = executionContext;
        }

        public Object[] getArguments() {
            return null;
        }

        public Method getMethod() {
            return executionContext.getHandler();
        }

        public Object getThis() {
            return executionContext.getActionBean();
        }

        public Object proceed() throws Throwable {
            return null;
        }
    }
}
