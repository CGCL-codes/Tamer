package de.juwimm.util.spring.httpinvoker;

import java.io.Serializable;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;
import org.springframework.remoting.support.RemoteInvocation;

/**
 * Provides the ability for subclasses to decorate a source
 * <code>RemoteInvocation</code>.  By default, all methods simply invoke
 * their counterpart on the delegate <code>RemoteInvocation</code>.
 * 
 * @author Andy DePue
 * @since 1.2.3
 * @see RemoteInvocation
 */
public abstract class RemoteInvocationDecorator extends RemoteInvocation {

    private RemoteInvocation delegate;

    public RemoteInvocationDecorator() {
        super();
    }

    public RemoteInvocationDecorator(final RemoteInvocation source) {
        super();
        this.delegate = source;
    }

    public void setMethodName(String methodName) {
        getDelegate().setMethodName(methodName);
    }

    public String getMethodName() {
        return getDelegate().getMethodName();
    }

    public void setParameterTypes(Class[] parameterTypes) {
        getDelegate().setParameterTypes(parameterTypes);
    }

    public Class[] getParameterTypes() {
        return getDelegate().getParameterTypes();
    }

    public void setArguments(Object[] arguments) {
        getDelegate().setArguments(arguments);
    }

    public Object[] getArguments() {
        return getDelegate().getArguments();
    }

    public void addAttribute(String key, Serializable value) {
        getDelegate().addAttribute(key, value);
    }

    public Serializable getAttribute(String key) {
        return getDelegate().getAttribute(key);
    }

    public void setAttributes(Map attributes) {
        getDelegate().setAttributes(attributes);
    }

    public Map getAttributes() {
        return getDelegate().getAttributes();
    }

    public Object invoke(Object targetObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getDelegate().invoke(targetObject);
    }

    public RemoteInvocation getDelegate() {
        return this.delegate;
    }

    public void setDelegate(final RemoteInvocation delegate) {
        this.delegate = delegate;
    }

    public String toString() {
        return "RemoteInvocationDecorator{" + "delegate=" + this.delegate + "}";
    }
}
