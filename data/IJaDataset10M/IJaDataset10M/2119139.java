package org.op4j.operators.qualities;

import org.javaruntype.type.Type;

/**
 * <p>
 * This interface contains methods for casts to map (Map<K,V>).
 * </p>
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface CastableToMapOperator {

    /**
     * <p>
     * Casts the operator's target as a map of the specified types.
     * </p>
     * 
     * @param <K> the type for the map's keys
     * @param <V> the type for the map's values
     * @param keyType the type for the map's keys
     * @param valueType the type for the map's values
     * @return the resulting map
     */
    public <K, V> Operator of(final Type<K> keyType, final Type<V> valueType);

    /**
     * <p>
     * Casts the operator's target as a map of the specified types.
     * </p>
     * 
     * @param <K> the type for the map's keys
     * @param <V> the type for the map's values
     * @param keyType the type for the map's keys
     * @param valueType the type for the map's values
     * @return the resulting map
     */
    public <K, V> Operator castToMapOf(final Type<K> keyType, final Type<V> valueType);
}
