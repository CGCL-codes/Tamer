package org.nakedobjects.runtime.persistence.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.nakedobjects.commons.exceptions.NakedObjectException;

public class ServiceUtil {

    public static String id(final Object object) {
        if (object instanceof SimpleRepository) {
            return "repository#" + ((SimpleRepository) object).getSpec().getFullName();
        }
        final Class<?> cls = object.getClass();
        try {
            final Method m = cls.getMethod("getId", new Class[0]);
            return (String) m.invoke(object, new Object[0]);
        } catch (final SecurityException e) {
            throw new NakedObjectException(e);
        } catch (final NoSuchMethodException e) {
            final String id = object.getClass().getName();
            return id.substring(id.lastIndexOf('.') + 1);
        } catch (final IllegalArgumentException e) {
            throw new NakedObjectException(e);
        } catch (final IllegalAccessException e) {
            throw new NakedObjectException(e);
        } catch (final InvocationTargetException e) {
            throw new NakedObjectException(e);
        }
    }
}
