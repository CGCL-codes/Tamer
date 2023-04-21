package de.akquinet.jbosscc.needle.inject;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for access (read/write) to methods and fields having certain
 * annotations via reflection.
 */
public class Injector {

    public List<Field> getAllEjbInjectedFields(final Object instance) {
        return getAllFieldsWithAnnotation(instance, EJB.class);
    }

    public List<Field> getAllResourceInjectedFields(final Object instance) {
        return getAllFieldsWithAnnotation(instance, Resource.class);
    }

    protected List<Field> getAllFieldsWithAnnotation(final Object instance, final Class<? extends Annotation> annotation) {
        Field[] fields = instance.getClass().getDeclaredFields();
        List<Field> result = new ArrayList<Field>();
        for (Field field : fields) {
            if (field.getAnnotation(annotation) != null) {
                result.add(field);
            }
        }
        Class<?> superClazz = instance.getClass().getSuperclass();
        while (superClazz != null) {
            Field[] extraFields = superClazz.getDeclaredFields();
            for (Field extra : extraFields) {
                if (Modifier.isPrivate(extra.getModifiers()) && extra.getAnnotation(annotation) != null) {
                    result.add(extra);
                }
            }
            superClazz = superClazz.getSuperclass();
        }
        return result;
    }

    private static class AlwaysOkAnnotationChecker<A extends Annotation> implements AnnotationChecker<A> {

        public boolean checkAnnotation(final Field field, final A annotation) {
            return true;
        }
    }

    public interface AnnotationChecker<A extends Annotation> {

        boolean checkAnnotation(Field field, A annotation);
    }

    private static boolean checkArguments(final Class<?>[] parameterTypes, final Object[] arguments) {
        boolean match = true;
        for (int i = 0; i < arguments.length; i++) {
            final Class<?> parameterClass = parameterTypes[i];
            final Class<?> argumentClass = arguments[i].getClass();
            if (!parameterClass.isAssignableFrom(argumentClass)) {
                boolean isInt = (parameterClass == int.class) && (argumentClass == Integer.class);
                boolean isDouble = (parameterClass == double.class) && (argumentClass == Double.class);
                if (!isInt && !isDouble) {
                    match = false;
                }
            }
        }
        return match;
    }

