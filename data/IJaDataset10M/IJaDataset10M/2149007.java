package net.sourceforge.pmd.jerry.ast.xpath;

public class ASTSlashSlash extends SimpleNode {

    public ASTSlashSlash(int id) {
        super(id);
    }

    public ASTSlashSlash(XPath2Parser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(XPath2ParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
