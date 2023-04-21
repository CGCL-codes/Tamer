package org.onemind.jxp.parser;

public class AstTildeUnaryExpression extends SimpleNode {

    public AstTildeUnaryExpression(int id) {
        super(id);
    }

    public AstTildeUnaryExpression(JxpParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JxpParserVisitor visitor, Object data) throws Exception {
        return visitor.visit(this, data);
    }
}
