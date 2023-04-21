package net.sf.myra.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Utility class to create class instances.
 * 
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 2315 $ $Date: 2008-02-08 15:54:20#$
 */
public abstract class ObjectFactory {

    /**
	 * Creates a new object instance of the specified class.
	 * 
	 * @param className the fully qualified class name of the object to be 
	 *        created.
	 * 
	 * @throws IllegalArgumentException if there is any error instantiating an 
	 *         object of the specified class.
	 */
    public static Object create(String className) {
        try {
            Class<?> c = Class.forName(className);
            return c.newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class '" + className + "' could not be found", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(className + " could not be instantiated", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(className + " could not be instantiated", e);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Class name not specified: null", e);
        }
    }

    /**
	 * Creates a new object instance of the specified class.
	 * 
	 * @param className the fully qualified class name of the object to be 
	 *        created.
	 * 
	 * @throws IllegalArgumentException if there is any error instantiating an 
	 *         object of the specified class.
	 */
    public static Object create(String className, Class<?>[] types, Object[] parameters) {
        try {
            Class<?> c = Class.forName(className);
            Constructor<?> constructor = c.getConstructor(types);
            return constructor.newInstance(parameters);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class '" + className + "' could not be found", e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Constructor for '" + className + "' could not be found", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(className + "could not be instantiated", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(className + "could not be instantiated", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(className + "could not be instantiated", e);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Class name not specified: null", e);
        }
    }
}
