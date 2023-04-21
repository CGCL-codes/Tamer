package org.w3c.flute.parser.selectors;

import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;

/**
 * @version $Revision: 1.2 $
 * @author  Philippe Le Hegaret
 */
public class ChildSelectorImpl implements DescendantSelector {

    Selector parent;

    SimpleSelector child;

    /**
     * An integer indicating the type of <code>Selector</code>
     */
    public short getSelectorType() {
        return Selector.SAC_CHILD_SELECTOR;
    }

    /**
     * Creates a new ChildSelectorImpl
     */
    public ChildSelectorImpl(Selector parent, SimpleSelector child) {
        this.parent = parent;
        this.child = child;
    }

    /**
     * Returns the parent selector.
     */
    public Selector getAncestorSelector() {
        return parent;
    }

    public SimpleSelector getSimpleSelector() {
        return child;
    }
}
