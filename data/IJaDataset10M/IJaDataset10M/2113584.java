package uk.ac.lkl.common.util.expression.operation;

import java.util.List;
import uk.ac.lkl.common.util.expression.ModifiableOperation;
import uk.ac.lkl.common.util.expression.Expression;
import uk.ac.lkl.common.util.value.BooleanValue;
import uk.ac.lkl.common.util.expression.operator.AndOperator;

public class AndOperation extends ModifiableOperation<BooleanValue, BooleanValue> {

    public AndOperation(List<Expression<BooleanValue>> operands) {
        super(new AndOperator(), operands);
    }
}
