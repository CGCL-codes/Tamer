package scheme4j.parser;

public class ScmTest extends ScmObject {

    public ScmTest(int id) {
        super(id);
    }

    public ScmTest(SchemeParser p, int id) {
        super(p, id);
    }

    public ScmObject eval() {
        return ((ScmObject) jjtGetChild(0)).evaluate();
    }
}
