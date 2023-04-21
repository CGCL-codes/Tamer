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
public interface Level4MapOfMapEntriesValueSelectedEntriesSelectedKeySelectedOperator<K1, K2, V, I> extends UniqOperator<Map<K1, Map<K2, V>>, I>, ExecutableSelectedOperator<K2, I>, ReplaceableOperator<K2, I>, SelectedOperator<K2, I> {

    public Level4MapOfMapEntriesValueSelectedEntriesSelectedKeyOperator<K1, K2, V, I> endIf();

    public Level4MapOfMapEntriesValueSelectedEntriesSelectedKeySelectedOperator<K1, K2, V, I> replaceWith(final K2 replacement);

    public Level4MapOfMapEntriesValueSelectedEntriesSelectedKeySelectedOperator<K1, K2, V, I> exec(final IFunction<? extends K2, ? super K2> function);

    public Level4MapOfMapEntriesValueSelectedEntriesSelectedKeySelectedOperator<K1, K2, V, I> eval(final IEvaluator<? extends K2, ? super K2> eval);

    public Level4MapOfMapEntriesValueSelectedEntriesSelectedKeySelectedOperator<K1, K2, V, I> convert(final IConverter<? extends K2, ? super K2> converter);
}
