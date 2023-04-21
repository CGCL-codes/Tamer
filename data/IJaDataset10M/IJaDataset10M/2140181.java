package net.wotonomy.foundation.internal;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
* A utility class to convert objects to a desired class.
*
* @author michael@mpowers.net
* @author $Author: cgruber $
* @version $Revision: 893 $
*/
public class ValueConverter {

    /**
    * Returns the specified object as converted to an instance of the 
    * specified class, or null if the conversion could not be performed.
    */
    public static Object convertObjectToClass(Object anObject, Class aClass) {
        if (aClass == String.class) {
            return getString(anObject);
        }
        if (aClass == Short.class) {
            return getShort(anObject);
        }
        if (aClass == short.class) {
            return getShort(anObject);
        }
        if (aClass == Integer.class) {
            return getInteger(anObject);
        }
        if (aClass == int.class) {
            return getInteger(anObject);
        }
        if (aClass == Long.class) {
            return getLong(anObject);
        }
        if (aClass == long.class) {
            return getLong(anObject);
        }
        if (aClass == Float.class) {
            return getFloat(anObject);
        }
        if (aClass == float.class) {
            return getFloat(anObject);
        }
        if (aClass == Double.class) {
            return getDouble(anObject);
        }
        if (aClass == double.class) {
            return getDouble(anObject);
        }
        if (aClass == java.util.Date.class) {
            return getDate(anObject);
        }
        if (aClass == Boolean.class) {
            return getBoolean(anObject);
        }
        if (aClass == boolean.class) {
            return getBoolean(anObject);
        }
        if (aClass == Character.class) {
            return getCharacter(anObject);
        }
        if (aClass == char.class) {
            return getCharacter(anObject);
        }
        if (aClass == Byte.class) {
            return getByte(anObject);
        }
        if (aClass == byte.class) {
            return getByte(anObject);
        }
        if (Collection.class.isAssignableFrom(aClass)) {
            return getCollection(anObject, aClass);
        }
        if (aClass.isArray()) {
            return getArray(anObject, aClass);
        }
        return convert(anObject, aClass);
    }

    /**
    * Called by convertObjectToClass() when we need to 
    * convert to an unrecognized type.
    * This implementation scans the constructors of the 
    * specified class for the best fit to the object.
    * and returns a new instance with that constructor.
    * Subclasses can override to directly support specific
    * types.
    */
    protected static Object convert(Object anObject, Class aClass) {
        Constructor[] ctors = aClass.getConstructors();
        Class[] types;
        for (int i = 0; i < ctors.length; i++) {
            types = ctors[i].getParameterTypes();
            if (types.length == 1) {
                if (types[0].equals(anObject.getClass())) {
                    try {
                        return ctors[i].newInstance(new Object[] { anObject });
                    } catch (Exception exc) {
                    }
                }
            }
        }
        for (int i = 0; i < ctors.length; i++) {
            types = ctors[i].getParameterTypes();
            if (types.length == 1) {
                if (anObject.getClass().isAssignableFrom(types[0])) {
                    try {
                        return ctors[i].newInstance(new Object[] { anObject });
                    } catch (Exception exc) {
                    }
                }
            }
        }
        return null;
    }

    /**
    * Tries to convert all objects to either Numbers or objects
    * that will produce a parsable toString result.
    */
    protected static Object preprocess(Object anObject) {
        if (anObject instanceof Boolean) {
            if (((Boolean) anObject).booleanValue()) {
                return new Double(1.0);
            }
            return new Double(0.0);
        }
        if (anObject instanceof Character) {
            return anObject.toString();
        }
        return anObject;
    }

    public static short getShortValue(Object anObject) {
        Short result = getShort(anObject);
        return (result == null) ? (short) 0 : result.shortValue();
    }

    public static Short getShort(Object anObject) {
        if (anObject == null) return new Short((short) 0);
        if ("".equals(anObject)) return new Short((short) 0);
        if (anObject instanceof Short) return (Short) anObject;
        anObject = preprocess(anObject);
        if (anObject instanceof Number) {
            return new Short(((Number) anObject).shortValue());
        }
        try {
            return Short.valueOf(anObject.toString());
        } catch (Exception exc) {
            return null;
        }
    }

    public static int getIntValue(Object anObject) {
        Integer result = getInteger(anObject);
        return (result == null) ? 0 : result.intValue();
    }

