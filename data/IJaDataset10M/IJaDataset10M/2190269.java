package org.carabiner.infopanel.inspector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.Component;
import java.awt.Container;
import javax.swing.CellRendererPane;
import javax.swing.JScrollBar;

/**
 * A Rule implementation that checks to see if a component (or any of
 * it's children), have a width and height of zero. This rule cannot reliably
 * check Scroll Bars, because scroll bar buttons are sometimes intentionally,
 * left with zero size.
 *
 * @author Ben Rady (benrady@gmail.com)
 *
 */
public class ZeroSizeRule extends AbstractRule {

    public ZeroSizeRule() {
        super();
    }

    public List inspectComponent(Component component) {
        List list = new ArrayList(1);
        if (hasZeroSize(component) && !isException(component)) {
            list.add(createWarning(component));
        }
        return list;
    }

    private boolean isException(Component component) {
        return component instanceof CellRendererPane;
    }

    public List inspectContainer(Container container) {
        if (!(container instanceof JScrollBar)) {
            return super.inspectContainer(container);
        }
        return Collections.EMPTY_LIST;
    }

    private boolean hasZeroSize(Component component) {
        return component.getWidth() == 0 && component.getHeight() == 0;
    }

    public String getName() {
        return "Zero Size";
    }

    public String getDescription() {
        return "Component has a width and height of zero.";
    }
}
