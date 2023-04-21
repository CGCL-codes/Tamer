package oracle.toplink.essentials.internal.parsing;

import oracle.toplink.essentials.expressions.*;
import oracle.toplink.essentials.queryframework.ReportQuery;

/**
 * INTERNAL
 * <p><b>Purpose</b>: Represent an ALL subquery.
 */
public class AllNode extends Node {

    /**
     * Return a new AllNode.
     */
    public AllNode() {
        super();
    }

    /**
     * INTERNAL
     * Validate node and calculate its type.
     */
    public void validate(ParseTreeContext context) {
        if (left != null) {
            left.validate(context);
            setType(left.getType());
        }
    }

    /**
     * INTERNAL
     * Generate the TopLink expression for this node
     */
    public Expression generateExpression(GenerationContext context) {
        SubqueryNode subqueryNode = (SubqueryNode) getLeft();
        ReportQuery reportQuery = subqueryNode.getReportQuery(context);
        Expression expr = context.getBaseExpression();
        return expr.all(reportQuery);
    }
}
