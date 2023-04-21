package org.zkforge.apache.el.parser;

import org.zkforge.javax.el.ELException;
import org.zkforge.apache.el.lang.EvaluationContext;

/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: markt $
 */
public final class AstCompositeExpression extends SimpleNode {

    public AstCompositeExpression(int id) {
        super(id);
    }

    public Class getType(EvaluationContext ctx) throws ELException {
        return String.class;
    }

    public Object getValue(EvaluationContext ctx) throws ELException {
        StringBuffer sb = new StringBuffer(16);
        Object obj = null;
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                obj = this.children[i].getValue(ctx);
                if (obj != null) {
                    sb.append(obj);
                }
            }
        }
        return sb.toString();
    }
}
