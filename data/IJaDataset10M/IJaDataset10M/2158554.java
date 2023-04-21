package org.jmlspecs.checker;

import org.multijava.mjc.*;
import org.multijava.util.compiler.PositionedError;
import org.multijava.util.compiler.TokenReference;

/**
 * JmlOldExpression.java
 *
 * @author Curtis Clifton
 * @version $Revision: 1.4 $
 */
public class JmlOldExpression extends JmlSpecExpressionWrapper {

    public JmlOldExpression(TokenReference where, JmlSpecExpression specExpression, String label) {
        super(where, specExpression);
        this.label = label;
    }

    public CType getType() {
        return specExpression.getType();
    }

    public String label() {
        return label;
    }

    /**
     * Typechecks the expression and mutates the context to record
     * information gathered during typechecking.
     *
     * @param context the context in which this expression appears
     * @return a desugared Java expression
     * @exception PositionedError if the check fails */
    public JExpression typecheck(CExpressionContextType context) throws PositionedError {
        if (!(context instanceof JmlExpressionContext)) {
            throw new IllegalArgumentException("JmlExpressionContext object expected");
        }
        if (!((JmlExpressionContext) context).oldOk()) {
            context.reportTrouble(new PositionedError(getTokenReference(), JmlMessages.OLD_NOT_ALLOWED));
        }
        JmlExpressionContext ectx = JmlExpressionContext.createOldExpression(context.getFlowControlContext());
        specExpression = (JmlSpecExpression) specExpression.typecheck(ectx);
        return this;
    }

    /**
     * Accepts the specified visitor.
     * @param p	the visitor
     */
    public void accept(MjcVisitor p) {
        if (p instanceof JmlVisitor) ((JmlVisitor) p).visitJmlOldExpression(this); else throw new UnsupportedOperationException(JmlNode.MJCVISIT_MESSAGE);
    }

    private String label;

    private JStatement target;
}
