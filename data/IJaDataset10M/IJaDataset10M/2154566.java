package org.plazmaforge.framework.uwt.swing.adapters;

import java.awt.FlowLayout;
import org.plazmaforge.framework.uwt.UIObject;

public class SwingCompositeAdapter extends SwingControlAdapter {

    public Object createDelegate(UIObject parent, UIObject element) {
        java.awt.Container parentDelegate = (java.awt.Container) getParentContent(parent.getDelegate());
        javax.swing.JPanel delegate = new javax.swing.JPanel();
        parentDelegate.add(delegate);
        return delegate;
    }

    protected java.awt.LayoutManager createDefaultCompositeLayout() {
        return new FlowLayout();
    }
}
