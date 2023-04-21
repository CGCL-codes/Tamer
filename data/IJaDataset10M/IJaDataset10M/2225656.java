package com.mycila.aop.internal;

import com.mycila.aop.ProxyConfig;
import com.mycila.aop.ProxyConstruction;
import com.mycila.aop.ProxyConstructor;
import com.mycila.aop.ProxyElement;
import org.aopalliance.intercept.MethodInvocation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JdkProxyConstruction implements ProxyConstruction, ProxyElement, InvocationHandler, ProxyConstructor {

    private final ProxyConfig proxyConfig;

    private boolean equalsDefined;

    public JdkProxyConstruction(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    @Override
    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    @Override
    public ProxyConstructor getProxyConstructor(Class<?>... parameterTypes) {
        return this;
    }

    @Override
    public Object newProxyInstance(Object... arguments) {
        for (Method method : proxyConfig.getTargetClass().getMethods()) {
            if (isEquals(method)) {
                equalsDefined = true;
                break;
            }
        }
        return Proxy.newProxyInstance(proxyConfig.getClassLoader(), proxyConfig.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!equalsDefined && isEquals(method)) return equals(args[0]);
        if (proxyConfig.isConfigExposed() && method.getDeclaringClass().isInterface() && method.getDeclaringClass().isAssignableFrom(ProxyElement.class)) return method.invoke(this, args);
        MethodInvocation invocation = new ReflectiveMethodInvocation(proxy, proxyConfig.getTarget(), method, args, proxyConfig.getMethodInterceptors());
        Object ret = invocation.proceed();
        return ret != null && ret == proxyConfig.getTarget() && method.getReturnType().isInstance(proxy) ? proxy : ret;
    }

    /**
     * Equality means interfaces, advisors and TargetSource are equal.
     * <p>The compared object may be a JdkDynamicAopProxy instance itself
     * or a dynamic proxy wrapping a JdkDynamicAopProxy instance.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        ProxyConfig otherConfig;
        if (other instanceof ProxyElement) {
            otherConfig = ((ProxyElement) other).getProxyConfig();
        } else if (Proxy.isProxyClass(other.getClass())) {
            Object ih = Proxy.getInvocationHandler(other);
            if (!(ih instanceof ProxyElement)) return false;
            otherConfig = ((ProxyElement) ih).getProxyConfig();
        } else return false;
        return proxyConfig == otherConfig || proxyConfig.getTarget().equals(otherConfig.getTarget());
    }

    private boolean isEquals(Method method) {
        return method.getName().equals("equals") && method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == Object.class;
    }
}
