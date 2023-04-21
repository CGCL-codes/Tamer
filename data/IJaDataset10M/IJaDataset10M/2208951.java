package jdice.expression.lib;

import java.io.PrintStream;
import jdice.expression.AbstractListExpression;
import jdice.expression.EvaluationContext;
import jdice.expression.Expression;
import jdice.expression.OperatorPrecedence;
import jdice.expression.Scope;
import jdice.values.ListValue;

/**
 * List conversion expression.
 * @author phatonin
 *
 */
public class ConvertList extends AbstractListExpression {

    /** Expression to convert. */
    public final Expression expr;

    /**
	 * Constructs a list conversion expression.
	 * @param expr
	 */
    public ConvertList(Expression expr) {
        super();
        this.expr = expr;
    }

    @Override
    public ListValue evaluateList(EvaluationContext ctx, Scope scope) {
        return expr.evaluateList(ctx, scope);
    }

    @Override
    public void tree(PrintStream out, int depth) {
        tree(out, depth, "list", expr);
    }

    @Override
    public OperatorPrecedence getOperatorPrecedence() {
        return OperatorPrecedence.CONVERT;
    }

    @Override
    public void toString(StringBuilder sb) {
        unaryOperatorString(sb, "list ", expr);
    }

    @Override
    public boolean isAssociative() {
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ConvertList other = (ConvertList) obj;
        if (expr == null) {
            if (other.expr != null) return false;
        } else if (!expr.equals(other.expr)) return false;
        return true;
    }
}
