package oracle.toplink.essentials.internal.parsing;

import oracle.toplink.essentials.expressions.*;

/**
 * INTERNAL
 * <p><b>Purpose</b>: Represent an AND in EJBQL
 * <p><b>Responsibilities</b>:<ul>
 * <li> Generate the correct expression for an AND in EJBQL
 * </ul>
 *    @author Jon Driscoll and Joel Lucuik
 *    @since TopLink 4.0
 */
public class AndNode extends LogicalOperatorNode {

    /**
     * AndNode constructor comment.
     */
    public AndNode() {
        super();
    }

    /**
     * INTERNAL
     * Return a TopLink expression by 'AND'ing the expressions from the left and right nodes
     */
    public Expression generateExpression(GenerationContext context) {
        Expression whereClause = getLeft().generateExpression(context);
        whereClause = whereClause.and(getRight().generateExpression(context));
        return whereClause;
    }
}
