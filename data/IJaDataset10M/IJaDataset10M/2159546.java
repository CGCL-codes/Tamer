package org.gcreator.pineapple.pinec.parser.tree;

/**
 *
 * @author Luís Reis
 */
public class LogicalAndOperation extends BinaryOperation {

    public LogicalAndOperation(final Expression left, final Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder("(");
        str.append(getLeftExpression().toString());
        str.append(") && (");
        str.append(getRightExpression().toString());
        str.append(")");
        return str.toString();
    }
}
