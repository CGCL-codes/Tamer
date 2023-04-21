package net.kortsoft.slparser;

import groovy.lang.DelegatingMetaClass;
import groovy.lang.MetaClass;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;

public class SLMetaClass extends DelegatingMetaClass {

    private SLBuilder builder;

    public SLMetaClass(MetaClass delegate, SLBuilder builder) {
        super(delegate);
        this.builder = builder;
    }

    public SLMetaClass(Class<?> theClass, SLBuilder builder) {
        super(theClass);
        this.builder = builder;
    }

    @Override
    public Object getProperty(Object object, String property) {
        try {
            return super.getProperty(object, property);
        } catch (MissingPropertyException e) {
            return builder.invokeMethod(property);
        }
    }

    @Override
    public Object invokeMethod(Object arg0, String methodName, Object[] arguments) {
        if (methodName.equals("text")) {
            return text(arguments);
        } else try {
            return super.invokeMethod(arg0, methodName, arguments);
        } catch (MissingMethodException e) {
            return invokeMissingMethod(arg0, methodName, arguments);
        }
    }

    @Override
    public Object invokeMethod(Object arg0, String methodName, Object argument) {
        if (methodName.equals("text")) {
            return text((Object[]) argument);
        }
        try {
            return super.invokeMethod(arg0, methodName, argument);
        } catch (MissingMethodException e) {
            return invokeMissingMethod(arg0, methodName, (Object[]) argument);
        }
    }

    private Object text(Object[] arguments) {
        for (Object argument : arguments) getBuilder().addText(argument);
        return arguments;
    }

    @Override
    public Object invokeMissingMethod(Object instance, String methodName, Object[] arguments) {
        Object invokeMethod = builder.invokeMethod(methodName, arguments);
        return invokeMethod;
    }

    public SLBuilder getBuilder() {
        return builder;
    }

    public void setMarkupBuilder(SLBuilder builder) {
        this.builder = builder;
    }
}
