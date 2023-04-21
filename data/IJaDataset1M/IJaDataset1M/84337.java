package org.op4j.operators.intf.setoflist;

import java.util.List;
import java.util.Set;
import org.javaruntype.type.Type;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.qualities.CastableToTypeOperator;
import org.op4j.operators.qualities.ExecutableOperator;
import org.op4j.operators.qualities.NavigatingCollectionOperator;
import org.op4j.operators.qualities.ReplaceableIfNullOperator;
import org.op4j.operators.qualities.ReplaceableOperator;
import org.op4j.operators.qualities.SelectableOperator;
import org.op4j.operators.qualities.UniqOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface Level2SetOfListElementsElementsOperator<T, I> extends UniqOperator<Set<List<T>>, I>, NavigatingCollectionOperator<T, I>, ExecutableOperator<T, I>, SelectableOperator<T, I>, ReplaceableOperator<T, I>, ReplaceableIfNullOperator<T, I>, CastableToTypeOperator<T, I> {

    public Level2SetOfListElementsElementsSelectedOperator<T, I> ifIndex(final int... indices);

    public Level2SetOfListElementsElementsSelectedOperator<T, I> ifTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level2SetOfListElementsElementsSelectedOperator<T, I> ifFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level2SetOfListElementsElementsSelectedOperator<T, I> ifNullOrFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level2SetOfListElementsElementsSelectedOperator<T, I> ifNotNullAndFalse(final IEvaluator<Boolean, ? super T> eval);

    public Level2SetOfListElementsElementsSelectedOperator<T, I> ifNull();

    public Level2SetOfListElementsElementsSelectedOperator<T, I> ifNullOrTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level2SetOfListElementsElementsSelectedOperator<T, I> ifIndexNot(final int... indices);

    public Level2SetOfListElementsElementsSelectedOperator<T, I> ifNotNull();

    public Level2SetOfListElementsElementsSelectedOperator<T, I> ifNotNullAndTrue(final IEvaluator<Boolean, ? super T> eval);

    public Level1SetOfListElementsOperator<T, I> endFor();

    public Level2SetOfListElementsElementsOperator<T, I> replaceWith(final T replacement);

    public Level2SetOfListElementsElementsOperator<T, I> replaceIfNullWith(final T replacement);

    public <X> Level2SetOfListElementsElementsOperator<X, I> convert(final IConverter<X, ? super T> converter);

    public <X> Level2SetOfListElementsElementsOperator<X, I> eval(final IEvaluator<X, ? super T> eval);

    public <X> Level2SetOfListElementsElementsOperator<X, I> exec(final IFunction<X, ? super T> function);

    public <X> Level2SetOfListElementsElementsOperator<X, I> asType(final Type<X> type);

    public Level2SetOfListElementsElementsOperator<?, I> asUnknown();
}
