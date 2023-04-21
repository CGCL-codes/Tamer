package org.op4j.operators.intf.setofset;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.ExecutableSetOfSetSelectedOperator;
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
public interface Level0SetOfSetSelectedOperator<T, I> extends UniqOperator<Set<Set<T>>, I>, NavigableCollectionOperator<Set<T>, I>, SortableOperator<Set<T>, I>, ModifiableCollectionOperator<Set<T>, I>, ExecutableSetOfSetSelectedOperator<T, I>, ReplaceableOperator<Set<Set<T>>, I>, SelectedOperator<Set<Set<T>>, I> {

    public Level0SetOfSetOperator<T, I> endIf();

    public Level1SetOfSetSelectedElementsOperator<T, I> forEach();

    public Level0SetOfSetSelectedOperator<T, I> sort();

    public Level0SetOfSetSelectedOperator<T, I> sort(final Comparator<? super Set<T>> comparator);

    public Level0SetOfSetSelectedOperator<T, I> add(final Set<T> newElement);

    public Level0SetOfSetSelectedOperator<T, I> addAll(final Set<T>... newElements);

    public Level0SetOfSetSelectedOperator<T, I> insert(final int position, final Set<T> newElement);

    public Level0SetOfSetSelectedOperator<T, I> insertAll(final int position, final Set<T>... newElements);

    public Level0SetOfSetSelectedOperator<T, I> addAll(final Collection<Set<T>> collection);

    public Level0SetOfSetSelectedOperator<T, I> removeAllIndexes(final int... indices);

    public Level0SetOfSetSelectedOperator<T, I> removeAllEqual(final Set<T>... values);

    public Level0SetOfSetSelectedOperator<T, I> removeAllTrue(final IEvaluator<Boolean, ? super Set<T>> eval);

    public Level0SetOfSetSelectedOperator<T, I> removeAllFalse(final IEvaluator<Boolean, ? super Set<T>> eval);

    public Level0SetOfSetSelectedOperator<T, I> removeAllNullOrFalse(final IEvaluator<Boolean, ? super Set<T>> eval);

    public Level0SetOfSetSelectedOperator<T, I> removeAllNotNullAndFalse(final IEvaluator<Boolean, ? super Set<T>> eval);

    public Level0SetOfSetSelectedOperator<T, I> removeAllNotNullAndTrue(final IEvaluator<Boolean, ? super Set<T>> eval);

    public Level0SetOfSetSelectedOperator<T, I> removeAllNullOrTrue(final IEvaluator<Boolean, ? super Set<T>> eval);

    public Level0SetOfSetSelectedOperator<T, I> removeAllIndexesNot(final int... indices);

    public Level0SetOfSetSelectedOperator<T, I> removeAllNull();

    public Level0SetOfSetSelectedOperator<T, I> convert(final IConverter<? extends Set<? extends Set<? extends T>>, ? super Set<Set<T>>> converter);

    public Level0SetOfSetSelectedOperator<T, I> eval(final IEvaluator<? extends Set<? extends Set<? extends T>>, ? super Set<Set<T>>> eval);

    public Level0SetOfSetSelectedOperator<T, I> replaceWith(final Set<Set<T>> replacement);

    public Level0SetOfSetSelectedOperator<T, I> exec(final IFunction<? extends Set<? extends Set<? extends T>>, ? super Set<Set<T>>> function);
}
