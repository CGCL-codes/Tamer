package ognl;

/**
 * @author Luke Blanshard (blanshlu@netscape.net)
 * @author Drew Davidson (drew@ognl.org)
 */
public abstract class ExpressionNode extends SimpleNode {

    public ExpressionNode(int i) {
        super(i);
    }

    public ExpressionNode(OgnlParser p, int i) {
        super(p, i);
    }

    /**
        Returns true iff this node is constant without respect to the children.
     */
    public boolean isNodeConstant(OgnlContext context) throws OgnlException {
        return false;
    }

    public boolean isConstant(OgnlContext context) throws OgnlException {
        boolean result = isNodeConstant(context);
        if ((_children != null) && (_children.length > 0)) {
            result = true;
            for (int i = 0; result && (i < _children.length); ++i) {
                if (_children[i] instanceof SimpleNode) {
                    result = ((SimpleNode) _children[i]).isConstant(context);
                } else {
                    result = false;
                }
            }
        }
        return result;
    }

    public String getExpressionOperator(int index) {
        throw new RuntimeException("unknown operator for " + OgnlParserTreeConstants.jjtNodeName[_id]);
    }

    public String toString() {
        String result = (_parent == null) ? "" : "(";
        if ((_children != null) && (_children.length > 0)) {
            for (int i = 0; i < _children.length; ++i) {
                if (i > 0) {
                    result += " " + getExpressionOperator(i) + " ";
                }
                result += _children[i].toString();
            }
        }
        if (_parent != null) {
            result = result + ")";
        }
        return result;
    }
}
