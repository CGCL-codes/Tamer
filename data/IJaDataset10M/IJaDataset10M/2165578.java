package com.baculsoft.json;

import com.baculsoft.lang.StringUtil;

/**
 * 
 * @author Natalino Nugeraha
 * @author Muhammad Edwin
 * 
 */
public final class Util {

    private Util() {
    }

    /**
	 * 
	 * @param obj
	 * @param dest
	 * @return
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final Object convertToStrict(Object obj, Class<?> dest) {
        if (obj == null) return null;
        if (dest.isAssignableFrom(obj.getClass())) return obj;
        if (dest.isPrimitive()) {
            if (dest == int.class) if (obj instanceof Number) return ((Number) obj).intValue(); else return StringUtil.valueOfInteger(obj.toString()); else if (dest == short.class) if (obj instanceof Number) return ((Number) obj).shortValue(); else return StringUtil.valueOfShort(obj.toString()); else if (dest == long.class) if (obj instanceof Number) return ((Number) obj).longValue(); else return StringUtil.valueOfLong(obj.toString()); else if (dest == byte.class) if (obj instanceof Number) return ((Number) obj).byteValue(); else return StringUtil.valueOfByte(obj.toString()); else if (dest == float.class) if (obj instanceof Number) return ((Number) obj).floatValue(); else return Float.valueOf(obj.toString()); else if (dest == double.class) if (obj instanceof Number) return ((Number) obj).doubleValue(); else return StringUtil.valueOfDouble(obj.toString()); else if (dest == char.class) {
                String asString = dest.toString();
                if (asString.length() > 0) return Character.valueOf(asString.charAt(0));
            } else if (dest == boolean.class) {
                return (Boolean) obj;
            }
            throw new RuntimeException(StringUtil.concat(StringUtil.concat("Primitive: Can not convert ", obj.getClass().getName(), " to "), dest.getName()));
        } else {
            if (dest.isEnum()) return Enum.valueOf((Class<Enum>) dest, obj.toString());
            if (dest == Integer.class) if (obj instanceof Number) return Integer.valueOf(((Number) obj).intValue()); else return StringUtil.valueOfInteger(obj.toString());
            if (dest == Long.class) if (obj instanceof Number) return Long.valueOf(((Number) obj).longValue()); else return StringUtil.valueOfLong(obj.toString());
            if (dest == Short.class) if (obj instanceof Number) return Short.valueOf(((Number) obj).shortValue()); else return StringUtil.valueOfShort(obj.toString());
            if (dest == Byte.class) if (obj instanceof Number) return Byte.valueOf(((Number) obj).byteValue()); else return StringUtil.valueOfByte(obj.toString());
            if (dest == Float.class) if (obj instanceof Number) return Float.valueOf(((Number) obj).floatValue()); else return Float.valueOf(obj.toString());
            if (dest == Double.class) if (obj instanceof Number) return Double.valueOf(((Number) obj).doubleValue()); else return StringUtil.valueOfDouble(obj.toString());
            if (dest == Character.class) {
                String asString = dest.toString();
                if (asString.length() > 0) return Character.valueOf(asString.charAt(0));
            }
            throw new RuntimeException(StringUtil.concat(StringUtil.concat("Object: Can not Convert ", obj.getClass().getName(), " to "), dest.getName()));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object convertToX(Object obj, Class<?> dest) {
        if (obj == null) return null;
        if (dest.isAssignableFrom(obj.getClass())) return obj;
        if (dest.isPrimitive()) {
            if (obj instanceof Number) return obj;
            if (dest == int.class) return StringUtil.valueOfInteger(obj.toString()); else if (dest == short.class) return StringUtil.valueOfShort(obj.toString()); else if (dest == long.class) return StringUtil.valueOfLong(obj.toString()); else if (dest == byte.class) return StringUtil.valueOfByte(obj.toString()); else if (dest == float.class) return Float.valueOf(obj.toString()); else if (dest == double.class) return StringUtil.valueOfDouble(obj.toString()); else if (dest == char.class) {
                String asString = dest.toString();
                if (asString.length() > 0) return Character.valueOf(asString.charAt(0));
            } else if (dest == boolean.class) {
                return (Boolean) obj;
            }
            throw new RuntimeException(StringUtil.concat(StringUtil.concat("Primitive: Can not convert ", obj.getClass().getName(), " to "), dest.getName()));
        } else {
            if (dest.isEnum()) return Enum.valueOf((Class<Enum>) dest, obj.toString());
            if (dest == Integer.class) if (obj instanceof Number) return Integer.valueOf(((Number) obj).intValue()); else return StringUtil.valueOfInteger(obj.toString());
            if (dest == Long.class) if (obj instanceof Number) return Long.valueOf(((Number) obj).longValue()); else return StringUtil.valueOfLong(obj.toString());
            if (dest == Short.class) if (obj instanceof Number) return Short.valueOf(((Number) obj).shortValue()); else return StringUtil.valueOfShort(obj.toString());
            if (dest == Byte.class) if (obj instanceof Number) return Byte.valueOf(((Number) obj).byteValue()); else return StringUtil.valueOfByte(obj.toString());
            if (dest == Float.class) if (obj instanceof Number) return Float.valueOf(((Number) obj).floatValue()); else return Float.valueOf(obj.toString());
            if (dest == Double.class) if (obj instanceof Number) return Double.valueOf(((Number) obj).doubleValue()); else return StringUtil.valueOfDouble(obj.toString());
            if (dest == Character.class) {
                String asString = dest.toString();
                if (asString.length() > 0) return Character.valueOf(asString.charAt(0));
            }
            throw new RuntimeException(StringUtil.concat(StringUtil.concat("Object: Can not Convert ", obj.getClass().getName(), " to "), dest.getName()));
        }
    }
}
