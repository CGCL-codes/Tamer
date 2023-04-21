package org.op4j.operators.intf.array;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import org.javaruntype.type.Type;
import org.op4j.functions.IFunction;
import org.op4j.operators.intf.generic.ILevel0GenericUniqOperator;
import org.op4j.operators.intf.list.ILevel0ListOperator;
import org.op4j.operators.intf.map.ILevel0MapOperator;
import org.op4j.operators.intf.set.ILevel0SetOperator;
import org.op4j.operators.qualities.CastableToArrayOperator;
import org.op4j.operators.qualities.ConvertibleToListOperator;
import org.op4j.operators.qualities.ConvertibleToMapOfArrayOperator;
import org.op4j.operators.qualities.ConvertibleToMapOperator;
import org.op4j.operators.qualities.ConvertibleToSetOperator;
import org.op4j.operators.qualities.DistinguishableOperator;
import org.op4j.operators.qualities.ExecutableArrayOperator;
import org.op4j.operators.qualities.GenerizableOperator;
import org.op4j.operators.qualities.ModifiableCollectionOperator;
import org.op4j.operators.qualities.NavigableCollectionOperator;
import org.op4j.operators.qualities.ReplaceableIfNullOperator;
import org.op4j.operators.qualities.ReplaceableOperator;
import org.op4j.operators.qualities.ReversibleOperator;
import org.op4j.operators.qualities.SelectableOperator;
import org.op4j.operators.qualities.SortableOperator;
import org.op4j.operators.qualities.TotalizableOperator;
import org.op4j.operators.qualities.UniqOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface ILevel0ArrayOperator<I, T> extends UniqOperator<T[]>, NavigableCollectionOperator<T>, DistinguishableOperator, SortableOperator<T>, CastableToArrayOperator, ModifiableCollectionOperator<T>, SelectableOperator<T[]>, ReplaceableOperator<T[]>, ReplaceableIfNullOperator<T[]>, GenerizableOperator<I, T[]>, ExecutableArrayOperator<T>, ConvertibleToListOperator, ConvertibleToSetOperator, ConvertibleToMapOperator<T>, ConvertibleToMapOfArrayOperator<T>, TotalizableOperator<I, T>, ReversibleOperator<T> {

    public ILevel0ArraySelectedOperator<I, T> ifTrue(final IFunction<? super T[], Boolean> eval);

    public ILevel0ArraySelectedOperator<I, T> ifFalse(final IFunction<? super T[], Boolean> eval);

    public ILevel0ArraySelectedOperator<I, T> ifNull();

    public ILevel0ArraySelectedOperator<I, T> ifNotNull();

    public ILevel1ArrayElementsOperator<I, T> forEach();

    public ILevel0ArrayOperator<I, T> distinct();

    public ILevel0ArrayOperator<I, T> sort();

    public ILevel0ArrayOperator<I, T> sort(final Comparator<? super T> comparator);

    public ILevel0ArrayOperator<I, T> sortBy(final IFunction<? super T, ?> by);

    public ILevel0ArrayOperator<I, T> add(final T newElement);

    public ILevel0ArrayOperator<I, T> addAll(final T... newElements);

    public ILevel0ArrayOperator<I, T> insert(final int position, final T newElement);

    public ILevel0ArrayOperator<I, T> insertAll(final int position, final T... newElements);

    public ILevel0ArrayOperator<I, T> addAll(final Collection<T> collection);

    public ILevel0ArrayOperator<I, T> removeAllIndexes(final int... indexes);

    public ILevel0ArrayOperator<I, T> removeAllEqual(final T... values);

    public ILevel0ArrayOperator<I, T> removeAllTrue(final IFunction<? super T, Boolean> eval);

    public ILevel0ArrayOperator<I, T> removeAllFalse(final IFunction<? super T, Boolean> eval);

    public ILevel0ArrayOperator<I, T> removeAllNullOrFalse(final IFunction<? super T, Boolean> eval);

    public ILevel0ArrayOperator<I, T> removeAllNotNullAndFalse(final IFunction<? super T, Boolean> eval);

    public ILevel0ArrayOperator<I, T> removeAllNotNullAndTrue(final IFunction<? super T, Boolean> eval);

    public ILevel0ArrayOperator<I, T> removeAllNullOrTrue(final IFunction<? super T, Boolean> eval);

    public ILevel0ArrayOperator<I, T> removeAllIndexesNot(final int... indexes);

    public ILevel0ArrayOperator<I, T> removeAllNull();

    public ILevel0ListOperator<I, T> toList();

    public ILevel0SetOperator<I, T> toSet();

    public <K, V> ILevel0MapOperator<I, K, V> toMap(final IFunction<? super T, Map.Entry<K, V>> mapBuilder);

    public <K, V> ILevel0MapOperator<I, K, V> toMap(final IFunction<? super T, K> keyFunction, final IFunction<? super T, V> valueFunction);

    public <K, V> ILevel0MapOperator<I, K, V[]> toGroupMapOf(final Type<V> valueType, final IFunction<? super T, Map.Entry<K, V>> mapBuilder);

    public <K, V> ILevel0MapOperator<I, K, V[]> toGroupMapOf(final Type<V> valueType, final IFunction<? super T, K> keyFunction, final IFunction<? super T, V> valueFunction);

    public <K> ILevel0MapOperator<I, K, T> zipKeys(final K... keys);

    public <V> ILevel0MapOperator<I, T, V> zipValues(final V... values);

    public <K> ILevel0MapOperator<I, K, T> zipKeysFrom(final Collection<K> keys);

    public <V> ILevel0MapOperator<I, T, V> zipValuesFrom(final Collection<V> values);

    public <K> ILevel0MapOperator<I, K, T> zipKeysFrom(final K[] keys);

    public <V> ILevel0MapOperator<I, T, V> zipValuesFrom(final V[] values);

    public <K> ILevel0MapOperator<I, K, T> zipKeysBy(final IFunction<? super T, K> keyEval);

    public <V> ILevel0MapOperator<I, T, V> zipValuesBy(final IFunction<? super T, V> valueEval);

    public <K> ILevel0MapOperator<I, K, T[]> zipAndGroupKeys(final K... keys);

    public <V> ILevel0MapOperator<I, T, V[]> zipAndGroupValues(final Type<V> valueType, final V... values);

    public <K> ILevel0MapOperator<I, K, T[]> zipAndGroupKeysFrom(final Collection<K> keys);

    public <V> ILevel0MapOperator<I, T, V[]> zipAndGroupValuesFrom(final Type<V> valueType, final Collection<V> values);

    public <K> ILevel0MapOperator<I, K, T[]> zipAndGroupKeysFrom(final K[] keys);

    public <V> ILevel0MapOperator<I, T, V[]> zipAndGroupValuesFrom(final Type<V> valueType, final V[] values);

    public <K> ILevel0MapOperator<I, K, T[]> zipAndGroupKeysBy(final IFunction<? super T, K> keyEval);

    public <V> ILevel0MapOperator<I, T, V[]> zipAndGroupValuesBy(final Type<V> valueType, final IFunction<? super T, V> valueEval);

    public ILevel0MapOperator<I, T, T> couple();

    public ILevel0MapOperator<I, T, T[]> coupleAndGroup();

    public ILevel0GenericUniqOperator<I, T[]> generic();

    public ILevel0ArrayOperator<I, T> replaceWith(final T[] replacement);

    public ILevel0ArrayOperator<I, T> replaceIfNullWith(final T[] replacement);

    public <X> ILevel0GenericUniqOperator<I, X> exec(final IFunction<? super T[], X> function);

    public ILevel0ArrayOperator<I, T> execAsArray(final IFunction<? super T[], ? extends T[]> function);

    public <X> ILevel0ArrayOperator<I, X> execAsArrayOf(final Type<X> type, final IFunction<? super T[], X[]> function);

    public ILevel0ArrayOperator<I, T> execIfNotNullAsArray(final IFunction<? super T[], ? extends T[]> function);

    public ILevel0ArrayOperator<I, T> execIfNullAsArray(final IFunction<? super T[], ? extends T[]> function);

    public ILevel0ArrayOperator<I, T> execIfTrueAsArray(final IFunction<? super T[], Boolean> eval, final IFunction<? super T[], ? extends T[]> function);

    public ILevel0ArrayOperator<I, T> execIfFalseAsArray(final IFunction<? super T[], Boolean> eval, final IFunction<? super T[], ? extends T[]> function);

    public <X> ILevel0ArrayOperator<I, X> execIfNotNullAsArrayOf(final Type<X> type, final IFunction<? super T[], X[]> function, final IFunction<? super T[], X[]> elseFunction);

    public <X> ILevel0ArrayOperator<I, X> execIfNullAsArrayOf(final Type<X> type, final IFunction<? super T[], X[]> function, final IFunction<? super T[], X[]> elseFunction);

    public <X> ILevel0ArrayOperator<I, X> execIfTrueAsArrayOf(final Type<X> type, final IFunction<? super T[], Boolean> eval, final IFunction<? super T[], X[]> function, final IFunction<? super T[], X[]> elseFunction);

    public <X> ILevel0ArrayOperator<I, X> execIfFalseAsArrayOf(final Type<X> type, final IFunction<? super T[], Boolean> eval, final IFunction<? super T[], X[]> function, final IFunction<? super T[], X[]> elseFunction);

    public ILevel0ArrayOperator<I, T> map(final IFunction<? super T, ? extends T> function);

    public <X> ILevel0ArrayOperator<I, X> map(final Type<X> type, final IFunction<? super T, X> function);

    public ILevel0ArrayOperator<I, T> mapIfNotNull(final IFunction<? super T, ? extends T> function);

    public ILevel0ArrayOperator<I, T> mapIfNull(final IFunction<? super T, ? extends T> function);

    public ILevel0ArrayOperator<I, T> mapIfTrue(final IFunction<? super T, Boolean> eval, final IFunction<? super T, ? extends T> function);

    public ILevel0ArrayOperator<I, T> mapIfFalse(final IFunction<? super T, Boolean> eval, final IFunction<? super T, ? extends T> function);

    public <X> ILevel0ArrayOperator<I, X> mapIfNotNull(final Type<X> type, final IFunction<? super T, X> function, final IFunction<? super T, X> elseFunction);

    public <X> ILevel0ArrayOperator<I, X> mapIfNull(final Type<X> type, final IFunction<? super T, X> function, final IFunction<? super T, X> elseFunction);

    public <X> ILevel0ArrayOperator<I, X> mapIfTrue(final Type<X> type, final IFunction<? super T, Boolean> eval, final IFunction<? super T, X> function, final IFunction<? super T, X> elseFunction);

    public <X> ILevel0ArrayOperator<I, X> mapIfFalse(final Type<X> type, final IFunction<? super T, Boolean> eval, final IFunction<? super T, X> function, final IFunction<? super T, X> elseFunction);

    public <X> ILevel0ArrayOperator<I, X> of(final Type<X> type);

    public <X> ILevel0ArrayOperator<I, X> castToArrayOf(final Type<X> type);

    public ILevel0GenericUniqOperator<I, Boolean> any(final IFunction<? super T, Boolean> eval);

    public ILevel0GenericUniqOperator<I, Boolean> all(final IFunction<? super T, Boolean> eval);

    public ILevel0ArrayOperator<I, T> reverse();
}
