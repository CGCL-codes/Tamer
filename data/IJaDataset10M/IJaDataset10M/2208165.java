package org.jmlspecs.checker;

import org.multijava.mjc.*;
import org.multijava.util.compiler.PositionedError;
import org.multijava.util.compiler.TokenReference;

/**
 * JmlInformalExpression.java
 *
 * @author Curtis Clifton
 * @version $Revision: 1.5 $
 */
public class JmlInformalExpression extends JmlExpression {

    /** Constructs an instance. The constructed informal description
     * is of type boolean. */
    public JmlInformalExpression(TokenReference where, String text) {
        this(where, text, true);
    }

    /** Constructs an instance. If the argument <code>isBoolean</code>
     * is true, a boolean-typed informal description is constructed;
     * otherwise, an int-typed one is constructed. */
    private JmlInformalExpression(TokenReference where, String text, boolean isBoolean) {
        super(where);
        this.text = text;
        this.isBoolean = isBoolean;
    }

    /** Returns a new informal description of type boolean. */
    public static JmlInformalExpression ofBoolean(TokenReference where, String text) {
        return new JmlInformalExpression(where, text, true);
    }

    /** Returns a new informal description of type int. */
    public static JmlInformalExpression ofInteger(TokenReference where, String text) {
        return new JmlInformalExpression(where, text, false);
    }

    /** Returns a new informal description of type int out of the
        given informal description expression. */
    public static JmlInformalExpression ofInteger(JmlInformalExpression expr) {
        return new JmlInformalExpression(expr.getTokenReference(), expr.text(), false);
    }

    /** Returns the text of this informal description. */
    public String text() {
        return text;
    }

    /** Returns the type of this informal description. It is either boolean or
     * int. */
    public CType getType() {
        if (isBoolean) {
            return CStdType.Boolean;
        } else {
            return CStdType.Integer;
        }
    }

    /** Returns true if this is a constant. */
    public boolean isConstant() {
        return false;
    }

    public JLiteral getLiteral() {
        if (isBoolean) {
            return new JBooleanLiteral(getTokenReference(), true);
        } else {
            return new JOrdinalLiteral(getTokenReference(), 1, CStdType.Integer);
        }
    }

    /**
     * Typechecks the expression and mutates the context to record
     * information gathered during typechecking.
     *
     * @param context the context in which this expression appears
     * @return a desugared Java expression
     * @exception PositionedError if the check fails */
    public JExpression typecheck(CExpressionContextType context) throws PositionedError {
        return this;
    }

    /**
     * Accepts the specified visitor.
     * @param p	the visitor
     */
    public void accept(MjcVisitor p) {
        if (p instanceof JmlVisitor) ((JmlVisitor) p).visitJmlInformalExpression(this); else throw new UnsupportedOperationException(JmlNode.MJCVISIT_MESSAGE);
    }

    /**
     * The text of this informal description.  */
    private String text;

    /**
     * Is this informal description of type boolean or int? */
    private final boolean isBoolean;
}
