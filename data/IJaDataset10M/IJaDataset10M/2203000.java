package org.op4j.operators.intf.mapofmap;

import java.util.Map;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
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
public interface Level4MapOfMapEntriesValueSelectedEntriesValueSelectedOperator<K1, K2, V, I> extends UniqOperator<Map<K1, Map<K2, V>>, I>, ExecutableSelectedOperator<V, I>, ReplaceableOperator<V, I>, SelectedOperator<V, I> {

    public Level4MapOfMapEntriesValueSelectedEntriesValueOperator<K1, K2, V, I> endIf();

    public Level4MapOfMapEntriesValueSelectedEntriesValueSelectedOperator<K1, K2, V, I> convert(final IConverter<? extends V, ? super V> converter);

    public Level4MapOfMapEntriesValueSelectedEntriesValueSelectedOperator<K1, K2, V, I> eval(final IEvaluator<? extends V, ? super V> eval);

    public Level4MapOfMapEntriesValueSelectedEntriesValueSelectedOperator<K1, K2, V, I> replaceWith(final V replacement);

    public Level4MapOfMapEntriesValueSelectedEntriesValueSelectedOperator<K1, K2, V, I> exec(final IFunction<? extends V, ? super V> function);
}
