package org.onemind.jxp.parser;

public class AstPrimitiveType extends SimpleNode {

    public AstPrimitiveType(int id) {
        super(id);
    }

    public AstPrimitiveType(JxpParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JxpParserVisitor visitor, Object data) throws Exception {
        return visitor.visit(this, data);
    }
}
