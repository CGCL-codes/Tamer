package no.uio.ifi.kjetilos.javatraits.parser;

public class ASTStatementExpressionList extends SimpleNode implements Visitable {

    public ASTStatementExpressionList(int id) {
        super(id);
    }

    public ASTStatementExpressionList(JavaTParser p, int id) {
        super(p, id);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
