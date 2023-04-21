package mipt.search.analytic;

/**
 * List of expressions
 */
public class Expressions {

    public Expression[] expressions;

    /**
 * 
 * @param expressions mipt.dbms.Expression[]
 */
    public Expressions(Expression[] expressions) {
        this.expressions = expressions;
    }

    /**
 * 
 * @param one mipt.search.Expression
 */
    public Expressions(Expression one) {
        this(new Expression[] { one });
    }

    /**
 * 
 * @param one mipt.search.Expression
 * @param two mipt.search.Expression
 */
    public Expressions(Expression one, Expression two) {
        this(new Expression[] { one, two });
    }

    /**
 * 
 * @param one mipt.search.Expression
 * @param two mipt.search.Expression
 * @param three mipt.search.Expression
 */
    public Expressions(Expression one, Expression two, Expression three) {
        this(new Expression[] { one, two, three });
    }

    /**
 * 
 * @param crit mipt.search.Expression
 */
    public void add(Expression expression) {
        int m = expressions.length;
        Expression[] newExpressions = new Expression[m + 1];
        System.arraycopy(expressions, 0, newExpressions, 0, m);
        expressions[m] = expression;
        expressions = newExpressions;
    }

    /**
 * 
 * @return boolean
 * @param obj java.lang.Object
 */
    public boolean equals(Object obj) {
        if (obj instanceof Expressions) {
            Expressions other = (Expressions) obj;
            int n = other.getExpressionCount();
            if (this.getExpressionCount() != n) return false;
            for (int i = 0; i < n; i++) {
                if (other.getExpression(i).equals(expressions[i])) return false;
            }
            return true;
        } else if (expressions.length == 1) {
            return expressions[0].equals(obj);
        }
        return false;
    }

    /**
 * 
 */
    public final Expression getExpression(int i) {
        return expressions[i];
    }

    /**
 * 
 * @return int
 */
    public final int getExpressionCount() {
        return expressions.length;
    }

    /**
 * 
 * @return int
 */
    public int hashCode() {
        int n = expressions.length, code = 0;
        for (int i = 0; i < n; i++) {
            code += expressions[i].hashCode();
        }
        return code;
    }

    /**
 * 
 * @return int
 * @param expression mipt.search.Expression
 */
    protected final int indexOf(Expression expression) {
        for (int i = 0; i < expressions.length; i++) if (expressions[i].equals(expression)) {
            return i;
        }
        return -1;
    }

    /**
 * 
 * @param expression mipt.search.Expression
 */
    public void remove(Expression expression) {
        int index = indexOf(expression);
        if (index < 0) return;
        int m = expressions.length - 1;
        Expression[] newExpressions = new Expression[m];
        System.arraycopy(expressions, 0, newExpressions, 0, index);
        System.arraycopy(expressions, index + 1, newExpressions, index, m - index);
        expressions = newExpressions;
    }

    /**
 * 
 * @param expression mipt.search.Expression
 */
    public void set(int index, Expression expression) {
        expressions[index] = expression;
    }

    /**
 * 
 * @return java.lang.String
 */
    public String toString() {
        int n = expressions.length;
        StringBuffer result = new StringBuffer(20 * n);
        for (int i = 0; i < n; i++) {
            if (i > 0) result.append(", ");
            result.append(expressions[i].toString());
        }
        return result.toString();
    }
}
