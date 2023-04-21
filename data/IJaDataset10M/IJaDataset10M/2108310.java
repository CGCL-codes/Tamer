package net.sf.k_automaton.exprs;

import java.util.LinkedList;

/**
 *
 * @author Dmitri Koulakov
 */
public class ChoiceExpr extends MultiOpExpr {

    public ChoiceExpr() {
    }

    public ChoiceExpr(Expr... operands) {
        super(operands);
    }

    public void calculateExplainable(LinkedList<Expr> stack) {
        if (explainable != null) return;
        if (stack.contains(this)) return;
        stack.addLast(this);
        try {
            boolean known = true;
            for (Expr operand : operands) {
                operand.calculateExplainable(stack);
                if (operand.explainable == null) {
                    known = false;
                } else if (operand.explainable) {
                    explainable = true;
                    return;
                }
            }
            if (known) explainable = false;
        } finally {
            stack.removeLast();
        }
    }

    public void calculateEmptyAcceptable(LinkedList<Expr> stack) {
        if (empty_acceptable != null) return;
        if (stack.contains(this)) return;
        stack.addLast(this);
        try {
            boolean known = true;
            for (Expr operand : operands) {
                operand.calculateEmptyAcceptable(stack);
                if (operand.empty_acceptable == null) {
                    known = false;
                } else if (operand.empty_acceptable) {
                    empty_acceptable = true;
                    return;
                }
            }
            if (known) empty_acceptable = false;
        } finally {
            stack.removeLast();
        }
    }
}
