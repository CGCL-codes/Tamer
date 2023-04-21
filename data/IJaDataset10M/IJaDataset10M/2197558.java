package org.gcreator.pineapple.pinedl.tree;

import org.gcreator.pineapple.pinedl.Token;
import org.gcreator.pineapple.pinedl.attributes.ComparisonType;

/**
 *
 * @author Luís Reis
 */
public class ComparisonNode extends ExpressionNode {

    public ComparisonType type = ComparisonType.EQUAL;

    public ExpressionNode left = null;

    public ExpressionNode right = null;

    public ComparisonNode(Token t) {
        super(t);
    }

    @Override
    public String toString() {
        return '(' + left.toString() + ") " + type.toString() + " (" + right.toString() + ')';
    }
}
