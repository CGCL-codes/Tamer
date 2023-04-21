package alefpp.parser.syntaxtree;

/**
 * Grammar production:
 * f0 -> "{"
 * f1 -> ( BlockStatement() )*
 * f2 -> "}"
 */
@SuppressWarnings("all")
public class Block implements Node {

    public NodeToken f0;

    public NodeListOptional f1;

    public NodeToken f2;

    public Block(NodeToken n0, NodeListOptional n1, NodeToken n2) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
    }

    public Block(NodeListOptional n0) {
        f0 = new NodeToken("{");
        f1 = n0;
        f2 = new NodeToken("}");
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
