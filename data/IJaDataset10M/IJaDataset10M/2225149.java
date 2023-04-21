package sidekick.ecmascript.parser;

public class ASTCatchClause extends SimpleNode {

    public ASTCatchClause(int id) {
        super(id);
    }

    public ASTCatchClause(EcmaScript p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(EcmaScriptVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
