package org.da.expressionj.model;

import java.util.List;

/** An expressoin with an indeterminate number of predicates.
 *
 * @since 0.6
 */
public interface MultipleAryExpression extends Expression {

    public void addExpression(Expression expr) throws Exception;

    public void setExpressions(List<Expression> exprs);

    public List<Expression> getExpressions();
}
