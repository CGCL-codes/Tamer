package uk.ac.lkl.common.util.expression.operation;

import uk.ac.lkl.common.util.value.NumericValue;
import uk.ac.lkl.common.util.expression.Expression;
import uk.ac.lkl.common.util.expression.ModifiableOperation;
import uk.ac.lkl.common.util.expression.operator.AbsoluteValueOperator;

public class AbsoluteValueOperation<N extends NumericValue<N>> extends ModifiableOperation<N, N> {

    public AbsoluteValueOperation(Expression<N> operand) {
        super(new AbsoluteValueOperator<N>(), operand);
    }
}
