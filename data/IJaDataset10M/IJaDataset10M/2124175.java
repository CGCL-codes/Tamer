package org.jmlspecs.checker;

import org.multijava.mjc.*;
import org.multijava.util.compiler.PositionedError;
import org.multijava.util.compiler.TokenReference;

/**
 * JmlExpression.java
 *
 * @author Curtis Clifton
 * @version $Revision: 1.5 $
 */
public abstract class JmlExpression extends JExpression implements Constants {

    public JmlExpression(TokenReference where) {
        super(where);
    }

    public JExpression typecheck(CExpressionContextType context) throws PositionedError {
        return this;
    }

    public void accept(MjcVisitor p) {
        if (p instanceof JmlVisitor) ((JmlVisitor) p).visitJmlExpression(this); else throw new UnsupportedOperationException(JmlNode.MJCVISIT_MESSAGE);
    }

    public void genCode(CodeSequence code) {
        fail("JmlInformalExpression.genCode(CodeSequence) not implemented");
    }
}
