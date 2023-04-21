package com.simconomy.server.performance.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import com.simconomy.server.performance.monitor.Monitor;

/**
 * AopAlliance MethodInterceptor that may be used within Spring and other aop
 * Alliance frameworks to collect method invocation times.
 *
 * @author void.fm
 * @version $Revision$
 */
public class PerformanceMethodCallInterceptor implements MethodInterceptor {

    private final Monitor monitor;

    public PerformanceMethodCallInterceptor(Monitor monitor) {
        this.monitor = monitor;
    }

    public Object invoke(MethodInvocation aMethodInvocation) throws Throwable {
        Class clazz = aMethodInvocation.getThis().getClass();
        monitor.startMeasurement(clazz.getName(), aMethodInvocation.getMethod().getName());
        try {
            return aMethodInvocation.proceed();
        } catch (Throwable t) {
            monitor.addException(t.getClass().getName());
            throw t;
        } finally {
            monitor.endMeasurement();
        }
    }

    /**
	 * Calculate short name for a given class.
	 *
	 * @param clazz
	 *            The class object.
	 * @return The short name for the given class.
	 */
    protected String calculateShortName(Class clazz) {
        String name = clazz.getName();
        int beginIndex = name.lastIndexOf('.');
        if (beginIndex > 0) {
            return name.substring(beginIndex + 1);
        } else {
            return name;
        }
    }
}
