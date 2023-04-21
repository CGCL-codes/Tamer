package com.curlap.orb.generator;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * MethodProperty
 */
public class MethodProperty implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String modifier;

    private String returnType;

    private String[] argumentTypes;

    private String serverMethodName;

    public MethodProperty() {
    }

    public MethodProperty(Method method) {
        name = method.getName();
        modifier = Modifier.toString(method.getModifiers());
        returnType = method.getReturnType().getName();
        Class[] params = method.getParameterTypes();
        argumentTypes = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            argumentTypes[i] = params[i].getName();
        }
        serverMethodName = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String[] getArgumentTypes() {
        return argumentTypes;
    }

    public void setArgumentTypes(String[] argumentTypes) {
        this.argumentTypes = argumentTypes;
    }

    public String getServerMethodName() {
        return serverMethodName;
    }

    public void setServerMethodName(String serverMethodName) {
        this.serverMethodName = serverMethodName;
    }
}
