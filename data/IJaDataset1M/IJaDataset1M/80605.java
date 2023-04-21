package no.uio.ifi.kjetilos.javatraits.parser;

public class ASTExpression extends SimpleNode implements Visitable {

    public ASTExpression(int id) {
        super(id);
    }

    public ASTExpression(JavaTParser p, int id) {
        super(p, id);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
