package codec.gen.parser;

public class ASTValue extends SimpleNode {

    /**
     * Creates a new ASTValue object.
     *
     * @param id DOCUMENT ME!
     */
    public ASTValue(int id) {
        super(id);
    }

    /**
     * Creates a new ASTValue object.
     *
     * @param p DOCUMENT ME!
     * @param id DOCUMENT ME!
     */
    public ASTValue(Asn1Parser p, int id) {
        super(p, id);
    }
}
