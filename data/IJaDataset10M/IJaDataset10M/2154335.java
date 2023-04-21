package org.op4j.operators.intf.mapofmap;

import java.util.Map;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.ExecutableMapEntrySelectedOperator;
import org.op4j.operators.qualities.NavigableMapEntryOperator;
import org.op4j.operators.qualities.ReplaceableOperator;
import org.op4j.operators.qualities.SelectedMapEntryOperator;
import org.op4j.operators.qualities.UniqOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface Level3MapOfMapEntriesValueEntriesSelectedOperator<K1, K2, V, I> extends UniqOperator<Map<K1, Map<K2, V>>, I>, NavigableMapEntryOperator<I>, ExecutableMapEntrySelectedOperator<K2, V, I>, ReplaceableOperator<Map.Entry<K2, V>, I>, SelectedMapEntryOperator<K2, V, I> {

    public Level3MapOfMapEntriesValueEntriesOperator<K1, K2, V, I> endIf();

    public Level4MapOfMapEntriesValueEntriesSelectedKeyOperator<K1, K2, V, I> onKey();

    public Level4MapOfMapEntriesValueEntriesSelectedValueOperator<K1, K2, V, I> onValue();

    public Level3MapOfMapEntriesValueEntriesSelectedOperator<K1, K2, V, I> replaceWith(final Map.Entry<K2, V> replacement);

    public Level3MapOfMapEntriesValueEntriesSelectedOperator<K1, K2, V, I> exec(final IFunction<? extends Map.Entry<? extends K2, ? extends V>, ? super Map.Entry<K2, V>> function);

    public Level3MapOfMapEntriesValueEntriesSelectedOperator<K1, K2, V, I> eval(final IEvaluator<? extends Map.Entry<? extends K2, ? extends V>, ? super Map.Entry<K2, V>> eval);

    public Level3MapOfMapEntriesValueEntriesSelectedOperator<K1, K2, V, I> convert(final IConverter<? extends Map.Entry<? extends K2, ? extends V>, ? super Map.Entry<K2, V>> converter);
}
