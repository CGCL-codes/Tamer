package no.uio.ifi.kjetilos.javatraits.parser;

public class ASTMethodDeclarator extends SimpleNode implements Visitable {

    public ASTMethodDeclarator(int id) {
        super(id);
    }

    public ASTMethodDeclarator(JavaTParser p, int id) {
        super(p, id);
    }

    /** 
	 * Make a shallow copy of this ASTMethodDeclarator
	 * 
	 * @return a shallow copy of this node.
	 */
    public ASTMethodDeclarator shallowCopy() {
        ASTMethodDeclarator copy = new ASTMethodDeclarator(this.parser, this.id);
        copy.jjtSetParent(jjtGetParent());
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            copy.jjtAddChild(jjtGetChild(i), i);
        }
        return copy;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
