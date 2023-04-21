package net.sf.serialex.transform;

import net.sf.serialex.Serialexable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class TransformerMap {

    private static Map<Class, Transformer> transformerMap = new HashMap<Class, Transformer>();

    static {
        transformerMap.put(Boolean.TYPE, new BooleanTransformer());
        transformerMap.put(Byte.TYPE, new ByteTransformer());
        transformerMap.put(Character.TYPE, new CharacterTransformer());
        transformerMap.put(Short.TYPE, new ShortTransformer());
        transformerMap.put(Integer.TYPE, new IntegerTransformer());
        transformerMap.put(Long.TYPE, new LongTransformer());
        transformerMap.put(Double.TYPE, new DoubleTransformer());
        transformerMap.put(Float.TYPE, new FloatTransformer());
        transformerMap.put(Boolean.class, new BooleanTransformer());
        transformerMap.put(Byte.class, new ByteTransformer());
        transformerMap.put(Character.class, new CharacterTransformer());
        transformerMap.put(Short.class, new ShortTransformer());
        transformerMap.put(Integer.class, new IntegerTransformer());
        transformerMap.put(Long.class, new LongTransformer());
        transformerMap.put(Double.class, new DoubleTransformer());
        transformerMap.put(Float.class, new FloatTransformer());
        transformerMap.put(String.class, new StringTransformer());
        transformerMap.put(Date.class, new DateTransformer());
        transformerMap.put(BigInteger.class, new BigIntegerTransformer());
        transformerMap.put(BigDecimal.class, new BigDecimalTransformer());
        transformerMap.put(List.class, new CollectionTransformer());
        transformerMap.put(Set.class, new CollectionTransformer());
        transformerMap.put(Collection.class, new CollectionTransformer());
        transformerMap.put(Map.class, new MapTransformer());
        transformerMap.put(boolean[].class, new BooleanArrayTransformer());
        transformerMap.put(byte[].class, new ByteArrayTransformer());
        transformerMap.put(char[].class, new CharacterArrayTransformer());
        transformerMap.put(double[].class, new DoubleArrayTransformer());
        transformerMap.put(float[].class, new FloatArrayTransformer());
        transformerMap.put(int[].class, new IntegerArrayTransformer());
        transformerMap.put(long[].class, new LongArrayTransformer());
        transformerMap.put(short[].class, new ShortArrayTransformer());
        transformerMap.put(String[].class, new StringArrayTransformer());
        transformerMap.put(Serialexable.class, new SerialexableTransformer());
        transformerMap.put(Boolean[].class, new BooleanWrapperArrayTransformer());
        transformerMap.put(Byte[].class, new ByteWrapperArrayTransformer());
        transformerMap.put(Character[].class, new CharacterWrapperArrayTransformer());
        transformerMap.put(Double[].class, new DoubleWrapperArrayTransformer());
        transformerMap.put(Float[].class, new FloatWrapperArrayTransformer());
        transformerMap.put(Integer[].class, new IntegerWrapperArrayTransformer());
        transformerMap.put(Long[].class, new LongWrapperArrayTransformer());
        transformerMap.put(Short[].class, new ShortWrapperArrayTransformer());
    }

    public static Transformer getTransformer(Class clazz) {
        return transformerMap.get(clazz);
    }
}
