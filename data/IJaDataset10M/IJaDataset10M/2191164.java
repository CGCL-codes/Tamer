package org.bbop.expression.parser;

import org.bbop.expression.ExpressionException;
import org.bbop.expression.JexlContext;
import org.apache.log4j.*;

public class ASTAssignment extends SimpleNode {

    protected static final Logger logger = Logger.getLogger(ASTAssignment.class);

    /**
     * Create the node given an id.
     * 
     * @param id node id.
     */
    public ASTAssignment(int id) {
        super(id);
    }

    /**
     * Create a node with the given parser and id.
     * 
     * @param p a parser.
     * @param id node id.
     */
    public ASTAssignment(Parser p, int id) {
        super(p, id);
    }

    /** {@inheritDoc} */
    @Override
    public Object jjtAccept(ParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    /** {@inheritDoc} */
    @Override
    public Object value(JexlContext context) throws Exception {
        SimpleNode left = (SimpleNode) jjtGetChild(0);
        Object right = ((SimpleNode) jjtGetChild(1)).value(context);
        if (left instanceof ASTReference) {
            ASTReference reference = (ASTReference) left;
            left = (SimpleNode) reference.jjtGetChild(0);
            if (left instanceof ASTIdentifier) {
                String identifier = ((ASTIdentifier) left).getIdentifierString();
                try {
                    context.setVariable(identifier, right);
                } catch (ExpressionException ex) {
                    ex.decorateException(this);
                }
            }
        } else {
            throwExpressionException("Illegal assignment to non-variable", null);
        }
        return right;
    }
}
