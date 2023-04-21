package com.rwoar.moo.client.connection;

import javax.swing.JComponent;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *  Convenience class to request focus on a component.
 *
 *  When the component is added to a realized Window then component will
 *  request focus immediately, since the ancestorAdded event is fired
 *  immediately.
 *
 *  When the component is added to a non realized Window, then the focus
 *  request will be made once the window is realized, since the
 *  ancestorAdded event will not be fired until then.
 */
public class RequestFocusListener implements AncestorListener {

    public void ancestorAdded(AncestorEvent e) {
        JComponent component = (JComponent) e.getComponent();
        component.requestFocusInWindow();
        component.removeAncestorListener(this);
    }

    public void ancestorMoved(AncestorEvent e) {
    }

    public void ancestorRemoved(AncestorEvent e) {
    }
}
