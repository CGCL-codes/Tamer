package oracle.toplink.essentials.internal.parsing;

import oracle.toplink.essentials.expressions.*;

/**
 * INTERNAL
 * <p><b>Purpose</b>: Represent a '<' in EJBQL
 * <p><b>Responsibilities</b>:<ul>
 * <li> Generate the correct expression for a '<'
 * </ul>
 *    @author Jon Driscoll and Joel Lucuik
 *    @since TopLink 4.0
 */
public class LessThanNode extends BinaryOperatorNode {

    /**
     * LessThanNode constructor comment.
     */
    public LessThanNode() {
        super();
    }

    /**
     * INTERNAL
     * Validate the current node and calculates its type.
     */
    public void validate(ParseTreeContext context) {
        super.validate(context);
        TypeHelper typeHelper = context.getTypeHelper();
        setType(typeHelper.getBooleanType());
    }

    /**
     * INTERNAL
     * Resolve the expression. The steps are:
     * 1. Set the expressionBuilder for the left and right nodes
     * 2. Generate the expression for the left node
     * 3. Add the .lessThan to the where clause returned from step 2
     * 4. Generate the expression for the right side and use it as the parameter for the .lessThan()
     * 5. Return the completed where clause to the caller
     */
    public Expression generateExpression(GenerationContext context) {
        Expression whereClause = getLeft().generateExpression(context);
        whereClause = whereClause.lessThan(getRight().generateExpression(context));
        return whereClause;
    }

    /**
     * INTERNAL
     * Get the string representation of this node.
     */
    public String getAsString() {
        return left.getAsString() + " < " + right.getAsString();
    }
}
