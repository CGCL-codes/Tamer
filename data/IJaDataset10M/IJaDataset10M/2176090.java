package alchemy.nec.tree;

/**
 * Storing element in array.
 * <pre><i>arrayexpr</i> [ <i>indexexpr</i> ] = <i>assignexpr</i></pre>
 * @author Sergey Basalaev
 */
public class AStoreExpr extends Expr {

    public Expr arrayexpr;

    public Expr indexexpr;

    public Expr assignexpr;

    public AStoreExpr(Expr arrayexpr, Expr indexexpr, Expr assignexpr) {
        this.arrayexpr = arrayexpr;
        this.indexexpr = indexexpr;
        this.assignexpr = assignexpr;
    }

    public Type rettype() {
        return BuiltinType.typeNone;
    }

    public Object accept(ExprVisitor v, Object data) {
        return v.visitAStore(this, data);
    }
}
