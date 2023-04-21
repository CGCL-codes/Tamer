package koala.dynamicjava.tree;

import java.util.List;
import koala.dynamicjava.tree.visitor.Visitor;

/**
 * This class represents the switch statement nodes of the syntax tree
 *
 * @author  Stephane Hillion
 * @version 1.0 - 1999/05/25
 */
public class SwitchStatement extends Statement {

    /**
     * The selector property name
     */
    public static final String SELECTOR = "selector";

    /**
     * The bindings property name
     */
    public static final String BINDINGS = "bindings";

    /**
     * The selector
     */
    private Expression selector;

    /**
     * The list of case bindings
     */
    private List bindings;

    /**
     * Creates a new switch statement
     * @param sel   the selector
     * @param cases the case bindings (SwitchBlocks)
     * @param fn    the filename
     * @param bl    the begin line
     * @param bc    the begin column
     * @param el    the end line
     * @param ec    the end column
     * @exception IllegalArgumentException if sel is null or cases is null
     */
    public SwitchStatement(Expression sel, List cases, String fn, int bl, int bc, int el, int ec) {
        super(fn, bl, bc, el, ec);
        if (sel == null) throw new IllegalArgumentException("sel == null");
        if (cases == null) throw new IllegalArgumentException("cases == null");
        selector = sel;
        bindings = cases;
    }

    /**
     * Gets the selector
     */
    public Expression getSelector() {
        return selector;
    }

    /**
     * Sets the selector
     * @exception IllegalArgumentException if e is null
     */
    public void setSelector(Expression e) {
        if (e == null) throw new IllegalArgumentException("e == null");
        firePropertyChange(SELECTOR, selector, selector = e);
    }

    /**
     * Returns the 'case' bindings
     */
    public List getBindings() {
        return bindings;
    }

    /**
     * Sets the bindings
     * @exception IllegalArgumentException if e is null
     */
    public void setBindings(List l) {
        if (l == null) throw new IllegalArgumentException("l == null");
        firePropertyChange(BINDINGS, bindings, bindings = l);
    }

    /**
     * Allows a visitor to traverse the tree
     * @param visitor the visitor to accept
     */
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visit(this);
    }
}
