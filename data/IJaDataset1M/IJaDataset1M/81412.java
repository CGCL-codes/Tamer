package com.curlap.orb.servlet;

/**
 * NewInstanceRequest
 */
public class NewInstanceRequest extends InstanceManagementRequest implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String className;

    private Object arguments[];

    public NewInstanceRequest() {
        className = null;
        arguments = null;
    }

    @Override
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setArguments(Object arguments[]) {
        this.arguments = arguments;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
