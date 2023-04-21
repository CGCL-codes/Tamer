package org.op4j.operators.intf.setofmap;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.ExecutableMapSelectedOperator;
import org.op4j.operators.qualities.ModifiableMapOperator;
import org.op4j.operators.qualities.NavigableMapOperator;
import org.op4j.operators.qualities.SelectedOperator;
import org.op4j.operators.qualities.SortableOperator;
import org.op4j.operators.qualities.UniqOperator;
import org.op4j.operators.qualities.ReplaceableOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> extends UniqOperator<Set<Map<K, V>>, I>, NavigableMapOperator<K, V, I>, SortableOperator<Map.Entry<K, V>, I>, ExecutableMapSelectedOperator<K, V, I>, ReplaceableOperator<Map<K, V>, I>, SelectedOperator<Map<K, V>, I>, ModifiableMapOperator<K, V, I> {

    public Level1SetOfMapSelectedElementsOperator<K, V, I> endIf();

    public Level2SetOfMapSelectedElementsSelectedEntriesOperator<K, V, I> forEachEntry();

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> sort();

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> sort(final Comparator<? super Map.Entry<K, V>> comparator);

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> put(final K newKey, final V newValue);

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> insert(final int position, final K newKey, final V newValue);

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> putAll(final Map<K, V> map);

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> insertAll(final int position, final Map<K, V> map);

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> removeAllKeys(final K... keys);

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> removeAllTrue(final IEvaluator<Boolean, ? super Map.Entry<K, V>> eval);

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> removeAllFalse(final IEvaluator<Boolean, ? super Map.Entry<K, V>> eval);

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> removeAllKeysNot(final K... keys);

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> convert(final IConverter<? extends Map<? extends K, ? extends V>, ? super Map<K, V>> converter);

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> eval(final IEvaluator<? extends Map<? extends K, ? extends V>, ? super Map<K, V>> eval);

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> replaceWith(final Map<K, V> replacement);

    public Level1SetOfMapSelectedElementsSelectedOperator<K, V, I> exec(final IFunction<? extends Map<? extends K, ? extends V>, ? super Map<K, V>> function);
}
