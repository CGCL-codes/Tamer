package com.riversoforion.acheron.enums;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import com.riversoforion.acheron.patterns.CheckedExceptions;

/**
 * <p>
 * Provides a helper for converting Enum values to and from their respective
 * {@literal "alternate"} values. The Enum class must have a method annotated
 * with {@link AlternateValue &#064;AlternateValue}.
 * </p>
 * <h2>Usage</h2>
 * <p>
 * This class can either be used directly by the Enum class (as a static
 * conversion method), or externally by a class that is outside of the Enum.
 * </p>
 * <h3>Internal Example</h3>
 * 
 * <pre>
 * 
 * 
 * public enum InternalEnum {
 *     FIRST, SECOND, THIRD;
 * 
 *     private static AlternateValueHelper&lt;InternalEnum&gt; helper = AlternateValueHelper.forEnum(InternalEnum.class);
 * 
 *     &#064;AlternateValue
 *     public int toNumericValue() {
 * 
 *         return this.ordinal() + 1;
 *     }
 * 
 *     public static InternalEnum fromNumericValue(int val) {
 * 
 *         return helper.fromAlternateValue(val);
 *     }
 * }
 * </pre>
 * 
 * <h3>External Example</h3>
 * 
 * <pre>
 * 
 * 
 * public static enum ExternalEnum {
 *     MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
 * 
 *     &#064;AlternateValue
 *     public String getAbbreviation() {
 * 
 *         return this.name().substring(0, 3).toLowerCase();
 *     }
 * }
 * </pre>
 * 
 * And usage from another class...
 * 
 * <pre>
 * 
 * 
 * AlternateValueHelper&lt;ExternalEnum&gt; helper = AlternateValueHelper.forEnum(ExternalEnum.class);
 * ExternalEnum val = helper.fromAlternateValue(&quot;fri&quot;);
 * </pre>
 * 
 * @author Eric McIntyre (<a
 *         href="mailto:macdaddy@riversoforion.com">macdaddy</a>)
 * @param <E>
 *            The Enum class to help.
 */
public class AlternateValueHelper<E extends Enum<E>> {

    private Map<Object, E> alternateValueMap = new HashMap<Object, E>();

    public static <E extends Enum<E>> AlternateValueHelper<E> forEnum(Class<E> enumClass) {
        AlternateValueHelper<E> helper = new AlternateValueHelper<E>(enumClass);
        return helper;
    }

    private AlternateValueHelper(Class<E> enumClass) {
        initializeAlternateValues(enumClass);
    }

    public E fromAlternateValue(Object val) {
        return this.alternateValueMap.get(val);
    }

    private void initializeAlternateValues(Class<E> enumClass) {
        Method altValueMethod = findAltValueMethod(enumClass);
        try {
            for (E val : enumClass.getEnumConstants()) {
                Object altVal = altValueMethod.invoke(val);
                this.alternateValueMap.put(altVal, val);
            }
        } catch (Exception e) {
            CheckedExceptions.throwAsUnchecked(e);
        }
    }

    private Method findAltValueMethod(Class<E> enumClass) {
        Method altValueMethod = null;
        for (Method m : enumClass.getMethods()) {
            if (m.isAnnotationPresent(AlternateValue.class)) {
                altValueMethod = m;
                break;
            }
        }
        if (altValueMethod == null) {
            throw new IllegalArgumentException(enumClass.getName() + " does not have an @AlternateValue method");
        }
        if (altValueMethod.getParameterTypes().length > 0) {
            throw new IllegalArgumentException("@AlternateValue method (" + altValueMethod.getName() + ") must not take any arguments");
        }
        return altValueMethod;
    }
}
