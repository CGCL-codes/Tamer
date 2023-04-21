package galronnlp.cfg.grammar;

import java.util.List;
import java.util.LinkedList;
import galronnlp.util.Symbol;

/**
 * A Unary CFG rule, of the form X => Y
 *
 * @author Daniel A. Galron
 */
public class UnaryRule implements Rule {

    private final int arity = 1;

    private Symbol LHS;

    private LinkedList<Symbol> RHS;

    /** Creates a new instance of UnaryRule */
    public UnaryRule(Symbol LHS, LinkedList<Symbol> RHS) {
        this.LHS = LHS;
        this.RHS = RHS;
    }

    /**
     * @return the RHS of the rule as a <code>List</code>
     */
    public List<Symbol> RHS() {
        return RHS;
    }

    /**
     * @return the LHS of the rule as a <code>Symbol</code>
     */
    public Symbol LHS() {
        return LHS;
    }

    /**
     * @return the number of RHS Symbols (1) of the rule
     */
    public int arity() {
        return arity;
    }

    public String toString() {
        String ret = this.LHS + " --> " + this.RHS.get(0);
        return ret;
    }

    public boolean hasTerminalRHS() {
        return false;
    }
}
