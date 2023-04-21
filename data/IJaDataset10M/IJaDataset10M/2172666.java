package org.nakedobjects.nof.reflect.java.facets.object.value;

import org.nakedobjects.noa.NakedObjectRuntimeException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public final class ClassUtil {

    private ClassUtil() {
    }

    public static Object newInstance(final Class clazz) {
        try {
            final Constructor constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (final SecurityException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (final IllegalArgumentException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (final InstantiationException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new NakedObjectRuntimeException(e);
        }
    }

    public static Class implementingClassOrNull(final Class classCandidate, final Class requiredClass) {
        if (classCandidate == null) {
            return null;
        }
        if (!requiredClass.isAssignableFrom(classCandidate)) {
            return null;
        }
        try {
            classCandidate.getConstructor(new Class[] {});
        } catch (final NoSuchMethodException ex) {
            return null;
        } catch (final SecurityException e) {
            throw null;
        }
        final int modifiers = classCandidate.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            return null;
        }
        return classCandidate;
    }

    public static Class implementingClassOrNull(final String classCandidateName, final Class requiredClass) {
        if (classCandidateName == null) {
            return null;
        }
        Class classCandidate = null;
        try {
            classCandidate = Class.forName(classCandidateName);
            return implementingClassOrNull(classCandidate, requiredClass);
        } catch (final ClassNotFoundException e) {
            return null;
        }
    }
}
