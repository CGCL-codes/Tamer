package oscript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> ( &lt;UNIX_SELF_EXECUTABLE_COMMENT&gt; )?
 * f1 -> Program(false)
 * f2 -> &lt;EOF&gt;
 * </PRE>
 */
public class ProgramFile implements Node {

    public NodeOptional f0;

    public Program f1;

    public NodeToken f2;

    public ProgramFile(NodeOptional n0, Program n1, NodeToken n2) {
        f0 = n0;
        f1 = n1;
        f2 = n2;
    }

    public ProgramFile(NodeOptional n0, Program n1) {
        f0 = n0;
        f1 = n1;
        f2 = new NodeToken("");
    }

    public void accept(oscript.visitor.Visitor v) {
        v.visit(this);
    }

    public Object accept(oscript.visitor.ObjectVisitor v, Object argu) {
        return v.visit(this, argu);
    }

    public oscript.NodeEvaluator nodeEvaluator;
}
