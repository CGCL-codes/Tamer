package de.fraunhofer.ipsi.xquery.tree.xpath;

import java.util.List;
import de.fraunhofer.ipsi.xquery.tree.XQueryNodeAbstract;
import de.fraunhofer.ipsi.xquery.tree.expression.XQueryExpression;
import de.fraunhofer.ipsi.xquery.util.PositionInfo;

public class XQueryPredicate extends XQueryNodeAbstract {

    private final List<XQueryExpression> expressions;

    /**
	 * Constructor
	 *
	 * @param    column              an int
	 * @param    line                an int
	 * @param    expr                a  XQueryExprSequence
	 *
	 */
    public XQueryPredicate(PositionInfo pos, List<XQueryExpression> expr) {
        super(pos);
        expressions = expr;
    }

    /**
	 * Method getExpressions
	 *
	 * @return   a XQueryExprSequence
	 *
	 */
    public List<XQueryExpression> getExpressions() {
        return expressions;
    }
}
