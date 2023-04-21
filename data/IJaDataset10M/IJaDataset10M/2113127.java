package org.op4j.operators.intf.mapoflist;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.op4j.functions.IFunction;
import org.op4j.operators.qualities.DistinguishableOperator;
import org.op4j.operators.qualities.ExecutableListSelectedOperator;
import org.op4j.operators.qualities.ModifiableCollectionOperator;
import org.op4j.operators.qualities.NavigableCollectionOperator;
import org.op4j.operators.qualities.ReplaceableOperator;
import org.op4j.operators.qualities.SelectedOperator;
import org.op4j.operators.qualities.SortableOperator;
import org.op4j.operators.qualities.UniqOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> extends UniqOperator<Map<K, List<V>>>, NavigableCollectionOperator<V>, DistinguishableOperator, SortableOperator<V>, ExecutableListSelectedOperator<V>, ReplaceableOperator<List<V>>, ModifiableCollectionOperator<V>, SelectedOperator<List<V>> {

    public Level2MapOfListSelectedEntriesValueOperator<K, V> endIf();

    public Level3MapOfListSelectedEntriesValueSelectedElementsOperator<K, V> forEach();

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> distinct();

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> sort();

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> sort(final Comparator<? super V> comparator);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> add(final V newElement);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> addAll(final V... newElements);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> insert(final int position, final V newElement);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> insertAll(final int position, final V... newElements);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> addAll(final Collection<V> collection);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> removeAllIndexes(final int... indexes);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> removeAllEqual(final V... values);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> removeAllTrue(final IFunction<Boolean, ? super V> eval);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> removeAllFalse(final IFunction<Boolean, ? super V> eval);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> removeAllNullOrFalse(final IFunction<Boolean, ? super V> eval);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> removeAllNotNullAndFalse(final IFunction<Boolean, ? super V> eval);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> removeAllNotNullAndTrue(final IFunction<Boolean, ? super V> eval);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> removeAllNullOrTrue(final IFunction<Boolean, ? super V> eval);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> removeAllIndexesNot(final int... indexes);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> removeAllNull();

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> execIfNotNullAsList(final IFunction<? extends List<? extends V>, ? super List<V>> function);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> replaceWith(final List<V> replacement);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> execAsList(final IFunction<? extends List<? extends V>, ? super List<V>> function);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> map(final IFunction<? extends V, ? super V> function);

    public Level2MapOfListSelectedEntriesValueSelectedOperator<K, V> mapIfNotNull(final IFunction<? extends V, ? super V> function);
}
