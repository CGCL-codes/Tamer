package ognl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Luke Blanshard (blanshlu@netscape.net)
 * @author Drew Davidson (drew@ognl.org)
 */
class ASTSelectFirst extends SimpleNode {

    public ASTSelectFirst(int id) {
        super(id);
    }

    public ASTSelectFirst(OgnlParser p, int id) {
        super(p, id);
    }

    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Node expr = _children[0];
        List answer = new ArrayList();
        ElementsAccessor elementsAccessor = OgnlRuntime.getElementsAccessor(OgnlRuntime.getTargetClass(source));
        for (Enumeration e = elementsAccessor.getElements(source); e.hasMoreElements(); ) {
            Object next = e.nextElement();
            if (OgnlOps.booleanValue(expr.getValue(context, next))) {
                answer.add(next);
                break;
            }
        }
        return answer;
    }

    public String toString() {
        return "{^ " + _children[0] + " }";
    }
}
