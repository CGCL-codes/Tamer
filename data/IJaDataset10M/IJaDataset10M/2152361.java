package alefpp.parser.syntaxtree;

/**
 * Grammar production:
 * f0 -> "["
 * f1 -> ( Expression() ( "," Expression() )* )?
 * f2 -> "]"
 */
@SuppressWarnings("all")
public class Array implements Node {

    public NodeToken f0;

    public NodeOptional f1;

    public NodeToken f2;

    public Array(NodeToken n0, NodeOptional n1, NodeToken n2) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
    }

    public Array(NodeOptional n0) {
        f0 = new NodeToken("[");
        f1 = n0;
        f2 = new NodeToken("]");
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
