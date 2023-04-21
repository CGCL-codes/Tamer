package org.ji18n.core.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import org.ji18n.core.I18N;

/**
 * @version $Id: FieldAccess.java 159 2008-07-03 01:28:51Z david_ward2 $
 * @author david at ji18n.org
 */
public class FieldAccess implements Access {

    private Field field;

    public FieldAccess(Field field) {
        if (!field.isAccessible()) field.setAccessible(true);
        this.field = field;
    }

    public I18N getI18N() {
        return field.getAnnotation(I18N.class);
    }

    public Class<?> getDeclaringClass() {
        return field.getDeclaringClass();
    }

    public I18N getDeclaringClassI18N() {
        return getDeclaringClass().getAnnotation(I18N.class);
    }

    public Class<?> getType() {
        return field.getType();
    }

    public Type getGenericType() {
        return field.getGenericType();
    }

    public String getName() {
        return field.getName();
    }

    public void setValue(Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }
    }
}
