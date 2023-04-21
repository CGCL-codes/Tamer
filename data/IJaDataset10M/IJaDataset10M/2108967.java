package org.eclipse.ufacekit.core.internal.databinding.sse.dom.events.adapter;

import org.eclipse.ufacekit.core.internal.databinding.sse.dom.events.MutationEventImpl;
import org.eclipse.ufacekit.core.internal.databinding.sse.dom.events.SSENode2W3cNodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.events.MutationEvent;

/**
 * DOM W3c event adapter to fire {@link MutationEvent} for node removed modified
 * {@link MutationEvent#getType()} == "DOMNodeRemoved".
 * 
 */
public class DOMNodeRemovedAdapter extends AbstractDOMEventAdapter implements INodeAdapter {

    public boolean isAdapterForType(Object type) {
        return type == DOMNodeRemovedAdapter.class;
    }

    public void notifyChanged(INodeNotifier notifier, int eventType, Object changedFeature, Object oldValue, Object newValue, int pos) {
        if (eventType == INodeNotifier.REMOVE && oldValue instanceof IDOMNode) {
            IDOMNode oldNode = (IDOMNode) oldValue;
            MutationEventImpl event = new MutationEventImpl();
            event.initMutationEvent(DOMNodeRemoved, true, false, oldNode, null, null, null, (short) 0);
            event.target = SSENode2W3cNodeAdapter.adapt(oldNode);
            super.doHandleEvent(event);
        }
    }
}
