package org.mili.core.properties;

import java.text.*;
import java.util.*;
import org.apache.commons.lang.*;

/**
 * This class defines the bottom methods to operate with properties.
 *
 * @author Michael Lieshoff
 */
class Props {

    public static final String PROP_LOGPROPS = createName(PropUtil.class, "LogProps");

    private static final String PATTERN = "{0}.{1}";

    private static final String MSG = "Property %s can't be %s! Perhaps property file is not " + "loaded or property is not setted correctly. Please set property to non %s " + "value, like the following:\n%s=<value of %s>";

    protected static Properties SYSTEM_PROPERTIES = System.getProperties();

    enum Usage {

        REQUIRED_NOT_NULL, REQUIRED_NOT_NULL_AND_NOT_EMPTY, OPTIONAL
    }

    Props() {
        super();
    }

    static byte getByte(Properties p, Object o, String k, Usage u) {
        return getByte(p, o.getClass(), k, u);
    }

    static byte getByte(Properties p, Class<?> c, String k, Usage u) {
        return getByte(p, createName(c, k), u);
    }

    static byte getByte(Properties p, String k, Usage u) {
        return get(p, k, u, Byte.class);
    }

    static short getShort(Properties p, Object o, String k, Usage u) {
        Validate.notNull(o);
        return getShort(p, o.getClass(), k, u);
    }

    static short getShort(Properties p, Class<?> c, String k, Usage u) {
        return getShort(p, createName(c, k), u);
    }

    static short getShort(Properties p, String k, Usage u) {
        return get(p, k, u, Short.class);
    }

    static int getInteger(Properties p, Object o, String k, Usage u) {
        Validate.notNull(o);
        return getInteger(p, o.getClass(), k, u);
    }

    static int getInteger(Properties p, Class<?> c, String k, Usage u) {
        return getInteger(p, createName(c, k), u);
    }

    static int getInteger(Properties p, String k, Usage u) {
        return get(p, k, u, Integer.class);
    }

    static char getChar(Properties p, Object o, String k, Usage u) {
        return getChar(p, o, k, u);
    }

    static char getChar(Properties p, Class<?> c, String k, Usage u) {
        return getChar(p, c, k, u);
    }

    static char getChar(Properties p, String k, Usage u) {
        return get(p, k, u, char.class);
    }

    static long getLong(Properties p, Object o, String k, Usage u) {
        Validate.notNull(o);
        return getLong(p, o.getClass(), k, u);
    }

    static long getLong(Properties p, Class<?> c, String k, Usage u) {
        return getLong(p, createName(c, k), u);
    }

    static long getLong(Properties p, String k, Usage u) {
        return get(p, k, u, Long.class);
    }

    static float getFloat(Properties p, Object o, String k, Usage u) {
        Validate.notNull(o);
        return getFloat(p, o.getClass(), k, u);
    }

    static float getFloat(Properties p, Class<?> c, String k, Usage u) {
        return getFloat(p, createName(c, k), u);
    }

    static float getFloat(Properties p, String k, Usage u) {
        return get(p, k, u, Float.class);
    }

    static double getDouble(Properties p, Object o, String k, Usage u) {
        Validate.notNull(o);
        return getDouble(p, o.getClass(), k, u);
    }

    static double getDouble(Properties p, Class<?> c, String k, Usage u) {
        return getDouble(p, createName(c, k), u);
    }

    static double getDouble(Properties p, String k, Usage u) {
        return get(p, k, u, Double.class);
    }

    static boolean getBoolean(Properties p, Object o, String k, Usage u) {
        Validate.notNull(o);
        return getBoolean(p, o.getClass(), k, u);
    }

    static boolean getBoolean(Properties p, Class<?> c, String k, Usage u) {
        return getBoolean(p, createName(c, k), u);
    }

    static boolean getBoolean(Properties p, String k, Usage u) {
        return get(p, k, u, boolean.class);
    }

    static String getString(Properties p, Object o, String k, Usage u) {
        Validate.notNull(o);
        return getString(p, o.getClass(), k, u);
    }

    static String getString(Properties p, Class<?> c, String k, Usage u) {
        return getString(p, createName(c, k), u);
    }

    static String getString(Properties p, String k, Usage u) {
        return get(p, k, u, String.class);
    }

    static <T> T getParameter(Map<String, ?> ps, Properties p, String k, Usage u, Class<T> c) {
        Validate.notNull(ps);
        Validate.notEmpty(k);
        Object o = ps.get(k);
        if (o == null) {
            return get(p, k, u, c);
        } else {
            return getValue(k, o, c);
        }
    }