    public static Integer getInteger(Object anObject) {
        if (anObject == null) return new Integer(0);
        if ("".equals(anObject)) return new Integer(0);
        if (anObject instanceof Integer) return (Integer) anObject;
        anObject = preprocess(anObject);
        if (anObject instanceof Number) {
            return new Integer(((Number) anObject).intValue());
        }
        try {
            return Integer.valueOf(anObject.toString());
        } catch (Exception exc) {
            return null;
        }
    }

    public static long getLongValue(Object anObject) {
        Long result = getLong(anObject);
        return (result == null) ? (long) 0 : result.longValue();
    }

    public static Long getLong(Object anObject) {
        if (anObject == null) return new Long(0);
        if ("".equals(anObject)) return new Long(0);
        if (anObject instanceof Long) return (Long) anObject;
        anObject = preprocess(anObject);
        if (anObject instanceof Number) {
            return new Long(((Number) anObject).longValue());
        }
        try {
            return Long.valueOf(anObject.toString());
        } catch (Exception exc) {
            return null;
        }
    }

    public static double getDoubleValue(Object anObject) {
        Double result = getDouble(anObject);
        return (result == null) ? 0.0f : result.doubleValue();
    }

    public static Double getDouble(Object anObject) {
        if (anObject == null) return new Double(0.0);
        if ("".equals(anObject)) return new Double(0);
        if (anObject instanceof Double) return (Double) anObject;
        anObject = preprocess(anObject);
        if (anObject instanceof Number) {
            return new Double(((Number) anObject).doubleValue());
        }
        try {
            return Double.valueOf(anObject.toString());
        } catch (Exception exc) {
            return null;
        }
    }

    public static float getFloatValue(Object anObject) {
        Float result = getFloat(anObject);
        return (result == null) ? 0.0f : result.floatValue();
    }

    public static Float getFloat(Object anObject) {
        if (anObject == null) return new Float(0.0);
        if ("".equals(anObject)) return new Float(0.0);
        if (anObject instanceof Float) return (Float) anObject;
        anObject = preprocess(anObject);
        if (anObject instanceof Number) {
            return new Float(((Number) anObject).floatValue());
        }
        try {
            return Float.valueOf(anObject.toString());
        } catch (Exception exc) {
            return null;
        }
    }

    public static char getCharValue(Object anObject) {
        Character result = getCharacter(anObject);
        return (result == null) ? (char) 0 : result.charValue();
    }

    public static Character getCharacter(Object anObject) {
        if (anObject == null) return new Character((char) 0);
        if (anObject instanceof Character) return (Character) anObject;
        anObject = preprocess(anObject);
        if (anObject instanceof Number) {
            return new Character((char) ((Number) anObject).byteValue());
        }
        try {
            return new Character(anObject.toString().charAt(0));
        } catch (Exception exc) {
            return null;
        }
    }

    public static byte getByteValue(Object anObject) {
        Byte result = getByte(anObject);
        return (result == null) ? (byte) 0 : result.byteValue();
    }

    public static Byte getByte(Object anObject) {
        if (anObject == null) return new Byte(Byte.MIN_VALUE);
        if ("".equals(anObject)) return new Byte(Byte.MIN_VALUE);
        if (anObject instanceof Byte) return (Byte) anObject;
        anObject = preprocess(anObject);
        if (anObject instanceof Number) {
            return new Byte(((Number) anObject).byteValue());
        }
        try {
            return Byte.decode(anObject.toString());
        } catch (Exception exc) {
        }
        try {
            return Byte.valueOf(anObject.toString());
        } catch (Exception exc) {
            return null;
        }
    }

    /**
	* Calls getBoolean and converts result to primitive.
	*/
    public static boolean getBooleanValue(Object anObject) {
        Boolean result = getBoolean(anObject);
        return (result == null) ? false : result.booleanValue();
    }

    /**
	* Numbers equal to zero are true; Strings equal to "yes" are true;
	* Strings are then passed to the Boolean constructor.
	* Other values return null.
	*/
    public static Boolean getBoolean(Object anObject) {
        if (anObject instanceof Boolean) {
            return (Boolean) anObject;
        }
        if (anObject instanceof Number) {
            return new Boolean(((Number) anObject).doubleValue() == 0.0);
        }
        if (anObject instanceof String) {
            if (anObject.toString().toLowerCase().equals("yes")) {
                return Boolean.TRUE;
            }
            return new Boolean((String) anObject);
        }
        return null;
    }

    /**
    * Get an appropriate String representation for the 
    * object.  Nulls are converted to "null".  Date are
    * formatted according to the current date format.
    * All else uses toString.
    */
    public static String getString(Object anObject) {
        if (anObject == null) return "null";
        if (anObject instanceof java.util.Date) {
            return dateFormat.format((java.util.Date) anObject);
        }
        return anObject.toString();
    }

