package ognl;

/**
 * User: avilches
 * Date: 17-dic-2008
 * Time: 16:17:50
 */
public abstract class ComparisonExpression extends ExpressionNode {

    protected ComparisonExpression(int i) {
        super(i);
    }

    protected ComparisonExpression(OgnlParser p, int i) {
        super(p, i);
    }
}
