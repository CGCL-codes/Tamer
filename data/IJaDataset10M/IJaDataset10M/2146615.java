package org.op4j.operators.impl.arrayofarray;

import org.javaruntype.type.Type;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operations.Operation;
import org.op4j.operators.impl.AbstractOperatorImpl;
import org.op4j.operators.intf.arrayofarray.Level2ArrayOfArraySelectedElementsElementsOperator;
import org.op4j.operators.intf.arrayofarray.Level2ArrayOfArraySelectedElementsElementsSelectedOperator;
import org.op4j.target.Target;
import org.op4j.target.Target.Normalization;

public class Level2ArrayOfArraySelectedElementsElementsSelectedOperatorImpl<T, I> extends AbstractOperatorImpl implements Level2ArrayOfArraySelectedElementsElementsSelectedOperator<T, I> {

    private final Type<? extends T> type;

    public Level2ArrayOfArraySelectedElementsElementsSelectedOperatorImpl(final Type<? extends T> type, final Target target) {
        super(target);
        this.type = type;
    }

    public Level2ArrayOfArraySelectedElementsElementsSelectedOperator<T, I> eval(final IEvaluator<? extends T, ? super T> eval) {
        return new Level2ArrayOfArraySelectedElementsElementsSelectedOperatorImpl<T, I>(this.type, getTarget().execute(eval, Normalization.NONE));
    }

    public Level2ArrayOfArraySelectedElementsElementsOperator<T, I> endIf() {
        return new Level2ArrayOfArraySelectedElementsElementsOperatorImpl<T, I>(this.type, getTarget().endSelect());
    }

    public Level2ArrayOfArraySelectedElementsElementsSelectedOperator<T, I> exec(final IFunction<? extends T, ? super T> function) {
        return new Level2ArrayOfArraySelectedElementsElementsSelectedOperatorImpl<T, I>(this.type, getTarget().execute(function, Normalization.NONE));
    }

    public Level2ArrayOfArraySelectedElementsElementsSelectedOperator<T, I> replaceWith(final T replacement) {
        return new Level2ArrayOfArraySelectedElementsElementsSelectedOperatorImpl<T, I>(this.type, getTarget().replaceWith(replacement));
    }

    public Level2ArrayOfArraySelectedElementsElementsSelectedOperator<T, I> convert(final IConverter<? extends T, ? super T> converter) {
        return new Level2ArrayOfArraySelectedElementsElementsSelectedOperatorImpl<T, I>(this.type, getTarget().execute(converter, Normalization.NONE));
    }

    public T[][] get() {
        return endIf().get();
    }

    public Operation<T[][], I> createOperation() {
        return endIf().createOperation();
    }
}
