package org.op4j.operators.intf.mapoflist;

import java.util.List;
import java.util.Map;
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
public interface Level3MapOfListSelectedEntriesSelectedValueSelectedElementsSelectedOperator<K, V> extends UniqOperator<Map<K, List<V>>>, ExecutableSelectedOperator<V>, ReplaceableOperator<V>, SelectedOperator<V> {

    public Level3MapOfListSelectedEntriesSelectedValueSelectedElementsOperator<K, V> endIf();

    public Level3MapOfListSelectedEntriesSelectedValueSelectedElementsSelectedOperator<K, V> execIfNotNull(final IFunction<? extends V, ? super V> function);

    public Level3MapOfListSelectedEntriesSelectedValueSelectedElementsSelectedOperator<K, V> replaceWith(final V replacement);

    public Level3MapOfListSelectedEntriesSelectedValueSelectedElementsSelectedOperator<K, V> exec(final IFunction<? extends V, ? super V> function);
}
