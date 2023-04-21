package oscript.syntaxtree;

/**
 * Grammar production:
 * <PRE>

 * f0 -> "'{"
 * f1 -> Program(true)
 * f2 -> "}"
 * </PRE>
 */
public class ShorthandFunctionPrimaryPrefix implements Node {

    public Node translated;

    public boolean hasVarInScope = true;

    public boolean hasFxnInScope = true;

    public NodeToken f0;

    public Program f1;

    public NodeToken f2;

    public ShorthandFunctionPrimaryPrefix(NodeToken n0, Program n1, NodeToken n2, boolean hasVarInScope, boolean hasFxnInScope) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
        this.hasVarInScope = hasVarInScope;
        this.hasFxnInScope = hasFxnInScope;
    }

    public ShorthandFunctionPrimaryPrefix(Program n0) {
        f0 = new NodeToken("'{");
        f1 = n0;
        f2 = new NodeToken("}");
    }

    public void accept(oscript.visitor.Visitor v) {
        v.visit(this);
    }

    public Object accept(oscript.visitor.ObjectVisitor v, Object argu) {
        return v.visit(this, argu);
    }
}
