package alchemy.nec.tree;

/**
 * Expression that is computed but its result is discarded.
 * @author Sergey Basalaev
 */
public class DiscardExpr extends Expr {

    public Expr expr;

    public DiscardExpr(Expr expr) {
        this.expr = expr;
    }

    public Type rettype() {
        return BuiltinType.typeNone;
    }

    public Object accept(ExprVisitor v, Object data) {
        return v.visitDiscard(this, data);
    }
}
