package net.nycjava.skylight1.dependencyinjection;

import static java.lang.String.format;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

/**
 * Given a DependencyInjectingObjectFactory, injects dependencies into a class.
 */
public class DependencyInjector {

    private DependencyInjectingObjectFactory dependencyInjectingObjectFactory;

    public DependencyInjector(DependencyInjectingObjectFactory aDependencyInjectingObjectFactory) {
        dependencyInjectingObjectFactory = aDependencyInjectingObjectFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> void injectDependenciesForClassHierarchy(T anObject) {
        Class<?> interfaceOfObject = anObject.getClass();
        do {
            injectDependenciesForSingleClass(anObject, (Class<T>) interfaceOfObject);
            interfaceOfObject = interfaceOfObject.getSuperclass();
        } while (interfaceOfObject != null);
    }

    private <T, S extends T> void injectDependenciesForSingleClass(S anObject, Class<T> aClass) {
        for (Field field : aClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Dependency.class)) {
                field.setAccessible(true);
                try {
                    if (field.get(anObject) == null) {
                        Class<?> classOfDependency = field.getType();
                        final Object injectedValue;
                        final int modifiers = classOfDependency.getModifiers();
                        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
                            injectedValue = createProxy(classOfDependency);
                        } else {
                            injectedValue = dependencyInjectingObjectFactory.getObject(classOfDependency);
                        }
                        field.set(anObject, injectedValue);
                    }
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(format("Unable to access field %s.", field.getName()), e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createProxy(final Class<T> aClass) {
        return (T) Proxy.newProxyInstance(DependencyInjectingObjectFactory.class.getClassLoader(), new Class[] { aClass }, new InvocationHandler() {

            private T object;

            public Object invoke(Object aProxy, Method aMethod, Object[] anArrayOfArguments) throws Throwable {
                if (object == null) {
                    final ObjectSource<T> objectSource = dependencyInjectingObjectFactory.getObjectSource(aClass);
                    if (objectSource == null) {
                        throw new RuntimeException(format("No source was registered for the dependecy of type %s.", aClass.getName()));
                    }
                    object = (T) objectSource.getObject();
                }
                return aMethod.invoke(object, anArrayOfArguments);
            }
        });
    }
}
