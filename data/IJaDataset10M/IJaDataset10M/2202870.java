package net.sf.nanomvc.flow.runtime;

import net.sf.nanomvc.core.NanoRuntimeException;

public class FlowAccessor extends ObjectAccessor {

    private Class<?> runtimeClass;

    public FlowAccessor(final Class<?> runtimeClass, PropertyAccessor[] propertyAccessors) {
        super(propertyAccessors);
        this.runtimeClass = runtimeClass;
    }

    public Class<?> getRuntimeClass() {
        return runtimeClass;
    }

    public Object getObject() {
        try {
            return runtimeClass.newInstance();
        } catch (InstantiationException e) {
            throw new NanoRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new NanoRuntimeException(e);
        }
    }

    public Object getObject(Object[] properties) {
        Object object = getObject();
        init(object, properties);
        return object;
    }
}
