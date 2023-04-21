package alefpp.parser.syntaxtree;

/**
 * Grammar production:
 * f0 -> "next"
 * f1 -> [ <IDENTIFIER> ]
 */
@SuppressWarnings("all")
public class Next implements Node {

    public NodeToken f0;

    public NodeOptional f1;

    public Next(NodeToken n0, NodeOptional n1) {
        f0 = n0;
        f1 = n1;
    }

    public Next(NodeOptional n0) {
        f0 = new NodeToken("next");
        f1 = n0;
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
