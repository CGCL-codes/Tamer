package deesel.parser.com.nodes;

import deesel.parser.ASTNode;
import deesel.parser.com.COMNode;
import deesel.parser.com.util.ClassDefFactory;
import deesel.parser.com.visitor.COMVisitor;
import deesel.parser.com.visitor.VisitorContext;
import deesel.util.logging.Logger;
import java.util.ArrayList;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class DeeselList extends AbstractCOMNode implements Expression {

    private static Logger log = Logger.getLogger(DeeselList.class);

    private ArrayList expressions = new ArrayList();

    public DeeselList() {
        super(null);
    }

    public DeeselList(ASTNode astNode) {
        super(astNode);
    }

    public Expression[] getExpressions() {
        return (Expression[]) expressions.toArray(new Expression[expressions.size()]);
    }

    public void addChild(Expression expression) {
        assertNotNull(expression, "Cannot add a null expression to a list.");
        expressions.add(expression);
    }

    public DeeselClass getType() {
        return ClassDefFactory.LIST;
    }

    public COMNode[] getAllChildren() {
        return (COMNode[]) expressions.toArray(new COMNode[expressions.size()]);
    }

    public void accept(COMVisitor visitor, VisitorContext context) {
        visitor.visit(this, context);
    }

    public void resolve() {
        super.resolve();
        for (int i = 0; i < expressions.size(); i++) {
            Expression expression = (Expression) expressions.get(i);
            DeeselClass type = expression.getType();
            if (type instanceof AdaptiveType) {
                ((AdaptiveType) type).setConcrete(true);
            }
        }
    }
}
