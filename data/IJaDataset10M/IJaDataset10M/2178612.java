package org.curjent.agent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Delegates class creation and loading to an existing <code>ClassLoader</code>.
 * See the class description for {@link AgentLoader}.
 */
public class DelegatingLoader implements AgentLoader {

    /** Class loader whose 'defineClass' method is called. */
    private final ClassLoader classLoader;

    /** Cached 'defineClass' method of {@link #classLoader}. */
    private final Method defineClass;

    /**
	 * Equivalent to <code>DelegatingLoader(instance.getClass().getClassLoader())</code>.
	 * 
	 * @see #DelegatingLoader(ClassLoader)
	 */
    public DelegatingLoader(Object instance) {
        this(instance.getClass().getClassLoader());
    }

    /**
	 * Equivalent to <code>DelegatingLoader(type.getClassLoader())</code>.
	 * 
	 * @see #DelegatingLoader(ClassLoader)
	 */
    public DelegatingLoader(Class<?> type) {
        this(type.getClassLoader());
    }

    /**
	 * Initializes this instance with the given <code>classLoader</code> for
	 * creating and loading generated classes. Reflection is used during
	 * construction to find and cache the loader's <code>defineClass</code>
	 * method.
	 * 
	 * @throws NullPointerException <code>classLoader == null</code>
	 * 
	 * @throws IllegalArgumentException <code>defineClass(String, byte[], int, int)</code>
	 * not found in the class of the given <code>classLoader</code>
	 * 
	 * @throws SecurityException Reflection is restricted by security settings.
	 */
    public DelegatingLoader(ClassLoader classLoader) {
        if (classLoader == null) throw new NullPointerException("classLoader == null");
        this.classLoader = classLoader;
        final String methodName = "defineClass";
        final int paramLength = 4;
        final Class<String> stringType = String.class;
        final Class<byte[]> bytesType = byte[].class;
        final Class<Integer> intType = int.class;
        Class<?> classType = classLoader.getClass();
        for (; ; ) {
            for (Method declared : classType.getDeclaredMethods()) {
                if (declared.getName().equals(methodName)) {
                    Class<?>[] params = declared.getParameterTypes();
                    if (params.length == paramLength && params[0] == stringType && params[1] == bytesType && params[2] == intType && params[3] == intType) {
                        declared.setAccessible(true);
                        defineClass = declared;
                        return;
                    }
                }
            }
            classType = classType.getSuperclass();
            if (classType == null) {
                throw new IllegalArgumentException("Method 'defineClass(String, byte[], int, int)' not found in ClassLoader: " + classLoader.getClass().getName());
            }
        }
    }

    /**
	 * Returns the <code>classLoader</code> instance this instance was
	 * constructed with.
	 * 
	 * @see #DelegatingLoader(ClassLoader)
	 */
    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
	 * Uses the previously cached <code>defineClass</code> method to create a
	 * generated class.
	 * 
	 * @throws IllegalStateException Wraps as the cause an
	 * <code>IllegalAccessException</code> thrown because the
	 * <code>defineClass</code> method is not accessible.
	 * 
	 * @throws AgentException Wraps as the cause an exception thrown by the
	 * <code>defineClass</code> method.
	 * 
	 * @see #DelegatingLoader(ClassLoader)
	 */
    @Override
    public Class<?> defineClass(String name, byte[] bytes) {
        try {
            return (Class<?>) defineClass.invoke(classLoader, name, bytes, 0, bytes.length);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("ClassLoader method is inaccessible: " + defineClass, e);
        } catch (InvocationTargetException e) {
            throw new AgentException("Failed to define class: " + name, e.getCause());
        }
    }
}
