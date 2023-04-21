package org.op4j.operators.intf.arrayoflist;

import java.util.List;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.ExecutableSelectedOperator;
import org.op4j.operators.qualities.NavigatingCollectionOperator;
import org.op4j.operators.qualities.SelectableOperator;
import org.op4j.operators.qualities.UniqOperator;
import org.op4j.operators.qualities.ReplaceableOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface Level2ArrayOfListElementsSelectedElementsOperator<T, I> extends UniqOperator<List<T>[], I>, ExecutableSelectedOperator<T, I>, ReplaceableOperator<T, I>, NavigatingCollectionOperator<T, I>, SelectableOperator<T, I> {

    public Level1ArrayOfListElementsSelectedOperator<T, I> endFor();

    public Level2ArrayOfListElementsSelectedElementsSelectedOperator<T, I> ifIndex(final int... indices);

    public Level2ArrayOfListElementsSelectedElementsSelectedOperator<T, I> ifTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level2ArrayOfListElementsSelectedElementsSelectedOperator<T, I> ifFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level2ArrayOfListElementsSelectedElementsSelectedOperator<T, I> ifNullOrFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level2ArrayOfListElementsSelectedElementsSelectedOperator<T, I> ifNotNullAndFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level2ArrayOfListElementsSelectedElementsSelectedOperator<T, I> ifNull();

    public Level2ArrayOfListElementsSelectedElementsSelectedOperator<T, I> ifNullOrTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level2ArrayOfListElementsSelectedElementsSelectedOperator<T, I> ifIndexNot(final int... indices);

    public Level2ArrayOfListElementsSelectedElementsSelectedOperator<T, I> ifNotNull();

    public Level2ArrayOfListElementsSelectedElementsSelectedOperator<T, I> ifNotNullAndTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level2ArrayOfListElementsSelectedElementsOperator<T, I> convert(final IConverter<? extends T, ? super T> converter);

    public Level2ArrayOfListElementsSelectedElementsOperator<T, I> eval(final IEvaluator<? extends T, ? super T> eval);

    public Level2ArrayOfListElementsSelectedElementsOperator<T, I> replaceWith(final T replacement);

    public Level2ArrayOfListElementsSelectedElementsOperator<T, I> exec(final IFunction<? extends T, ? super T> function);
}
