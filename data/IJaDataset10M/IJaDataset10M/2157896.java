package org.jmlspecs.checker;

import org.multijava.mjc.*;
import org.multijava.util.compiler.TokenReference;
import org.multijava.util.compiler.PositionedError;

/**
 * A JML AST node class for the <tt>diverges</tt> clause.
 *
 * @author Yoonsik Cheon
 * @version $Revision: 1.5 $
 */
public class JmlDivergesClause extends JmlPredicateClause {

    public JmlDivergesClause(TokenReference where, boolean isRedundantly, JmlPredicate predOrNot) {
        super(where, isRedundantly, predOrNot);
    }

    public int preferredOrder() {
        return PORDER_DIVERGES_CLAUSE;
    }

    /** Returns an appropriate context for checking this clause. */
    protected JmlExpressionContext createContext(CFlowControlContextType context) {
        return JmlExpressionContext.createPrecondition(context);
    }

    /**
     * Accepts the specified visitor.
     * @param p	the visitor
     */
    public void accept(MjcVisitor p) {
        if (p instanceof JmlVisitor) ((JmlVisitor) p).visitJmlDivergesClause(this); else throw new UnsupportedOperationException(JmlNode.MJCVISIT_MESSAGE);
    }
}
