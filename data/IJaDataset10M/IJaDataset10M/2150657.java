package net.sf.jzeno.aop;

import java.lang.reflect.Method;

/**
 * @author ddhondt
 * 
 * This interface is implemented by all aspects in the system. It allows aspects
 * to perform certain functions before and after method invocation on the object
 * being decorated with these aspects.
 */
public interface Aspect {

    Object invoke(Method method, Object[] arguments) throws Throwable;
}
