package org.onemind.jxp.parser;

public class AstStarAssignExpression extends SimpleNode {

    public AstStarAssignExpression(int id) {
        super(id);
    }

    public AstStarAssignExpression(JxpParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JxpParserVisitor visitor, Object data) throws Exception {
        return visitor.visit(this, data);
    }
}
