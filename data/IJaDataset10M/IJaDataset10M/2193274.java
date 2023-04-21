package name.levering.ryan.sparql.logic.expression;

import name.levering.ryan.sparql.common.RdfBindingRow;
import name.levering.ryan.sparql.model.data.UnaryExpressionData;
import name.levering.ryan.sparql.model.logic.ExpressionLogic;
import name.levering.ryan.sparql.common.Value;

/**
 * An abstract superclass of all of the unary expression logic in this package.
 * This is responsible for evaluating the expression and then delegating the
 * evaluation of the resulting Value object to the subclasses.
 * 
 * @author Ryan Levering
 * @version 1.0
 */
public abstract class UnaryLogic implements ExpressionLogic {

    /**
     * The unary expression data to evaluate and delegate.
     */
    private UnaryExpressionData data;

    /**
     * Creates a new unary logic object with particular expression data to
     * evaluate.
     * 
     * @param data the data containing the expression for evaluation
     */
    public UnaryLogic(UnaryExpressionData data) {
        this.data = data;
    }

    /**
     * Implements the expression logic by evaluating the expression and
     * delegating to the subclass methods.
     * 
     * @param bindings the value bindings to pass on to the expression
     *            evaluation
     * @return the result of the subclass operation on the expression results
     */
    public Value evaluate(RdfBindingRow bindings) {
        return this.evaluate(this.data.getExpression().evaluate(bindings));
    }

    /**
     * Template method called by this class to delegate the actual operation
     * evaluation to the subclass.
     * 
     * @param value the result of evaluating the expression
     * @return the result of evaluating the operation
     */
    public abstract Value evaluate(Value value);
}
