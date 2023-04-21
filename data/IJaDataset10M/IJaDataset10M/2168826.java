package org.freebxml.omar.client.ui.web.columns;

import org.jaxen.javabean.JavaBeanXPath;

public class Column {

    private final String label;

    private final JavaBeanXPath expression;

    public Column(String label, JavaBeanXPath expression) {
        this.label = label;
        this.expression = expression;
    }

    public String getLabel() {
        return label;
    }

    public JavaBeanXPath getExpression() {
        return expression;
    }
}
