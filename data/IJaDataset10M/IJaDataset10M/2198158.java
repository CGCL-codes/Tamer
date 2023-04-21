package org.jfeature.fpi.impl.rte;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jfeature.fpi.FPBound;
import org.jfeature.fpi.FPEnvUtils;
import org.jfeature.fpi.FPException;
import org.jfeature.fpi.impl.SimpleFPBound;

final class RTEnvUtils implements FPEnvUtils<Class<?>, Method, Class<?>, Annotation> {

    public RTEnvUtils() {
    }

    public boolean isAssignableTypeMirror(Class<?> from, Class<?> to) {
        return to.isAssignableFrom(from);
    }

    public <Dummy> Dummy throwRuntimeException(String msg) {
        throw new RTRuntimeException(msg);
    }

    public <Dummy> Dummy throwRuntimeException(String msg, Throwable cause) {
        throw new RTRuntimeException(msg, cause);
    }

    public <Dummy> Dummy throwRuntimeException(Throwable cause) {
        throw new RTRuntimeException(cause);
    }

    public Class<?> toEnvClass(Class<?> cl) {
        return cl;
    }

    public Class<?>[] getSuperInterfaces(Class<?> of) {
        return of.getInterfaces();
    }

    public FPException<Method> toJFeatureException(Throwable t) {
        if (t instanceof FPException) return (FPException<Method>) t;
        return new RTRuntimeException(t);
    }

    public Class<?> toInterfaceDeclaration(Class<?> cl) {
        return cl;
    }

    public Class<?> getObjectTypeMirror() {
        return Object.class;
    }

    public Class<?> classToTypeMirror(Class<?> cl) {
        return cl;
    }

    public Class<?> classToInterfaceDeclaration(Class<?> cl) {
        return cl;
    }

    public Class<?> classToTypeDeclaration(Class<?> cl) {
        return cl;
    }

    public Class<?> typeMirrorToInterfaceDeclaration(Class<?> cl) {
        return cl;
    }

    public Class<?> typeMirrorToTypeDeclaration(Class<?> cl) {
        return cl;
    }

    public Class<?> typeDeclarationToTypeMirror(Class<?> cl) {
        return cl;
    }

    public Method getMethod(Class<?> of, String name, List<Class<?>> params) {
        try {
            return of.getMethod(name, params.toArray(new Class[params.size()]));
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public Class<?> getVoidTypeMirror() {
        return Void.TYPE;
    }

    public Collection<Method> getMethods(Class<?> of) {
        return Arrays.asList(of.getMethods());
    }

    @SuppressWarnings("unchecked")
    public Method getMethod(Class<?> of, String name) {
        return getMethod(of, name, Collections.EMPTY_LIST);
    }

    public Annotation annotationAnnotationValue(Annotation anno, String name) {
        try {
            return (Annotation) anno.annotationType().getMethod(name).invoke(anno);
        } catch (InvocationTargetException e) {
            throw new RTRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RTRuntimeException(e);
        }
    }

    public <T> List<T> arrayAnnotationValue(Annotation anno, String name) {
        try {
            return Arrays.asList((T[]) anno.annotationType().getMethod(name).invoke(anno));
        } catch (InvocationTargetException e) {
            throw new RTRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RTRuntimeException(e);
        }
    }

    public boolean booleanAnnotationValue(Annotation anno, String name) {
        try {
            return ((Boolean) anno.annotationType().getMethod(name).invoke(anno)).booleanValue();
        } catch (InvocationTargetException e) {
            throw new RTRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RTRuntimeException(e);
        }
    }

    public byte byteAnnotationValue(Annotation anno, String name) {
        try {
            return ((Byte) anno.annotationType().getMethod(name).invoke(anno)).byteValue();
        } catch (InvocationTargetException e) {
            throw new RTRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RTRuntimeException(e);
        }
    }

    public char charAnnotationValue(Annotation anno, String name) {
        try {
            return ((Character) anno.annotationType().getMethod(name).invoke(anno)).charValue();
        } catch (InvocationTargetException e) {
            throw new RTRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RTRuntimeException(e);
        }
    }

    public double doubleAnnotationValue(Annotation anno, String name) {
        try {
            return ((Double) anno.annotationType().getMethod(name).invoke(anno)).doubleValue();
        } catch (InvocationTargetException e) {
            throw new RTRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RTRuntimeException(e);
        }
    }

    public <T extends Enum<T>> T enumAnnotationValue(Annotation anno, String name) {
        try {
            return (T) anno.annotationType().getMethod(name).invoke(anno);
        } catch (InvocationTargetException e) {
            throw new RTRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RTRuntimeException(e);
        }
    }

    public float floatAnnotationValue(Annotation anno, String name) {
        try {
            return ((Float) anno.annotationType().getMethod(name).invoke(anno)).floatValue();
        } catch (InvocationTargetException e) {
            throw new RTRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RTRuntimeException(e);
        }
    }

    public int intAnnotationValue(Annotation anno, String name) {
        try {
            return ((Integer) anno.annotationType().getMethod(name).invoke(anno)).intValue();
        } catch (InvocationTargetException e) {
            throw new RTRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RTRuntimeException(e);
        }
    }

    public long longAnnotationValue(Annotation anno, String name) {
        try {
            return ((Long) anno.annotationType().getMethod(name).invoke(anno)).longValue();
        } catch (InvocationTargetException e) {
            throw new RTRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RTRuntimeException(e);
        }
    }

    public short shortAnnotationValue(Annotation anno, String name) {
        try {
            return ((Short) anno.annotationType().getMethod(name).invoke(anno)).shortValue();
        } catch (InvocationTargetException e) {
            throw new RTRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RTRuntimeException(e);
        }
    }

    public String stringAnnotationValue(Annotation anno, String name) {
        try {
            return (String) anno.annotationType().getMethod(name).invoke(anno);
        } catch (InvocationTargetException e) {
            throw new RTRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RTRuntimeException(e);
        }
    }

    public Class<?> typeAnnotationValue(Annotation anno, String name) {
        try {
            return (Class) anno.annotationType().getMethod(name).invoke(anno);
        } catch (InvocationTargetException e) {
            throw new RTRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RTRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RTRuntimeException(e);
        }
    }

    public <T extends Annotation> T getAnnotation(Class<?> type, Class<T> annoClass) {
        return type.getAnnotation(annoClass);
    }

    public Class<?> annotationMirrorToTypeDeclaration(Annotation anno) {
        return anno.annotationType();
    }

    public String getTypeDeclarationQualifiedName(Class<?> type) {
        return type.getCanonicalName();
    }

    public List<? extends Annotation> getMethodAnnotations(Method method) {
        return Arrays.asList(method.getAnnotations());
    }

    public List<? extends Annotation> getTypeAnnotations(Class<?> type) {
        return Arrays.asList(type.getAnnotations());
    }

    public FPBound<Class<?>> typeMirrorToBound(Class<?> type) {
        return SimpleFPBound.valueOf(type);
    }
}
