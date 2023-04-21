package deesel.parser;

import deesel.util.logging.Logger;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class BooleanExpression extends SimpleNode {

    private static Logger log = Logger.getLogger(BooleanExpression.class);

    public BooleanExpression(int i) {
        super(i);
    }

    public BooleanExpression(DeeselParser p, int i) {
        super(p, i);
    }
}
