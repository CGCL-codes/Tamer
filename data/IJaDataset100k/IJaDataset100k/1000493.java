package org.op4j.operators.intf.listoflist;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.DistinguishableOperator;
import org.op4j.operators.qualities.ExecutableListSelectedOperator;
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
public interface Level1ListOfListSelectedElementsSelectedOperator<T, I> extends UniqOperator<List<List<T>>, I>, NavigableCollectionOperator<T, I>, DistinguishableOperator<I>, SortableOperator<T, I>, ExecutableListSelectedOperator<T, I>, ReplaceableOperator<List<T>, I>, SelectedOperator<List<T>, I>, ModifiableCollectionOperator<T, I> {

    public Level1ListOfListSelectedElementsOperator<T, I> endIf();

    public Level2ListOfListSelectedElementsSelectedElementsOperator<T, I> forEach();

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> distinct();

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> sort();

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> sort(final Comparator<? super T> comparator);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> add(final T newElement);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> addAll(final T... newElements);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> insert(final int position, final T newElement);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> insertAll(final int position, final T... newElements);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> addAll(final Collection<T> collection);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> removeAllIndexes(final int... indices);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> removeAllEqual(final T... values);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> removeAllTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> removeAllFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> removeAllNullOrFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> removeAllNotNullAndFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> removeAllNotNullAndTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> removeAllNullOrTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> removeAllIndexesNot(final int... indices);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> removeAllNull();

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> convert(final IConverter<? extends List<? extends T>, ? super List<T>> converter);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> eval(final IEvaluator<? extends List<? extends T>, ? super List<T>> eval);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> replaceWith(final List<T> replacement);

    public Level1ListOfListSelectedElementsSelectedOperator<T, I> exec(final IFunction<? extends List<? extends T>, ? super List<T>> function);
}
