package ognl;

/**
 * @author Luke Blanshard (blanshlu@netscape.net)
 * @author Drew Davidson (drew@ognl.org)
 */
class ASTBitNegate extends NumericExpression {

    public ASTBitNegate(int id) {
        super(id);
    }

    public ASTBitNegate(OgnlParser p, int id) {
        super(p, id);
    }

    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        return OgnlOps.bitNegate(_children[0].getValue(context, source));
    }

    public String toString() {
        return "~" + _children[0];
    }
}
