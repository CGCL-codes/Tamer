package org.op4j.operators.intf.arrayofset;

import java.util.Set;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.ExecutableSelectedOperator;
import org.op4j.operators.qualities.SelectedOperator;
import org.op4j.operators.qualities.UniqOperator;
import org.op4j.operators.qualities.ReplaceableOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface Level2ArrayOfSetSelectedElementsElementsSelectedOperator<T, I> extends UniqOperator<Set<T>[], I>, ExecutableSelectedOperator<T, I>, ReplaceableOperator<T, I>, SelectedOperator<T, I> {

    public Level2ArrayOfSetSelectedElementsElementsOperator<T, I> endIf();

    public Level2ArrayOfSetSelectedElementsElementsSelectedOperator<T, I> convert(final IConverter<? extends T, ? super T> converter);

    public Level2ArrayOfSetSelectedElementsElementsSelectedOperator<T, I> eval(final IEvaluator<? extends T, ? super T> eval);

    public Level2ArrayOfSetSelectedElementsElementsSelectedOperator<T, I> replaceWith(final T replacement);

    public Level2ArrayOfSetSelectedElementsElementsSelectedOperator<T, I> exec(final IFunction<? extends T, ? super T> function);
}
