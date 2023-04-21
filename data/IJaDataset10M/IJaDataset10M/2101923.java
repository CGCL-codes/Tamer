package org.op4j.operators.impl.fn.set;

import java.util.Set;
import org.op4j.functions.Function;
import org.op4j.functions.IFunction;
import org.op4j.operators.impl.AbstractOperator;
import org.op4j.operators.intf.set.ILevel1SetElementsSelectedOperator;
import org.op4j.operators.qualities.UniqFnOperator;
import org.op4j.target.Target;
import org.op4j.target.Target.Normalisation;

public final class Level1SetElementsSelectedOperator<I, T> extends AbstractOperator implements UniqFnOperator<I, Set<T>>, ILevel1SetElementsSelectedOperator<I, T> {

    public Level1SetElementsSelectedOperator(final Target target) {
        super(target);
    }

    public Level1SetElementsOperator<I, T> endIf() {
        return new Level1SetElementsOperator<I, T>(getTarget().endSelect());
    }

    public Level1SetElementsSelectedOperator<I, T> exec(final IFunction<? super T, ? extends T> function) {
        return new Level1SetElementsSelectedOperator<I, T>(getTarget().execute(function, Normalisation.NONE));
    }

    public Level1SetElementsSelectedOperator<I, T> replaceWith(final T replacement) {
        return new Level1SetElementsSelectedOperator<I, T>(getTarget().replaceWith(replacement, Normalisation.NONE));
    }

    public Function<I, Set<T>> get() {
        return endIf().get();
    }
}
