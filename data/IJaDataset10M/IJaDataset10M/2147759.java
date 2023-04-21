package net.sf.easyweb4j.model.converters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConverterFactory {

    private static Map<Class<?>, Converter> converters;

    static {
        converters = new HashMap<Class<?>, Converter>();
        converters.put(String.class, new StringConverter());
        converters.put(Date.class, new DateConverter());
        converters.put(Character.class, new CharacterConverter(false));
        converters.put(Character.TYPE, new CharacterConverter(true));
        converters.put(Boolean.class, new BooleanConverter(true));
        converters.put(Boolean.TYPE, new BooleanConverter(true));
        converters.put(Byte.class, new NumberConverter(Byte.class));
        converters.put(Byte.TYPE, new NumberConverter(Byte.TYPE));
        converters.put(Short.class, new NumberConverter(Short.class));
        converters.put(Short.TYPE, new NumberConverter(Short.TYPE));
        converters.put(Integer.class, new NumberConverter(Integer.class));
        converters.put(Integer.TYPE, new NumberConverter(Integer.TYPE));
        converters.put(Long.class, new NumberConverter(Long.class));
        converters.put(Long.TYPE, new NumberConverter(Long.TYPE));
        converters.put(Float.class, new NumberConverter(Float.class));
        converters.put(Float.TYPE, new NumberConverter(Float.TYPE));
        converters.put(Double.class, new NumberConverter(Double.class));
        converters.put(Double.TYPE, new NumberConverter(Double.TYPE));
        converters.put(BigInteger.class, new NumberConverter(BigInteger.class));
        converters.put(BigDecimal.class, new NumberConverter(BigDecimal.class));
    }

    public static Converter getConverter(Class<?> type) {
        return converters.get(type);
    }
}
