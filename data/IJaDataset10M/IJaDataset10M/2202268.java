package uk.ac.lkl.common.util.expression.operator;

import uk.ac.lkl.common.util.value.IntegerValue;

public class IntegerDivisionOperator extends DivisionOperator<IntegerValue> {

    @Override
    public IntegerValue getOne() {
        return IntegerValue.ONE;
    }
}
