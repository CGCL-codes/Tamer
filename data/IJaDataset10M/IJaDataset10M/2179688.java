package steal.examples.expression.visitor;

import steal.examples.expression.model.DivOperator;
import steal.examples.expression.model.Literal;
import steal.examples.expression.model.MinusOperator;
import steal.examples.expression.model.MulOperator;
import steal.examples.expression.model.PlusOperator;
import steal.examples.expression.model.SubOperator;

public interface Visitor {

    void visitPlusOperator(PlusOperator op);

    void visitSubOperator(SubOperator op);

    void visitDivOperator(DivOperator op);

    void visitMulOperator(MulOperator op);

    void visitMinusOperator(MinusOperator op);

    void visitLiteral(Literal literal);
}
