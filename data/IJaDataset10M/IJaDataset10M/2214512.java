package org.zeleos.zwt.parser.zecma262;

public class ASTLogicalANDExpressionNoIn extends SimpleNode {

    public ASTLogicalANDExpressionNoIn(int id) {
        super(id);
    }

    public ASTLogicalANDExpressionNoIn(ZECMA262 p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public void jjtAccept(ZECMA262Visitor visitor, java.io.Writer data) throws org.zeleos.zwt.parser.zecma262.ZECMA262VisitorException {
        visitor.visit(this, data);
    }
}
