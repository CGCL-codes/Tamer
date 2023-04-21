package org.op4j.operators.qualities;

import org.op4j.operators.intf.mapofset.ILevel0MapOfSetOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface IConvertibleToMapOfSetOperator<K, V> {

    public ILevel0MapOfSetOperator<K, V> toMapOfSet();
}
