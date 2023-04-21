package org.sodeja.runtime.scheme4;

import org.sodeja.runtime.Evaluator;
import org.sodeja.runtime.Frame;

public class ValueExpression<T> implements CompiledSchemeExpression {

    public final T value;

    public ValueExpression(T value) {
        this.value = value;
    }

    @Override
    public Object eval(Evaluator<CompiledSchemeExpression> evaluator, Frame<CompiledSchemeExpression> frame) {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ValueExpression)) {
            return false;
        }
        return this.value.equals(((ValueExpression<?>) obj).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
