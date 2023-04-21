package org.t2framework.commons.exception;

import org.t2framework.commons.util.Reflections.ClassUtil;
import org.t2framework.commons.util.Reflections.MethodUtil;

public class NoSuchConstructorRuntimeException extends BaseRuntimeException {

    private static final long serialVersionUID = 1L;

    private Class<?>[] argTypes;

    public NoSuchConstructorRuntimeException(Throwable cause, Class<?> clazz) {
        super(cause, "ECMN0019", clazz.getName());
    }

    public NoSuchConstructorRuntimeException(Class<?> targetClass, Class<?>[] argTypes, NoSuchMethodException cause) {
        super(cause, "ECMN0020", getArgs(targetClass, argTypes, cause));
        this.argTypes = argTypes;
    }

    private static Object[] getArgs(Class<?> targetClass, Class<?>[] argTypes, Throwable cause) {
        return new Object[] { targetClass.getName(), MethodUtil.getSignature(ClassUtil.getShortClassName(targetClass), argTypes), cause };
    }

    public Class<?>[] getArgTypes() {
        return argTypes;
    }
}
