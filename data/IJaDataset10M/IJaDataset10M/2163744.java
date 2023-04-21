package jps.ast;

/**
 *
 * @author Radoslaw.Kowalczyk
 */
public class PlusExpression extends BinaryExpression {

    public PlusExpression(Expression leftExpression, Expression rightExpression) {
        super(leftExpression, rightExpression);
    }
}
