package org.jmlspecs.jml6.boogie.ast;

import org.jmlspecs.jml6.boogie.BoogieSource;

/**
 * BoogieExpression BoogieMapOperator
 * @author DSRG
 */
public class BoogieMapAccess extends BoogieExpression {

    BoogieMapOperator operator = new BoogieMapExpressionOperator(this);

    BoogieExpression expression = BoogieIdentifier.newUnknownIdentifier(this);

    public BoogieMapAccess(BoogieNode parent) {
        super(parent);
    }

    public void setExpression(BoogieExpression expression) {
        this.expression = expression;
    }

    public BoogieExpression getExpression() {
        return expression;
    }

    public void setOperator(BoogieMapOperator operator) {
        this.operator = operator;
    }

    @Override
    public void toBuffer(BoogieSource source) {
        expression.toBuffer(source);
        operator.toBuffer(source);
    }

    public BoogieMapOperator getOperator() {
        return operator;
    }
}
