package koala.dynamicjava.tree;

import java.util.List;
import koala.dynamicjava.tree.visitor.Visitor;

/**
 * This class represents the function call nodes of the syntax tree
 *
 * @author  Stephane Hillion
 * @version 1.0 - 1999/10/18
 */
public class FunctionCall extends MethodCall {

    /**
     * Creates a new node
     * @param mn    the function name
     * @param args  the arguments. Can be null.
     * @param fn    the filename
     * @param bl    the begin line
     * @param bc    the begin column
     * @param el    the end line
     * @param ec    the end column
     * @exception IllegalArgumentException if mn is null
     */
    public FunctionCall(String mn, List args, String fn, int bl, int bc, int el, int ec) {
        super(mn, args, fn, bl, bc, el, ec);
    }

    /**
     * Allows a visitor to traverse the tree
     * @param visitor the visitor to accept
     */
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visit(this);
    }
}
