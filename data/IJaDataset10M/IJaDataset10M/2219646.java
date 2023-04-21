package org.chessworks.common.javatools.collections;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.chessworks.common.javatools.RetargetableProxy;

/**
 * @author "Doug Bateman" ("DuckStorm")
 */
public class ProxyTargetInvocationHandler<T> implements InvocationHandler, RetargetableProxy<T> {

    private T proxyTarget;

    public ProxyTargetInvocationHandler() {
    }

    public ProxyTargetInvocationHandler(T initialTarget) {
        this.proxyTarget = initialTarget;
    }

    public T getProxyTarget() {
        return proxyTarget;
    }

    public void setProxyTarget(T proxyTarget) {
        this.proxyTarget = proxyTarget;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxyTarget, args);
    }
}
