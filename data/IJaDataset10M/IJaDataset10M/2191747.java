package net.infopeers.restrant.commons.populate.clone;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import net.infopeers.restrant.commons.populate.Populator;

class MethodInvoker implements Invoker {

    private Object fromObject;

    private Object toObject;

    private PropertyDescriptor fdesc;

    private PropertyDescriptor tdesc;

    public Object getFromObject() {
        return fromObject;
    }

    MethodInvoker(Object from, PropertyDescriptor fdesc, Object to, PropertyDescriptor tdesc) {
        this.fromObject = from;
        this.toObject = to;
        this.fdesc = fdesc;
        this.tdesc = tdesc;
    }

    public Object invokeGetter() {
        try {
            return fdesc.getReadMethod().invoke(fromObject, Populator.INVALID_OBJECT);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void invokeSetter(Object arg) {
        try {
            tdesc.getWriteMethod().invoke(toObject, new Object[] { arg });
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Class getGetterClass() {
        return fdesc.getPropertyType();
    }

    public Class getSetterClass() {
        return tdesc.getPropertyType();
    }

    public Object getToObject() {
        return toObject;
    }

    public Object getTargetObject() {
        Object obj;
        try {
            obj = tdesc.getReadMethod().invoke(toObject, Populator.INVALID_OBJECT);
            return obj;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
