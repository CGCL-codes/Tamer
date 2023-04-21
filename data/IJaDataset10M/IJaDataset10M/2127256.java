package steal.examples.expression.impl;

import steal.examples.expression.model.DivOperator;
import steal.examples.expression.visitor.Visitor;

public class DivOperatorImpl extends BinaryOperatorImpl implements DivOperator {

    public void accept(Visitor v) {
        v.visitDivOperator(this);
    }
}
