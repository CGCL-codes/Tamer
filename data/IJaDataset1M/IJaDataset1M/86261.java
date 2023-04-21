package org.geonetwork.domain.filter110.comparisonop;

import org.geonetwork.domain.filter110.expression.Expression;

/**
 * 
 * @author heikki doeleman
 *
 */
public class BinaryComparisonOp extends ComparisonOps {

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expression1 == null) ? 0 : expression1.hashCode());
        result = prime * result + ((expression2 == null) ? 0 : expression2.hashCode());
        result = prime * result + (matchCase ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        BinaryComparisonOp other = (BinaryComparisonOp) obj;
        if (expression1 == null) {
            if (other.expression1 != null) return false;
        } else if (!expression1.equals(other.expression1)) return false;
        if (expression2 == null) {
            if (other.expression2 != null) return false;
        } else if (!expression2.equals(other.expression2)) return false;
        if (matchCase != other.matchCase) return false;
        return true;
    }

    private Expression expression1;

    private Expression expression2;

    private boolean matchCase = true;

    public Expression getExpression1() {
        return expression1;
    }

    public void setExpression1(Expression expression1) {
        this.expression1 = expression1;
    }

    public Expression getExpression2() {
        return expression2;
    }

    public void setExpression2(Expression expression2) {
        this.expression2 = expression2;
    }

    public boolean isMatchCase() {
        return matchCase;
    }

    public void setMatchCase(boolean matchCase) {
        this.matchCase = matchCase;
    }
}
