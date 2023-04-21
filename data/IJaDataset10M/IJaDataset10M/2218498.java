package de.fraunhofer.isst.axbench.axlang.syntaxtree;

/**
 * Grammar production:
 * f0 -> <FUNCTION>
 * f1 -> GlobalInstancePath()
 */
public class GlobalFunctionInstance implements Node {

    public NodeToken f0;

    public GlobalInstancePath f1;

    public GlobalFunctionInstance(NodeToken n0, GlobalInstancePath n1) {
        f0 = n0;
        f1 = n1;
    }

    public GlobalFunctionInstance(GlobalInstancePath n0) {
        f0 = new NodeToken("function");
        f1 = n0;
    }

    public void accept(de.fraunhofer.isst.axbench.axlang.visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(de.fraunhofer.isst.axbench.axlang.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(de.fraunhofer.isst.axbench.axlang.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(de.fraunhofer.isst.axbench.axlang.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }
}
