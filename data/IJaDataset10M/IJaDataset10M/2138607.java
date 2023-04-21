package org.mwanzia;

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * <p>
 * Represents a remote call into a Mwanzia application. A call is identified by:
 * </p>
 * 
 * <ul>
 * <li>target (optional) - the object on which the remote instance method is
 * being called. For static method calls, target is null.</li>
 * <li>targetClass - the classname of the object/class on which the remote
 * method is being called.</li>
 * <li>method - the name of the method being called</li>
 * <li>arguments - arguments to the method that is being called</li>
 * </ul>
 * 
 * @author percy
 * 
 */
class Call {

    private Object target;

    private String targetClass;

    private String method;

    private List arguments;

    public Call() {
    }

    public Call(Object target, String targetClass, String method, List arguments) {
        super();
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
    }

    @JsonProperty
    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    @JsonProperty
    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    @JsonProperty
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @JsonProperty
    public List getArguments() {
        return arguments;
    }

    public void setArguments(List arguments) {
        this.arguments = arguments;
    }
}
