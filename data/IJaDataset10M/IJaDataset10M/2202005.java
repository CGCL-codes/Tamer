package org.jmlspecs.checker;

import org.multijava.mjc.*;
import org.multijava.util.compiler.TokenReference;
import org.multijava.util.compiler.JavaStyleComment;
import org.multijava.util.compiler.PositionedError;

/**
 * JmlNondetChoiceStatement.java
 *
 * @author Curtis Clifton
 * @version $Revision: 1.5 $
 */
public class JmlNondetChoiceStatement extends JmlModelProgStatement {

    public JmlNondetChoiceStatement(TokenReference where, JBlock[] alternativeStatements, JavaStyleComment[] comments) {
        super(where, comments);
        this.alternativeStatements = alternativeStatements;
    }

    /**
     * Returns an array of <code>JStatement</code> arrays, each of
     * them representing a jml-compound-statement.  */
    public JBlock[] alternativeStatements() {
        return alternativeStatements;
    }

    /**
     * Typechecks this statement in the context in which it
     * appears.  Mutates the context to record the information learned
     * during checking.
     *
     * @param context	the context in which this appears
     * @exception PositionedError if any checks fail */
    public void typecheck(CFlowControlContextType context) throws PositionedError {
        CFlowControlContextType inside = context.createFlowControlContext(getTokenReference());
        CFlowControlContextType[] branchContexts = new CFlowControlContextType[alternativeStatements.length];
        for (int i = 0; i < alternativeStatements.length; i++) {
            branchContexts[i] = inside.cloneContext();
            alternativeStatements[i].typecheck(branchContexts[i]);
        }
        boolean isFirst = true;
        for (int i = 0; i < alternativeStatements.length; i++) {
            if (isFirst && branchContexts[i].isReachable()) {
                context.adopt(branchContexts[i]);
                isFirst = false;
            } else if (branchContexts[i].isReachable()) {
                context.merge(branchContexts[i]);
            }
        }
        context.checkingComplete();
    }

    /**
     * Accepts the specified visitor.
     * @param p	the visitor
     */
    public void accept(MjcVisitor p) {
        if (p instanceof JmlVisitor) ((JmlVisitor) p).visitJmlNondetChoiceStatement(this); else throw new UnsupportedOperationException(JmlNode.MJCVISIT_MESSAGE);
    }

    private JBlock[] alternativeStatements;
}
