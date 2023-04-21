package org.zeleos.zwt.parser.zecma262;

public class ASTFunctionBody extends SimpleNode {

    public ASTFunctionBody(int id) {
        super(id);
    }

    public ASTFunctionBody(ZECMA262 p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public void jjtAccept(ZECMA262Visitor visitor, java.io.Writer data) throws org.zeleos.zwt.parser.zecma262.ZECMA262VisitorException {
        visitor.visit(this, data);
    }
}
