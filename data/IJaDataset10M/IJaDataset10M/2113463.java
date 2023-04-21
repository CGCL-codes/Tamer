package org.op4j.operators.qualities;

import java.util.Set;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface ExecutableSetOfSetSelectedOperator<T, I> {

    public ExecutableSetOfSetSelectedOperator<T, I> exec(final IFunction<? extends Set<? extends Set<? extends T>>, ? super Set<Set<T>>> function);

    public ExecutableSetOfSetSelectedOperator<T, I> eval(final IEvaluator<? extends Set<? extends Set<? extends T>>, ? super Set<Set<T>>> eval);

    public ExecutableSetOfSetSelectedOperator<T, I> convert(final IConverter<? extends Set<? extends Set<? extends T>>, ? super Set<Set<T>>> converter);
}
