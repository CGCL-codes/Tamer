package com.googlecode.ehcache.annotations.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Eric Dalquist
 * @version $Revision: 656 $
 */
public class MockMethodInvocation implements MethodInvocation {

    private Method method;

    private Object[] arguments;

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return this.method;
    }

    public Object[] getArguments() {
        return this.arguments;
    }

    public AccessibleObject getStaticPart() {
        throw new UnsupportedOperationException();
    }

    public Object getThis() {
        throw new UnsupportedOperationException();
    }

    public Object proceed() throws Throwable {
        throw new UnsupportedOperationException();
    }
}
