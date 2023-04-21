package org.xmlpull.v1.builder.xpath.jaxen.expr;

import org.xmlpull.v1.builder.xpath.jaxen.ContextSupport;
import org.xmlpull.v1.builder.xpath.jaxen.Navigator;
import org.xmlpull.v1.builder.xpath.jaxen.expr.iter.IterableAxis;

public class DefaultProcessingInstructionNodeStep extends DefaultStep {

    private String name;

    public DefaultProcessingInstructionNodeStep(IterableAxis axis, String name) {
        super(axis);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean matches(Object node, ContextSupport support) {
        Navigator nav = support.getNavigator();
        boolean isPi = nav.isProcessingInstruction(node);
        if (isPi) {
            String name = getName();
            if (name == null || "".equals(name)) {
                return true;
            } else {
                return name.equals(nav.getProcessingInstructionTarget(node));
            }
        }
        return false;
    }
}
