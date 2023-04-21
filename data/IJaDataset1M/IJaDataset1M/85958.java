package alefpp.parser.syntaxtree;

/**
 * Grammar production:
 * f0 -> ";"
 */
@SuppressWarnings("all")
public class EmptyStatement implements Node {

    public NodeToken f0;

    public EmptyStatement(NodeToken n0) {
        f0 = n0;
    }

    public EmptyStatement() {
        f0 = new NodeToken(";");
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
