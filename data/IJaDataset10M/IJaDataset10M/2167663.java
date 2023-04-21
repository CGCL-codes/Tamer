package org.piuframework.service;

import org.piuframework.service.impl.MethodInvocation;

/**
 * TODO
 * 
 * @author Dirk Mascher
 */
public interface Interceptor {

    public String getName();

    public Object invoke(MethodInvocation invocation) throws Throwable;
}
