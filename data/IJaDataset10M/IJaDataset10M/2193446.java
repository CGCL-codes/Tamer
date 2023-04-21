package koala.dynamicjava.tree;

import koala.dynamicjava.tree.visitor.Visitor;

/**
 * This class represents the shift right assign expression nodes of the syntax tree
 *
 * @author  Stephane Hillion
 * @version 1.0 - 1999/04/25
 */
public class ShiftRightAssignExpression extends AssignExpression {

    /**
     * Initializes the expression
     * @param lexp  the LHS expression
     * @param rexp  the RHS expression
     * @exception IllegalArgumentException if lexp is null or rexp is null
     */
    public ShiftRightAssignExpression(Expression lexp, Expression rexp) {
        this(lexp, rexp, null, 0, 0, 0, 0);
    }

    /**
     * Initializes the expression
     * @param lexp  the LHS expression
     * @param rexp  the RHS expression
     * @param fn    the filename
     * @param bl    the begin line
     * @param bc    the begin column
     * @param el    the end line
     * @param ec    the end column
     * @exception IllegalArgumentException if lexp is null or rexp is null
     */
    public ShiftRightAssignExpression(Expression lexp, Expression rexp, String fn, int bl, int bc, int el, int ec) {
        super(lexp, rexp, fn, bl, bc, el, ec);
    }

    /**
     * Allows a visitor to traverse the tree
     * @param visitor the visitor to accept
     */
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visit(this);
    }
}
