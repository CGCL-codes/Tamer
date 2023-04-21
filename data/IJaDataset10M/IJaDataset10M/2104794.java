package org.aspectme.api;

import org.aspectme.cldc.reflect.Method;

/**
 * The joinpoint for method invocations.
 * 
 * @author Magnus Robertsson
 */
public interface MethodInvocation extends Joinpoint {

    public Method getMethod();

    public Object[] getArguments();
}
