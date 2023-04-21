package adt.spatial.test;

import java.lang.reflect.Field;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 *         Some convenience methods for retrieving non-public fields
 */
public class Reflect {

    /**
	 * Get an arbitrary typed field of an instance.
	 * 
	 * @param o
	 *            the instance to get the field from.
	 * @param name
	 *            the name of the field.
	 * @return the value of that object's field.
	 * @throws SecurityException
	 *             on failure
	 * @throws NoSuchFieldException
	 *             on failure
	 * @throws IllegalArgumentException
	 *             on failure
	 * @throws IllegalAccessException
	 *             on failure
	 */
    public static Object getObject(Object o, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = o.getClass().getDeclaredField(name);
        field.setAccessible(true);
        Object value = field.get(o);
        return value;
    }

    /**
	 * Get a float field of an instance.
	 * 
	 * @param o
	 *            the instance to get the field from.
	 * @param name
	 *            the name of the field.
	 * @return the value of that object's field.
	 * @throws SecurityException
	 *             on failure
	 * @throws NoSuchFieldException
	 *             on failure
	 * @throws IllegalArgumentException
	 *             on failure
	 * @throws IllegalAccessException
	 *             on failure
	 */
    public static float getFloat(Object o, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = o.getClass().getDeclaredField(name);
        field.setAccessible(true);
        Float value = (Float) field.get(o);
        return value;
    }

    /**
	 * Get an integer field of an instance.
	 * 
	 * @param o
	 *            the instance to get the field from.
	 * @param name
	 *            the name of the field.
	 * @return the value of that object's field.
	 * @throws SecurityException
	 *             on failure
	 * @throws NoSuchFieldException
	 *             on failure
	 * @throws IllegalArgumentException
	 *             on failure
	 * @throws IllegalAccessException
	 *             on failure
	 */
    public static int getInt(Object o, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = o.getClass().getDeclaredField(name);
        field.setAccessible(true);
        Integer value = (Integer) field.get(o);
        return value;
    }
}
