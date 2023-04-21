package net.sf.orcc.ir;

/**
 * This class defines a unary expression.
 * 
 * @author Matthieu Wipliez
 * @author Jerome Gorin
 * @model extends="net.sf.orcc.ir.Expression"
 */
public interface ExprUnary extends Expression {

    /**
	 * Returns the operand of this unary expression as an expression.
	 * 
	 * @return the operand of this unary expression
	 * @model containment="true"
	 */
    Expression getExpr();

    /**
	 * Returns the operator of this unary expression.
	 * 
	 * @return the operator of this unary expression
	 * @model
	 */
    OpUnary getOp();

    /**
	 * Returns the type of this expression.
	 * 
	 * @return the type of this expression
	 * @model containment="true"
	 */
    Type getType();

    void setExpr(Expression expr);

    void setOp(OpUnary op);

    /**
	 * Sets the type of this expression.
	 * 
	 * @param type
	 *            the type of this expression
	 */
    void setType(Type type);
}
