package sidekick.ecmascript.parser;

public class ASTIfStatement extends SimpleNode {

    public ASTIfStatement(int id) {
        super(id);
    }

    public ASTIfStatement(EcmaScript p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(EcmaScriptVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
