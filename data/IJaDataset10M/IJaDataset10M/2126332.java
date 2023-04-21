package org.jdesktop.xpath.function;

import java.util.List;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

/**
 *
 * @author richardallenbair
 */
public class EndsWith extends AbstractFunction {

    /** Creates a new instance of EndsWith */
    public EndsWith() {
        super("ends-with", 2);
    }

    public Object evaluate(List args) throws XPathFunctionException {
        String s1 = getStringParam(args.get(0));
        String s2 = getStringParam(args.get(1));
        return s1.endsWith(s2);
    }
}
