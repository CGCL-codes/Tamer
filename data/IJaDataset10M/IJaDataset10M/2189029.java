package deesel.parser.com.nodes;

import deesel.parser.ASTNode;
import deesel.parser.com.COMNode;
import deesel.parser.com.visitor.COMVisitor;
import deesel.parser.com.visitor.VisitorContext;
import deesel.util.logging.Logger;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class VariableReferenceComponent extends AbstractCOMNode implements PrimaryExpressionComponent {

    private static Logger log = Logger.getLogger(VariableReferenceComponent.class);

    private Variable variable;

    public VariableReferenceComponent(ASTNode node, Variable variable) {
        super(node);
        this.variable = variable;
    }

    public DeeselClass getType() {
        return variable.getType();
    }

    public COMNode[] getAllChildren() {
        return new COMNode[0];
    }

    public void accept(COMVisitor visitor, VisitorContext context) {
        visitor.visit(this, context);
    }

    public Variable getVariable() {
        return variable;
    }
}
