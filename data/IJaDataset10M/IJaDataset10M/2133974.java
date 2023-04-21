package koala.dynamicjava.tree;

import koala.dynamicjava.tree.visitor.Visitor;

/**
 * This class represents the field access nodes of the syntax tree
 *
 * @author  Stephane Hillion
 * @version 1.0 - 1999/04/24
 */
public class ObjectFieldAccess extends FieldAccess implements ExpressionContainer {

    /**
     * The expression on which this field access applies
     */
    private Expression expression;

    /**
     * Creates a new field access node
     * @param exp   the expression on which this field access applies
     * @param fln   the field name
     * @exception IllegalArgumentException if exp is null or fln is null
     */
    public ObjectFieldAccess(Expression exp, String fln) {
        this(exp, fln, null, 0, 0, 0, 0);
    }

    /**
     * Creates a new field access node
     * @param exp   the expression on which this field access applies
     * @param fln   the field name
     * @param fn    the filename
     * @param bl    the begin line
     * @param bc    the begin column
     * @param el    the end line
     * @param ec    the end column
     * @exception IllegalArgumentException if exp is null or fln is null
     */
    public ObjectFieldAccess(Expression exp, String fln, String fn, int bl, int bc, int el, int ec) {
        super(fln, fn, bl, bc, el, ec);
        if (exp == null) throw new IllegalArgumentException("exp == null");
        expression = exp;
    }

    /**
     * Returns the expression on which this field access applies
     */
    public Expression getExpression() {
        return expression;
    }

    /**
     * Sets the expression on which this field access applies
     * @exception IllegalArgumentException if e is null
     */
    public void setExpression(Expression e) {
        if (e == null) throw new IllegalArgumentException("e == null");
        firePropertyChange(EXPRESSION, expression, expression = e);
    }

    /**
     * Allows a visitor to traverse the tree
     * @param visitor the visitor to accept
     */
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visit(this);
    }
}