    static String get(Properties p, Object o, String k, Usage u) {
        Validate.notNull(o);
        return get(p, o.getClass(), k, u);
    }

    static String get(Properties p, Class<?> c, String k, Usage u) {
        Validate.notNull(c);
        return get(p, createName(c.getName(), k), u, String.class);
    }

    static String get(Properties p, String k, Usage u) {
        return get(p, k, u, String.class);
    }

    static <T> T get(Properties p, String k, Usage u, Class<T> c) {
        return get(p, k, u, c, null);
    }

    static <T> T get(Properties p, String k, Usage u, Class<T> c, T dv) {
        Validate.notNull(u);
        Validate.notNull(c);
        Validate.notEmpty(k);
        String s = p != null ? p.getProperty(k) : null;
        if (Boolean.getBoolean(PROP_LOGPROPS)) {
            PropLogger.INSTANCE.log(k, u, c, dv);
        }
        switch(u) {
            case REQUIRED_NOT_NULL:
                if (s == null) {
                    throw new IllegalArgumentException(String.format(MSG, k, "null", "null", k, c.getName()));
                }
                break;
            case REQUIRED_NOT_NULL_AND_NOT_EMPTY:
                if (s == null || s.length() == 0) {
                    throw new IllegalArgumentException(String.format(MSG, k, "null or empty", "null or empty", k, c.getName()));
                }
                break;
            case OPTIONAL:
                if (s == null || s.length() == 0) {
                    if (dv != null && dv.toString().length() > 0) {
                        s = dv.toString();
                    }
                }
            default:
                break;
        }
        return getValue(k, s, c);
    }

    static void set(Properties p, Object o, String k, String v) {
        Validate.notNull(o);
        set(p, o.getClass(), k, v);
    }

    static void set(Properties p, Class<?> c, String k, String v) {
        Validate.notNull(c);
        Validate.notEmpty(k);
        set(p, createName(c, k), v);
    }

    static void set(Properties p, String k, String v) {
        Validate.notNull(p);
        Validate.notEmpty(k);
        p.setProperty(k, v);
    }

    /**
     * Erzeugt aus einem Objekt und einem einfachen Namen einen Propertynamen.
     *
     * @param o Objekt.
     * @param k
     *            Propertyname.
     * @return Propertyname, mit Paket- und Klassennamen als Prefix.
     */
    static String createName(Object o, String k) {
        Validate.notNull(o);
        return createName(o.getClass(), k);
    }

    /**
     * Erzeugt aus einer Klasse und einem einfachen Namen einen Propertynamen.
     *
     * @param c
     *            Klasse.
     * @param k
     *            Propertyname.
     * @return Propertyname, mit Paket- und Klassennamen als Prefix.
     */
    static String createName(Class<?> c, String k) {
        Validate.notNull(c);
        return createName(c.getName(), k);
    }

    /**
     * Erzeugt aus einer namen und einem weiteren Namen einen Propertynamen.
     *
     * @param n0
     *            Name.
     * @param n1
     *            Propertyname.
     * @return Propertyname, mit Paket- und Klassennamen als Prefix.
     */
    static String createName(String n0, String n1) {
        Validate.notEmpty(n0);
        Validate.notEmpty(n1);
        return MessageFormat.format(PATTERN, n0, n1);
    }

    private static <T> T getValue(String k, Object s, Class<T> c) {
        Validate.notNull(c);
        Validate.notEmpty(k);
        if (c == Byte.class) {
            return s == null ? getValue(k, "0", c) : (T) Byte.valueOf(s.toString());
        } else if (c == Boolean.class) {
            return s == null ? getValue(k, "false", c) : (T) Boolean.valueOf(s.toString());
        } else if (c == Short.class) {
            return s == null ? getValue(k, "0", c) : (T) Short.valueOf(s.toString());
        } else if (c == Integer.class) {
            return s == null ? getValue(k, "0", c) : (T) Integer.valueOf(s.toString());
        } else if (c == Float.class) {
            return s == null ? getValue(k, "0", c) : (T) Float.valueOf(s.toString());
        } else if (c == Long.class) {
            return s == null ? getValue(k, "0", c) : (T) Long.valueOf(s.toString());
        } else if (c == Double.class) {
            return s == null ? getValue(k, "0", c) : (T) Double.valueOf(s.toString());
        } else if (c == Character.class) {
            return s == null ? (T) Character.valueOf(' ') : (T) Character.valueOf(s.toString().charAt(0));
        } else if (c == String.class) {
            return s == null ? null : (T) String.valueOf(s);
        } else {
            return (T) s;
        }
    }
}
