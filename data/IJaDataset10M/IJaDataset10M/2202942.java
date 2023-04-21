package no.uio.ifi.kjetilos.javatraits.parser;

public class ASTType extends SimpleNode implements Visitable {

    public ASTType(int id) {
        super(id);
    }

    public ASTType(JavaTParser p, int id) {
        super(p, id);
    }

    /**
   * Check if the child node is a reference type
   * 
   * @return <code>true</code> if this type is a reference type, <code>false</code> if not.
   */
    public boolean isReferenceType() {
        return jjtGetChild(0) instanceof ASTReferenceType;
    }

    /**
   * Check if the child node is a primitive type
   * 
   * @return <code>true</code> if this type is a primitive type, <code>false</code> if not.
   */
    public boolean isPrimitiveType() {
        return jjtGetChild(0) instanceof ASTPrimitiveType;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
