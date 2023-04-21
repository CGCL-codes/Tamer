package oracle.toplink.essentials.internal.parsing;

import oracle.toplink.essentials.expressions.*;

/**
 * INTERNAL
 * <p><b>Purpose</b>: Used to resolve the LENGTH Function in EJBQL
 * <p><b>Responsibilities</b>:<ul>
 * <li> Generate the correct expression for a LENGTH in EJBQL
 * </ul>
 *    @author Jon Driscoll and Joel Lucuik
 *    @since TopLink 4.0
 */
public class LengthNode extends StringFunctionNode {

    /**
     * LengthNode constructor comment.
     */
    public LengthNode() {
        super();
    }

    /**
     * INTERNAL
     * Validate the current node and calculates its type.
     */
    public void validate(ParseTreeContext context) {
        TypeHelper typeHelper = context.getTypeHelper();
        if (left != null) {
            left.validate(context);
            left.validateParameter(context, typeHelper.getStringType());
        }
        setType(typeHelper.getIntType());
    }

    /**
     * INTERNAL
     * Generate the TopLink expression for this node
     */
    public Expression generateExpression(GenerationContext context) {
        Expression whereClause = getLeft().generateExpression(context);
        whereClause = whereClause.length();
        return whereClause;
    }
}
