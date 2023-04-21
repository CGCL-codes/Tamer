package com.liferay.portal.spring.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * <a href="ServiceInterceptor.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ServiceInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            if ((_classLoader != null) && (contextClassLoader != _classLoader)) {
                Thread.currentThread().setContextClassLoader(_classLoader);
            }
            return invocation.proceed();
        } catch (Throwable t) {
            if (_exceptionSafe) {
                return null;
            } else {
                throw t;
            }
        } finally {
            if ((_classLoader != null) && (contextClassLoader != _classLoader)) {
                Thread.currentThread().setContextClassLoader(contextClassLoader);
            }
        }
    }

    public void setClassLoader(ClassLoader classLoader) {
        _classLoader = classLoader;
    }

    public void setExceptionSafe(boolean exceptionSafe) {
        _exceptionSafe = exceptionSafe;
    }

    private ClassLoader _classLoader;

    private boolean _exceptionSafe;
}
