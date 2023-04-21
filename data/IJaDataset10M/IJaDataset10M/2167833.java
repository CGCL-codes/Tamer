package org.peaseplate.domain.conversion;

import java.util.HashMap;
import java.util.Map;
import org.peaseplate.Conversion;

public class ConversionCollection<SOURCE> {

    private final Class<SOURCE> sourceType;

    private final Map<Class<?>, Conversion<SOURCE, ?>> conversions;

    public ConversionCollection(Class<SOURCE> sourceType) {
        super();
        this.sourceType = sourceType;
        conversions = new HashMap<Class<?>, Conversion<SOURCE, ?>>();
    }

    public Class<SOURCE> getSourceType() {
        return sourceType;
    }

    public void add(Class<?> targetType, Conversion<SOURCE, ?> conversion) {
        conversions.put(targetType, conversion);
    }

    @SuppressWarnings("unchecked")
    protected <TARGET> Conversion<SOURCE, TARGET> get(Class<TARGET> targetType) {
        return (Conversion<SOURCE, TARGET>) conversions.get(targetType);
    }

    public <TARGET> TARGET convert(SOURCE value, Class<TARGET> targetType) throws ConversionException {
        Conversion<SOURCE, TARGET> conversion = resolve(targetType);
        if (conversion == null) throw new ConversionException(value, targetType, "No conversion defined from " + getSourceType() + " to " + targetType);
        try {
            return conversion.convert(value);
        } catch (Throwable th) {
            throw new ConversionException(value, targetType, "Could not convert " + getSourceType() + " to " + targetType, th);
        }
    }

    public <TARGET> boolean isConvertable(Class<TARGET> targetType) {
        return resolve(targetType) != null;
    }

    protected <TARGET> Conversion<SOURCE, TARGET> resolve(Class<TARGET> targetType) {
        @SuppressWarnings("unchecked") Conversion<SOURCE, TARGET> result = (Conversion<SOURCE, TARGET>) conversions.get(targetType);
        if (result == null) {
            result = resolveFromPrimitiveType(targetType);
            if (result == null) {
                result = resolveFromInterfaces(targetType);
                if (result == null) result = resolveFromSuperType(targetType);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <TARGET> Conversion<SOURCE, TARGET> resolveFromPrimitiveType(Class<TARGET> targetType) {
        Conversion<SOURCE, TARGET> result = null;
        if (targetType == boolean.class) result = (Conversion<SOURCE, TARGET>) resolve(Boolean.class); else if (targetType == byte.class) result = (Conversion<SOURCE, TARGET>) resolve(Byte.class); else if (targetType == short.class) result = (Conversion<SOURCE, TARGET>) resolve(Short.class); else if (targetType == int.class) result = (Conversion<SOURCE, TARGET>) resolve(Integer.class); else if (targetType == long.class) result = (Conversion<SOURCE, TARGET>) resolve(Long.class); else if (targetType == float.class) result = (Conversion<SOURCE, TARGET>) resolve(Float.class); else if (targetType == double.class) result = (Conversion<SOURCE, TARGET>) resolve(Double.class); else if (targetType == char.class) result = (Conversion<SOURCE, TARGET>) resolve(Character.class);
        if (result != null) conversions.put(targetType, result);
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <TARGET> Conversion<SOURCE, TARGET> resolveFromSuperType(Class<TARGET> targetType) {
        Conversion<SOURCE, TARGET> result = null;
        Class<? super TARGET> superType = targetType.getSuperclass();
        if (superType != null) {
            result = (Conversion<SOURCE, TARGET>) get(superType);
            if (result == null) result = (Conversion<SOURCE, TARGET>) resolveFromSuperType(superType);
            if (result != null) conversions.put(targetType, result);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <TARGET> Conversion<SOURCE, TARGET> resolveFromInterfaces(Class<TARGET> targetType) {
        Conversion<SOURCE, TARGET> result = null;
        Class<?>[] interfaces = targetType.getInterfaces();
        if (interfaces != null) {
            for (Class<?> entry : interfaces) {
                result = (Conversion<SOURCE, TARGET>) get(entry);
                if (result != null) {
                    conversions.put(entry, result);
                    break;
                }
            }
        }
        return result;
    }
}
