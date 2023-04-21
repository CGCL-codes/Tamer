package com.angel.common.converters.transformers;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections15.Transformer;
import com.angel.common.converters.base.ObjectTransformer;

/**
 *
 * @author William
 *
 */
public class FloatTransformer implements ObjectTransformer {

    public static Map<Class<?>, Transformer<Float, ? extends Object>> TRANSFORMERS = new HashMap<Class<?>, Transformer<Float, ? extends Object>>();

    public FloatTransformer() {
        super();
        TRANSFORMERS.put(Integer.class, new Transformer<Float, Integer>() {

            public Integer transform(Float object) {
                return Integer.valueOf(object.toString());
            }
        });
        TRANSFORMERS.put(Long.class, new Transformer<Float, Long>() {

            public Long transform(Float object) {
                return Long.valueOf(object.toString());
            }
        });
        TRANSFORMERS.put(Double.class, new Transformer<Float, Double>() {

            public Double transform(Float object) {
                return Double.valueOf(object.toString());
            }
        });
        TRANSFORMERS.put(Short.class, new Transformer<Float, Short>() {

            public Short transform(Float object) {
                return Short.valueOf(String.valueOf(object));
            }
        });
        TRANSFORMERS.put(Boolean.class, new Transformer<Float, Boolean>() {

            public Boolean transform(Float object) {
                return Boolean.valueOf(object.intValue() == 1 ? true : false);
            }
        });
        TRANSFORMERS.put(Character.class, new Transformer<Float, Character>() {

            public Character transform(Float object) {
                return Character.valueOf(String.valueOf(object).charAt(0));
            }
        });
        TRANSFORMERS.put(String.class, new Transformer<Float, String>() {

            public String transform(Float object) {
                return object.toString();
            }
        });
        TRANSFORMERS.put(Float.class, new Transformer<Float, Float>() {

            public Float transform(Float object) {
                return Float.valueOf(object.toString());
            }
        });
    }

    public Transformer<Float, ? extends Object> getTransformer(Class<?> clazz) {
        return TRANSFORMERS.get(clazz);
    }
}
