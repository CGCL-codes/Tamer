package org.eclipse.ufacekit.core.databinding.instance.dom;

import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.ufacekit.core.databinding.instance.AbstractInstanceObservedContainer;
import org.w3c.dom.Node;

/**
 * DOM container implementation which manage dirty of DOM Node observed (DOM
 * Node instance change).
 * 
 * <p>
 * DOM Node observed is dirty when the Node has not parent {@link Node}.
 * </p>
 * 
 * <p>
 * {@link DOMInstanceObservedContainer#observeInstances()} must be called to
 * check if observed Node instance has changed or not.
 * </p>
 * 
 */
public class DOMInstanceObservedContainer extends AbstractInstanceObservedContainer {

    protected void dispose(Object observed) {
    }

    protected boolean isDirty(Object oldInstance, IObserving observing) {
        if (oldInstance == null) return true;
        return (((Node) oldInstance).getParentNode() == null);
    }
}
