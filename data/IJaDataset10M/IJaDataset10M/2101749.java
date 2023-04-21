package org.brandao.brutos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.brandao.brutos.mapping.MethodForm;

/**
 * @deprecated 
 * @author Afonso Brandao
 */
public class DefaultResourceMethod implements ResourceMethod {

    MethodForm method;

    public DefaultResourceMethod(MethodForm method) {
        this.method = method;
    }

    public Object invoke(Object source, Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return method.invoke(source, args);
    }

    public Class getResourceClass() {
        return method.getMethod() == null ? null : method.getMethod().getDeclaringClass();
    }

    public Method getMethod() {
        return method.getMethod();
    }

    public Class returnType() {
        return method.getMethod().getReturnType();
    }

    public Class[] getParametersType() {
        return method.getMethod().getParameterTypes();
    }

    public boolean isAbstract() {
        return this.method.isAbstract();
    }

    public MethodForm getMethodForm() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
