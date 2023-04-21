package net.sf.beanshield.aop;

import net.sf.beanshield.session.ShieldSessionImplementor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import java.lang.reflect.Method;

/**
 * This interceptor does only one thing: It catches the call to setSession and returns just before invocation
 * of the target bean.
 */
public class InitProxyInterceptor implements MethodInterceptor {

    private static final InitProxyInterceptor INSTANCE = new InitProxyInterceptor();

    public static InitProxyInterceptor getSingletonInstance() {
        return INSTANCE;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        String methodName = method.getName();
        int methodArgumentCount = method.getParameterTypes().length;
        if (methodArgumentCount == 1 && "init".equals(methodName) && invocation.getArguments()[0] instanceof ShieldSessionImplementor) {
            return null;
        } else {
            return invocation.proceed();
        }
    }
}
