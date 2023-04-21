package com.wideplay.warp.persist.internal;

import net.jcip.annotations.Immutable;
import net.sf.cglib.proxy.InvocationHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * <p>
 *
 * This is a simple adapter to convert a JDK dynamic proxy invocation into an aopalliance invocation.
 * Mainly for supporting {@code @Finder} on interface methods.
 * </p>
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 * @since 1.0
 */
@Immutable
public class AopAllianceJdkProxyAdapter implements InvocationHandler {

    private final MethodInterceptor interceptor;

    public AopAllianceJdkProxyAdapter(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public Object invoke(final Object object, final Method method, final Object[] objects) throws Throwable {
        return interceptor.invoke(new MethodInvocation() {

            public Method getMethod() {
                return method;
            }

            public Object[] getArguments() {
                return objects;
            }

            public Object proceed() throws Throwable {
                return method.invoke(object, objects);
            }

            public Object getThis() {
                return object;
            }

            public AccessibleObject getStaticPart() {
                return method;
            }
        });
    }
}
