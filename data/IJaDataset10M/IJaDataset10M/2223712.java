package com.gm.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 抽象动态代理
 * @author azheng
 */
public abstract class BaseDynamicProxy implements InvocationHandler {

    protected Object target;

    public Object createProxyObject(Object obj) {
        this.target = obj;
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
    }

    protected abstract void doBefore();

    protected abstract Object doAfter(Object res);

    /** 
	 * 调用代理对象的方法，并返回方法调用结果
	 * (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        doBefore();
        Object res = method.invoke(target, args);
        Object endRes = doAfter(res);
        return endRes;
    }
}
