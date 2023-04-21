package bee;

import java.util.List;

public class AstParameterList extends SimpleNode {

    public List list;

    public boolean varargs;

    public AstParameterList(int id) {
        super(id);
    }

    public AstParameterList(Parser p, int id) {
        super(p, id);
    }
}
