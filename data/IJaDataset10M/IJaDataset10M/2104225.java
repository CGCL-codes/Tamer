package alefpp.parser.syntaxtree;

/**
 * Grammar production:
 * f0 -> RelationalExpression()
 * f1 -> ( ( "==" | RelationalNotEqual() ) RelationalExpression() )*
 */
@SuppressWarnings("all")
public class EqualityExpression implements Node {

    public RelationalExpression f0;

    public NodeListOptional f1;

    public EqualityExpression(RelationalExpression n0, NodeListOptional n1) {
        f0 = n0;
        f1 = n1;
    }

    public void accept(alefpp.parser.visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(alefpp.parser.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(alefpp.parser.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(alefpp.parser.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }
}
