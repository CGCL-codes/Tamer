package org.op4j.operators.intf.setofmap;

import java.util.Map;
import java.util.Set;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.ExecutableMapEntrySelectedOperator;
import org.op4j.operators.qualities.NavigableMapEntryOperator;
import org.op4j.operators.qualities.SelectedMapEntryOperator;
import org.op4j.operators.qualities.UniqOperator;
import org.op4j.operators.qualities.ReplaceableOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface Level2SetOfMapElementsSelectedEntriesSelectedOperator<K, V, I> extends UniqOperator<Set<Map<K, V>>, I>, NavigableMapEntryOperator<I>, ExecutableMapEntrySelectedOperator<K, V, I>, ReplaceableOperator<Map.Entry<K, V>, I>, SelectedMapEntryOperator<K, V, I> {

    public Level2SetOfMapElementsSelectedEntriesOperator<K, V, I> endIf();

    public Level3SetOfMapElementsSelectedEntriesSelectedKeyOperator<K, V, I> onKey();

    public Level3SetOfMapElementsSelectedEntriesSelectedValueOperator<K, V, I> onValue();

    public Level2SetOfMapElementsSelectedEntriesSelectedOperator<K, V, I> replaceWith(final Map.Entry<K, V> replacement);

    public Level2SetOfMapElementsSelectedEntriesSelectedOperator<K, V, I> exec(final IFunction<? extends Map.Entry<? extends K, ? extends V>, ? super Map.Entry<K, V>> function);

    public Level2SetOfMapElementsSelectedEntriesSelectedOperator<K, V, I> eval(final IEvaluator<? extends Map.Entry<? extends K, ? extends V>, ? super Map.Entry<K, V>> eval);

    public Level2SetOfMapElementsSelectedEntriesSelectedOperator<K, V, I> convert(final IConverter<? extends Map.Entry<? extends K, ? extends V>, ? super Map.Entry<K, V>> converter);
}
