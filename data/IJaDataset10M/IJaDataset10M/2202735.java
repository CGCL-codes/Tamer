package org.codecover.instrumentation.java15.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> "case" Expression() ":"
 *       | "default" ":"
 * </PRE>
 */
@SuppressWarnings("all")
public class SwitchLabel implements Node {

    private Node parent;

    public NodeChoice f0;

    public SwitchLabel(NodeChoice n0) {
        f0 = n0;
        if (f0 != null) f0.setParent(this);
    }

    public void accept(org.codecover.instrumentation.java15.visitor.Visitor v) {
        v.visit(this);
    }

    public void accept(org.codecover.instrumentation.java15.visitor.VisitorWithException v) throws java.io.IOException {
        v.visit(this);
    }

    public <R, A> R accept(org.codecover.instrumentation.java15.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(org.codecover.instrumentation.java15.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(org.codecover.instrumentation.java15.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }

    public void setParent(Node n) {
        parent = n;
    }

    public Node getParent() {
        return parent;
    }

    /** for debugging purposes */
    @Override
    public String toString() {
        return org.codecover.instrumentation.java15.visitor.TreeSourceFileImageDumper.convertToString(this);
    }
}
