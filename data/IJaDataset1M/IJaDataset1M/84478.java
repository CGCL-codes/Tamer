package sidekick.ecmascript.parser;

public class ASTCaseGuard extends SimpleNode {

    public ASTCaseGuard(int id) {
        super(id);
    }

    public ASTCaseGuard(EcmaScript p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(EcmaScriptVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
