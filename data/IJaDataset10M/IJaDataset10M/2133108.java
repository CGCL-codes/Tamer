package org.bbop.expression.parser;

import org.bbop.expression.JexlContext;
import java.util.Collection;
import java.util.Map;
import org.apache.log4j.*;

public class ASTEmptyFunction extends SimpleNode {

    protected static final Logger logger = Logger.getLogger(ASTEmptyFunction.class);

    /**
     * Create the node given an id.
     * 
     * @param id node id.
     */
    public ASTEmptyFunction(int id) {
        super(id);
    }

    /**
     * Create a node with the given parser and id.
     * 
     * @param p a parser.
     * @param id node id.
     */
    public ASTEmptyFunction(Parser p, int id) {
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
        SimpleNode sn = (SimpleNode) jjtGetChild(0);
        Object o = sn.value(jc);
        if (o == null) {
            return Boolean.TRUE;
        }
        if (o instanceof String && "".equals(o)) {
            return Boolean.TRUE;
        }
        if (o.getClass().isArray() && ((Object[]) o).length == 0) {
            return Boolean.TRUE;
        }
        if (o instanceof Collection && ((Collection) o).isEmpty()) {
            return Boolean.TRUE;
        }
        if (o instanceof Map && ((Map) o).isEmpty()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
