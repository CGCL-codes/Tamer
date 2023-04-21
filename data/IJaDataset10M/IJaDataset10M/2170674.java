package no.uio.ifi.kjetilos.javatraits.parser;

public class ASTTypeParameter extends SimpleNode {

    public ASTTypeParameter(int id) {
        super(id);
    }

    public ASTTypeParameter(JavaTParser p, int id) {
        super(p, id);
    }
}
