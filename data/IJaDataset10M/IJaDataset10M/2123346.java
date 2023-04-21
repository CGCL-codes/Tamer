package com.gwtent.client.aop.intercept.impl;

import com.gwtent.client.aop.intercept.MethodInterceptor;
import com.gwtent.client.aop.intercept.MethodInvocation;

/**
 * This class is the default implement of last call in AOP invoke chain.
 * 
 * Please don't use this for other purpose,
 * when a call arrive to AOP Generated class, method will check 
 * Interceptor's type, if the type assignable of this class,
 * Generated class will direct call super class's implement.
 * 
 * @author JLuo
 *
 */
public class MethodInterceptorFinalAdapter implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {
        return invocation.getMethod().invoke(invocation.getThis(), invocation.getArguments());
    }
}
