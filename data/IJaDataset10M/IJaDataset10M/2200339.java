package org.jmetis.conversion;

import java.util.HashMap;
import java.util.Map;

/**
 * TheIdentityConverter. Returns the source value (the identity function).
 */
public class IdentityConverter implements ITypeConverter {

    private static Class<?>[][] primitiveMap = new Class[][] { { Integer.TYPE, Integer.class }, { Short.TYPE, Short.class }, { Long.TYPE, Long.class }, { Double.TYPE, Double.class }, { Byte.TYPE, Byte.class }, { Float.TYPE, Float.class }, { Boolean.TYPE, Boolean.class }, { Character.TYPE, Character.class } };

    private static Map<Class<?>, Object> PRIMITIVE_DEFAULT_VALUES;

    private Class<?> sourceType;

    private Class<?> targetType;

    public IdentityConverter(Class<?> sourceType, Class<?> targetType) {
        super();
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    public IdentityConverter(Class<?> type) {
        this(type, type);
    }

    /**
	 * (Non-API) isPrimitiveTypeMatchedWithBoxed.
	 * 
	 * @param sourceClass
	 * @param toClass
	 * @return true if sourceClass and toType are matched primitive/boxed types
	 */
    public boolean isPrimitiveTypeMatchedWithBoxed(Class sourceClass, Class toClass) {
        for (Class[] element : IdentityConverter.primitiveMap) {
            if (toClass.equals(element[0]) && sourceClass.equals(element[1])) {
                return true;
            }
            if (sourceClass.equals(element[0]) && toClass.equals(element[1])) {
                return true;
            }
        }
        return false;
    }

    public <T> T convertTo(Object sourceValue, Class<T> targetType) {
        if (targetType.isPrimitive()) {
            if (sourceValue == null) {
                return targetType.cast(IdentityConverter.PRIMITIVE_DEFAULT_VALUES.get(targetType));
            }
        }
        if (sourceValue != null) {
            Class<?> sourceClass = sourceValue.getClass();
            if (targetType.isPrimitive()) {
                if (sourceClass.equals(this.targetType) || this.isPrimitiveTypeMatchedWithBoxed(sourceClass, this.targetType)) {
                    return targetType.cast(sourceValue);
                }
                throw new ConversionException("Boxed and unboxed types do not match");
            }
            if (!this.targetType.isAssignableFrom(sourceClass)) {
                throw new ConversionException(sourceClass.getName() + " is not assignable to " + this.targetType.getName());
            }
        }
        return (T) sourceValue;
    }

    static {
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES = new HashMap<Class<?>, Object>();
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Boolean.TYPE, Boolean.FALSE);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Boolean.class, Boolean.FALSE);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Byte.TYPE, (byte) 0);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Byte.class, (byte) 0);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Character.TYPE, (char) 0);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Character.class, (char) 0);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Double.TYPE, 0.0);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Double.class, 0.0);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Float.TYPE, 0.0F);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Float.class, 0.0F);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Integer.TYPE, 0);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Integer.class, 0);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Long.TYPE, 0L);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Long.class, 0L);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Short.TYPE, (short) 0);
        IdentityConverter.PRIMITIVE_DEFAULT_VALUES.put(Short.class, (short) 0);
    }
}
