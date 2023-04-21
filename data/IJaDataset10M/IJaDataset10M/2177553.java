package org.plazmaforge.studio.reportdesigner.model.crosstab;

import java.util.ArrayList;
import java.util.List;
import org.plazmaforge.studio.reportdesigner.model.IExpressionHolder;
import org.plazmaforge.studio.reportdesigner.model.IExpressionListHolder;
import org.plazmaforge.studio.reportdesigner.model.data.Expression;
import org.plazmaforge.studio.reportdesigner.util.ExpressionUtils;

public class CrosstabBucket implements IExpressionHolder, IExpressionListHolder {

    private byte order = BucketDefinition.ORDER_ASCENDING;

    private Expression expression;

    private Expression comparatorExpression;

    public byte getOrder() {
        if (order < BucketDefinition.ORDER_ASCENDING || order > BucketDefinition.ORDER_DESCENDING) {
            order = BucketDefinition.ORDER_ASCENDING;
        }
        return order;
    }

    public void setOrder(byte order) {
        this.order = order;
    }

    public Expression getExpression() {
        if (expression == null) {
            expression = new Expression();
        }
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public String getExpressionText() {
        return expression == null ? null : expression.getText();
    }

    public void setExpressionText(String text) {
        getExpression().setText(text);
    }

    public String getValueClassName() {
        return expression == null ? null : expression.getValueClassName();
    }

    public void setValueClassName(String valueClassName) {
        getExpression().setValueClassName(valueClassName);
    }

    public Expression getComparatorExpression() {
        if (comparatorExpression == null) {
            comparatorExpression = new Expression();
        }
        return comparatorExpression;
    }

    public void setComparatorExpression(Expression comparatorExpression) {
        this.comparatorExpression = comparatorExpression;
    }

    public List<Expression> getExpressions() {
        List<Expression> expressions = new ArrayList<Expression>();
        populateExpressions(expressions);
        return expressions;
    }

    public void populateExpressions(List<Expression> expressions) {
        addExpression(expressions, expression);
        addExpression(expressions, comparatorExpression);
    }

    public void addExpression(List<Expression> expressions, Expression expression) {
        ExpressionUtils.addExpression(expressions, expression);
    }
}
