package ansem.syntaxtree;

import gci.tree.IRType;

/**
 * Grammar production:
 * <PRE>
 * f0 -> &lt;VIRGULA&gt;
 * f1 -> Exp()
 * </PRE>
 */
public class ExpRest implements Node {

    public NodeToken f0;

    public Exp f1;

    public ExpRest(NodeToken n0, Exp n1) {
        f0 = n0;
        f1 = n1;
    }

    public ExpRest(Exp n0) {
        f0 = new NodeToken(",");
        f1 = n0;
    }

    public IRType accept(ansem.visitor.IRVisitor v) throws SemanticException {
        return v.visit(this);
    }

    public void accept(ansem.visitor.Visitor v) throws SemanticException {
        v.visit(this);
    }

    public <R, A> R accept(ansem.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(ansem.visitor.GJNoArguVisitor<R> v) throws SemanticException {
        return v.visit(this);
    }

    public <A> void accept(ansem.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }
}
