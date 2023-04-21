package org.jmlspecs.checker;

import org.multijava.mjc.*;
import org.multijava.util.compiler.PositionedError;
import org.multijava.util.compiler.TokenReference;

/**
 * JmlInformalStoreRef.java
 *
 * @author Curtis Clifton
 * @version $Revision: 1.5 $
 */
public class JmlInformalStoreRef extends JmlStoreRef {

    public JmlInformalStoreRef(TokenReference where, String text) {
        super(where);
        this.text = text;
    }

    public String text() {
        return text;
    }

    public boolean isInformalStoreRef() {
        return true;
    }

    /**
     * Typechecks this store reference in the given visibility,
     * <code>privacy</code>, and mutates the context,
     * <code>context</code>, to record information gathered during
     * typechecking.
     *
     * @param context the context in which this store reference appears
     * @param privacy the visibility in which to typecheck
     * @return a desugared store reference
     * @exception PositionedError if the check fails */
    public JmlStoreRef typecheck(CExpressionContextType context, long privacy) throws PositionedError {
        return this;
    }

    /**
     * Accepts the specified visitor.
     * @param p	the visitor
     */
    public void accept(MjcVisitor p) {
        if (p instanceof JmlVisitor) ((JmlVisitor) p).visitJmlInformalStoreRef(this); else throw new UnsupportedOperationException(JmlNode.MJCVISIT_MESSAGE);
    }

    /**
     * The text of this informal description store reference.  */
    private final String text;
}
