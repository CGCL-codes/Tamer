package org.onemind.jxp.parser;

public class AstUnaryPlusExpression extends SimpleNode {

    public AstUnaryPlusExpression(int id) {
        super(id);
    }

    public AstUnaryPlusExpression(JxpParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JxpParserVisitor visitor, Object data) throws Exception {
        return visitor.visit(this, data);
    }
}
