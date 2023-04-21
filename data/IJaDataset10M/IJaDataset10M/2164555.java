package com.yilan.java.binding;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Han Liu  2007-10-23 
 * The Object passing down the java instance communication 
 */
public class JavaInvocationObject implements Serializable {

    private static final long serialVersionUID = 2025879024310531455L;

    private String invocationId;

    private transient Method method;

    /** The fields are for serialization */
    private Class<?> interfaze;

    private String methodName;

    private Class<?>[] methodargs;

    private String[] methodargsNames;

    private Class<?> resultType;

    private Object[] args;

    private Object result;

    private transient Object proxy;

    public String getInvocationId() {
        return invocationId;
    }

    public void setInvocationId(String invocationId) {
        this.invocationId = invocationId;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setMethod(Method method) {
        this.method = method;
        this.interfaze = method.getDeclaringClass();
        this.methodName = method.getName();
        this.methodargs = method.getParameterTypes();
        methodargsNames = new String[methodargs.length];
        for (int i = 0; i < methodargs.length; i++) {
            methodargsNames[i] = methodargs[i].getName();
        }
        resultType = method.getReturnType();
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public Class<?> getInterfaze() {
        return interfaze;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getMethodargs() {
        return methodargs;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String[] getMethodargsNames() {
        return methodargsNames;
    }
}
