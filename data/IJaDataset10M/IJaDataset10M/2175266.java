package org.bbop.expression.parser;

import org.bbop.expression.JexlContext;
import org.bbop.expression.util.Coercion;
import org.apache.log4j.*;

public class ASTLENode extends SimpleNode {

    protected static final Logger logger = Logger.getLogger(ASTLENode.class);

    /**
     * Create the node given an id.
     * 
     * @param id node id.
     */
    public ASTLENode(int id) {
        super(id);
    }

    /**
     * Create a node with the given parser and id.
     * 
     * @param p a parser.
     * @param id node id.
     */
    public ASTLENode(Parser p, int id) {
        super(p, id);
    }

    /** {@inheritDoc} */
    @Override
    public Object jjtAccept(ParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    /** {@inheritDoc} */
    @Override
    public Object value(JexlContext jc) throws Exception {
        Object left = ((SimpleNode) jjtGetChild(0)).value(jc);
        Object right = ((SimpleNode) jjtGetChild(1)).value(jc);
        if (left == right) {
            return Boolean.TRUE;
        } else if ((left == null) || (right == null)) {
            return Boolean.FALSE;
        } else if (Coercion.isFloatingPoint(left) || Coercion.isFloatingPoint(right)) {
            double leftDouble = Coercion.coerceDouble(left).doubleValue();
            double rightDouble = Coercion.coerceDouble(right).doubleValue();
            return leftDouble <= rightDouble ? Boolean.TRUE : Boolean.FALSE;
        } else if (Coercion.isNumberable(left) || Coercion.isNumberable(right)) {
            long leftLong = Coercion.coerceLong(left).longValue();
            long rightLong = Coercion.coerceLong(right).longValue();
            return leftLong <= rightLong ? Boolean.TRUE : Boolean.FALSE;
        } else if (left instanceof String || right instanceof String) {
            String leftString = left.toString();
            String rightString = right.toString();
            return leftString.compareTo(rightString) <= 0 ? Boolean.TRUE : Boolean.FALSE;
        } else if (left instanceof Comparable) {
            return ((Comparable) left).compareTo(right) <= 0 ? Boolean.TRUE : Boolean.FALSE;
        } else if (right instanceof Comparable) {
            return ((Comparable) right).compareTo(left) >= 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        throwExpressionException("Invalid comparison : LE ");
        return null;
    }
}
