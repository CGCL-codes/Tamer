package de.keepondreaming.xml.util;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Various helper methods 
 * 
 * $Author: wintermond $
 * $Date: 2005/07/10 18:32:11 $
 * $Log: Util.java,v $
 * Revision 1.4  2005/07/10 18:32:11  wintermond
 * Revamp to improve performance, most operations could benefit from the use of HashMaps
 *
 * Revision 1.3  2005/07/09 14:11:16  wintermond
 * Changed method signatur of createObject - switched parameter ordering
 *
 * Revision 1.2  2005/07/09 10:02:19  wintermond
 * Received some generic methods from the other classes, concerning reflection based topics
 *
 */
public class Util {

    private static final Class[] LONG_TYPE_PARAMETER = new Class[] { Long.TYPE };

    private static final Class[] STRING_PARAMETER = new Class[] { String.class };

    private static Map<Class, Object> primitiveDefaultValueCacheM = new HashMap<Class, Object>(17);

    private static Map<Class, Class> primitiveObjectCacheM = new HashMap<Class, Class>(17);

    private static Map<Class, Constructor> constructorCacheM = new HashMap<Class, Constructor>(47);

    private static Map<Class, Class> object2PrimitiveClassCacheM = new HashMap<Class, Class>(47);

    static {
        primitiveDefaultValueCacheM.put(Integer.TYPE, new Integer(0));
        primitiveDefaultValueCacheM.put(Byte.TYPE, new Byte((byte) 0));
        primitiveDefaultValueCacheM.put(Short.TYPE, new Short((short) 0));
        primitiveDefaultValueCacheM.put(Float.TYPE, new Float(0));
        primitiveDefaultValueCacheM.put(Double.TYPE, new Double(0));
        primitiveDefaultValueCacheM.put(Boolean.TYPE, Boolean.TRUE);
        primitiveDefaultValueCacheM.put(Character.TYPE, new Character(' '));
        primitiveObjectCacheM.put(Integer.TYPE, Integer.class);
        primitiveObjectCacheM.put(Byte.TYPE, Byte.class);
        primitiveObjectCacheM.put(Short.TYPE, Short.class);
        primitiveObjectCacheM.put(Float.TYPE, Float.class);
        primitiveObjectCacheM.put(Double.TYPE, Double.class);
        primitiveObjectCacheM.put(Boolean.TYPE, Boolean.class);
        primitiveObjectCacheM.put(Character.TYPE, Character.class);
        object2PrimitiveClassCacheM.put(Integer.class, Integer.TYPE);
        object2PrimitiveClassCacheM.put(Short.class, Short.TYPE);
        object2PrimitiveClassCacheM.put(Byte.class, Byte.TYPE);
        object2PrimitiveClassCacheM.put(Float.class, Float.TYPE);
        object2PrimitiveClassCacheM.put(Double.class, Double.TYPE);
        object2PrimitiveClassCacheM.put(Boolean.class, Boolean.TYPE);
        object2PrimitiveClassCacheM.put(Character.class, Character.TYPE);
    }

    /**
	 * Uppercases the first letter of a string if <code>in</code> is not null
	 * 
	 * @param in
	 * 
	 * @return The input string with the first letter as uppercase
	 */
    public static String capitalize(String in) {
        String result = null;
        if (in != null) {
            result = in.substring(0, 1).toUpperCase() + in.substring(1);
        }
        return result;
    }

    /**
     * Creates for a primitive class an object of the corresponding object type.
     * 
     * @param primitiveClass 
     * 
     * @return A newly created Object of the requested type, initialized with default values
     */
    public static Object createPrimitiveDefaultValue(Class primitiveClass) {
        Object result = primitiveDefaultValueCacheM.get(primitiveClass);
        return result;
    }

    /**
     * Returns a new Object of the concurrent primitive type passed by
     * <code>primitive</code> and initializes it with <code>value</code>
     * 
     * @param primitive
     * @param value
     * 
     * @return A new Object of the concurrent primitive type, null if an error
     *         occurs
     */
    public static Object getPrimitiveObject(Class primitive, String value) {
        Object result = null;
        Class transformer = null;
        transformer = primitiveObjectCacheM.get(primitive);
        result = createObject(transformer, value);
        return result;
    }

    /**
     * Creates a new instances of an object, if the object has a constructor with a single
     * {@link String} parameter
     * @param transformer
     * @param value
     * 
     * @return A new instance of the specified class or null if an error occurs
     */
    public static Object createObject(Class transformer, String value) {
        Object result = null;
        try {
            Constructor constructor = constructorCacheM.get(transformer);
            if (!Date.class.isAssignableFrom(transformer)) {
                if (constructor == null) {
                    constructor = transformer.getConstructor(STRING_PARAMETER);
                    constructorCacheM.put(transformer, constructor);
                }
                result = constructor.newInstance(new Object[] { value });
            } else {
                if (constructor == null) {
                    constructor = transformer.getConstructor(LONG_TYPE_PARAMETER);
                    constructorCacheM.put(transformer, constructor);
                }
                Long date = new Long(value);
                result = constructor.newInstance(new Object[] { date });
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Retrieves for an object assignable to a primitive value
     * the assigned primitive class type.
     * 
     * @param primitive
     * 
     * @return Primitive class type of the passed <code>primitive</code> 
     * object. Null if there is no primitive class for this type available
     */
    public static Class computePrimitiveClass(Object primitive) {
        Class result = object2PrimitiveClassCacheM.get(primitive.getClass());
        return result;
    }
}
