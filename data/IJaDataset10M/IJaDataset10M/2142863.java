package org.op4j.operators.intf.array;

import org.op4j.functions.IFunction;
import org.op4j.operators.qualities.ExecutableSelectedOperator;
import org.op4j.operators.qualities.ReplaceableOperator;
import org.op4j.operators.qualities.SelectedOperator;
import org.op4j.operators.qualities.UniqOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface Level1ArraySelectedElementsSelectedOperator<T> extends UniqOperator<T[]>, SelectedOperator<T>, ExecutableSelectedOperator<T>, ReplaceableOperator<T> {

    public Level1ArraySelectedElementsOperator<T> endIf();

    public Level1ArraySelectedElementsSelectedOperator<T> execIfNotNull(final IFunction<? extends T, ? super T> function);

    public Level1ArraySelectedElementsSelectedOperator<T> replaceWith(final T replacement);

    public Level1ArraySelectedElementsSelectedOperator<T> exec(final IFunction<? extends T, ? super T> function);
}
