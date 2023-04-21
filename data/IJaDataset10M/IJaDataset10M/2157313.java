package org.xmlpull.v1.builder.xpath.jaxen.expr;

import org.xmlpull.v1.builder.xpath.jaxen.ContextSupport;
import org.xmlpull.v1.builder.xpath.jaxen.Navigator;
import org.xmlpull.v1.builder.xpath.jaxen.expr.iter.IterableAxis;

public class DefaultTextNodeStep extends DefaultStep {

    public DefaultTextNodeStep(IterableAxis axis) {
        super(axis);
    }

    public boolean matches(Object node, ContextSupport support) {
        Navigator nav = support.getNavigator();
        return nav.isText(node);
    }

    public String getText() {
        return getAxisName() + "::text()" + super.getText();
    }
}
