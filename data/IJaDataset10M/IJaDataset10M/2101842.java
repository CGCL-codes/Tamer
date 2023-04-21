package com.curl.orb.common;

import java.util.Arrays;

/**
 * Request to invoke the instance in HttpSession.
 * 
 * @author Hitoshi Okada
 * @since 0.5
 */
public class InvokeHttpSessionRequest extends InstanceManagementRequest {

    private static final long serialVersionUID = 1L;

    private String objectId;

    private String className;

    private String methodName;

    private Object arguments[];

    public InvokeHttpSessionRequest() {
        super();
        objectId = null;
        className = null;
        methodName = null;
        arguments = null;
    }

    @Override
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setArguments(Object arguments[]) {
        this.arguments = arguments;
    }

    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "[Header:" + getHeader() + " ]" + "[Object ID:" + objectId + " ]" + "[Class name:" + className + " ]" + "[Method name:" + methodName + " ]" + "[Arguments:" + Arrays.toString(arguments) + "]";
    }
}
