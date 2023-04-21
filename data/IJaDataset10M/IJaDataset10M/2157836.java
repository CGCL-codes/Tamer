package org.onemind.jxp.parser;

public class AstObjectAllocationExpression extends SimpleNode {

    public AstObjectAllocationExpression(int id) {
        super(id);
    }

    public AstObjectAllocationExpression(JxpParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JxpParserVisitor visitor, Object data) throws Exception {
        return visitor.visit(this, data);
    }
}
