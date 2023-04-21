package org.op4j.operators.intf.mapofarray;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import org.op4j.functions.IFunction;
import org.op4j.operators.qualities.DistinguishableOperator;
import org.op4j.operators.qualities.ExecutableArraySelectedOperator;
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
public interface Level2MapOfArrayEntriesValueSelectedOperator<K, V> extends UniqOperator<Map<K, V[]>>, NavigableCollectionOperator<V>, DistinguishableOperator, SortableOperator<V>, ExecutableArraySelectedOperator<V>, ReplaceableOperator<V[]>, ModifiableCollectionOperator<V>, SelectedOperator<V[]> {

    public Level2MapOfArrayEntriesValueOperator<K, V> endIf();

    public Level3MapOfArrayEntriesValueSelectedElementsOperator<K, V> forEach();

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> distinct();

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> sort();

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> sort(final Comparator<? super V> comparator);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> add(final V newElement);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> addAll(final V... newElements);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> insert(final int position, final V newElement);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> insertAll(final int position, final V... newElements);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> addAll(final Collection<V> collection);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> removeAllIndexes(final int... indexes);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> removeAllEqual(final V... values);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> removeAllTrue(final IFunction<Boolean, ? super V> eval);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> removeAllFalse(final IFunction<Boolean, ? super V> eval);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> removeAllNullOrFalse(final IFunction<Boolean, ? super V> eval);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> removeAllNotNullAndFalse(final IFunction<Boolean, ? super V> eval);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> removeAllNotNullAndTrue(final IFunction<Boolean, ? super V> eval);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> removeAllNullOrTrue(final IFunction<Boolean, ? super V> eval);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> removeAllIndexesNot(final int... indexes);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> removeAllNull();

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> execIfNotNullAsArray(final IFunction<? extends V[], ? super V[]> function);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> replaceWith(final V[] replacement);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> execAsArray(final IFunction<? extends V[], ? super V[]> function);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> map(final IFunction<? extends V, ? super V> function);

    public Level2MapOfArrayEntriesValueSelectedOperator<K, V> mapIfNotNull(final IFunction<? extends V, ? super V> function);
}
