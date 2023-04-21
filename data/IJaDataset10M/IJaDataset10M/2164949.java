package org.op4j.operators.qualities;

import org.javaruntype.type.Type;
import org.op4j.operators.intf.mapofarray.Level0MapOfArrayOperator;

/**
 * <p>
 * This interface contains methods for casts to map of array (Map<K,V[]>).
 * </p>
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface CastableToMapOfArrayOperator {

    /**
     * <p>
     * Casts the operator's target as a map of array of the specified types.
     * </p>
     * 
     * @param <K> the type for the map's keys
     * @param <V> the type for the map's values (elements of the value arrays)
     * @param keyType the type for the map's keys
     * @param valueType the type for the map's values (elements of the value arrays)
     * @return the resulting map of array
     */
    public <K, V> Level0MapOfArrayOperator<K, V> asMapOfArrayOf(final Type<K> keyType, final Type<V> valueType);
}
