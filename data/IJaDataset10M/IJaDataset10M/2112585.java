package koala.dynamicjava.tree;

import koala.dynamicjava.tree.visitor.Visitor;

/**
 * This class represents the plus expression nodes of the syntax tree
 *
 * @author  Stephane Hillion
 * @version 1.0 - 1999/04/25
 */
public class PlusExpression extends UnaryExpression {

    /**
     * Initializes the expression
     * @param exp   the target expression
     * @exception IllegalArgumentException if exp is null
     */
    public PlusExpression(Expression exp) {
        this(exp, null, 0, 0, 0, 0);
    }

    /**
     * Initializes the expression
     * @param exp   the target expression
     * @param fn    the filename
     * @param bl    the begin line
     * @param bc    the begin column
     * @param el    the end line
     * @param ec    the end column
     * @exception IllegalArgumentException if exp is null
     */
    public PlusExpression(Expression exp, String fn, int bl, int bc, int el, int ec) {
        super(exp, fn, bl, bc, el, ec);
    }

    /**
     * Allows a visitor to traverse the tree
     * @param visitor the visitor to accept
     */
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visit(this);
    }
}
