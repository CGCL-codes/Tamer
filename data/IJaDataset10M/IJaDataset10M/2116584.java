package net.sf.jdpa.cg.model;

/**
 * @author Andreas Nilsson
 */
public class ConditionalExpression implements Expression {

    public Condition $elseif(Expression condition) {
        return null;
    }

    public Expression $else(Statement statement) {
        return null;
    }

    public ConstructType getConstructType() {
        return null;
    }
}
