package org.op4j.operators.intf.listofset;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.ExecutableSetSelectedOperator;
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
public interface Level1ListOfSetSelectedElementsSelectedOperator<T, I> extends UniqOperator<List<Set<T>>, I>, NavigableCollectionOperator<T, I>, SortableOperator<T, I>, ExecutableSetSelectedOperator<T, I>, ReplaceableOperator<Set<T>, I>, SelectedOperator<Set<T>, I>, ModifiableCollectionOperator<T, I> {

    public Level1ListOfSetSelectedElementsOperator<T, I> endIf();

    public Level2ListOfSetSelectedElementsSelectedElementsOperator<T, I> forEach();

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> sort();

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> sort(final Comparator<? super T> comparator);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> add(final T newElement);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> addAll(final T... newElements);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> insert(final int position, final T newElement);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> insertAll(final int position, final T... newElements);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> addAll(final Collection<T> collection);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> removeAllIndexes(final int... indices);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> removeAllEqual(final T... values);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> removeAllTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> removeAllFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> removeAllNullOrFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> removeAllNotNullAndFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> removeAllNotNullAndTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> removeAllNullOrTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> removeAllIndexesNot(final int... indices);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> removeAllNull();

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> convert(final IConverter<? extends Set<? extends T>, ? super Set<T>> converter);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> eval(final IEvaluator<? extends Set<? extends T>, ? super Set<T>> eval);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> replaceWith(final Set<T> replacement);

    public Level1ListOfSetSelectedElementsSelectedOperator<T, I> exec(final IFunction<? extends Set<? extends T>, ? super Set<T>> function);
}
