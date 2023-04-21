package org.objectstyle.cayenne.exp;

public class TstUnaryExpSuite extends TstExpressionSuite {

    private static final TstExpressionCase negative1 = buildNegative1();

    private static final TstExpressionCase negative2 = buildNegative2();

    private static final TstExpressionCase negative3 = buildNegative3();

    /** Cayenne syntax: "-5" */
    private static TstExpressionCase buildNegative1() {
        Expression e1 = ExpressionFactory.expressionOfType(Expression.NEGATIVE);
        e1.setOperand(0, new Integer(5));
        return new TstExpressionCase("Painting", e1, "-?", 1, 1);
    }

    /** Cayenne syntax: "-estimatedPrice" */
    private static TstExpressionCase buildNegative2() {
        Expression e1 = ExpressionFactory.expressionOfType(Expression.NEGATIVE);
        Expression e10 = ExpressionFactory.expressionOfType(Expression.OBJ_PATH);
        e10.setOperand(0, "estimatedPrice");
        e1.setOperand(0, e10);
        return new TstExpressionCase("Painting", e1, "-ta.ESTIMATED_PRICE", 2, 1);
    }

    /** Cayenne syntax: "-toGallery.paintingArray.estimatedPrice" */
    private static TstExpressionCase buildNegative3() {
        Expression e1 = ExpressionFactory.expressionOfType(Expression.NEGATIVE);
        Expression e10 = ExpressionFactory.expressionOfType(Expression.OBJ_PATH);
        e10.setOperand(0, "toGallery.paintingArray.estimatedPrice");
        e1.setOperand(0, e10);
        return new TstExpressionCase("Exhibit", e1, "-ta.ESTIMATED_PRICE", 2, 1);
    }

    public TstUnaryExpSuite() {
        addCase(negative1);
        addCase(negative2);
        addCase(negative3);
    }
}
