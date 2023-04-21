package emast.model.function;

import emast.model.propositional.Expression;
import emast.model.propositional.Interpretation;
import emast.model.propositional.Proposition;
import emast.model.propositional.operator.BinaryOperator;
import emast.model.state.State;

/**
 *
 * @author And
 */
public class PropositionFunctionItem extends AbstractFunctionItem {

    private Interpretation interpretation;

    public PropositionFunctionItem(final State state, final Interpretation interpretation) {
        super(state);
        this.interpretation = interpretation;
    }

    public PropositionFunctionItem() {
        interpretation = new Interpretation();
    }

    public PropositionFunctionItem(final State state, final Proposition... pProps) {
        super(state);
        interpretation = new Interpretation();
        for (final Proposition proposition : pProps) {
            interpretation.put(proposition, Boolean.TRUE);
        }
    }

    /**
     * Get the value of interpretation
     *
     * @return the value of interpretation
     */
    public Interpretation getInterpretation() {
        return interpretation;
    }

    /**
     * Set the value of interpretation
     *
     * @param interpretation new value of interpretation
     */
    public void setInterpretation(final Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    @Override
    public String toString() {
        return getState().toString() + ": " + getInterpretation().toString();
    }

    public Expression getExpression() {
        Expression expression = null;
        for (final Proposition prop : getInterpretation().keySet()) {
            if (getInterpretation().get(prop)) {
                if (expression == null) {
                    expression = new Expression(prop);
                } else {
                    expression.add(new Expression(prop), BinaryOperator.AND);
                }
            }
        }
        return expression;
    }
}