    /**
	 * Get the value of a given fields on a given object via reflection.
	 *
	 * @param object
	 *            -- target object of field access
	 * @param clazz
	 *            -- type of argument object
	 * @param fieldName
	 *            -- name of the field
	 * @return -- the value of the represented field in object; primitive values
	 *         are wrapped in an appropriate object before being returned
	 */
    public static Object getField(final Object object, final Class<?> clazz, final String fieldName) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.get(object);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Could not get field value: " + fieldName, e);
        }
    }

    /**
	 * Get the value of a given fields on a given object via reflection.
	 *
	 * @param object
	 *            -- target object of field access
	 * @param fieldName
	 *            -- name of the field
	 * @return -- the value of the represented field in object; primitive values
	 *         are wrapped in an appropriate object before being returned
	 */
    public static Object getField(final Object object, final String fieldName) {
        return getField(object, object.getClass(), fieldName);
    }

    /**
	 * Invoke a given method with given arguments on a given object via
	 * reflection.
	 *
	 * @param object
	 *            -- target object of invocation
	 * @param clazz
	 *            -- type of argument object
	 * @param methodName
	 *            -- name of method to be invoked
	 * @param arguments
	 *            -- arguments for method invocation
	 * @return -- method object to which invocation is actually dispatched
	 */
    public static Object invokeMethod(final Object object, final Class<?> clazz, final String methodName, final Object... arguments) {
        for (final Method declaredMethod : clazz.getDeclaredMethods()) {
            if (declaredMethod.getName().equals(methodName)) {
                final Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                if (parameterTypes.length == arguments.length) {
                    final boolean match = checkArguments(parameterTypes, arguments);
                    if (match) {
                        try {
                            if (!declaredMethod.isAccessible()) {
                                declaredMethod.setAccessible(true);
                            }
                            return declaredMethod.invoke(object, arguments);
                        } catch (final Exception exc) {
                            throw new IllegalArgumentException("Error invoking method: " + methodName, exc);
                        }
                    }
                }
            }
        }
        throw new IllegalArgumentException("Method " + methodName + ":" + Arrays.toString(arguments) + " not found");
    }

    /**
	 * Invoke a given method with given arguments on a given object via
	 * reflection.
	 *
	 * @param object
	 *            -- target object of invocation
	 * @param methodName
	 *            -- name of method to be invoked
	 * @param arguments
	 *            -- arguments for method invocation
	 * @return -- method object to which invocation is actually dispatched
	 */
    public static Object invokeMethod(final Object object, final String methodName, final Object... arguments) {
        return invokeMethod(object, object.getClass(), methodName, arguments);
    }

    /**
	 * Changing the value of a given field.
	 *
	 * @param object
	 *            -- target object of injection
	 * @param clazz
	 *            -- type of argument object
	 * @param fieldName
	 *            -- name of field whose value is to be set
	 * @param value
	 *            -- object that is injected
	 */
    public static void setField(final Object object, final Class<?> clazz, final String fieldName, final Object value) throws NoSuchFieldException {
        final Field field = clazz.getDeclaredField(fieldName);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Changing the value of a given field.
	 *
	 * @param object
	 *            -- target object of injection
	 * @param fieldName
	 *            -- name of field whose value is to be set
	 * @param value
	 *            -- object that is injected
	 */
    public static void setField(final Object object, final String fieldName, final Object value) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                setField(object, clazz, fieldName, value);
                return;
            } catch (NoSuchFieldException e) {
            }
            clazz = clazz.getSuperclass();
        }
    }

    public interface InjectionObjectCreator {

        public Object getObjectToBeInjected();
    }

    public static InjectionObjectCreator wrap(final Object object) {
        return new InjectionObjectCreator() {

            public Object getObjectToBeInjected() {
                return object;
            }
        };
    }

    private <A extends Annotation> void injectAllAnnotatedFieldsForOneClass(final Object injectionTarget, final Class<A> annotationClass, final InjectionObjectCreator injectedObject, final Class<? extends Object> classOfInjectionTargetToCheck, final AnnotationChecker<A> annotationChecker) {
        final Field[] declaredFields = classOfInjectionTargetToCheck.getDeclaredFields();
        for (final Field field : declaredFields) {
            if (field.isAnnotationPresent(annotationClass)) {
                final A annotation = field.getAnnotation(annotationClass);
                if (annotationChecker.checkAnnotation(field, annotation)) {
                    field.setAccessible(true);
                    try {
                        field.set(injectionTarget, injectedObject.getObjectToBeInjected());
                    } catch (final Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public final <A extends Annotation> void injectAllDeclaredFields(final Object injectionTarget, final Class<A> annotationClass, final Object injectedObject) {
        injectAllDeclaredFields(injectionTarget, annotationClass, wrap(injectedObject));
    }

    /**
	 * Injects an object in all fields that are marked with the passed
	 * annotation.
	 *
	 * @param injectionTarget
	 *            -- target object of injection
	 * @param annotationClass
	 *            -- annotation that marks the fields, whose values are to be
	 *            injected
	 * @param injectedObject
	 *            -- object that is injected
	 * @param <A>
	 *            -- formal class parameter for annotation
	 */
    public final <A extends Annotation> void injectAllDeclaredFields(final Object injectionTarget, final Class<A> annotationClass, final InjectionObjectCreator injectedObject) {
        injectAllDeclaredFields(injectionTarget, annotationClass, injectedObject, new AlwaysOkAnnotationChecker<A>());
    }

    public final <A extends Annotation> void injectAllDeclaredFields(final Object injectionTarget, final Class<A> annotationClass, final Object injectedObject, final AnnotationChecker<A> annotationChecker) {
        injectAllDeclaredFields(injectionTarget, annotationClass, wrap(injectedObject), annotationChecker);
    }

    /**
	 * Injects an object in all fields that are marked with the passed
	 * annotation and that are accepted by annotationChecker.
	 *
	 * @param injectionTarget
	 *            -- target object of injection
	 * @param annotationClass
	 *            -- annotation that marks the fields, whose values are to be
	 *            injected
	 * @param injectedObject
	 *            -- object that is injected
	 * @param annotationChecker
	 *            -- strategy object to be executed before values are injected
	 * @param <A>
	 *            -- formal class parameter for annotation
	 */
    public final <A extends Annotation> void injectAllDeclaredFields(final Object injectionTarget, final Class<A> annotationClass, final InjectionObjectCreator injectedObject, final AnnotationChecker<A> annotationChecker) {
        assert injectionTarget != null;
        assert annotationClass != null;
        Class<? extends Object> currentClass = injectionTarget.getClass();
        while (currentClass != Object.class) {
            injectAllAnnotatedFieldsForOneClass(injectionTarget, annotationClass, injectedObject, currentClass, annotationChecker);
            currentClass = currentClass.getSuperclass();
        }
    }

    /**
	 * Injects an EJB-mock in all fields that are annotated with \@EJB and whose
	 * type implement the given businessInterface.
	 *
	 * @param injectionTarget
	 *            -- target object of injection
	 * @param businessInterface
	 *            -- type to be implemented by the type of the target fields
	 * @param ejbMock
	 *            -- mock value for the target fiels. Must also implement
	 *            businessInterface.
	 * @param <T>
	 *            -- parameter class correlated with businessInterface
	 */
    public final <T> void injectEjb(final Object injectionTarget, final Class<?> businessInterface, final T ejbMock) {
        assert businessInterface.isAssignableFrom(ejbMock.getClass());
        Class<? extends Object> currentClass = injectionTarget.getClass();
        while (currentClass != Object.class) {
            injectEJB(injectionTarget, businessInterface, ejbMock, currentClass);
            currentClass = currentClass.getSuperclass();
        }
    }

    private <T> void injectEJB(final Object injectionTarget, final Class<?> businessInterface, final T ejbMock, final Class<? extends Object> currentClass) {
        final Field[] declaredFields = currentClass.getDeclaredFields();
        for (final Field field : declaredFields) {
            final boolean withEjbAnnotation = field.isAnnotationPresent(EJB.class);
            final Class<?> fieldType = field.getType();
            final boolean implementsBusinessInterface = businessInterface.isAssignableFrom(fieldType);
            if (withEjbAnnotation && implementsBusinessInterface) {
                field.setAccessible(true);
                try {
                    field.set(injectionTarget, ejbMock);
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
	 * Injects an object to exactly one field that is marked with the given
	 * annotation.
	 *
	 * @param injectionTarget
	 *            -- target object of injection
	 * @param annotationClass
	 *            -- annotation that marks the fields, whose values are to be
	 *            injected
	 * @param injectedObject
	 *            -- object that is injected
	 */
    public final void injectOneDeclaredField(final Object injectionTarget, final Class<? extends Annotation> annotationClass, final Object injectedObject) {
        assert injectionTarget != null;
        assert annotationClass != null;
        assert injectedObject != null;
        int injectedCount = 0;
        final Field[] declaredFields = injectionTarget.getClass().getDeclaredFields();
        for (final Field field : declaredFields) {
            if (field.isAnnotationPresent(annotationClass)) {
                final Class<?> targetType = field.getType();
                final Class<?> sourceType = injectedObject.getClass();
                if (targetType.isAssignableFrom(sourceType)) {
                    injectedCount++;
                    field.setAccessible(true);
                    try {
                        field.set(injectionTarget, injectedObject);
                    } catch (final Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        if (injectedCount != 1) {
            throw new IllegalArgumentException("Not injected or injected more than once.");
        }
    }

    /**
	 * Injects all declared fields which are annotated with \@PersistenceContext
	 * .
	 *
	 * @param injectionTarget
	 *            -- target object of injection
	 * @param entityManager
	 *            -- object that is injected
	 */
    public final void injectPersistenceContext(final Object injectionTarget, final EntityManager entityManager) {
        injectAllDeclaredFields(injectionTarget, PersistenceContext.class, entityManager);
    }

    /**
	 * Injects an object to the field with a given name and with annotation
	 * \@Resource .
	 *
	 * @param injectionTarget
	 *            -- target object of injection
	 * @param jndiName
	 *            -- name of the target field
	 * @param object
	 *            -- object that is injected
	 */
    public final void injectResourceParameter(final Object injectionTarget, final String jndiName, final Object object) {
        injectAllDeclaredFields(injectionTarget, Resource.class, object, new AnnotationChecker<Resource>() {

            public boolean checkAnnotation(final Field field, final Resource annotation) {
                return annotation.mappedName().equals(jndiName);
            }
        });
    }
}
