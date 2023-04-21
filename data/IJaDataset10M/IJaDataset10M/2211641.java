package org.matheclipse.parser.client.eval;

import org.matheclipse.parser.client.ast.ASTNode;
import org.matheclipse.parser.client.math.MathUtils;

/**
 * 
 */
public class DoubleNode extends ASTNode {

    private final double value;

    public DoubleNode(double value) {
        super("DoubleNode");
        this.value = value;
    }

    public double doubleValue() {
        return value;
    }

    public String toString() {
        return Double.toString(value);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DoubleNode) {
            return value == ((DoubleNode) obj).value;
        }
        return false;
    }

    public int hashCode() {
        return MathUtils.hash(value);
    }
}
