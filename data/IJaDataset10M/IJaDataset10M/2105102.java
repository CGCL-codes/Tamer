package org.op4j.operators.intf.mapofset;

import java.util.Map;
import java.util.Set;
import org.op4j.functions.IFunction;
import org.op4j.operators.qualities.ExecutableSelectedOperator;
import org.op4j.operators.qualities.ReplaceableOperator;
import org.op4j.operators.qualities.SelectedOperator;
import org.op4j.operators.qualities.UniqOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface Level3MapOfSetEntriesSelectedValueElementsSelectedOperator<K, V> extends UniqOperator<Map<K, Set<V>>>, ExecutableSelectedOperator<V>, ReplaceableOperator<V>, SelectedOperator<V> {

    public Level3MapOfSetEntriesSelectedValueElementsOperator<K, V> endIf();

    public Level3MapOfSetEntriesSelectedValueElementsSelectedOperator<K, V> execIfNotNull(final IFunction<? extends V, ? super V> function);

    public Level3MapOfSetEntriesSelectedValueElementsSelectedOperator<K, V> replaceWith(final V replacement);

    public Level3MapOfSetEntriesSelectedValueElementsSelectedOperator<K, V> exec(final IFunction<? extends V, ? super V> function);
}
