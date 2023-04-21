package alefpp.parser.syntaxtree;

/**
 * Grammar production:
 * f0 -> "autoload"
 * f1 -> Block()
 */
@SuppressWarnings("all")
public class ModuleAutoload implements Node {

    public NodeToken f0;

    public Block f1;

    public ModuleAutoload(NodeToken n0, Block n1) {
        f0 = n0;
        f1 = n1;
    }

    public ModuleAutoload(Block n0) {
        f0 = new NodeToken("autoload");
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
