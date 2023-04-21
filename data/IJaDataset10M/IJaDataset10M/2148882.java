package org.streets.commons.util;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A utility class to convert the given Object into any type.
 * 
 * @author Zenberg.D
 * 
 */
public final class TypeConverter {

    private static final String COLOR_KEY = "0123456789ABCDEF";

    /** Constant for the prefix of hex numbers. */
    private static final String HEX_PREFIX = "0x";

    /** Constant for the radix of hex numbers. */
    private static final int HEX_RADIX = 16;

    /**
	 * Constant for the argument classes of the Number constructor that takes a
	 * String.
	 */
    private static final Class<?>[] CONSTR_ARGS = { String.class };

    /**
	 * Private constructor prevents instances from being created.
	 */
    private TypeConverter() {
    }

    /**
	 * Convert the specified object into a Boolean. Internally the
	 * <code>org.apache.commons.lang.BooleanUtils</code> class from the <a
	 * href="http://jakarta.apache.org/commons/lang/">Commons Lang</a> project
	 * is used to perform this conversion. This class accepts some more tokens
	 * for the boolean value of <b>true</b>, e.g. <code>yes</code> and
	 * <code>on</code>. Please refer to the documentation of this class for
	 * more details.
	 *
	 * @param value
	 *            the value to convert
	 * @return the converted value
	 * @throws ConvertException
	 *             thrown if the value cannot be converted to a boolean
	 */
    public static Boolean toBoolean(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return "true".equalsIgnoreCase((String) value) || "yes".equalsIgnoreCase((String) value) || "y".equalsIgnoreCase((String) value);
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }
        throw new RuntimeException("The value " + value + " can't be converted to a Boolean object");
    }

    /**
	 * Convert the specified object into a Byte.
	 *
	 * @param value
	 *            the value to convert
	 * @return the converted value
	 * @throws ConvertException
	 *             thrown if the value cannot be converted to a byte
	 */
    public static Byte toByte(Object value) {
        if (value == null) {
            return null;
        }
        Number n = toNumber(value, Byte.class);
        if (n instanceof Byte) {
            return (Byte) n;
        } else {
            return new Byte(n.byteValue());
        }
    }

    /**
	 * Tries to convert the specified object into a number object. This method
	 * is used by the conversion methods for number types. Note that the return
	 * value is not in always of the specified target class, but only if a new
	 * object has to be created.
	 *
	 * @param value
	 *            the value to be converted (must not be <b>null</b>)
	 * @param targetClazz
	 *            the target class of the conversion (must be derived from
	 *            <code>java.lang.Number</code>)
	 * @return the converted number
	 * @throws ConvertException
	 *             if the object cannot be converted
	 */
    public static Number toNumber(Object value, Class<?> targetClazz) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) return (Number) value;
        String str = value.toString();
        if (str.startsWith(HEX_PREFIX)) {
            try {
                return new BigInteger(str.substring(HEX_PREFIX.length()), HEX_RADIX);
            } catch (NumberFormatException nex) {
                throw new RuntimeException("Could not convert " + str + " to " + targetClazz.getName() + "! Invalid hex number.", nex);
            }
        }
        try {
            Constructor<?> constr = targetClazz.getConstructor(CONSTR_ARGS);
            return (Number) constr.newInstance(new Object[] { str });
        } catch (InvocationTargetException itex) {
            throw new RuntimeException("Could not convert " + str + " to " + targetClazz.getName(), itex.getTargetException());
        } catch (Exception ex) {
            throw new RuntimeException("Conversion error when trying to convert " + str + " to " + targetClazz.getName(), ex);
        }
    }

    /**
	 * Convert the specified object into a Short.
	 *
	 * @param value
	 *            the value to convert
	 * @return the converted value
	 * @throws ConvertException
	 *             thrown if the value cannot be converted to a short
	 */
    public static Short toShort(Object value) {
        if (value == null) {
            return null;
        }
        Number n = toNumber(value, Short.class);
        if (n instanceof Short) {
            return (Short) n;
        } else {
            return new Short(n.shortValue());
        }
    }

    /**
	 * Convert the specified object into an Integer.
	 *
	 * @param value
	 *            the value to convert
	 * @return the converted value
	 * @throws ConvertException
	 *             thrown if the value cannot be converted to an integer
	 */
    public static Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        Number n = toNumber(value, Integer.class);
        if (n instanceof Integer) {
            return (Integer) n;
        } else {
            return new Integer(n.intValue());
        }
    }

    /**
	 * Convert the specified object into a Long.
	 *
	 * @param value
	 *            the value to convert
	 * @return the converted value
	 * @throws ConvertException
	 *             thrown if the value cannot be converted to a Long
	 */
    public static Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        Number n = toNumber(value, Long.class);
        if (n instanceof Long) {
            return (Long) n;
        } else {
            return new Long(n.longValue());
        }
    }

    /**
	 * Convert the specified object into a Float.
	 *
	 * @param value
	 *            the value to convert
	 * @return the converted value
	 * @throws ConvertException
	 *             thrown if the value cannot be converted to a Float
	 */
    public static Float toFloat(Object value) {
        if (value == null) {
            return null;
        }
        Number n = toNumber(value, Float.class);
        if (n instanceof Float) {
            return (Float) n;
        } else {
            return new Float(n.floatValue());
        }
    }

    /**
	 * Convert the specified object into a Double.
	 *
	 * @param value
	 *            the value to convert
	 * @return the converted value
	 * @throws ConvertException
	 *             thrown if the value cannot be converted to a Double
	 */
    public static Double toDouble(Object value) {
        if (value == null) {
            return null;
        }
        Number n = toNumber(value, Double.class);
        if (n instanceof Double) {
            return (Double) n;
        } else {
            return new Double(n.doubleValue());
        }
    }

    /**
	 * Convert the specified object into a BigInteger.
	 *
	 * @param value
	 *            the value to convert
	 * @return the converted value
	 * @throws ConvertException
	 *             thrown if the value cannot be converted to a BigInteger
	 */
    public static BigInteger toBigInteger(Object value) {
        if (value == null) {
            return null;
        }
        Number n = toNumber(value, BigInteger.class);
        if (n instanceof BigInteger) {
            return (BigInteger) n;
        } else {
            return BigInteger.valueOf(n.longValue());
        }
    }

    /**
	 * Convert the specified object into a BigDecimal.
	 *
	 * @param value
	 *            the value to convert
	 * @return the converted value
	 * @throws ConvertException
	 *             thrown if the value cannot be converted to a BigDecimal
	 */
    public static BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        Number n = toNumber(value, BigDecimal.class);
        if (n instanceof BigDecimal) {
            return (BigDecimal) n;
        } else {
            return new BigDecimal(n.doubleValue());
        }
    }

    /**
	 * Convert the specified object into a Date.
	 *
	 * @param value
	 *            the value to convert
	 * @param format
	 *            the DateFormat pattern to parse String values
	 * @return the converted value
	 * @throws RuntimeException
	 *             thrown if the value cannot be converted to a Calendar
	 */
    public static Date toDate(Object value, String format) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        if (value instanceof Calendar) {
            return ((Calendar) value).getTime();
        }
        if (value instanceof Long) {
            return new Date(((Long) value).longValue());
        }
        if (value instanceof String) {
            try {
                return new SimpleDateFormat(format).parse((String) value);
            } catch (ParseException e) {
                throw new RuntimeException("The value " + value + " can't be converted to a Date", e);
            }
        } else {
            throw new RuntimeException("The value " + value + " can't be converted to a Date");
        }
    }

    public static Timestamp toTimestamp(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Timestamp) {
            return (Timestamp) value;
        }
        if (value instanceof Long) {
            return new Timestamp(((Long) value).longValue());
        }
        if (value instanceof Date) {
            return new Timestamp(((Date) value).getTime());
        }
        if (value instanceof String) {
            String v = (String) value;
            if (v.indexOf("-") < 0) {
                Date date = new Date();
                v = DateFormatUtils.format(date, "yyyy-MM-dd") + " " + v;
            }
            if (v.indexOf(":") < 0) {
                v += " 00:00:00";
            }
            Date ts;
            try {
                ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(v);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return new Timestamp(ts.getTime());
        } else {
            throw new RuntimeException("The value " + value + " can't be converted to a Timestamp");
        }
    }

    /**
	 * Convert the specified object into a Calendar.
	 *
	 * @param value
	 *            the value to convert
	 * @param format
	 *            the DateFormat pattern to parse String values
	 * @return the converted value
	 * @throws RuntimeException
	 *             thrown if the value cannot be converted to a Calendar
	 */
    public static Calendar toCalendar(Object value, String format) {
        if (value == null) {
            return null;
        }
        if (value instanceof Calendar) {
            return (Calendar) value;
        }
        if (value instanceof Date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) value);
            return calendar;
        }
        if (value instanceof String) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new SimpleDateFormat(format).parse((String) value));
                return calendar;
            } catch (ParseException e) {
                throw new RuntimeException("The value " + value + " can't be converted to a Calendar", e);
            }
        } else {
            throw new RuntimeException("The value " + value + " can't be converted to a Calendar");
        }
    }

    public static String toString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        if (value instanceof Boolean) {
            if (((Boolean) value).booleanValue()) return "true"; else return "false";
        }
        if (value instanceof Date) {
            return FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss").format((Date) value);
        }
        return value.toString();
    }

    public static char toChar(Object value, char defaultValue) {
        try {
            return toChar(value);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    public static char toChar(Object value) throws RuntimeException {
        if (null == value) {
            throw new RuntimeException("Impossible to convert " + value + " from " + (null == value ? "unknown" : value.getClass().getName()) + " to " + char.class.getName());
        }
        if (value instanceof Character) {
            return ((Character) value).charValue();
        }
        if (value instanceof Number) {
            int int_value = ((Number) value).intValue();
            return (char) int_value;
        }
        if (value instanceof String && 1 == ((String) value).length()) {
            return ((String) value).charAt(0);
        }
        throw new RuntimeException("Impossible to convert " + value + " from " + (null == value ? "unknown" : value.getClass().getName()) + " to " + char.class.getName());
    }

    public static boolean toBoolean(Object value, boolean defaultValue) {
        try {
            return toBoolean(value);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    public static byte toByte(Object value, byte defaultValue) {
        try {
            return toByte(value);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    public static short toShort(Object value, short defaultValue) {
        try {
            return toShort(value);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    public static int toInt(Object value, int defaultValue) {
        try {
            return toInt(value);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    public static int toInt(Object value) throws RuntimeException {
        if (null == value) {
            throw new RuntimeException("Impossible to convert " + value + " from " + (null == value ? "unknown" : value.getClass().getName()) + " to " + int.class.getName());
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Impossible to convert " + value + " from " + (null == value ? "unknown" : value.getClass().getName()) + " to " + int.class.getName());
            }
        }
        throw new RuntimeException("Impossible to convert " + value + " from " + (null == value ? "unknown" : value.getClass().getName()) + " to " + int.class.getName());
    }

    public static long toLong(Object value, long defaultValue) {
        try {
            return toLong(value);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    public static float toFloat(Object value, float defaultValue) {
        try {
            return toFloat(value);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    public static double toDouble(Object value, double defaultValue) {
        try {
            return toDouble(value);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    /**
	 * Convert the specified object into an URL.
	 *
	 * @param value
	 *            the value to convert
	 * @return the converted value
	 * @throws RuntimeException
	 *             thrown if the value cannot be converted to an URL
	 */
    public static URL toURL(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof URL) {
            return (URL) value;
        }
        if (value instanceof String) {
            try {
                return new URL((String) value);
            } catch (MalformedURLException e) {
                throw new RuntimeException("The value " + value + " can't be converted to an URL", e);
            }
        } else {
            throw new RuntimeException("The value " + value + " can't be converted to an URL");
        }
    }

    /**
	 * Convert the specified object into a Locale.
	 *
	 * @param value
	 *            the value to convert
	 * @return the converted value
	 * @throws RuntimeException
	 *             thrown if the value cannot be converted to a Locale
	 */
    public static Locale toLocale(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Locale) return (Locale) value;
        if (value instanceof String) {
            String[] elements = ((String) value).split("_ ");
            String language = elements.length > 0 ? elements[0] : "";
            String country = elements.length > 1 ? elements[1] : "";
            String variant = elements.length > 2 ? elements[2] : "";
            if (language.length() > 0) {
                return new Locale(language, country, variant);
            } else {
                throw new RuntimeException("The value " + value + " can't be converted to a Locale");
            }
        } else {
            throw new RuntimeException("The value " + value + " can't be converted to a Locale");
        }
    }

    /**
	 * Convert the specified object into a Color. If the value is a String, the
	 * format allowed is (#)?[0-9A-F]{6}([0-9A-F]{2})?. Examples:
	 * <ul>
	 * <li>FF0000 (red)</li>
	 * <li>0000FFA0 (semi transparent blue)</li>
	 * <li>#CCCCCC (gray)</li>
	 * <li>#00FF00A0 (semi transparent green)</li>
	 * </ul>
	 *
	 * @param value
	 *            the value to convert
	 * @return the converted value
	 * @throws RuntimeException
	 *             thrown if the value cannot be converted to a Color
	 */
    public static Color toColor(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Color) return (Color) value;
        if (value instanceof String && !StringUtils.isBlank((String) value)) {
            String color = ((String) value).trim();
            int[] components = new int[3];
            int minlength = components.length * 2;
            if (color.length() < minlength) {
                throw new RuntimeException("The value " + value + " can't be converted to a Color");
            }
            if (color.startsWith("#")) {
                color = color.substring(1);
            }
            try {
                for (int i = 0; i < components.length; i++) {
                    components[i] = Integer.parseInt(color.substring(2 * i, 2 * i + 2), HEX_RADIX);
                }
                int alpha;
                if (color.length() >= minlength + 2) {
                    alpha = Integer.parseInt(color.substring(minlength, minlength + 2), HEX_RADIX);
                } else {
                    alpha = Color.black.getAlpha();
                }
                return new Color(components[0], components[1], components[2], alpha);
            } catch (Exception e) {
                throw new RuntimeException("The value " + value + " can't be converted to a Color", e);
            }
        } else {
            throw new RuntimeException("The value " + value + " can't be converted to a Color");
        }
    }

    /**
	 * Convert gived value to indicated target Class
	 * @param value
	 * @param target
	 * @return
	 * @throws RuntimeException
	 */
    public static Object toType(Object value, Class<?> target) throws RuntimeException {
        if (null == target || null == value) {
            return null;
        }
        if (value.getClass() == target) {
            return value;
        }
        if (target.isPrimitive()) {
            if (target == boolean.class) {
                return toBoolean(value);
            }
            if (target == char.class) {
                return toChar(value);
            }
            if (target == byte.class) {
                return toByte(value);
            }
            if (target == short.class) {
                return toShort(value);
            }
            if (target == int.class) {
                return toInt(value);
            }
            if (target == long.class) {
                return toLong(value);
            }
            if (target == float.class) {
                return toFloat(value);
            }
            if (target == double.class) {
                return toDouble(value);
            }
        } else {
            if (target == Boolean.class) {
                return toBoolean(value);
            }
            if (target == Character.class) {
                return toChar(value);
            }
            if (target == Byte.class) {
                return toByte(value);
            }
            if (target == Short.class) {
                return toShort(value);
            }
            if (target == Integer.class) {
                return toInt(value);
            }
            if (target == Long.class) {
                return toLong(value);
            }
            if (target == Float.class) {
                return toFloat(value);
            }
            if (target == Double.class) {
                return toDouble(value);
            }
            if (target == BigInteger.class) {
                return toBigInteger(value);
            }
            if (target == java.sql.Date.class) {
                Date d = toDate(value, "yyyy-MM-dd");
                return new java.sql.Date(d.getTime());
            }
            if (target == java.sql.Time.class) {
                Date d = toDate(value, "HH:mm:ss");
                return new java.sql.Time(d.getTime());
            }
            if (target == Date.class) {
                return toDate(value, "yyyy-MM-dd HH:mm:ss");
            }
            if (target == Timestamp.class) {
                return toTimestamp(value);
            }
            if (target == URL.class) {
                return toURL(value);
            }
            if (target == Locale.class) {
                return toLocale(value);
            }
            if (target == Color.class) {
                return toColor(value);
            }
            if (String.class.isAssignableFrom(target)) {
                return toString(value);
            }
            if (target.isAssignableFrom(value.getClass())) {
                return value;
            }
        }
        return value;
    }

    /**
	 * RGB array to HEX string
	 * @param rgb
	 * @return
	 */
    public static String toHEX(int[] rgb) {
        StringBuffer sb = new StringBuffer();
        sb.append("#");
        sb.append(COLOR_KEY.substring((int) Math.floor(rgb[0] / 16), (int) Math.floor(rgb[0] / 16) + 1));
        sb.append(COLOR_KEY.substring(rgb[0] % 16, (rgb[0] % 16) + 1));
        sb.append(COLOR_KEY.substring((int) Math.floor(rgb[1] / 16), (int) Math.floor(rgb[1] / 16) + 1));
        sb.append(COLOR_KEY.substring(rgb[1] % 16, (rgb[1] % 16) + 1));
        sb.append(COLOR_KEY.substring((int) Math.floor(rgb[2] / 16), (int) Math.floor(rgb[2] / 16) + 1));
        sb.append(COLOR_KEY.substring(rgb[2] % 16, (rgb[2] % 16) + 1));
        return sb.toString();
    }

    /**
	 * HEX string to RGB array
	 * @param hex
	 * @return
	 */
    public static int[] toRGB(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1, hex.length()).toUpperCase();
        } else {
            hex = hex.toUpperCase();
        }
        int[] hexArray = new int[6];
        char[] c = hex.toCharArray();
        for (int i = 0; i < hex.length(); i++) {
            if (c[i] == 'A') {
                hexArray[i] = 10;
            } else if (c[i] == 'B') {
                hexArray[i] = 11;
            } else if (c[i] == 'C') {
                hexArray[i] = 12;
            } else if (c[i] == 'D') {
                hexArray[i] = 13;
            } else if (c[i] == 'E') {
                hexArray[i] = 14;
            } else if (c[i] == 'F') {
                hexArray[i] = 15;
            } else {
                hexArray[i] = Integer.parseInt(new Character(c[i]).toString());
            }
        }
        int[] rgb = new int[3];
        rgb[0] = (hexArray[0] * 16) + hexArray[1];
        rgb[1] = (hexArray[2] * 16) + hexArray[3];
        rgb[2] = (hexArray[4] * 16) + hexArray[5];
        return rgb;
    }
}
