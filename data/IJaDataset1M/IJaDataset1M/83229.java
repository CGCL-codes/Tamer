package org.jmlspecs.checker;

import org.multijava.mjc.*;
import org.multijava.util.compiler.TokenReference;
import org.multijava.util.compiler.PositionedError;

/**
 * JmlDurationClause.java
 *
 * @author Gary T. Leavens
 * @version $Revision: 1.5 $
 */
public class JmlDurationClause extends JmlPredicateClause {

    public JmlDurationClause(TokenReference where, boolean isRedundantly, JmlSpecExpression specExp, JmlPredicate pred) {
        super(where, isRedundantly, pred);
        this.specExp = specExp;
    }

    public int preferredOrder() {
        return PORDER_DURATION_CLAUSE;
    }

    public JmlSpecExpression specExp() {
        return specExp;
    }

    public boolean isNotSpecified() {
        return specExp == null;
    }

    /**
     * Typechecks this <code>duration</code> clause in the context in which
     * it appears. Mutates the context to record the information learned
     * during checking.
     *
     * @param context the context in which this appears
     * @exception PositionedError if any checks fail */
    public void typecheck(CFlowControlContextType context, long privacy) throws PositionedError {
        if (isNotSpecified()) return;
        JmlExpressionContext dctx = createContext(context);
        specExp = (JmlSpecExpression) specExp.typecheck(dctx, privacy);
        check(context, specExp.getType().isOrdinal(), JmlMessages.BAD_TYPE_IN_DURATION_CLAUSE, specExp.getType().toVerboseString());
        if (predOrNot != null) {
            predOrNot = (JmlPredicate) predOrNot.typecheck(JmlExpressionContext.createPostcondition(context), privacy);
        }
    }

    /** Returns an appropriate context for checking this clause. */
    protected JmlExpressionContext createContext(CFlowControlContextType context) {
        return JmlExpressionContext.createDuration(context);
    }

    /**
     * Accepts the specified visitor.
     * @param p	the visitor
     */
    public void accept(MjcVisitor p) {
        if (p instanceof JmlVisitor) ((JmlVisitor) p).visitJmlDurationClause(this); else throw new UnsupportedOperationException(JmlNode.MJCVISIT_MESSAGE);
    }

    private JmlSpecExpression specExp;
}
