package org.op4j.operators.qualities;

import org.op4j.operators.intf.setoflist.Level0SetOfListOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface ConvertibleToSetOfListOperator<T, I> {

    public Level0SetOfListOperator<T, I> toSetOfList();
}
