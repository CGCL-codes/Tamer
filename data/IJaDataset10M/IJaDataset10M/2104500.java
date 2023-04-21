package com.ibm.tuningfork.infra.stream.expression.syntax;

import com.ibm.tuningfork.infra.stream.expression.base.Expression;
import com.ibm.tuningfork.infra.stream.expression.syntax.Ast.IExpression;

/**
 * Specializations of this class translate between LPG expressions (implementors of IExpression) and ForkTalk Expression
 *   objects.  There is one kind of translator for each kind of LPG expression.
 */
abstract class AstTranslator {

    /**
     * Perform translation
     * @param from the IExpression to translate from
     * @param parser the ForkTalkExpressionParser that is doing the translation
     * @return the translation
     */
    abstract Expression translate(IExpression from, ForkTalkExpressionParser parser);

    /**
     * Check that two expressions are scalar, else issue exception with offending operator
     * @param left the left operand expression
     * @param right the right operand expression
     * @param operator the possibly erroneous operator
     * @throws IllegalArgumentException if either expression isn't scalar
     */
    final void mustBeScalar(Expression left, Expression right, String operator) {
        if (!left.getType().isScalar() || !right.getType().isScalar()) {
            throw new IllegalArgumentException(operator + " only defined between scalar types");
        }
    }

    /**
     * Check that two expressions are numeric, else issue exception with offending operator
     * @param left the left operand expression
     * @param right the right operand expression
     * @param operator the possibly erroneous operator
     * @throws IllegalArgumentException if either expression isn't scalar
     */
    final void mustBeNumeric(Expression left, Expression right, String operator) {
        if (!left.getType().isNumeric() || !right.getType().isNumeric()) {
            throw new IllegalArgumentException(operator + " only defined between numeric types");
        }
    }
}
