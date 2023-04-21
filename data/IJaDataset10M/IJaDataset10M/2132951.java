package org.granite.tide.seam;

import java.io.Serializable;

/**
 * @author William DRAI
 */
public class ContextResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String componentName;

    private String expression;

    public ContextResult() {
    }

    public ContextResult(String componentName, String expression) {
        this.componentName = componentName;
        this.expression = expression;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        if (expression == null) return componentName;
        return componentName + "." + expression;
    }

    @Override
    public int hashCode() {
        return (componentName + "." + expression).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !object.getClass().equals(ContextResult.class)) return false;
        ContextResult result = (ContextResult) object;
        if (!result.getComponentName().equals(componentName)) return false;
        if (expression == null) return result.getExpression() == null;
        return expression.equals(result.getExpression());
    }
}
