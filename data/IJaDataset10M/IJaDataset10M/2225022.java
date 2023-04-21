package org.op4j.operators.impl.arrayofset;

import java.util.Set;
import org.javaruntype.type.Type;
import org.javaruntype.type.Types;
import org.op4j.functions.IFunction;
import org.op4j.functions.converters.IConverter;
import org.op4j.functions.evaluators.IEvaluator;
import org.op4j.operators.impl.Operator;
import org.op4j.operators.intf.arrayofset.ILevel1ArrayOfSetElementsOperator;
import org.op4j.operators.intf.arrayofset.ILevel2ArrayOfSetElementsElementsOperator;
import org.op4j.target.Target;
import org.op4j.target.Target.Structure;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public class Level2ArrayOfSetElementsElementsOperator<T> extends Operator implements ILevel2ArrayOfSetElementsElementsOperator<T> {

    public Level2ArrayOfSetElementsElementsOperator(final Target target) {
        super(target);
    }

    public <X> ILevel2ArrayOfSetElementsElementsOperator<X> convert(final IConverter<X, ? super T> converter) {
        return new Level2ArrayOfSetElementsElementsOperator<X>(getTarget().execute(converter));
    }

    public ILevel1ArrayOfSetElementsOperator<T> endFor() {
        return new Level1ArrayOfSetElementsOperator<T>(getTarget().endIterate(Structure.SET, null));
    }

    public <X> ILevel2ArrayOfSetElementsElementsOperator<X> eval(final IEvaluator<X, ? super T> eval) {
        return new Level2ArrayOfSetElementsElementsOperator<X>(getTarget().execute(eval));
    }

    public <X> ILevel2ArrayOfSetElementsElementsOperator<X> exec(final IFunction<X, ? super T> function) {
        return new Level2ArrayOfSetElementsElementsOperator<X>(getTarget().execute(function));
    }

    public <X> ILevel2ArrayOfSetElementsElementsOperator<X> asType(final Type<X> type) {
        return endFor().endFor().generic().asArrayOfSetOf(type).forEach().forEach();
    }

    public ILevel2ArrayOfSetElementsElementsOperator<?> asUnknown() {
        return asType(Types.OBJECT);
    }

    public Set<T>[] get() {
        return endFor().endFor().get();
    }
}
