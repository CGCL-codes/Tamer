package org.jdesktop.el.impl.parser;

import org.jdesktop.el.ELException;
import org.jdesktop.el.impl.lang.EvaluationContext;

/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: kchung $
 */
public class BooleanNode extends SimpleNode {

    /**
     * @param i
     */
    public BooleanNode(int i) {
        super(i);
    }

    public Class getType(EvaluationContext ctx) throws ELException {
        return Boolean.class;
    }
}
