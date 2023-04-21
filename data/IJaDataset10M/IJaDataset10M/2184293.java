package oracle.toplink.essentials.internal.parsing;

import oracle.toplink.essentials.expressions.*;

public class MemberOfNode extends BinaryOperatorNode {

    private boolean notIndicated = false;

    private Expression leftExpression = null;

    /**
     * Return a new MemberOfNode
     */
    public MemberOfNode() {
        super();
    }

    /**
     * INTERNAL makeNodeOneToMany:
     * Traverse to the leaf on theNode and mark as one to many
     */
    public void makeNodeOneToMany(Node theNode) {
        Node currentNode = theNode;
        do {
            if (!currentNode.hasRight()) {
                ((AttributeNode) currentNode).setRequiresCollectionAttribute(true);
                return;
            }
            currentNode = currentNode.getRight();
        } while (true);
    }

    /**
     * INTERNAL
     * Validate node and calculates its type.
     */
    public void validate(ParseTreeContext context) {
        super.validate(context);
        Node left = getLeft();
        if (left.isVariableNode() && ((VariableNode) left).isAlias(context)) {
            context.usedVariable(((VariableNode) left).getCanonicalVariableName());
        }
        left.validateParameter(context, right.getType());
        TypeHelper typeHelper = context.getTypeHelper();
        setType(typeHelper.getBooleanType());
    }

    public Expression generateExpression(GenerationContext context) {
        if (getRight().isParameterNode()) {
            makeNodeOneToMany(getLeft());
        } else {
            makeNodeOneToMany(getRight());
        }
        if (notIndicated()) {
            Expression resultFromRight = null;
            context.setMemberOfNode(this);
            this.setLeftExpression(getLeft().generateExpression(context));
            resultFromRight = getRight().generateExpression(context);
            context.setMemberOfNode(null);
            this.setLeftExpression(null);
            return resultFromRight;
        } else {
            return getRight().generateExpression(context).equal(getLeft().generateExpression(context));
        }
    }

    /**
     * INTERNAL
     * Indicate if a NOT was found in the WHERE clause.
     * Examples:
     *        ...WHERE ... NOT MEMBER OF
     */
    public void indicateNot() {
        notIndicated = true;
    }

    public boolean notIndicated() {
        return notIndicated;
    }

    public void setLeftExpression(Expression newLeftExpression) {
        leftExpression = newLeftExpression;
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }
}
