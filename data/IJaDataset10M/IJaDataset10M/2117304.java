package org.ujorm.criterion;

import org.ujorm.Ujo;

/**
 * The BinaryCriterion implementation allows to join two another Criterions into the binary tree.
 * @since 0.90
 * @author Pavel Ponec
 */
public class BinaryCriterion<UJO extends Ujo> extends Criterion<UJO> {

    private final Criterion<UJO> crn1;

    private final Criterion<UJO> crn2;

    private final BinaryOperator operator;

    public BinaryCriterion(final Criterion<UJO> criterion1, final BinaryOperator operator, final Criterion<UJO> criterion2) {
        this.crn1 = criterion1;
        this.crn2 = criterion2;
        this.operator = operator;
    }

    /** Returns the left node of the parrent */
    @Override
    public final Criterion<UJO> getLeftNode() {
        return crn1;
    }

    /** Returns the right node of the parrent */
    @Override
    public final Criterion<UJO> getRightNode() {
        return crn2;
    }

    /** Returns an operator */
    @Override
    public final BinaryOperator getOperator() {
        return operator;
    }

    @Override
    public boolean evaluate(UJO ujo) {
        boolean e1 = crn1.evaluate(ujo);
        switch(operator) {
            case AND:
                return e1 && crn2.evaluate(ujo);
            case OR:
                return e1 || crn2.evaluate(ujo);
            case XOR:
                return e1 != crn2.evaluate(ujo);
            case NAND:
                return !(e1 && crn2.evaluate(ujo));
            case NOR:
                return !(e1 || crn2.evaluate(ujo));
            case EQ:
                return e1 == crn2.evaluate(ujo);
            case NOT:
                return !e1;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    /** Is it a Binary Criterion */
    @Override
    public final boolean isBinary() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        printNode(crn1, result);
        result.append(' ').append(operator.name()).append(' ');
        printNode(crn2, result);
        return result.toString();
    }

    /** Print simple node */
    private void printNode(final Criterion cn, final StringBuilder out) {
        boolean parentheses = cn.getOperator() != BinaryOperator.AND;
        if (parentheses) out.append('(');
        out.append(cn);
        if (parentheses) out.append(')');
    }
}