    /**
    * Converts the object into the specified collection class.
    * If unable to convert in any other way, resorts to creating
    * a new collection of the specified type containing the
    * specified object.
    */
    public static Collection getCollection(Object anObject, Class aCollectionClass) {
        if (anObject == null) return null;
        if (aCollectionClass.isAssignableFrom(anObject.getClass())) {
            return (Collection) anObject;
        }
        Collection converted = null;
        if (anObject instanceof Collection) {
            converted = (Collection) anObject;
        } else if (anObject.getClass().isArray()) {
            try {
                int length = Array.getLength(anObject);
                converted = new LinkedList();
                for (int i = 0; i < length; i++) {
                    converted.add(Array.get(anObject, i));
                }
            } catch (Exception exc) {
            }
        } else if (anObject instanceof Map) {
            converted = ((Map) anObject).values();
        }
        if (converted == null) {
            converted = new LinkedList();
            converted.add(anObject);
        }
        Collection result = null;
        if (converted != null) {
            try {
                Constructor ctor = aCollectionClass.getConstructor(new Class[] { Collection.class });
                result = (Collection) ctor.newInstance(new Object[] { converted });
            } catch (Exception exc) {
                try {
                    result = new LinkedList();
                    result.addAll(converted);
                } catch (Exception exc2) {
                    result = null;
                }
            }
        }
        return result;
    }

    /**
    * Convert the object to the specified array type.
    */
    public static Object getArray(Object anObject, Class anArrayClass) {
        if (anObject == null) return null;
        if (anObject.getClass().isArray()) {
            try {
                int length = Array.getLength(anObject);
                Object result = Array.newInstance(anArrayClass.getComponentType(), length);
                for (int i = 0; i < length; i++) {
                    Array.set(result, i, Array.get(anObject, i));
                }
                return result;
            } catch (Exception exc) {
            }
        }
        if (anObject instanceof Map) {
            anObject = ((Map) anObject).values();
        }
        if (anObject instanceof Collection) {
            try {
                int length = ((Collection) anObject).size();
                Object result = Array.newInstance(anArrayClass.getComponentType(), length);
                Iterator it = ((Collection) anObject).iterator();
                for (int i = 0; i < length; i++) {
                    Array.set(result, i, it.next());
                }
                return result;
            } catch (Exception exc) {
            }
        }
        if (anObject.getClass().equals(anArrayClass.getComponentType())) {
            try {
                Object result = Array.newInstance(anArrayClass.getComponentType(), 1);
                Array.set(result, 0, anObject);
                return result;
            } catch (Exception exc) {
            }
        }
        return null;
    }

    /**
    * Get an appropriate Date from the given object.
    */
    public static java.util.Date getDate(Object anObject) {
        if (anObject == null) return new java.util.Date(0);
        if (anObject instanceof java.util.Date) return (java.util.Date) anObject;
        if (anObject instanceof Number) {
            return new java.util.Date(getLongValue(anObject));
        }
        try {
            return dateFormat.parse(anObject.toString());
        } catch (Exception exc) {
            return null;
        }
    }

    private static java.text.DateFormat dateFormat = new java.text.SimpleDateFormat();

    public static java.text.DateFormat getDateFormat() {
        return dateFormat;
    }

    public static void setDateFormat(java.text.DateFormat aDateFormat) {
        if (aDateFormat != null) {
            dateFormat = aDateFormat;
        }
    }

    /**
    * Returns the "inverted" value of the specified object.
    * Numbers except for chars and bytes are converted to 
    * their negative representation.  Chars and bytes are
    * treated as booleans.  String are converted to booleans.
    * Booleans are converted to their opposite.
    * All other types return null.
    */
    public static Object invert(Object anObject) {
        if (anObject == null) return null;
        Class aClass = anObject.getClass();
        if (((anObject instanceof Number) && !(anObject instanceof Byte) && !(anObject instanceof Character)) || (aClass == short.class) || (aClass == int.class) || (aClass == long.class) || (aClass == float.class) || (aClass == double.class)) {
            return convertObjectToClass(new Double(getDoubleValue(anObject) * -1), aClass);
        }
        Boolean converted = getBoolean(anObject);
        if (converted != null) {
            if (converted.booleanValue()) {
                return convertObjectToClass(Boolean.FALSE, anObject.getClass());
            } else {
                return convertObjectToClass(Boolean.TRUE, anObject.getClass());
            }
        }
        return null;
    }
}
