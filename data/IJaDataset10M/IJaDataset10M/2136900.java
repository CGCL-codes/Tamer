package steal.examples.expression.impl;

import steal.examples.expression.factory.Factory;
import steal.examples.expression.model.DivOperator;
import steal.examples.expression.model.Literal;
import steal.examples.expression.model.MinusOperator;
import steal.examples.expression.model.MulOperator;
import steal.examples.expression.model.PlusOperator;
import steal.examples.expression.model.SubOperator;

public class FactoryImpl implements Factory {

    public DivOperator createDivOperator() {
        return new DivOperatorImpl();
    }

    public Literal createLiteral() {
        return new LiteralImpl();
    }

    public MinusOperator createMinusOperator() {
        return new MinusOperatorImpl();
    }

    public MulOperator createMulOperator() {
        return new MulOperatorImpl();
    }

    public PlusOperator createPlusOperator() {
        return new PlusOperatorImpl();
    }

    public SubOperator createSubOperator() {
        return new SubOperatorImpl();
    }
}
