package org.spockframework.builder;

import org.codehaus.groovy.runtime.InvokerHelper;
import groovy.lang.*;

public abstract class DelegatingScript extends Script {

    private volatile Object $delegate;

    public void $setDelegate(Object delegate) {
        this.$delegate = delegate;
    }

    @Override
    public Object getProperty(String property) {
        try {
            return InvokerHelper.getProperty($delegate, property);
        } catch (MissingPropertyException e) {
            return super.getProperty(property);
        }
    }

    @Override
    public void setProperty(String property, Object newValue) {
        try {
            InvokerHelper.setProperty($delegate, property, newValue);
        } catch (MissingPropertyException e) {
            super.setProperty(property, newValue);
        }
    }

    @Override
    public Object invokeMethod(String name, Object args) {
        try {
            return InvokerHelper.invokeMethod($delegate, name, args);
        } catch (MissingMethodException e) {
            return super.invokeMethod(name, args);
        }
    }
}
