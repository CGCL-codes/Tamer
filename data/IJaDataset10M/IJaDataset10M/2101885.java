package at.redcross.tacos.web.security;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.expression.Expression;
import org.springframework.security.access.ConfigAttribute;

public class WebExpressionConfigAttribute implements ConfigAttribute {

    private static final long serialVersionUID = -3846838394392901404L;

    private final String attribute;

    private final Expression expression;

    public WebExpressionConfigAttribute(String attribute, Expression expression) {
        this.attribute = attribute;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("attribute", attribute).append("expression", expression.getValue()).toString();
    }

    @Override
    public String getAttribute() {
        return attribute;
    }

    public Expression getExpression() {
        return expression;
    }
}
