package org.tm4j.tolog.parser;

public class ASTVarTest extends SimpleNode {

    public ASTVarTest(int id) {
        super(id);
    }

    public ASTVarTest(Tolog p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(TologVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
