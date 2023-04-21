package diuf.diva.hephaistk.dialog;

import diuf.diva.hephaistk.config.LoggingManager;

public class TypeConverter {

    public static Object convert(String value, Class<?> type) {
        try {
            if (type.equals(String.class)) {
                return value;
            }
            if (type.equals(int.class) || type.equals(Integer.class)) {
                return Integer.valueOf(value).intValue();
            }
            if (type.equals(float.class) || type.equals(Float.class)) {
                return Float.valueOf(value);
            }
            if (type.equals(double.class) || type.equals(Double.class)) {
                return Double.valueOf(value);
            }
            if (type.equals(boolean.class) || type.equals(Boolean.class)) {
                return Boolean.valueOf(value);
            }
            if (type.equals(long.class) || type.equals(Long.class)) {
                return Long.valueOf(value);
            }
            if (type.equals(byte.class) || type.equals(Byte.class)) {
                return Byte.valueOf(value);
            }
            if (type.equals(char.class) || type.equals(Character.class)) {
                return value.charAt(0);
            }
            return null;
        } catch (NumberFormatException e) {
            LoggingManager.getLogger().error("could not cast value '" + value + "' to type " + type);
            return null;
        }
    }

    public static Class<?> convertPrimitive(Class<?> type) {
        if (type.equals(int.class)) return Integer.class;
        if (type.equals(boolean.class)) return Boolean.class;
        if (type.equals(long.class)) return Long.class;
        if (type.equals(double.class)) return Double.class;
        if (type.equals(float.class)) return Float.class;
        if (type.equals(byte.class)) return Byte.class;
        if (type.equals(char.class)) return Character.class;
        if (type.equals(short.class)) return Short.class;
        return type;
    }
}
