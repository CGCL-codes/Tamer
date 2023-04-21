package name.nirav.opath.parse.ast.expr;

import name.nirav.opath.Variable;

/**
 * @author Nirav Thaker
 * 
 */
public class LiteralExpression extends Expression {

    private final boolean isRegEx;

    public LiteralExpression(Object value, boolean isRegEx) {
        super(value);
        this.isRegEx = isRegEx;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Object evaluate(Variable context) {
        return getValue();
    }

    public boolean isRegEx() {
        return isRegEx;
    }
}
