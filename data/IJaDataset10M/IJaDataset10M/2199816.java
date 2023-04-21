package org.op4j.operators.qualities;

import java.util.Map;
import org.javaruntype.type.Type;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.intf.generic.Level0GenericUniqOperator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface ExecutableArrayOfMapOperator<K, V, I> {

    public <X, Y> ExecutableArrayOfMapOperator<X, Y, I> exec(final IFunction<? extends Map<X, Y>[], ? super Map<K, V>[]> function);

    public <X, Y> ExecutableArrayOfMapOperator<X, Y, I> eval(final IEvaluator<? extends Map<X, Y>[], ? super Map<K, V>[]> eval);

    public <X, Y> ExecutableArrayOfMapOperator<X, Y, I> convert(final IConverter<? extends Map<X, Y>[], ? super Map<K, V>[]> converter);

    public <X> Level0GenericUniqOperator<X, I> exec(final Type<X> resultType, final IFunction<? extends X, ? super Map<K, V>[]> function);

    public <X> Level0GenericUniqOperator<X, I> eval(final Type<X> resultType, final IEvaluator<? extends X, ? super Map<K, V>[]> eval);

    public <X> Level0GenericUniqOperator<X, I> convert(final Type<X> resultType, final IConverter<? extends X, ? super Map<K, V>[]> converter);
}
