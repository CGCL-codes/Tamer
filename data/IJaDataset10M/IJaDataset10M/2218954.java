package org.xfeep.asura.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import org.xfeep.asura.core.reflect.TypeItem;

public class DependOnVirtualFieldItem implements TypeItem {

    Annotation annotation;

    Class<?> declaringClass;

    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return (A) (annotation.getClass().isAssignableFrom(annotationType) ? annotation : null);
    }

    public Annotation[] getAnnotations() {
        return new Annotation[] { annotation };
    }

    public Class getDeclaringClass() {
        return declaringClass;
    }

    public Class[] getMemberTypes() {
        return null;
    }

    public String getName() {
        return null;
    }

    public Class getType() {
        return null;
    }

    public Object getValue(Object target) throws InvocationTargetException, IllegalAccessException {
        return null;
    }

    public boolean isReadOnly() {
        return false;
    }

    public boolean isWriteOnly() {
        return false;
    }

    public void setValue(Object target, Object v) throws InvocationTargetException, IllegalAccessException {
    }
}
