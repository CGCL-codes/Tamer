package org.masterview.user.client.data;

public class Filter {

    protected String propertyName;

    protected String expression;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Filter(String propertyName, String expression) {
        this.expression = expression;
        this.propertyName = propertyName;
    }
}
