package scheme4j.parser;

public class ScmQuotation extends ScmObject {

    /** costruttore che deve essere utilizzato esclusivamente da javacc */
    public ScmQuotation(int id) {
        super(id);
    }

    /** costruttore che deve essere utilizzato esclusivamente da javacc */
    public ScmQuotation(SchemeParser p, int id) {
        super(p, id);
    }

    public ScmObject eval() {
        return ((ScmObject) jjtGetChild(0)).evaluate();
    }
}
