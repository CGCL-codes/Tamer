package codec.gen.parser;

import java.io.IOException;
import java.io.Writer;

public class ASTSignedNumber extends SimpleNode {

    /**
     * DOCUMENT ME!
     */
    String value = null;

    /**
     * Creates a new ASTSignedNumber object.
     *
     * @param id DOCUMENT ME!
     */
    public ASTSignedNumber(int id) {
        super(id);
    }

    /**
     * Creates a new ASTSignedNumber object.
     *
     * @param p DOCUMENT ME!
     * @param id DOCUMENT ME!
     */
    public ASTSignedNumber(Asn1Parser p, int id) {
        super(p, id);
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    protected void printNodeValue(Writer out) throws IOException {
        out.write(value);
    }
}
