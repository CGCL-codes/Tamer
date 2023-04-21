package jintgen.jigir;

import cck.parser.SourcePoint;
import jintgen.isdl.parser.Token;

/**
 * The <code>DotExpr</code> class represents an access of a field of an operand
 * (or addressing mode). For example, <code>a.f</code>.
 *
 * @author Ben L. Titzer
 */
public class DotExpr extends Expr {

    /**
     * The <code>operand</code> field stores a reference to the name of the operand
     * or variable that has the field.
     */
    public final Expr expr;

    /**
     * The <code>field</code> field stores the name of the field being accessed.
     */
    public final Token field;

    public DotExpr(Expr e, Token f) {
        expr = e;
        field = f;
    }

    /**
     * The <code>accept()</code> method implements one half of the visitor pattern so that client visitors can
     * traverse the syntax tree easily and in an extensible way.
     *
     * @param v the visitor to accept
     */
    public void accept(CodeVisitor v) {
        v.visit(this);
    }

    /**
     * The <code>accept()</code> method implements one half of the visitor pattern for rebuilding of
     * expressions. This visitor allows code to be slightly modified while only writing visit methods for the
     * parts of the syntax tree affected.
     *
     * @param r the rebuilder to accept
     * @return the result of calling the appropriate <code>visit()</code> method of the rebuilder
     */
    public <Res, Env> Res accept(CodeAccumulator<Res, Env> r, Env env) {
        return r.visit(this, env);
    }

    /**
     * The <code>getPrecedence()</code> method returns a number representing the precedence (order
     * of operations) for this operator. This is used to correct nest the operators with parentheses
     * when printing them out, for example.
     * @return an precedence number for this operation
     */
    public int getPrecedence() {
        return PREC_TERM;
    }

    public SourcePoint getSourcePoint() {
        return new SourcePoint(expr.getSourcePoint(), field.getSourcePoint());
    }
}
