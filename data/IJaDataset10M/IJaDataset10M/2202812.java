package org.op4j.operators.intf.arrayoflist;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.DistinguishableOperator;
import org.op4j.operators.qualities.ExecutableArrayOfListSelectedOperator;
import org.op4j.operators.qualities.ModifiableCollectionOperator;
import org.op4j.operators.qualities.NavigableCollectionOperator;
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
public interface Level0ArrayOfListSelectedOperator<T, I> extends UniqOperator<List<T>[], I>, NavigableCollectionOperator<List<T>, I>, DistinguishableOperator<I>, SortableOperator<List<T>, I>, ModifiableCollectionOperator<List<T>, I>, ExecutableArrayOfListSelectedOperator<T, I>, ReplaceableOperator<List<T>[], I>, SelectedOperator<List<T>[], I> {

    public Level0ArrayOfListOperator<T, I> endIf();

    public Level1ArrayOfListSelectedElementsOperator<T, I> forEach();

    public Level0ArrayOfListSelectedOperator<T, I> distinct();

    public Level0ArrayOfListSelectedOperator<T, I> sort();

    public Level0ArrayOfListSelectedOperator<T, I> sort(final Comparator<? super List<T>> comparator);

    public Level0ArrayOfListSelectedOperator<T, I> add(final List<T> newElement);

    public Level0ArrayOfListSelectedOperator<T, I> addAll(final List<T>... newElements);

    public Level0ArrayOfListSelectedOperator<T, I> insert(final int position, final List<T> newElement);

    public Level0ArrayOfListSelectedOperator<T, I> insertAll(final int position, final List<T>... newElements);

    public Level0ArrayOfListSelectedOperator<T, I> addAll(final Collection<List<T>> collection);

    public Level0ArrayOfListSelectedOperator<T, I> removeAllIndexes(final int... indices);

    public Level0ArrayOfListSelectedOperator<T, I> removeAllEqual(final List<T>... values);

    public Level0ArrayOfListSelectedOperator<T, I> removeAllTrue(final IEvaluator<Boolean, ? super List<T>> eval);

    public Level0ArrayOfListSelectedOperator<T, I> removeAllFalse(final IEvaluator<Boolean, ? super List<T>> eval);

    public Level0ArrayOfListSelectedOperator<T, I> removeAllNullOrFalse(final IEvaluator<Boolean, ? super List<T>> eval);

    public Level0ArrayOfListSelectedOperator<T, I> removeAllNotNullAndFalse(final IEvaluator<Boolean, ? super List<T>> eval);

    public Level0ArrayOfListSelectedOperator<T, I> removeAllNotNullAndTrue(final IEvaluator<Boolean, ? super List<T>> eval);

    public Level0ArrayOfListSelectedOperator<T, I> removeAllNullOrTrue(final IEvaluator<Boolean, ? super List<T>> eval);

    public Level0ArrayOfListSelectedOperator<T, I> removeAllIndexesNot(final int... indices);

    public Level0ArrayOfListSelectedOperator<T, I> removeAllNull();

    public Level0ArrayOfListSelectedOperator<T, I> convert(final IConverter<? extends List<? extends T>[], ? super List<T>[]> converter);

    public Level0ArrayOfListSelectedOperator<T, I> eval(final IEvaluator<? extends List<? extends T>[], ? super List<T>[]> eval);

    public Level0ArrayOfListSelectedOperator<T, I> replaceWith(final List<T>[] replacement);

    public Level0ArrayOfListSelectedOperator<T, I> exec(final IFunction<? extends List<? extends T>[], ? super List<T>[]> function);
}
