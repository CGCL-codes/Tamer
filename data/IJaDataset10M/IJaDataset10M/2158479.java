package com.googlecode.proxymatic.core.handlers.method;

import com.googlecode.proxymatic.core.MethodInvoker;
import com.googlecode.proxymatic.core.ReturnTypeModifier;
import com.googlecode.proxymatic.core.RuntimeContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConstructorMethodInvoker implements MethodInvoker {

    private final Constructor targetConstructor;

    private final ParameterModifier parameterModifier;

    private final ReturnTypeModifier returnTypeModifier;

    public ConstructorMethodInvoker(Constructor targetConstructor) {
        this(targetConstructor, ParameterModifier.NO_MODIFICATIONS, ReturnTypeModifier.NO_MODIFICATIONS);
    }

    public ConstructorMethodInvoker(Constructor targetConstructor, ParameterModifier parameterModifier, ReturnTypeModifier returnTypeModifier) {
        this.targetConstructor = targetConstructor;
        this.parameterModifier = parameterModifier;
        this.returnTypeModifier = returnTypeModifier;
    }

    public Object invoke(Method method, Object[] parameters, RuntimeContext context) throws Throwable {
        try {
            targetConstructor.setAccessible(true);
            Object[] modifiedParameters = parameterModifier.modifyParameters(parameters, method.getParameterTypes(), targetConstructor.getParameterTypes());
            Object o = targetConstructor.newInstance(modifiedParameters);
            return returnTypeModifier.modifyReturnType(method, o, targetConstructor.getDeclaringClass());
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    public Class[] getExceptionTypes() {
        return targetConstructor.getExceptionTypes();
    }
}
