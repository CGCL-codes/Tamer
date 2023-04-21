package org.op4j.operators.intf.setofarray;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import org.javaruntype.type.Type;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.DistinguishableOperator;
import org.op4j.operators.qualities.ExecutableArraySelectedOperator;
import org.op4j.operators.qualities.ModifiableCollectionOperator;
import org.op4j.operators.qualities.NavigableArrayOperator;
import org.op4j.operators.qualities.NavigatingCollectionOperator;
import org.op4j.operators.qualities.SelectableOperator;
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
public interface Level1SetOfArraySelectedElementsOperator<T, I> extends UniqOperator<Set<T[]>, I>, NavigableArrayOperator<T, I>, DistinguishableOperator<I>, SortableOperator<T, I>, ExecutableArraySelectedOperator<T, I>, ReplaceableOperator<T[], I>, NavigatingCollectionOperator<T[], I>, ModifiableCollectionOperator<T, I>, SelectableOperator<T[], I> {

    public Level0SetOfArraySelectedOperator<T, I> endFor();

    public Level1SetOfArraySelectedElementsSelectedOperator<T, I> ifIndex(final int... indices);

    public Level1SetOfArraySelectedElementsSelectedOperator<T, I> ifTrue(final IEvaluator<Boolean, ? super T[]> eval);

    public Level1SetOfArraySelectedElementsSelectedOperator<T, I> ifFalse(final IEvaluator<Boolean, ? super T[]> eval);

    public Level1SetOfArraySelectedElementsSelectedOperator<T, I> ifNullOrFalse(final IEvaluator<Boolean, ? super T[]> eval);

    public Level1SetOfArraySelectedElementsSelectedOperator<T, I> ifNotNullAndFalse(final IEvaluator<Boolean, ? super T[]> eval);

    public Level1SetOfArraySelectedElementsSelectedOperator<T, I> ifNull();

    public Level1SetOfArraySelectedElementsSelectedOperator<T, I> ifNullOrTrue(final IEvaluator<Boolean, ? super T[]> eval);

    public Level1SetOfArraySelectedElementsSelectedOperator<T, I> ifIndexNot(final int... indices);

    public Level1SetOfArraySelectedElementsSelectedOperator<T, I> ifNotNull();

    public Level1SetOfArraySelectedElementsSelectedOperator<T, I> ifNotNullAndTrue(final IEvaluator<Boolean, ? super T[]> eval);

    public Level2SetOfArraySelectedElementsElementsOperator<T, I> forEach(final Type<T> elementType);

    public Level1SetOfArraySelectedElementsOperator<T, I> distinct();

    public Level1SetOfArraySelectedElementsOperator<T, I> sort();

    public Level1SetOfArraySelectedElementsOperator<T, I> sort(final Comparator<? super T> comparator);

    public Level1SetOfArraySelectedElementsOperator<T, I> add(final T newElement);

    public Level1SetOfArraySelectedElementsOperator<T, I> addAll(final T... newElements);

    public Level1SetOfArraySelectedElementsOperator<T, I> insert(final int position, final T newElement);

    public Level1SetOfArraySelectedElementsOperator<T, I> insertAll(final int position, final T... newElements);

    public Level1SetOfArraySelectedElementsOperator<T, I> addAll(final Collection<T> collection);

    public Level1SetOfArraySelectedElementsOperator<T, I> removeAllIndexes(final int... indices);

    public Level1SetOfArraySelectedElementsOperator<T, I> removeAllEqual(final T... values);

    public Level1SetOfArraySelectedElementsOperator<T, I> removeAllTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level1SetOfArraySelectedElementsOperator<T, I> removeAllFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level1SetOfArraySelectedElementsOperator<T, I> removeAllNullOrFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level1SetOfArraySelectedElementsOperator<T, I> removeAllNotNullAndFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level1SetOfArraySelectedElementsOperator<T, I> removeAllNotNullAndTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level1SetOfArraySelectedElementsOperator<T, I> removeAllNullOrTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level1SetOfArraySelectedElementsOperator<T, I> removeAllIndexesNot(final int... indices);

    public Level1SetOfArraySelectedElementsOperator<T, I> removeAllNull();

    public Level1SetOfArraySelectedElementsOperator<T, I> convert(final IConverter<? extends T[], ? super T[]> converter);

    public Level1SetOfArraySelectedElementsOperator<T, I> eval(final IEvaluator<? extends T[], ? super T[]> eval);

    public Level1SetOfArraySelectedElementsOperator<T, I> replaceWith(final T[] replacement);

    public Level1SetOfArraySelectedElementsOperator<T, I> exec(final IFunction<? extends T[], ? super T[]> function);
}
