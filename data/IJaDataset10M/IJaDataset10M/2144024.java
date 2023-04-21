package deesel.parser.com.nodes;

import deesel.parser.ASTNode;
import deesel.parser.com.COMNode;
import deesel.parser.com.exceptions.COMIntegrityException;
import deesel.parser.com.exceptions.CannotAddChildOfThisTypeException;
import deesel.parser.com.util.ClassDefFactory;
import deesel.parser.com.visitor.COMVisitor;
import deesel.parser.com.visitor.VisitorContext;
import deesel.util.logging.Logger;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class Instanceof extends AbstractCOMNode implements COMNode, Expression {

    private static Logger log = Logger.getLogger(Instanceof.class);

    private Expression expression;

    private DeeselClass classType;

    public Instanceof(ASTNode astNode) {
        super(astNode);
    }

    public Expression getExpression() {
        return expression;
    }

    public void accept(COMVisitor visitor, VisitorContext context) {
        visitor.visit(this, context);
    }

    public DeeselClass getClassType() {
        return classType;
    }

    public DeeselClass getType() {
        return ClassDefFactory.BOOLEAN_PRIM;
    }

    public COMNode[] getAllChildren() {
        return new COMNode[] { expression, classType };
    }

    public void addChild(Type type) {
        assertIsNull(classType, "Cannot add more than one type to an instanceof.");
        assertNotNull(type, "Cannot add a null type to an instanceof.");
        this.classType = ClassDefFactory.findClass(type, this);
    }

    public void addChild(Expression node) throws CannotAddChildOfThisTypeException {
        assertIsNull(expression, "Cannot add more than one expression to an instanceof.");
        assertNotNull(node, "Cannot add a null node to an instanceof.");
        if (expression == null) {
            expression = node;
        }
    }

    public void validate() throws COMIntegrityException {
        super.validate();
        assertNotNull(expression, "The expression of an instanceof expression cannot be null.");
        assertNotNull(classType, "The class of an instanceof expression cannot be null.");
    }
}
