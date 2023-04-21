package org.simbrain.network.gui.filters;

import org.simbrain.util.Predicate;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PNodeFilter;

/**
 * Abstract filter, implements both <code>PNodeFilter</code>
 * and <code>Predicate</code> interfaces.
 */
public abstract class AbstractFilter implements PNodeFilter, Predicate {

    /** @see Predicate */
    public boolean evaluate(final Object object) {
        if (object instanceof PNode) {
            PNode node = (PNode) object;
            return accept(node);
        } else {
            return false;
        }
    }

    /** @see PNodeFilter */
    public abstract boolean accept(final PNode node);

    /** @see PNodeFilter */
    public boolean acceptChildrenOf(final PNode node) {
        return true;
    }
}
