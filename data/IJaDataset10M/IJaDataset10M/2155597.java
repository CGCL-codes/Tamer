package org.op4j.operators.intf.arrayofarray;

import org.javaruntype.type.Type;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.IConvertibleOperator;
import org.op4j.operators.qualities.IEvaluableOperator;
import org.op4j.operators.qualities.IExecutableOperator;
import org.op4j.operators.qualities.INavigatingCollectionOperator;
import org.op4j.operators.qualities.ICastableToTypeOperator;
import org.op4j.operators.qualities.IUniqOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface ILevel2ArrayOfArrayElementsElementsOperator<T> extends IUniqOperator<T[][]>, INavigatingCollectionOperator<T>, IConvertibleOperator<T>, IEvaluableOperator<T>, IExecutableOperator<T>, ICastableToTypeOperator<T> {

    public ILevel1ArrayOfArrayElementsOperator<T> endFor();

    public <X> ILevel2ArrayOfArrayElementsElementsOperator<X> convert(final IConverter<X, ? super T> converter);

    public <X> ILevel2ArrayOfArrayElementsElementsOperator<X> eval(final IEvaluator<X, ? super T> eval);

    public <X> ILevel2ArrayOfArrayElementsElementsOperator<X> exec(final IFunction<X, ? super T> function);

    public <X> ILevel2ArrayOfArrayElementsElementsOperator<X> asType(final Type<X> type);

    public ILevel2ArrayOfArrayElementsElementsOperator<?> asUnknown();
}
