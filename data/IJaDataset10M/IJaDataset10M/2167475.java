package org.op4j.operators.intf.arrayofmap;

import java.util.Map;
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
public interface Level2ArrayOfMapSelectedElementsEntriesSelectedOperator<K, V, I> extends UniqOperator<Map<K, V>[], I>, NavigableMapEntryOperator<I>, ExecutableMapEntrySelectedOperator<K, V, I>, ReplaceableOperator<Map.Entry<K, V>, I>, SelectedMapEntryOperator<K, V, I> {

    public Level2ArrayOfMapSelectedElementsEntriesOperator<K, V, I> endIf();

    public Level3ArrayOfMapSelectedElementsEntriesSelectedKeyOperator<K, V, I> onKey();

    public Level3ArrayOfMapSelectedElementsEntriesSelectedValueOperator<K, V, I> onValue();

    public Level2ArrayOfMapSelectedElementsEntriesSelectedOperator<K, V, I> replaceWith(final Map.Entry<K, V> replacement);

    public Level2ArrayOfMapSelectedElementsEntriesSelectedOperator<K, V, I> exec(final IFunction<? extends Map.Entry<? extends K, ? extends V>, ? super Map.Entry<K, V>> function);

    public Level2ArrayOfMapSelectedElementsEntriesSelectedOperator<K, V, I> eval(final IEvaluator<? extends Map.Entry<? extends K, ? extends V>, ? super Map.Entry<K, V>> eval);

    public Level2ArrayOfMapSelectedElementsEntriesSelectedOperator<K, V, I> convert(final IConverter<? extends Map.Entry<? extends K, ? extends V>, ? super Map.Entry<K, V>> converter);
}
