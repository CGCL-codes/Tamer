package net.entropysoft.transmorph.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Set of known immutable classes
 * 
 * @author Cedric Chabanois (cchabanois at gmail.com)
 * 
 */
public class ImmutableClasses {

    private static final Set<Class<?>> knownImmutables = new HashSet<Class<?>>(Arrays.asList(String.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, BigInteger.class, BigDecimal.class, Character.class));

    public static synchronized void addImmutableClass(Class<?> clazz) {
        knownImmutables.add(clazz);
    }

    public static synchronized boolean isKnownImmutableClass(Class<?> clazz) {
        return knownImmutables.contains(clazz);
    }
}
