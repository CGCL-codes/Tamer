package org.googlecode.horseshoe.lang;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Utility class for working with classes
 * 
 * @author Eric Hauser
 */
@SuppressWarnings("unchecked")
public class ClassHelper {

    private static List<Class<?>> SIMPLE_CLASSES = new ArrayList();

    static {
        SIMPLE_CLASSES.addAll(Arrays.asList(Float.class, Double.class, Integer.class, Date.class, Boolean.class, Byte.class, Character.class, BigDecimal.class, String.class));
    }

    private static List<Class<?>> STREAMABLE_CLASSES = new ArrayList();

    static {
        STREAMABLE_CLASSES.add(Byte[].class);
        STREAMABLE_CLASSES.add(byte[].class);
        STREAMABLE_CLASSES.addAll(SIMPLE_CLASSES);
    }

    /**
   * Checks to see if a class is a simple type or not.  Simple types are primitive wrapper classes and primitives.  
   * This will also return true for BigDecimal.
   * 
   * @param clazz The clazz to check
   * @return True if it is a simple Class
   */
    public static boolean isSimple(Class<?> clazz) {
        for (Class<?> c : SIMPLE_CLASSES) {
            if (c.equals(clazz)) {
                return true;
            }
        }
        return clazz.isPrimitive();
    }

    /**
   * Cheks to see whether the given object is a Collection or Array.
   * 
   * @param o
   * @return
   */
    public static boolean isCollectionArrayOrMap(Object o) {
        return Collection.class.isAssignableFrom(o.getClass()) || Object[].class.isAssignableFrom(o.getClass()) || Map.class.isAssignableFrom(o.getClass());
    }

    /**
   * @param clazz
   * @return
   */
    public static boolean IsStreamable(Class<?> clazz) {
        for (Class<?> c : STREAMABLE_CLASSES) {
            if (c.equals(clazz)) {
                return true;
            }
        }
        return clazz.isPrimitive();
    }
}
