package name.levering.ryan.sparql.parser.model;

import java.util.ArrayList;
import java.util.Collection;
import name.levering.ryan.sparql.model.logic.ExpressionLogic;

public class ASTPropertyList extends SimpleNode {

    public ASTPropertyList(int id) {
        super(id);
    }

    public Collection getStatements(ExpressionLogic subject) {
        Collection statements = new ArrayList();
        for (int i = 0; i < this.jjtGetNumChildren(); i++) {
            statements.addAll(((ASTVerbObject) this.jjtGetChild(i)).getStatements(subject));
        }
        return statements;
    }

    public String toString() {
        StringBuffer output = new StringBuffer();
        for (int i = 0; i < this.jjtGetNumChildren(); i++) {
            output.append(this.jjtGetChild(i));
            if (i + 1 < this.jjtGetNumChildren()) {
                output.append("; ");
            }
        }
        return output.toString();
    }
}
